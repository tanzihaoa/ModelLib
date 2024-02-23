package com.tzh.mylibrary.util.img;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ImageDTO {
    public File file;
    public int status;
    public String image;

    /**
     * 类型 视频还是图片 1图片 2视频
     */
    public int type;

    public ImageDTO(File file){
        this.file = file;
    }

    public ImageDTO(String url){
        this.image = url;
    }

    public ImageDTO(File file, int status){
        this.file = file;
        this.status = status;

    }

    public static List<File> toListFile(List<ImageDTO> lists){
        List<File> list = new ArrayList<>();
        for (ImageDTO dto:lists) {
            list.add(dto.file);
        }
        return list;
    }


    public static List<ImageDTO> toListImageDTO(List<String> lists){
        List<ImageDTO> list = new ArrayList<>();
        for (String url:lists) {
            list.add(new ImageDTO(url));
        }
        return list;
    }

    public static List<String> toListString(List<ImageDTO> lists){
        List<String> list = new ArrayList<>();
        if(lists != null){
            for (ImageDTO dto:lists) {
                list.add(dto.image);
            }
        }
        return list;
    }

    public static String sHA1(Context context){
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }

            String result = hexString.toString();
            return result.substring(0, result.length()-1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
