package cn.wenjig.crm.util.validator;

import cn.wenjig.crm.common.annotation.validation.NotHaveBlank;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 数据校验类 禁止包含空格 " "
 */
public class NotHaveBlankValidator implements ConstraintValidator<NotHaveBlank, String> {
    @Override
    public void initialize(NotHaveBlank constraintAnnotation) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s != null && s.contains(" ")) {
            return false;
        }
        return true;
    }
}
