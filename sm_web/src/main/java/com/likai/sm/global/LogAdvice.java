package com.likai.sm.global;

import com.likai.sm.entity.Log;
import com.likai.sm.entity.Staff;
import com.likai.sm.service.LogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
@Aspect
public class LogAdvice {
    @Autowired
    private LogService logService;
    @After("execution(* com.likai.sm.controller.*.*(..)) && !execution(* com.likai.sm.controller.SelfController.*(..)) && !execution(* com.likai.sm.controller.*.to*(..))")
    public void operationLog(JoinPoint joinPoint){
        Log log = new Log();
        log.setMoudle(joinPoint.getTarget().getClass().getSimpleName());
        log.setOperation(joinPoint.getSignature().getName());
        HttpServletRequest request=(HttpServletRequest)joinPoint.getArgs()[0];
        HttpSession session=request.getSession();
        Object object=session.getAttribute("USER");
        Staff staff=(Staff)object;
        log.setOperator(staff.getAccount());
        log.setResult("成功");
        logService.addOperationLog(log);
    }

    @AfterThrowing(throwing = "e",pointcut = "execution(* com.likai.sm.controller.*.*(..)) && !execution(* com.likai.sm.controller.SelfController.*(..))")
    public void systemLog(JoinPoint joinPoint,Throwable e){
        Log log = new Log();
        log.setMoudle(joinPoint.getTarget().getClass().getSimpleName());
        log.setOperation(joinPoint.getSignature().getName());
        HttpServletRequest request=(HttpServletRequest)joinPoint.getArgs()[0];
        HttpSession session=request.getSession();
        Object object=session.getAttribute("USER");
        Staff staff=(Staff)object;
        log.setOperator(staff.getAccount());
        log.setResult(e.getClass().getSimpleName());
        logService.addSystemLog(log);
    }

    @After("execution(* com.likai.sm.controller.SelfController.login(..))")
    public void loginLog(JoinPoint joinPoint){
        log(joinPoint);
    }

    @Before("execution(* com.likai.sm.controller.SelfController.logout(..))")
    public void logoutLog(JoinPoint joinPoint){
        log(joinPoint);
    }

    private void log(JoinPoint joinPoint){
        Log log = new Log();
        log.setMoudle(joinPoint.getTarget().getClass().getSimpleName());
        log.setOperation(joinPoint.getSignature().getName());
        HttpServletRequest request=(HttpServletRequest)joinPoint.getArgs()[0];
        HttpSession session=request.getSession();
        Object object=session.getAttribute("USER");
        if(object==null){
            log.setOperator(request.getParameter("account"));
            log.setResult("失败");
        }else {
            Staff staff=(Staff)object;
            log.setOperator(staff.getAccount());
            log.setResult("成功");
        }
        logService.addLoginLog(log);
    }
}
