package com.tzh.myapplication.network.http;

import java.io.Serializable;

/**
 * Created on 2020/11/29 0001.
 */
public class BaseBean implements Serializable{
    public StatusBean status;
    public String result;

    public static class StatusBean implements Serializable {
        /**
         * status_code : 0
         * status_reason : ""
         */
        public int status_code;
        public String status_reason;
    }
}
