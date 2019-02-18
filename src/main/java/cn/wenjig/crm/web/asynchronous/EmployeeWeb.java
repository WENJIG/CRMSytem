package cn.wenjig.crm.web.asynchronous;

import cn.wenjig.crm.common.annotation.SystemLog;
import cn.wenjig.crm.common.annotation.validation.NotHaveBlank;
import cn.wenjig.crm.common.enums.OperationType;
import cn.wenjig.crm.data.entity.Employee;
import cn.wenjig.crm.service.EmployeeService;
import cn.wenjig.crm.util.JsonUtil;
import cn.wenjig.crm.util.MD5Util;
import cn.wenjig.crm.web.BaseWeb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Controller
@RequestMapping(value = "/emp")
public class EmployeeWeb extends BaseWeb {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeWeb(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @RolesAllowed({"ROLE_超级管理员"})
    @SystemLog(description = "查看所有员工", level = 2, operationType = OperationType.SELECT)
    @RequestMapping(value = "/findAll", method = RequestMethod.POST)
    @ResponseBody
    public String findAll() {
        return JsonUtil.toJson(employeeService.findAll());
    }

    @RolesAllowed({"ROLE_超级管理员", "ROLE_管理员"})
    @SystemLog(description = "添加一位新的员工", level = 2, operationType = OperationType.INSERT)
    @RequestMapping(value = "/addOne", method = RequestMethod.POST)
    @ResponseBody
    public String addEmp(@RequestParam(name = "account")
                         @Pattern(regexp = "\"^[a-z0-9A-Z]+[- | a-z0-9A-Z . _]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\\\.)+[a-zA-Z]{2,}$\"", message = "账户不是正确的邮箱格式")
                         @NotBlank(message = "账户不能为空")
                         @NotHaveBlank(message = "账户不能含有空格")
                                     String account,
                         @RequestParam(name = "password")
                         @NotBlank(message = "密码不能为空")
                         @NotHaveBlank(message = "密码不能含有空格")
                         @Size(min = 8, max = 16, message = "密码长度必须大于8并且小于16")
                                 String password,
                         @RequestParam(name = "nickname")
                         @NotBlank(message = "昵称不能为空")
                         @NotHaveBlank(message = "昵称不能含有空格")
                         @Size(min = 1, max = 16, message = "昵称长度必须大于1并且小于16")
                                     String nickName,
                         @RequestParam(name = "realname")
                         @NotBlank(message = "真实姓名不能为空")
                         @NotHaveBlank(message = "真实姓名不能含有空格")
                         @Size(min = 1, max = 16, message = "真实姓名长度必须大于1并且小于16")
                                     String realname,
                         @RequestParam(name = "phoneNo")
                         @NotBlank(message = "电话号码不能为空")
                         @NotHaveBlank(message = "电话号码不能含有空格")
                         @Size(min = 11, max = 30, message = "电话号码长度必须大于11并且小于30")
                                     String phoneNo,
                         @NotBlank(message = "办公电话号码不能为空")
                         @NotHaveBlank(message = "办公电话号码不能含有空格")
                         @Size(min = 1, max = 30, message = "办公电话长度必须大于1并且小于30")
                         @RequestParam(name = "officeTel")
                                     String officeTel) {
        if (employeeService.findByName(account) != null) {
            return "该账号已存在！";
        }
        Employee employee = new Employee();
        employee.setEmail(account);
        employee.setPassword(MD5Util.md5Encode(password));
        employee.setNickname(nickName);
        employee.setRealname(realname);
        employee.setPhoneNo(phoneNo);
        employee.setOfficeTel(officeTel);
        employeeService.addOne(employee);
        return "添加成功！";
    }

}
