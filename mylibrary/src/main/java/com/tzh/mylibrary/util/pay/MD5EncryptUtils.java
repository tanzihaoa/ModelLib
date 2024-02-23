package com.tzh.mylibrary.util.pay;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * 类名：MD5EncryptUtils 类描述：MD5加密
 * ----------------------------------------------------------
 * 
 * 
 * ---------------------------------------------------------- Copyright
 */

public class MD5EncryptUtils {

	private static final String ALGORIHM = "MD5";

	public MD5EncryptUtils() {
	}

	/**
	 * 方法说明：调用此方法进行md5加密 方法名称：md5Encrypt
	 * 
	 * @param text
	 * @return 返回值：String
	 */
	public static String md5Encrypt(String text) {
		try {
			if (text == null || "".equals(text)) {
				return null;
			}
			byte byteTmpe[];
			MessageDigest md = MessageDigest.getInstance("MD5");
			byteTmpe = md.digest(text.getBytes());
			return byte2hex(byteTmpe);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String byte2hex(byte b[]) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0xff);
			if (stmp.length() == 1) {
				hs = (new StringBuilder(String.valueOf(hs))).append("0")
						.append(stmp).toString();
			} else {
				hs = (new StringBuilder(String.valueOf(hs))).append(stmp)
						.toString();
			}
		}

		return hs.toUpperCase();
	}

	public static String encrypt(String plainText) {
		String str = "";
		if (!TextUtils.isEmpty(plainText)) {
			StringBuffer s = new StringBuffer(md5Encrypt(plainText));
			s = s.reverse();
			plainText = s.toString();
			str = md5Encrypt(plainText).toUpperCase();
		}
		return str;
	}
	
	
	public static String MD5Encode(String origin, String charsetname) {
        String resultString = null;
        try {  
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname)) {
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes()));
			} else {
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes(charsetname)));
			}
        } catch (Exception exception) {
        }  
        return resultString;  
    }
	
	private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)  
            resultSb.append(byteToHexString(b[i]));  
  
        return resultSb.toString();  
    }  
  
    private static String byteToHexString(byte b) {
        int n = b;  
        if (n < 0) {
			n += 256;
		}
        int d1 = n / 16;  
        int d2 = n % 16;  
        return hexDigits[d1] + hexDigits[d2];  
    }
    
    private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
        "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };




}
