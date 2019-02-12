package cn.wenjig.crm.common.enums;

import cn.wenjig.crm.common.local.SpringUtil;
import cn.wenjig.crm.common.local.thread.LogThread;

public enum LogManage {

    LOG;

    private LogThread logThread;

    LogManage() {
        logThread = (LogThread) SpringUtil.getBean(LogThread.class);
    }

    public LogThread getLog() {
        return logThread;
    }

}
