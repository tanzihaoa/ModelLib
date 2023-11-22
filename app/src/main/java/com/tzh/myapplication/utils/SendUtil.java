package com.tzh.myapplication.utils;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;

import androidx.core.content.FileProvider;

import java.io.File;

public class SendUtil {
    public static void sendSms(Context context, String phone, String text, File file, PendingIntent mIntent){

        Uri fileUri = FileProvider.getUriForFile(context, "com.tzh.myapplication.fileprovider", file);
//        Uri imageUri = Uri.parse(file.getAbsolutePath());
        // 创建发送彩信的 Intent
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra("address", phone);
        intent.putExtra("subject", "彩信主题");
        intent.putExtra("sms_body", text);
        intent.putExtra(Intent.EXTRA_STREAM, fileUri);
        intent.setType("image/*");

        // 发送彩信
        SmsManager.getDefault().sendMultimediaMessage(context, fileUri,"application/vnd.wap.mms-message", intent.getExtras(), mIntent);
    }

    public static void send(Context mContext){
//        SmsManager.getDefault().sendMultimediaMessage(context, uri, null, null, intent);
    }
}
