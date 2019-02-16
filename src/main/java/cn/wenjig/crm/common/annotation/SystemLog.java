package cn.wenjig.crm.common.annotation;

import cn.wenjig.crm.common.enums.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SystemLog {

    String description() default "无说明。";

    int level() default 0;

    OperationType operationType() default OperationType.UNKNOWN;

}
