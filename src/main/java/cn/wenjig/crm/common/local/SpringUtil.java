package cn.wenjig.crm.common.local;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringUtil implements ApplicationContextAware{

    // Spring应用上下文环境
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.applicationContext = applicationContext;
    }

    private static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    //通过名称获取上下文的bean 类名首字母小写
    public static Object getBean(String beanId) throws BeansException {
        return getApplicationContext().getBean(beanId);
    }

    //通过类型获取上下文中的bean
    public static Object getBean(Class<?> requiredType){
        return getApplicationContext().getBean(requiredType);
    }
}
