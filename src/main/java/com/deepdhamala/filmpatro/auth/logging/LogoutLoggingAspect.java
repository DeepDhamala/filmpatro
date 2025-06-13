package com.deepdhamala.filmpatro.auth.logging;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogoutLoggingAspect {

    @AfterReturning(
            pointcut = "execution(* com.deepdhamala.filmpatro.auth.LogoutService.logout(..))",
            returning = "result")
    public void afterLogoutSuccess(JoinPoint joinPoint, Object result) {
        log.info("Logout method executed: {}", joinPoint.getSignature());
        // Optionally process or log info from joinPoint.getArgs()
    }

    @AfterThrowing(
            pointcut = "execution(* com.deepdhamala.filmpatro.auth.LogoutService.logout(..))",
            throwing = "ex")
    public void afterLogoutError(JoinPoint joinPoint, Throwable ex) {
        log.error("Exception during logout: {}", ex.getMessage());
    }
}
