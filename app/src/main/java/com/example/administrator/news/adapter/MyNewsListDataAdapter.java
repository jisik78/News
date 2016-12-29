package com.example.administrator.news.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.news.R;
import com.example.administrator.news.entity.NewsBean;
import com.example.administrator.news.utils.CacheUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/12/14.
 */

public class MyNewsListDataAdapter extends BaseAdapter {

    private Context context;
    public List<NewsBean.ResultBean.DataBean> datas;

    public MyNewsListDataAdapter(Context context, List<NewsBean.ResultBean.DataBean> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View view = null;
        ViewHolder holder = null;

        if (convertView == null) {

            holder = new ViewHolder();
            view = View.inflate(context, R.layout.item_listview_fragment_news_activity_main, null);
            holder.iv_pic = (ImageView) view.findViewById(R.id.iv_item_listview_fragment_news_activity_main);
            holder.tv_title = (TextView) view.findViewById(R.id.tv_title_item_listview_fragment_news_activity_main);
            holder.tv_date = (TextView) view.findViewById(R.id.tv_date_item_listview_fragment_news_activity_main);

            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        /*
        //请求并加载图片方法一 ：NoHttp第三方
        final ImageView iv = holder.iv_pic;
        Request<Bitmap> imageRequest = NoHttp.createImageRequest(datas.get(i).getThumbnail_pic_s());

        NoHttpInstance.getInstance().add(1, imageRequest, new OnResponseListener<Bitmap>() {
            @Override
            public void onStart(int what) {
            }
            @Override
            public void onSucceed(int what, Response<Bitmap> response) {
                iv.setImageBitmap(response.get());
            }
            @Override
            public void onFailed(int what, Response<Bitmap> response) {
            }
            @Override
            public void onFinish(int what) {
            }
        });*/

        //请求并加载图片方法2 ： Glide第三方
        Glide.with(context).load(datas.get(i).getThumbnail_pic_s()).crossFade().into(holder.iv_pic);

        String readedUrl = CacheUtil.getStringFromSp(context,CacheUtil.READED);
        if(readedUrl.contains(datas.get(i).getUrl())){
            holder.tv_title.setTextColor(Color.GRAY);
        }else {
            holder.tv_title.setTextColor(Color.BLACK);
        }

        holder.tv_title.setText(datas.get(i).getTitle());
        holder.tv_date.setText(datas.get(i).getDate());

        return view;
    }

    class ViewHolder {

        ImageView iv_pic;
        TextView tv_title;
        TextView tv_date;

    }

}
