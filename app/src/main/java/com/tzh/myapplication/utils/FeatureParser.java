package com.tzh.myapplication.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.tzh.myapplication.ui.dto.StepDto;
import com.tzh.myapplication.ui.dto.Steps;

import java.lang.reflect.Method;
import java.util.LinkedList;

public class FeatureParser {
    public static String[] projection = new String[] {
            Steps.ID,
            Steps.BEGIN_TIME,
            Steps.END_TIME,
            Steps.MODE,
            Steps.STEPS
    };

    public static boolean getBoolean(String name, boolean defaultValue) {
        try {
            Class featureParserClass = Class.forName("miui.util.FeatureParser");
            Method method = featureParserClass.getMethod("getBoolean", String.class, boolean.class);
            return (Boolean) method.invoke(null, name, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    //查询小米手机步数数据
    public static LinkedList<StepDto> getAllSteps(Context context) {
        LinkedList<StepDto> steps = new LinkedList<StepDto>();
        Cursor cursor = context.getContentResolver().query(
                Steps.CONTENT_URI, projection,
                Steps.BEGIN_TIME + " >= ?" ,//查询条件  begin_time大于今天0点 为今天的值
                new String[]{DateTime.getInstance().getNowTime_Long()+""},//判断值
                Steps.DEFAULT_SORT_ORDER);
        if (cursor.moveToFirst()) {
            do {
                StepDto s = new StepDto(cursor.getInt(0), cursor.getLong(1), cursor.getLong(2),
                        cursor.getInt(3),
                        cursor.getInt(4));
                steps.add(s);
            } while (cursor.moveToNext());
        }

        return steps;
    }
}
