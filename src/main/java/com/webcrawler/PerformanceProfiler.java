package com.webcrawler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Performance profiler that uses Java Dynamic Proxies to time methods annotated with @Profiled.
 */
public class PerformanceProfiler implements InvocationHandler {
    private final Object target;
    private final ConcurrentMap<String, Long> methodTimes;

    private PerformanceProfiler(Object target) {
        this.target = target;
        this.methodTimes = new ConcurrentHashMap<>();
    }

    /**
     * Creates a proxy for the target object that profiles @Profiled methods.
     *
     * @param target The object to profile
     * @param <T> Type of the object
     * @return Proxied object
     */
    @SuppressWarnings("unchecked")
    public static <T> T wrap(T target) {
        Class<?> targetClass = target.getClass();
        PerformanceProfiler profiler = new PerformanceProfiler(target);
        
        return (T) Proxy.newProxyInstance(
            targetClass.getClassLoader(),
            targetClass.getInterfaces(),
            profiler
        );
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Check if the method is annotated with @Profiled
        Method targetMethod = target.getClass().getMethod(method.getName(), method.getParameterTypes());
        boolean isProfiled = targetMethod.isAnnotationPresent(Profiled.class);

        if (isProfiled) {
            long startTime = System.nanoTime();
            try {
                return method.invoke(target, args);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            } finally {
                long endTime = System.nanoTime();
                long duration = endTime - startTime;
                String methodName = method.getName();
                methodTimes.put(methodName, duration);
                System.out.println("[PROFILER] " + methodName + " took " + 
                                   duration / 1_000_000.0 + " ms");
            }
        } else {
            try {
                return method.invoke(target, args);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
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
