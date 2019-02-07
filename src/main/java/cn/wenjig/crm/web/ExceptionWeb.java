package cn.wenjig.crm.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/exception")
public class ExceptionWeb {

    @RequestMapping(value = "/403")
    public String get403View() {
        return "/error/403.html";
    }

}
