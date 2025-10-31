package com.loopin.loopinbackend.global.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LoggingAspect() {
        objectMapper.registerModule(new JavaTimeModule())
                .enable(SerializationFeature.INDENT_OUTPUT) // 들여쓰기, 줄바꿈
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // 배열이 아닌 문자열로 반환
    }

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
                String json = objectMapper.writeValueAsString(args[i]);
                sb.append(paramNames[i]).append("=").append(json);
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
            String json = objectMapper.writeValueAsString(result);
            log.info("✅ [RESPONSE] {} => {}", methodName, json);
        } catch (Exception e) {
            log.warn("⚠️ [RESPONSE LOGGING ERROR] {}", e.getMessage());
        }
    }

}
