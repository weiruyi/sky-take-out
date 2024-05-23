package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointcut() {}

    @Before("autoFillPointcut()")
    public void autoFill(JoinPoint joinPoint) throws Throwable {
        log.info("开始进行公共字段的自动填充...");

        // 方法签名对象
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();

        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);  //获得方法的注解对象
        OperationType operationType = autoFill.value();  //获取数据库操作类型

        //获取被拦截方法参数
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0) {
            return;
        }
        Object entity = args[0];

        //准备赋值数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        //根据不同操作类型赋值
        if(operationType == OperationType.INSERT) {
            Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
            Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
            //通过反射赋值
            setCreateTime.invoke(entity, now);
            setCreateUser.invoke(entity, currentId);
        }
        Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
        Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

        setUpdateTime.invoke(entity, now);
        setUpdateUser.invoke(entity, currentId);


    }
}
