package io.weyoui.weyouiappcore.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Component
@Aspect
public class PerfAspect {

    @Around("@annotation(PerfLogging)")
    public Object logPerf(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object retVal = proceedingJoinPoint.proceed();
        stopWatch.stop();

        log.info(stopWatch.prettyPrint());

        return retVal;
    }
}
