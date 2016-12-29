package com.example.administrator.news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.news.R;
import com.example.administrator.news.entity.MyMessage;

import java.util.List;

/**
 * Created by Administrator on 2016/12/23.
 */

public class MyChatListAdapter extends BaseAdapter {

    private List<MyMessage> datas;
    private Context context;

    public List<MyMessage> getDatas() {
        return datas;
    }

    public void setDatas(List<MyMessage> datas) {
        this.datas = datas;
    }

    public MyChatListAdapter(List<MyMessage> datas, Context context) {
        this.datas = datas;
        this.context = context;
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

        ChatViewHolder holder = null;

        if (getItemViewType(i) == 0) {
            if (convertView == null) {

                holder = new ChatViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_from_message_chat_fragment_activity_main, viewGroup, false);
                holder.textView = (TextView) convertView.findViewById(R.id.tv_item_message_from_chat_fragment);
                convertView.setTag(holder);

            } else {
                holder = (ChatViewHolder) convertView.getTag();
            }
            holder.textView.setText(datas.get(i).getText());

        } else if (getItemViewType(i) == 1) {

            if (convertView == null) {
                holder = new ChatViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_to_message_chat_fragment_activity_main, viewGroup, false);
                holder.textView = (TextView) convertView.findViewById(R.id.tv_item_message_to_chat_fragment);
                convertView.setTag(holder);
            } else {
                holder = (ChatViewHolder) convertView.getTag();
            }
            holder.textView.setText(datas.get(i).getText());

        }
        return convertView;
    }


    //标准写法
    @Override
    public int getItemViewType(int position) {

        int type = 0;

        switch (datas.get(position).getType()) {

            case 0:
                type = 0;
                return type;
            case 1:
                type = 1 ;
                return type ;
        }

        return type;


    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    class ChatViewHolder {

        TextView textView;
    }
}
