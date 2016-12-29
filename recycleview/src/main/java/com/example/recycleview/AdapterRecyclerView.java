package com.example.recycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2016/12/20.
 */

public class AdapterRecyclerView extends RecyclerView.Adapter<AdapterRecyclerView.MyViewHolder>{

    public List<String> datas;
    public Context context;

    public AdapterRecyclerView(List<String> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

      View view = LayoutInflater.from(context).inflate(R.layout.item_recycle_view_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.tv.setText(datas.get(position));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{

    private ImageView iv;
    private TextView tv;

    public MyViewHolder(View itemView) {
        super(itemView);

        iv = (ImageView) itemView.findViewById(R.id.iv_item_recycler_view);
        tv = (TextView) itemView.findViewById(R.id.tv_item_recycler_view);

    }
}




}
