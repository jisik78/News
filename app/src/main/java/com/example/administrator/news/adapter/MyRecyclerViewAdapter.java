package com.example.administrator.news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.news.R;
import com.example.administrator.news.entity.NewsBean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/21.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>{


    public OnRecyclerViewItemClickListener listener;
    private Context context;
    private List<NewsBean.ResultBean.DataBean> datas;

    public MyRecyclerViewAdapter(Context context, List<NewsBean.ResultBean.DataBean> datas) {
        this.context = context;
        this.datas = datas;
    }

    public List<NewsBean.ResultBean.DataBean> getDatas(){

        return datas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(context).inflate(R.layout.item_recyc_view_fragment_image_activity_main,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Glide.with(context).load(datas.get(position).getThumbnail_pic_s()).into(holder.iv);
        holder.tv.setText(datas.get(position).getAuthor_name());

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

       ImageView iv;
        TextView tv;

        public MyViewHolder(final View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv_item_recyc_view_faragment_image);
            tv = (TextView) itemView.findViewById(R.id.tv_item_recyc_view_fragment_image);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    listener.onItemClick(itemView,getLayoutPosition());
                }
            });

        }

    }

    //设置监听器的方法
    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener){

        this.listener = listener;
    }


    //自定义item监听器
    public interface OnRecyclerViewItemClickListener{

        void onItemClick(View v, int position);
    }



}
