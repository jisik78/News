package com.example.administrator.news.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.news.R;
import com.example.administrator.news.entity.NewsCollect;
import com.example.administrator.news.utils.MyOrmliteOpenHelper;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2016/12/22.
 */

public class MyNewsCollectAdapter extends BaseAdapter {


    private List<NewsCollect> datas;
    private Context context;

    public MyNewsCollectAdapter(List<NewsCollect> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    public List<NewsCollect> getDatas() {
        return datas;
    }

    public void setDatas(List<NewsCollect> datas) {
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
                Glide.with(context).load(datas.get(i).getImg()).into(holder.iv_pic);
                holder.tv_title.setText(datas.get(i).getTitle());
                holder.tv_date.setText(datas.get(i).getDate());

        return view;
    }


    class ViewHolder{

        ImageView iv_pic;
        TextView tv_title;
        TextView tv_date;
    }



}
