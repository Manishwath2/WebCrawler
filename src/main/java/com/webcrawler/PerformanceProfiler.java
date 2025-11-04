package com.webcrawler;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Performance profiler that uses Java Dynamic Proxies to time methods annotated with @Profiled.
 */
public class PerformanceProfiler implements InvocationHandler {
    private final Object target;
    private final ConcurrentMap<String, Long> methodTimes;
    private final Clock clock;
    private final String profileOutputPath;

    private PerformanceProfiler(Object target, Clock clock, String profileOutputPath) {
        this.target = target;
        this.methodTimes = new ConcurrentHashMap<>();
        this.clock = clock;
        this.profileOutputPath = profileOutputPath;
    }

    /**
     * Creates a proxy for the target object that profiles @Profiled methods.
     *
     * @param target The object to profile
     * @param clock Clock for timestamps
     * @param profileOutputPath Path to write profile data (empty = stdout)
     * @param <T> Type of the object
     * @return Proxied object
     */
    @SuppressWarnings("unchecked")
    public static <T> T wrap(T target, Clock clock, String profileOutputPath) {
        Class<?> targetClass = target.getClass();
        PerformanceProfiler profiler = new PerformanceProfiler(target, clock, profileOutputPath);
        
        return (T) Proxy.newProxyInstance(
            targetClass.getClassLoader(),
            targetClass.getInterfaces(),
            profiler
        );
    }

    /**
     * Creates a proxy with system clock and console output.
     */
    @SuppressWarnings("unchecked")
    public static <T> T wrap(T target) {
        return wrap(target, Clock.systemDefaultZone(), "");
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Bypass profiling for Object.equals()
        if ("equals".equals(method.getName()) && method.getDeclaringClass() == Object.class) {
            try {
                return method.invoke(target, args);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        }

        // Check if the method is annotated with @Profiled
        Method targetMethod;
        try {
            targetMethod = target.getClass().getMethod(method.getName(), method.getParameterTypes());
        } catch (NoSuchMethodException e) {
            // Method not found in target, just invoke it
            try {
                return method.invoke(target, args);
            } catch (InvocationTargetException ex) {
                throw ex.getCause();
            }
        }

        boolean isProfiled = targetMethod.isAnnotationPresent(Profiled.class);

        if (isProfiled) {
            long startTime = System.nanoTime();
            try {
                return method.invoke(target, args);
            } catch (InvocationTargetException e) {
                // Rethrow the exact exception (not wrapped)
                throw e.getCause();
            } finally {
                long endTime = System.nanoTime();
                long duration = endTime - startTime;
                String className = target.getClass().getName();
                String methodName = method.getName();
                methodTimes.put(className + "#" + methodName, duration);
                
                // Write profiling data
                writeProfilingData(className, methodName, duration);
            }
        } else {
            try {
                return method.invoke(target, args);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        }
    }

    private void writeProfilingData(String className, String methodName, long durationNanos) {
        long millis = durationNanos / 1_000_000;
        long seconds = millis / 1000;
        long remainingMillis = millis % 1000;
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;

        String formattedDuration = String.format("%dm %ds %dms", minutes, remainingSeconds, remainingMillis);
        String logEntry = className + "#" + methodName + " took " + formattedDuration;

        try {
            if (profileOutputPath == null || profileOutputPath.trim().isEmpty()) {
                // Write to stdout
                System.out.println("[PROFILER] " + logEntry);
            } else {
                // Append to file
                try (Writer writer = new FileWriter(profileOutputPath, true)) {
                    String timestamp = ZonedDateTime.now(clock).format(
                        DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz")
                    );
                    writer.write("Run at " + timestamp + "\n");
                    writer.write(logEntry + "\n");
                    writer.flush();
                }
            }
        } catch (IOException e) {
            // Fallback to stderr if file write fails
            System.err.println("Failed to write profiling data: " + e.getMessage());
            System.err.println("[PROFILER] " + logEntry);
        }
    }

    /**
     * Gets the timing information for all profiled methods.
     *
     * @return Map of method names to execution times in nanoseconds
     */
    public ConcurrentMap<String, Long> getMethodTimes() {
        return new ConcurrentHashMap<>(methodTimes);
    }
}
