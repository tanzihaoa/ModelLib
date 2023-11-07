package com.tzh.myapplication.utils;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.Telephony;
import android.telephony.SmsManager;

import com.klinker.android.send_message.Message;
import com.klinker.android.send_message.Settings;
import com.klinker.android.send_message.Transaction;
import com.tzh.myapplication.R;

import java.io.File;
import java.util.ArrayList;

public class SendUtil {
    public static void sendSms(Context context, String phone, String text, File file, PendingIntent intent){
        // 创建一个ContentValues对象，用于存储彩信的相关信息
        ContentValues values = new ContentValues();
        values.put(Telephony.Mms.Addr.ADDRESS,phone); // 收件人号码
        values.put(Telephony.Mms.Addr.CHARSET, "彩信主题");

        // 创建一个ArrayList对象，用于存储彩信的附件
        ArrayList<ContentValues> attachments = new ArrayList<>();
        ContentValues attachment = new ContentValues();
        attachment.put(Telephony.Mms.Part.CONTENT_TYPE, "image/jpeg");
        attachment.put(Telephony.Mms.Part.FILENAME, file.getName());
        attachment.put(Telephony.Mms.Part.NAME,file.getName());
        attachment.put(Telephony.Mms.Part.CONTENT_ID, "<image>");
        attachment.put(Telephony.Mms.Part.CONTENT_LOCATION,file.getName());
        attachment.put(Telephony.Mms.Part.MSG_ID, 1);
        attachment.put(Telephony.Mms.Part.SEQ, 1);
        attachment.put(Telephony.Mms.Part.TEXT,text);
        attachment.put(Telephony.Mms.Part._DATA,file.getAbsolutePath());
        attachments.add(attachment);

        // 插入彩信的Uri
        Uri uri = context.getContentResolver().insert(Telephony.Mms.Outbox.CONTENT_URI, values);

        // 将附件插入到Uri中
        for (ContentValues att : attachments) {
            att.put(Telephony.Mms.Part.MSG_ID, uri.getLastPathSegment());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                context.getContentResolver().insert(Telephony.Mms.Part.CONTENT_URI, att);
            }
        }

        // 发送彩信
        SmsManager.getDefault().sendMultimediaMessage(context, uri, phone, null, intent);
    }


    public static void sendMms(Context mContext,String text,String phone, Bitmap mBitmap){
        Settings settings = new Settings();
        settings.setUseSystemSending(true);
        Transaction transaction = new Transaction(mContext, settings);
        Message message = new Message(text, phone);
        message.setImage(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.load_state_empty));
        transaction.sendNewMessage(message, Transaction.NO_THREAD_ID);
    }
}
