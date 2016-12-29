package com.example.administrator.news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.news.R;
import com.example.administrator.news.utils.CacheUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/12.
 */

public class LogoActivity extends AppCompatActivity {
    @BindView(R.id.iv_activity_logo)
    ImageView mIv;
    @BindView(R.id.tv_counting_activity_logo)
    TextView mTv_counting;

    private int leftTime = 3;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:

                    calcLeftTime();
                    break;
            }
        }
    };

    /**
     * 计算倒计时时间
     */
    private void calcLeftTime() {
        if (leftTime > 0) {
            String newText = "广告倒计时：" + leftTime-- + "秒";
            mTv_counting.setText(newText);
            handler.sendEmptyMessageDelayed(0, 1000);
        } else {
            boolean booleanFromSp = CacheUtil.getBooleanFromSp(LogoActivity.this, CacheUtil.IS_FIRST,true);

            if (booleanFromSp) {

                Intent intent = new Intent(LogoActivity.this, LeadActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(LogoActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        ButterKnife.bind(this);

        initAnim();
    }


    /**
     * 初始化动画
     */
    private void initAnim() {

        /*AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(3200);
        mIv.startAnimation(alphaAnimation);*/

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.anim_logo);
        mIv.startAnimation(anim);

        handler.sendEmptyMessageDelayed(0, 1000);

    }


}
