package io.weyoui.weyouiappcore.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

@Slf4j
@Component
@Aspect
public class PerfAspect {

    @Around("@annotation(PerfLogging)")
    public Object logPerf(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        log.info("---------- " + proceedingJoinPoint.toString() + " START ----------");
        log.info("START TIME : " + LocalDateTime.ofInstant(Instant.ofEpochMilli(start), TimeZone.getDefault().toZoneId()));

        try {
            return proceedingJoinPoint.proceed();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            log.info("END TIME : " + LocalDateTime.ofInstant(Instant.ofEpochMilli(finish), TimeZone.getDefault().toZoneId()));
            log.info("---------- " + proceedingJoinPoint.toString() + " END ----------" + timeMs + "ms");
        }
    }
}
