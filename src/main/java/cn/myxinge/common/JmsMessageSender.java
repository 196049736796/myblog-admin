package cn.myxinge.common;

import cn.myxinge.service.jms.Producer;
import com.alibaba.fastjson.JSONObject;
import org.apache.activemq.command.ActiveMQQueue;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.jms.Destination;
import java.lang.reflect.Method;

/**
 * Created by chenxinghua on 2017/12/14.
 * 拦截业务，当执行增删改时更新redis缓存
 */
@Aspect
@Configuration
public class JmsMessageSender {
    @Autowired
    private Producer producer;
    private static String[] methodPrefix = {
            "save","update","delete","add"
    };
    private static final Logger LOG = LoggerFactory.getLogger(JmsMessageSender.class);

    /*
     * 定义一个切入点
     */
    // @Pointcut("execution (* findById*(..))")
    @Pointcut("execution(* cn.myxinge.service.impl..*(..))")
    public void excudeService() {

    }

    //配置环绕通知
    @Around("excudeService()")
    public Object twiceAsOld(ProceedingJoinPoint joinPoint) throws Exception {
        try {
            Signature sig = joinPoint.getSignature();

            MethodSignature msig = null;
            if (!(sig instanceof MethodSignature)) {
                throw new IllegalArgumentException("该注解只能用于方法");
            }
            msig = (MethodSignature) sig;
            Object target = joinPoint.getTarget();
            Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());

            String methodName = currentMethod.getName();

            for(String m : methodPrefix){
                if(methodName.startsWith(m)){
                    Destination destination = new ActiveMQQueue("redisFlush.queue");
                    //发送消息
                    producer.sendMessage(destination,"flushall-redis");
                    break;
                }
            }

            Object proceed = joinPoint.proceed();
            return proceed;
        } catch (Throwable throwable) {
            LOG.error("发生异常", throwable);
        }
        return null;
    }

}
