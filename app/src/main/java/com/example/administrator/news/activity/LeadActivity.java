package com.example.administrator.news.activity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.administrator.news.R;
import com.example.administrator.news.utils.CacheUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LeadActivity extends AppCompatActivity {

    @BindView(R.id.vp_activity_lead)
    ViewPager mVp_lead;
    @BindView(R.id.btn_activity_lead)
    Button mBtn_enter;
    @BindView(R.id.ll_indicators_activity_lead)
    LinearLayout llIndicators;

    //四个页面
    private int[] imgs = new int[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead);
        ButterKnife.bind(this);

        //初始化视图数据
        initData();
        //初始化指示器  四个小圆点
        initIndicator();

        //设置适配器
        MyPagerAdapter adapter = new MyPagerAdapter();
        mVp_lead.setAdapter(adapter);
        //初始化监听器
        mVp_lead.setOnPageChangeListener(new MyPagerListener());

    }

    /**
     * 初始化四个小圆点    indicator指示器
     */
    private void initIndicator() {

        for (int i = 0; i < imgs.length; i++) {
            View v = new View(this);
            v.setBackgroundResource(R.drawable.shape_indicator_normal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(30, 30);

            if (i != 0) {
                params.leftMargin = 30;
            } else {
                v.setBackgroundResource(R.drawable.shape_indicator_pressed);
            }

            v.setLayoutParams(params);
            llIndicators.addView(v);
        }
    }

    /**
     * 初始化视图数据
     */
    private void initData() {
        imgs[0] = R.drawable.bd;
        imgs[1] = R.drawable.welcome;
        imgs[2] = R.drawable.wy;
        imgs[3] = R.drawable.small;
    }

    /**按钮跳转点击事件*/
    @OnClick(R.id.btn_activity_lead)
    public  void  onClick(){
        Intent intent = new Intent(LeadActivity.this, MainActivity.class);
        startActivity(intent);
        CacheUtil.putBooleanIntoSp(LeadActivity.this,"is_first",false);
        finish();
    }





    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imgs.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView iv = new ImageView(LeadActivity.this);
            iv.setBackgroundResource(imgs[position]);
            container.addView(iv);

            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((ImageView) object);
        }
    }


    /**
     * 监听器
     */
    class MyPagerListener implements ViewPager.OnPageChangeListener {


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            for(int i = 0 ;i< imgs.length; i++){

                llIndicators.getChildAt(i).setBackgroundResource(R.drawable.shape_indicator_normal);
            }

            llIndicators.getChildAt(position).setBackgroundResource(R.drawable.shape_indicator_pressed);

            if(position >= 3){

                mBtn_enter.setVisibility(View.VISIBLE);
            }


        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}
