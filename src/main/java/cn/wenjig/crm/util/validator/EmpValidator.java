package cn.wenjig.crm.util.validator;

public class EmpValidator {

    public static boolean all(String email, String password, String nickname, String realname, String phoneNo, String officeTel) {
        return email(email) && password(password) && nickname(nickname) && realname(realname) && phoneNo(phoneNo) && officeTel(officeTel);
    }

    public static boolean email(String s) {
        return s.matches("^[a-z0-9A-Z]+[- | a-z0-9A-Z . _]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
    }

    public static boolean password(String s) {
        return s.length() >= 8 && s.length() <= 16;
    }

    public static boolean nickname(String s) {
        return s.length() >= 1 && s.length() <= 16;
    }

    public static boolean realname(String s) {
        return s.length() >= 1 && s.length() <= 16;
    }

    public static boolean officeTel(String s) {
        return s.length() >= 1 && s.length() <= 30;
    }

    public static boolean phoneNo(String s) {
        return s.length() >= 11 && s.length() <= 30;
    }
}
