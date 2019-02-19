package cn.wenjig.crm;

import cn.wenjig.crm.common.enums.LogManage;
import cn.wenjig.crm.common.enums.PermissionManage;
import cn.wenjig.crm.common.local.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;

import java.util.Scanner;

@ServletComponentScan
@SpringBootApplication
public class CrmApplication {

    public static void main(String[] args) {
        ApplicationContext app = SpringApplication.run(CrmApplication.class, args);
        SpringUtil springUtil = new SpringUtil();
        springUtil.setApplicationContext(app);
        init();

        // 等待关闭服务器指令
        Scanner input = new Scanner(System.in);
        while (true) {
            int exit = input.nextInt();
            if (exit == 1008611) {
                System.exit(0);
            }
        }

    }

    private static void init() {
        PermissionManage.RUNTIME.getPermissionService().init();
        LogManage.LOG.getLog().init();
    }

}

