package cn.wenjig.crm.data.domain;

import cn.wenjig.crm.data.entity.LogInfo;
import cn.wenjig.crm.util.JsonUtil;

import java.io.Serializable;
import java.util.LinkedList;

public class LogListDomain implements Serializable {

    private LinkedList<LogInfo> logInfos;
    private Long count;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }

    public LinkedList<LogInfo> getLogInfos() {
        return logInfos;
    }

    public void setLogInfos(LinkedList<LogInfo> logInfos) {
        this.logInfos = logInfos;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
