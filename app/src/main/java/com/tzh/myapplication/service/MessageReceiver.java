package com.tzh.myapplication.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.tzh.myapplication.livedata.SmsLiveData;
import com.tzh.myapplication.ui.dto.SmsDto;
import com.tzh.myapplication.utils.DateTime;

public class MessageReceiver extends BroadcastReceiver {

    private static final String SMS_RECEIVER_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    @Override
    public void onReceive(Context context, Intent intent) {
        StringBuilder sBuilder = new StringBuilder();
        SmsDto dto = new SmsDto();
        String format = intent.getStringExtra("format");
        if(SMS_RECEIVER_ACTION.equals(intent.getAction())) {
            Bundle bundle = intent.getExtras();
            if(null != bundle) {
                Object[] pdus = (Object[])bundle.get("pdus");
                assert pdus != null;
                SmsMessage[] messages = new SmsMessage[pdus.length];
                for(int i = 0; i < messages.length; ++i) {
                    messages[i] = SmsMessage.createFromPdu((byte[])pdus[i],format);
                }
                for(SmsMessage msg : messages) {
                    sBuilder.append("来自：").append(msg.getDisplayOriginatingAddress()).append("\n").append("短信内容：");
                    sBuilder.append(msg.getDisplayMessageBody()).append("\n");
                    dto.setMobile(msg.getDisplayOriginatingAddress());
                    dto.setContent(dto.getContent() != null?dto.getContent()+msg.getDisplayMessageBody():msg.getDisplayMessageBody());
                    dto.setStatus("接收短信");
                    dto.setTime(DateTime.getNowTime());
                }
                SmsLiveData.Companion.getInstance().setValue(dto);
            }
        }
        Toast.makeText(context, "您收到了一条短信!!\n" + sBuilder, Toast.LENGTH_LONG).show();
    }

}