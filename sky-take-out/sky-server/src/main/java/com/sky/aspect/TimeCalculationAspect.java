package com.sky.aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class TimeCalculationAspect {
    @Around("execution(* com.sky.service.*.*(..))") //切入点
    public Object timeCalculation(ProceedingJoinPoint joinPoint) throws Throwable {//连接点
        long startTime = System.currentTimeMillis();//开始记录
        Object result = joinPoint.proceed();//方法 运行
        long endTime = System.currentTimeMillis();//结束记录
        long totalTime = endTime - startTime;//计算时间
        log.info(joinPoint.getSignature() + "Total time taken: {} ms", totalTime);
        return result;
    }
}
