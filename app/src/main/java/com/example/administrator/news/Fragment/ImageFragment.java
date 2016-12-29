package com.example.administrator.news.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.news.R;
import com.example.administrator.news.activity.WebActivity;
import com.example.administrator.news.adapter.MyRecyclerViewAdapter;
import com.example.administrator.news.entity.NewsBean;
import com.example.administrator.news.utils.Constant;
import com.example.administrator.news.utils.NoHttpInstance;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

/**
 * Created by Administrator on 2016/12/14.
 */

public class ImageFragment extends Fragment{

    private RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_fragment_image_activity_main,container,false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recy_view_image_fragment_activity_main);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));

        String urlStr = Constant.AHALF + "shishang"+Constant.BHALF;
        Request<String> stringRequest = NoHttp.createStringRequest(urlStr);
        NoHttpInstance.getInstance().add(0, stringRequest, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {
                
            }

            @Override
            public void onSucceed(int what, Response<String> response) {

                String jsonStr = response.get();

                NewsBean newsBean = JSON.parseObject(jsonStr,NewsBean.class);
                final MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(getContext(),newsBean.getResult().getData());

                //设置Item监听器（自定义监听器，在自定义adapter中）
                adapter.setOnRecyclerViewItemClickListener(new MyRecyclerViewAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {

                        String url = adapter.getDatas().get(position).getUrl();
                        String img_url = adapter.getDatas().get(position).getThumbnail_pic_s();
                        Intent intent = new Intent(getContext(), WebActivity.class);

                        intent.putExtra("url",url);
                        intent.putExtra("img_url",img_url);

                        startActivity(intent);
                    }
                });

                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailed(int what, Response<String> response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });

        return view;
    }
}
