package com.xxx.seckill.util;


import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

/**
 * MD5工具类
 *
 */
@Component
public class MD5Util {

    public static String md5(String src){
        //DigestUtils生成MD5密码
        return DigestUtils.md5Hex(src);
    }

    private static final String salt = "1a2b3c4d";

    /**
     * 对输入的密码进行第一次加密
     * @param inputPass
     * @return
     */
    public static String inputPassToFromPass(String inputPass){
        //对密码进行混淆
        String str = "" + salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    /**
     * 二次加密：对客户端传入到服务器的密码进行二次加密。
     * @param formPass 第一次加密过后的数据
     * @param salt
     * @return
     */
    public static String formPassToDBPass(String formPass, String salt){
        String str = "" + salt.charAt(0) + salt.charAt(2) + formPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    /**
     * 对明文密码进行两次加密，第二次加密指定盐，返回加密后的密文。
     * @param inputPass
     * @param salt
     * @return
     */
    public static String inputPassToDBPass(String inputPass, String salt){
        String fromPass = inputPassToFromPass(inputPass);
        String dbPass = formPassToDBPass(fromPass, salt);
        return dbPass;
    }


    public static void main(String[] args) {
//        System.out.println(inputPassToFromPass("123456"));
//        System.out.println(formPassToDBPass("ce21b747de5af71ab5c2e20ff0a60eea", "1a2b3c4d"));
//        System.out.println(inputPassToDBPass("123456", "1a2b3c4d"));


        System.out.println(inputPassToDBPass("11111", "1a2b3c4d"));
        System.out.println(inputPassToDBPass("123456", "1a2b3c4d"));
    }
}
