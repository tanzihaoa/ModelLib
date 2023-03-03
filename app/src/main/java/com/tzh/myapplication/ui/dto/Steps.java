package com.tzh.myapplication.ui.dto;

import android.net.Uri;

public class Steps {
    /* Data Field */
    public static final String ID = "_id";
    public static final String BEGIN_TIME = "_begin_time";
    public static final String END_TIME = "_end_time";
    // 0: NOT SUPPORT 1:REST 2:WALKING 3:RUNNING
    public static final String MODE = "_mode";
    public static final String STEPS = "_steps";
    /* Default sort order */
    public static final String DEFAULT_SORT_ORDER = "_id asc";
    /* Authority */
    public static final String AUTHORITY = "com.miui.providers.steps";
    /* Content URI */
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/item");

}


