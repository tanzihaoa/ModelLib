package com.tzh.mylibrary.util.general;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.widget.TextView;
import androidx.databinding.BindingAdapter;
import com.tzh.mylibrary.type.FontType;

/**
 * 字体设置相关工具类
 */
public class FontUtil {
    private static Typeface SJXZType;
    public static void initFont(Context context){
        AssetManager mgr= context.getAssets();//得到AssetManager
        SJXZType  = Typeface.createFromAsset(mgr, "font/"+ FontType.SJXZ+".TTF");//根据路径得到Typeface
    }

    /**
     * 设置字体
     * @param textView textView
     * @param name 字体包名字
     */
    public static void loadFont(TextView textView,@FontType String name){
        if(SJXZType == null){
            initFont(textView.getContext());
        }
        switch (name){
            case FontType.SJXZ:
                textView.setTypeface(SJXZType);//设置字体
                break;
        }
    }


    /**
     * 设置字体
     * @param textView textView
     */
    @BindingAdapter(value = "loadSJXZFont")
    public static void loadSJXZFont(TextView textView,int type){
        if(SJXZType == null){
            initFont(textView.getContext());
        }

        textView.setTypeface(SJXZType);//设置字体
    }
}
