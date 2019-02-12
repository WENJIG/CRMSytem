package cn.wenjig.crm.common.aspect;

import cn.wenjig.crm.common.annotation.SystemLogAnnotation;
import cn.wenjig.crm.common.enums.PermissionManage;
import cn.wenjig.crm.common.local.thread.LogThread;
import cn.wenjig.crm.data.entity.LogInfo;
import cn.wenjig.crm.util.DateUtil;
import cn.wenjig.crm.util.JsonUtil;
import cn.wenjig.crm.web.BaseWeb;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SystemLog {

    private final LogThread logThread;

    @Autowired
    public SystemLog(LogThread logThread) {
        this.logThread = logThread;
    }

    /**
     * 此处的切面类切点是被打上了SystemLogAnnotation注解的方法
     */
    @Pointcut(value = "@annotation(cn.wenjig.crm.common.annotation.SystemLogAnnotation)")
    public void baseLog(){}

    /**
     * 切点在进入方法之前
     * @Before("baseLog()") public void doBeforeAdvice(JoinPoint joinPoint) {}
     */
    /**
     * 切点在返回值时
     * @AfterReturning(returning = "ret", pointcut = "baseLog()") public void doAfterReturning(Object ret) {}
     */
    /**
     * 切点在抛出异常时
     * @AfterThrowing("baseLog()") public void doThrows(JoinPoint joinPoint) {}
     */
    /**
     * 切点在执行完成后, 但是 是final增强, 无论如何都会执行。
     * @After("baseLog()") public void doAfter(JoinPoint joinPoint) {}
     */

    /**
     * 方法环绕增强
     * 说明：
     * 既可以在目标方法之前织入增强动作，也可以在执行目标方法之后织入增强动作；
     * 它可以决定目标方法在什么时候执行，如何执行，甚至可以完全阻止目标目标方法的执行；
     * 它可以改变执行目标方法的参数值，也可以改变执行目标方法之后的返回值；
     * 当需要改变目标方法的返回值时，只能使用Around
     * 需求扩展：
     * 可以用Around来实现一些特殊权限, 或者增强之类的, 比如新增一个上传文件压缩。
     */
    @Around(value = "baseLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object res = null;
        long startTime = System.currentTimeMillis();
        long endTime = 0;
        long runTime = 0;
        try {
            res =  joinPoint.proceed();
            endTime = System.currentTimeMillis();
            runTime = endTime - startTime;
            return res;
        } finally {
            // 方法执行完成后创建并添加日志
            createLog(joinPoint, res, startTime, endTime, runTime);
        }
    }

    /**
     * @Description: 创建一个新的日志
     * @param joinPoint, res, startTime, endTime, runTime
     * @Return void
     */
    private void createLog(JoinPoint joinPoint, Object res, long startTime, long endTime, long runTime) {
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        LogInfo logInfo = new LogInfo();
        logInfo.setEmployeeId(PermissionManage.RUNTIME.getPermissionService().getId(BaseWeb.getUser().getUsername()));
        logInfo.setMethod(signature.getDeclaringTypeName() + "." + signature.getMethod().getName());
        logInfo.setStartTime(DateUtil.changeToDateString1(startTime));
        logInfo.setEndTime(DateUtil.changeToDateString1(endTime));
        logInfo.setRunTime(runTime);
        logInfo.setArgs(JsonUtil.toJson(joinPoint.getArgs()));
        logInfo.setReturnValue(JsonUtil.toJson(res));

        // 将注解中的值set进本次日志对象
        SystemLogAnnotation annotation = signature.getMethod().getAnnotation(SystemLogAnnotation.class);
        if(annotation != null){
            logInfo.setDescription(annotation.description());
            logInfo.setLevel(annotation.level());
            logInfo.setOperating_type(annotation.operationType().getValue());
        }
        // 将日志加入日志队列, 以便进行后续的扩展操作
        logThread.addLogInfo(logInfo);
    }

}
