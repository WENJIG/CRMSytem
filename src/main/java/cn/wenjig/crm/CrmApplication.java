package cn.wenjig.crm;

import cn.wenjig.crm.common.local.PermissionManage;
import cn.wenjig.crm.common.local.SpringUtil;
import cn.wenjig.crm.data.entity.Employee;
import cn.wenjig.crm.repository.EmployeeRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;

@ServletComponentScan
@SpringBootApplication
public class CrmApplication {

    public static void main(String[] args) {
        ApplicationContext app = SpringApplication.run(CrmApplication.class, args);
        SpringUtil springUtil = new SpringUtil();
        springUtil.setApplicationContext(app);
        PermissionManage.RUNTIME.getPermissionService().init();
    }

}

