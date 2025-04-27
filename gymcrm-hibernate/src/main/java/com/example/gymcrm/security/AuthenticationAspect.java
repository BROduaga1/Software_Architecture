package com.example.gymcrm.security;

import com.example.gymcrm.service.TraineeService;
import com.example.gymcrm.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthenticationAspect {

    private final TraineeService traineeService;
    private final TrainerService trainerService;

    @Around("@annotation(com.example.gymcrm.security.Authenticated)")
    public Object authenticate(ProceedingJoinPoint joinPoint) throws Throwable {
        String username = AuthenticationContext.getUsername();
        String password = AuthenticationContext.getPassword();

        boolean isAuthenticated = traineeService.isAuthenticated(username, password) || trainerService.isAuthenticated(username, password);
        if (isAuthenticated) {
            return joinPoint.proceed();
        } else {
            throw new SecurityException("User is not authenticated");
        }
    }
}
