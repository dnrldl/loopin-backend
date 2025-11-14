package com.loopin.loopinbackend.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* com.loopin.loopinbackend.domain..controller..*(..))")
    public void controllerMethods() {}

    @Pointcut("execution(* com.loopin.loopinbackend.domain..service..*(..)) || execution(* com.loopin.loopinbackend.domain..repository..*(..))")
    public void businessMethods() {}

    @Before("controllerMethods()")
    public void logControllerRequest(JoinPoint joinPoint) {
        String methodName = getMethodName(joinPoint);
        String args = getArguments(joinPoint);
        log.info("[REQUEST] {}({})", methodName, args);
    }

    @Before("businessMethods()")
    public void logBusinessMethod(JoinPoint joinPoint) {
        String methodName = getMethodName(joinPoint);
        String args = getArguments(joinPoint);
        log.info("[METHOD] {}({})", methodName, args);
    }

    private String getMethodName(JoinPoint joinPoint) {
        return joinPoint.getSignature().getDeclaringTypeName()
                + "." + joinPoint.getSignature().getName();
    }

    private String getArguments(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        if (paramNames == null || paramNames.length == 0) return "";

        StringBuilder sb = new StringBuilder();
        IntStream.range(0, paramNames.length).forEach(i -> {
            try {
                sb.append(paramNames[i]).append("=").append(args[i]);
                if (i < paramNames.length - 1) sb.append(", ");
            } catch (Exception e) {
                sb.append(paramNames[i]).append("=<?>");
            }
        });
        return sb.toString();
    }

    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logControllerResponse(JoinPoint joinPoint, Object result) {
        try {
            String methodName = joinPoint.getSignature().getDeclaringTypeName()
                    + "." + joinPoint.getSignature().getName();

            log.info("[RESPONSE] {} => {}", methodName, result.toString());
        } catch (Exception e) {
            log.warn("[RESPONSE LOGGING ERROR] {}", e.getMessage());
        }
    }

}
