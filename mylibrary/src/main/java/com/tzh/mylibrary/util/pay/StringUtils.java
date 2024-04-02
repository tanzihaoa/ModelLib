package com.tzh.mylibrary.util.pay;

import android.text.TextUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static final char UNDERLINE = '_';

    public static String removeBlanks(String content) {
        return content.trim().replaceAll("\n( *\n)+", "\n");
    }

    public static boolean isEmpty(String content) {
        return content == null || content.equals("");
    }

    public static boolean isValid(String content) {
        return content != null && !isEmpty(content.trim());
    }

    public static String hideMobile(String mobile) {
        if (mobile.length() == 11) {
            return mobile.substring(0, 3) + "****" + mobile.substring(7);
        }
        return mobile;
    }

    public static String hideIDCode(String idCode) {
        if (idCode.length() > 10) {
            return idCode.substring(0, 6) + "****" + idCode.substring(idCode.length() - 4);
        }
        return idCode;
    }

    public static String insertBlank(String content, int subLength) {
        content = content.replaceAll(" ", "");
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < content.length()) {
            sb.append(content.charAt(i));
            i++;
            if (i % subLength == 0 && i < content.length()) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    public static String camelToUnderline(String param) {
        if (isValid(param) && param.matches(".*[A-Z].*")) {
            final int len = param.length();
            final StringBuilder sb = new StringBuilder();
            char c;
            for (int i = 0; i < len; i++) {
                c = param.charAt(i);
                if (Character.isUpperCase(c)) {
                    sb.append(UNDERLINE);
                    sb.append(Character.toLowerCase(c));
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        }
        return param;
    }

    public static String underlineToCamel(String param) {
        if (isValid(param) && param.matches(".*_.*")) {
            final int len = param.length();
            final StringBuilder sb = new StringBuilder();
            char c;
            for (int i = 0; i < len; i++) {
                c = param.charAt(i);
                if (c == UNDERLINE) {
                    if (++i < len) {
                        sb.append(Character.toUpperCase(param.charAt(i)));
                    }
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        }
        return param;
    }

    public static boolean isNumeric(String num) {
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(num);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static String listToString(List<String> list){
        String text = "";
        for (String str : list) {
            if(TextUtils.isEmpty(text)){
                text += str;
            }else{
                text += "," + str;
            }
        }

        return text;
    }
}