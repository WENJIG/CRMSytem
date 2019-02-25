package cn.wenjig.crm.common.local.thread;

import cn.wenjig.crm.common.enums.LogManage;
import cn.wenjig.crm.data.entity.LogInfo;
import cn.wenjig.crm.service.SystemLogService;
import cn.wenjig.crm.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final SystemLogService systemLogService;

    @Autowired
    public LogThread(SystemLogService systemLogService) {
        this.systemLogService = systemLogService;
    }

    public void init() {

        // 添加一个正常关闭jvm时将要执行的钩子 如果执行失败 或 非正常关闭, 内存中的日志会永久丢失
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                writeToDB();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
        scheduledThreadPool = Executors.newScheduledThreadPool(2);
        autoWriteToDB();
    }

    /**
     * @Description: 往队列中添加一个新的日志信息
     * @param logInfo
     * @Return void
     */
    public void addLogInfo(LogInfo logInfo) {
        logInfoQueue.add(logInfo);
    }

    /**
     * @Description: 线程池开启一个循环线程, 用于一段时间自动将日志写入数据库
     * @param
     * @Return void
     */
    public void autoWriteToDB() {
        scheduledThreadPool.scheduleAtFixedRate(() -> {
            Queue<LogInfo> backup = null;
            StringBuilder stringBuilder = new StringBuilder(DateUtil.getSystemPreciseDate1() + " " + Thread.currentThread().getName() + "日志写入开始:");
            try {
                if (!logInfoQueue.isEmpty()) {
                    // 备份一下将要写入数据库的日志
                    backup = writeToDB();
                    stringBuilder.append("成功！本次写入 ").append(backup.size()).append(" 个日志信息");
                    System.out.println(stringBuilder.toString());
                } else {
                    stringBuilder.append("本次无写入。");
                    System.out.println(stringBuilder.toString());
                }
            }catch (Exception e) {
                e.printStackTrace();
                // 如果自动添加失败, 将备份的日志重新加入队列, 等待下次自动添加。 在加一个消息通知, 或者写入本地？
                logInfoQueue.addAll(backup);
                stringBuilder.append("失败！失败个数: ").append(backup.size());
                System.err.println(stringBuilder.toString());
            }
        }, 0, 15, TimeUnit.MINUTES);
    }

    /**
     * @Description: 将队列中的日志写入数据库
     * @param
     * @Return void
     */
    private Queue<LogInfo> writeToDB() throws Exception {
        Queue<LogInfo> logInfos = new ConcurrentLinkedQueue<>(logInfoQueue);
        logInfoQueue.clear();
        try {
            systemLogService.addLog(logInfos);
        } catch (Exception e) {
            e.printStackTrace();
            return logInfos;
        }
        return logInfos;
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
