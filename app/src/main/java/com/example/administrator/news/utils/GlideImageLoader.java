package com.example.administrator.news.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by Administrator on 2016/12/16.
 */

public class GlideImageLoader extends ImageLoader{


    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {

        Glide.with(context).load(path).crossFade().centerCrop().into(imageView);


    }
}
