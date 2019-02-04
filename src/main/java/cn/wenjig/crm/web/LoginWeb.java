package cn.wenjig.crm.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/login")
public class LoginWeb {

    @RequestMapping(value = "/")
    public String getLoginView() {
        return "/login.html";
    }


}
