package cn.myxinge.common;

import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by chenxinghua on 2017/11/20.
 * 写日志接口 、、统一  切面配置
 */
@Aspect
@Configuration
public class WriteMyLog {

    private static final Logger LOG = LoggerFactory.getLogger(WriteMyLog.class);

    /*
     * 定义一个切入点
     */
    // @Pointcut("execution (* findById*(..))")
    @Pointcut("execution(* cn.myxinge.controller..*(..))")
    public void excudeService() {
    }

    //配置环绕通知
    @Around("excudeService()")
    public Object twiceAsOld(ProceedingJoinPoint joinPoint) throws Exception {

        try {
            Object proceed = joinPoint.proceed();
            String msg = "\n----->method："
                    + joinPoint.getSignature();
            msg += "\n----->arguments: " + getArgsStr(joinPoint.getArgs());
            LOG.info(msg.concat("\n----->return: " + proceed));
            return proceed;
        } catch (Throwable e) {
            LOG.error("\n----->method："
                    + joinPoint.getSignature()
                    + "thows Exception： " + e);
        } finally {

        }
        return null;
    }


    /**
     * 返回参数字符串
     *
     * @return
     */
    private String getArgsStr(Object[] args) {
        if (null == args || args.length == 0) {
            return "no args input..";
        }

        StringBuffer sb = new StringBuffer();
        for (Object o : args) {
            if (null == o) {
                sb.append("| null");
                continue;
            }

            sb.append("| "+o.toString());
        }

        return sb.toString();
    }

}
















