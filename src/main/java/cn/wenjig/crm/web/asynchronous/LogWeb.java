package cn.wenjig.crm.web.asynchronous;

import cn.wenjig.crm.common.annotation.SystemLog;
import cn.wenjig.crm.common.enums.LogManage;
import cn.wenjig.crm.common.enums.OperationType;
import cn.wenjig.crm.common.local.thread.LogThread;
import cn.wenjig.crm.data.domain.LogListDomain;
import cn.wenjig.crm.data.entity.LogInfo;
import cn.wenjig.crm.service.SystemLogService;
import cn.wenjig.crm.util.JsonUtil;
import cn.wenjig.crm.web.BaseWeb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.security.RolesAllowed;
import java.util.LinkedList;

@Controller
@RequestMapping(value = "/log")
public class LogWeb extends BaseWeb {

    private final SystemLogService systemLogService;

    @Autowired
    public LogWeb(SystemLogService systemLogService) {
        this.systemLogService = systemLogService;
    }

    @RolesAllowed({"ROLE_超级管理员"})
    @SystemLog(description = "从内存中查看日志(还未被写入数据库)", isLogReturn = false, level = 4, operationType = OperationType.SELECT)
    @RequestMapping(value = "/readByRam", method = RequestMethod.POST)
    @ResponseBody
    public String readLogByRam() {
        LinkedList<LogInfo> logs = LogManage.LOG.getLog().getByRam();
        return JsonUtil.toJson(logs);
    }

    @RolesAllowed({"ROLE_超级管理员"})
    @SystemLog(description = "从数据库中查看日志", isLogReturn = false, level = 5, operationType = OperationType.SELECT)
    @RequestMapping(value = "/readByDB", method = RequestMethod.POST)
    @ResponseBody
    public String readLogByDB(@RequestParam(name = "start", required = false, defaultValue = "1")
                                int start,
                              @RequestParam(name = "capacity", required = false, defaultValue = "10")
                                int capacity) {
        int startPage = (start - 1) * capacity;
        if (startPage < 0) {
            LogListDomain logListDomain = systemLogService.findAllByIndexPage(0, capacity);
            return JsonUtil.toJson(logListDomain);
        }
        LogListDomain logListDomain = systemLogService.findAllByIndexPage(((start - 1) * capacity), capacity);
        return JsonUtil.toJson(logListDomain);
    }

    @RolesAllowed({"ROLE_超级管理员"})
    @SystemLog(description = "不等待自动写入,将内存中的日志立即写入数据库", isLogReturn = false, level = 5, operationType = OperationType.INSERT)
    @RequestMapping(value = "/writeNowToDB", method = RequestMethod.POST)
    @ResponseBody
    public String writeNowToDB() {
        try {
            LogManage.LOG.getLog().autoWriteToDB();
            return "写入成功！";
        } catch (Exception e) {
            return "写入失败! ";
        }
    }
}
