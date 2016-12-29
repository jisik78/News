package com.example.administrator.news.utils;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.RequestQueue;

/**
 * Created by Administrator on 2016/12/13.
 */

public class NoHttpInstance {

    private static RequestQueue instance;
    private static Object o = new Object();

    private NoHttpInstance() {

    }

    public static RequestQueue getInstance() {

        if (instance == null) {
            synchronized (o) {
                if (instance == null) {
                    instance = NoHttp.newRequestQueue();
                }
            }
        }
        return instance;
    }
}
