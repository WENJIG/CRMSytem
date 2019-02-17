package cn.wenjig.crm.web.asynchronous;

import cn.wenjig.crm.common.annotation.SystemLog;
import cn.wenjig.crm.common.enums.LogManage;
import cn.wenjig.crm.common.enums.OperationType;
import cn.wenjig.crm.common.local.thread.LogThread;
import cn.wenjig.crm.data.entity.LogInfo;
import cn.wenjig.crm.util.JsonUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.security.RolesAllowed;
import java.util.LinkedList;

@Controller
@RequestMapping(value = "/log")
public class LogWeb {

    @RolesAllowed({"ROLE_超级管理员"})
    @SystemLog(description = "从内存中查看日志(还未被写入数据库)", level = 4, operationType = OperationType.SELECT)
    @RequestMapping(value = "/readByRam", method = RequestMethod.POST)
    @ResponseBody
    public String readLogByRam() {
        LinkedList<LogInfo> logs = LogManage.LOG.getLog().getByRam();
        return JsonUtil.toJson(logs);
    }
}
