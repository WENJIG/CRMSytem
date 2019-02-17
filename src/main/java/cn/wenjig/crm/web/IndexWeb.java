package cn.wenjig.crm.web;

import cn.wenjig.crm.common.annotation.SystemLog;
import cn.wenjig.crm.common.enums.OperationType;
import cn.wenjig.crm.data.entity.JobInfo;
import cn.wenjig.crm.service.EmployeeService;
import cn.wenjig.crm.service.JobInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.security.RolesAllowed;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
@RequestMapping(value = "/index")
public class IndexWeb extends BaseWeb {

    private final EmployeeService employeeService;
    private final JobInfoService jobInfoService;

    @Autowired
    public IndexWeb(EmployeeService employeeService, JobInfoService jobInfoService) {
        this.employeeService = employeeService;
        this.jobInfoService = jobInfoService;
    }

    @RolesAllowed({"ROLE_在职员工"})
    @SystemLog(description = "登录成功！重定向到自己的主页")
    @RequestMapping(value = "/")
    public String redirectLoadView() throws UnsupportedEncodingException {
        return "redirect:/index/" + java.net.URLEncoder.encode(getUser().getUsername(), "UTF-8");
    }

    @RolesAllowed({"ROLE_在职员工"})
    @SystemLog(description = "加载自己的主页", level = 1, operationType = OperationType.SELECT)
    @RequestMapping(value = "/{account}")
    public String loadView(@PathVariable String account, Model model) {
        if (isMe(account)) {
            return "redirect:/exception/403";
        }
        model.addAttribute("indexPageTitle", account);

        List<JobInfo> myJobs = jobInfoService.findAllJobByEmployeeId(getId(getUser().getUsername()));
        model.addAttribute("myJobs", myJobs);

        return "index.html";
    }

    private boolean isMe(String account) {
        return !account.equals(getUser().getUsername());
    }

}
