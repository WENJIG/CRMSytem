package cn.wenjig.crm.web;

import cn.wenjig.crm.data.entity.Employee;
import cn.wenjig.crm.data.entity.JobInfo;
import cn.wenjig.crm.repository.JobInfoRepository;
import cn.wenjig.crm.service.EmployeeService;
import cn.wenjig.crm.service.JobInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.security.RolesAllowed;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
    @RequestMapping(value = "/")
    public String redirectLoadView() throws UnsupportedEncodingException {
        return "redirect:/index/" + java.net.URLEncoder.encode(getUser().getUsername(), "UTF-8");
    }

    @RolesAllowed({"ROLE_在职员工"})
    @RequestMapping(value = "/{account}")
    public String loadView(@PathVariable String account, Model model) {
        if (!account.equals(getUser().getUsername())) {
            return "redirect:/exception/403";
        }
        model.addAttribute("indexPageTitle", account);

        List<JobInfo> myJobs = jobInfoService.findAllJobByEmployeeId(getId(getUser().getUsername()));
        model.addAttribute("myJobs", myJobs);


        return "index.html";
    }

}
