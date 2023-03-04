package com.tzh.mylibrary.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

public class ViewMarginsUtil {
    public static void setMargins(View view,int leftMargin,int topMargin,int rightMargin,int bottomMargin){
        ViewGroup.MarginLayoutParams margin2;
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            margin2 = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        } else {
            margin2 = new ViewGroup.MarginLayoutParams(view.getLayoutParams());
        }

        margin2.setMargins(DpToUtil.dip2px(view.getContext(),leftMargin), DpToUtil.dip2px(view.getContext(),topMargin),DpToUtil.dip2px(view.getContext(), rightMargin),DpToUtil.dip2px(view.getContext(),bottomMargin));
        view.setLayoutParams(margin2);
    }

    public static void setMarginsToPx(View view,int leftMargin,int topMargin,int rightMargin,int bottomMargin){
        ViewGroup.MarginLayoutParams margin2;
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            margin2 = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        } else {
            margin2 = new ViewGroup.MarginLayoutParams(view.getLayoutParams());
        }

        margin2.setMargins(leftMargin,topMargin,rightMargin,bottomMargin);
        view.setLayoutParams(margin2);
    }

    public static void setViewWidth(Context context,View view, int width){
        ViewGroup.LayoutParams linearParams = view.getLayoutParams();
        linearParams.width = getScreenWidth(context) - DpToUtil.dip2px(context,width);
        view.setLayoutParams(linearParams);
    }

    public static int getScreenWidth(Context context){
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

}
