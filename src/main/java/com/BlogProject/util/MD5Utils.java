package com.BlogProject.util;

import java.security.MessageDigest;

public class MD5Utils {
    static final char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

    /**
     * This method returns a MD5 value of input String
     * @param plainText
     * @return MD5 value of plainText
     */

    public static String MD5Upper(String plainText) {
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 使用指定的字节更新摘要
            md.update(plainText.getBytes());

            // 获得密文
            byte[] mdResult = md.digest();
            // 把密文转换成十六进制的字符串形式
            int j = mdResult.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = mdResult[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];// 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移
                str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    //版权声明：本文为CSDN博主「Charge8」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
    //原文链接：https://blog.csdn.net/qq_42402854/article/details/89182536

//    public static void main(String[] args) {
//        System.out.println(MD5Upper(""));
//    }
}
