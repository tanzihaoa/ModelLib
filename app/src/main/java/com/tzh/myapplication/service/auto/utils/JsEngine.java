package com.tzh.myapplication.service.auto.utils;


import android.util.Log;

import com.tzh.myapplication.R;
import com.tzh.myapplication.utils.ToastUtil;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;


public class JsEngine {
    public static String run(String jsCode) {
        //jsCode = jsCode.replace("\n", "");
        Log.i("js运行代码", jsCode);
        Context rhino = Context.enter();
        rhino.setOptimizationLevel(-1);
        Object result = null;
        try {
            Scriptable scope = rhino.initStandardObjects();
            result = rhino.evaluateString(scope, jsCode, "JavaScript", 1, null);
        } catch (Throwable e) {
            Log.i("","JS执行错误:" + e.toString() + " 错误代码:" + jsCode);
//            ToastUtil.show(R.string.js_error);
        } finally {
            Context.exit();
        }
        if (result != null) return result.toString();
        return "Undefined";
    }
}
