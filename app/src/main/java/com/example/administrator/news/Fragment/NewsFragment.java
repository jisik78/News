package com.example.administrator.news.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.administrator.news.R;
import com.example.administrator.news.adapter.MyNewsPagerAdapter;
import com.example.administrator.news.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/14.
 */

public class NewsFragment extends Fragment {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private List<Fragment> fragments;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.layout_fragment_news_activity_main, null);
        viewPager = (ViewPager) linearLayout.findViewById(R.id.vp_fragment_news_activity_main);
        tabLayout = (TabLayout) linearLayout.findViewById(R.id.tablayout_fragment_news_activity_main);
        ButterKnife.bind(this, viewPager);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        return linearLayout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragments = new ArrayList<>();
        for (int i = 0; i < Constant.NEWS_TYPES.length; i++) {
            fragments.add(new NewsBeanFragment(Constant.NEWS_TYPES[i]));
        }

        MyNewsPagerAdapter adapter = new MyNewsPagerAdapter(getFragmentManager(), fragments);
        viewPager.setAdapter(adapter);

    }

    /*
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_fragment_news_activity_main, null);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Request<String> stringRequest = NoHttp.createStringRequest(Constant.BASE_URL+Constant.NEWS_QUERY);
        NoHttpInstance.getInstance().add(Constant.WHAT_NEWS_REQUEST, stringRequest, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                String result = response.get();

                NewsBean newsBean = JSON.parseObject(result,NewsBean.class);

                Toast.makeText(getContext(), newsBean.getResult().getData().get(2).getTitle(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailed(int what, Response<String> response) {
                Toast.makeText(getContext(), "response.getException():"+response.getException(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish(int what) {

            }
        });

    }*/
}
