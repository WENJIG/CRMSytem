package cn.wenjig.crm.web;

import cn.wenjig.crm.data.entity.Employee;
import cn.wenjig.crm.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.security.RolesAllowed;
import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping(value = "/index")
public class IndexWeb extends BaseWeb {

    private final EmployeeService employeeService;

    @Autowired
    public IndexWeb(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @RolesAllowed({"ROLE_在职员工"})
    @RequestMapping(value = "/")
    public String redirectLoadView() throws UnsupportedEncodingException {
        return "redirect:/index/" + java.net.URLEncoder.encode(getUser().getUsername(), "UTF-8");
    }

    @RolesAllowed({"ROLE_在职员工"})
    @RequestMapping(value = "/{account}")
    public String loadView(@PathVariable String account) {

        return "index.html";
    }

}
