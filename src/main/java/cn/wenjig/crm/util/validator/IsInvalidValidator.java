package cn.wenjig.crm.util.validator;

public class IsInvalidValidator {

    /**
     * @param str
     * @Description: 所有条件全部检查
     * @Return boolean
     */
    public static boolean all(String str) {
        return isNull(str) || isBlank(str) || containBlank(str);
    }

    /**
     * @param str
     * @Description: 字符串是否为null
     * @Return boolean
     */
    private static boolean isNull(String str) {
        return str == null;
    }

    /**
     * @param str
     * @Description: 字符串是否为空
     * @Return boolean
     */
    private static boolean isBlank(String str) {
        return str.equals("");
    }

    /**
     * @param str
     * @Description: 字符串是否包含空格
     * @Return boolean
     */
    private static boolean containBlank(String str) {
        char[] c = str.toCharArray();
        for (char aC : c) {
            if (aC == (char) 32) {
                return true;
            }
        }
        return false;
    }

    /**
     * @Description: 字符串是否是正整数
     * @param str
     * @Return boolean
     */
    public static boolean isNumber(String str) {
        return str.matches("^\\d+$");
    }

}
