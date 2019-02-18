package cn.wenjig.crm.common.local.thread;

import cn.wenjig.crm.data.entity.LogInfo;
import cn.wenjig.crm.util.DateUtil;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * 日志操作，这里采用本地线程的方式操作日志
 */
@Component
public class LogThread {

    private ScheduledExecutorService scheduledThreadPool;
    private Queue<LogInfo> logInfoQueue = new ConcurrentLinkedQueue<>();

    public void init() {

        Runtime.getRuntime().addShutdownHook(new Thread(this::writeToDB));
        scheduledThreadPool = Executors.newScheduledThreadPool(4);

        scheduledThreadPool.scheduleAtFixedRate(() -> {
            System.out.println(DateUtil.getSystemPreciseDate1() + " " + Thread.currentThread().getName() + "日志写入开始:***********************************************************************");
            try {
                // 可能会出现问题的地方，例如事务回滚
                if (!logInfoQueue.isEmpty()) {
                    //writeToDB();
                }
            }catch (Exception e) {
                e.printStackTrace();
                // 处理一下
            }
            System.out.println(DateUtil.getSystemPreciseDate1() + " " + Thread.currentThread().getName() + "日志写入完成:***********************************************************************");
        }, 0, 5, TimeUnit.MINUTES);
    }

    public void addLogInfo(LogInfo logInfo) {
        logInfoQueue.add(logInfo);
    }

    /**
     * @Description: 将队列中的日志写入数据库
     * @param
     * @Return void
     */
    private void writeToDB() {

    }

    /**
     * @Description: 将队列中的日志转换为JSON写入本地磁盘, 以TXT的形式保存
     * @param
     * @Return void
     */
    private void writeToLocalTXT() {

    }

    public LinkedList<LogInfo> getByRam() {
        LinkedList<LogInfo> logList = new LinkedList();
        logList.addAll(logInfoQueue);
        return logList;
    }

}
