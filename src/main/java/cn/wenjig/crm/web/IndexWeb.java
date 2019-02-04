package cn.wenjig.crm.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.security.RolesAllowed;

@Controller
@RequestMapping(value = "/index")
public class IndexWeb {

    @RequestMapping(value = "/")
    @ResponseBody
    @RolesAllowed({"ROLE_超级管理员"})
    public String loadView() {
        return "欢迎回来,超级管理员";
    }

}
