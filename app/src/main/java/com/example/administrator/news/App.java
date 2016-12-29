package com.example.administrator.news;

import android.app.Application;
import android.widget.Toast;

import com.wilddog.client.SyncReference;
import com.wilddog.client.WilddogSync;
import com.wilddog.wilddogauth.WilddogAuth;
import com.wilddog.wilddogauth.model.WilddogUser;
import com.wilddog.wilddogcore.WilddogApp;
import com.wilddog.wilddogcore.WilddogOptions;
import com.yolanda.nohttp.NoHttp;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;
import cn.smssdk.SMSSDK;

/**
 * Created by Administrator on 2016/12/13.
 */

public class App extends Application {

    public static WilddogAuth auth;
    public static SyncReference ref;
    public static WilddogUser user;
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化NoHttp
        NoHttp.initialize(this);

        //初始化野狗
        WilddogOptions options = new WilddogOptions.Builder().setSyncUrl("https://news18940198390.wilddogio.com").build();
        WilddogApp.initializeApp(this, options);
        //认证对象
        auth = WilddogAuth.getInstance();
        //获取SyncReference 实例
        ref = WilddogSync.getInstance().getReference("users");
        //拿到当前用户
        WilddogUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            user = currentUser;
        }else {
            Toast.makeText(this, "用户未登陆", Toast.LENGTH_SHORT).show();
        }

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        ShareSDK.initSDK(this);

        SMSSDK.initSDK(this, "1a47a39785dd0", "c110954926774709a09dcfba622c9830");



    }

}
