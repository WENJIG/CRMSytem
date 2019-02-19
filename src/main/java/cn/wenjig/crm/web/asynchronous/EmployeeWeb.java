package cn.wenjig.crm.web.asynchronous;

import cn.wenjig.crm.common.annotation.SystemLog;
import cn.wenjig.crm.common.enums.OperationType;
import cn.wenjig.crm.data.entity.Employee;
import cn.wenjig.crm.service.EmployeeService;
import cn.wenjig.crm.util.JsonUtil;
import cn.wenjig.crm.util.MD5Util;
import cn.wenjig.crm.util.validator.EmpValidator;
import cn.wenjig.crm.util.validator.IsInvalidValidator;
import cn.wenjig.crm.web.BaseWeb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.security.RolesAllowed;

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
    public String addEmp(@RequestParam(name = "account") String account,
                         @RequestParam(name = "password") String password,
                         @RequestParam(name = "nickname") String nickName,
                         @RequestParam(name = "realname") String realname,
                         @RequestParam(name = "phoneNo") String phoneNo,
                         @RequestParam(name = "officeTel") String officeTel) {
        if (IsInvalidValidator.all(account) || IsInvalidValidator.all(password) ||
                IsInvalidValidator.all(nickName) || IsInvalidValidator.all(realname) ||
                IsInvalidValidator.all(phoneNo) || IsInvalidValidator.all(officeTel)) {
            return "数据中不能含有空格符";
        }
        if (!EmpValidator.all(account, password, nickName, realname, phoneNo, officeTel)) {
            return "数据格式不规范";
        }
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
