package com.example.administrator.news.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.example.administrator.news.R;
import com.example.administrator.news.utils.NoHttpInstance;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebActivity extends AppCompatActivity {
    @BindView(R.id.webview_activity_web)
    WebView webview;
    @BindView(R.id.toolbar_activity_web)
    Toolbar toolbar;
    @BindView(R.id.pb_activity_web)
    ProgressBar pb;
    @BindView(R.id.iv_activity_web)
    ImageView imageView;
    @BindView(R.id.collapsing_toolbar_activity_web)
    CollapsingToolbarLayout collapsingToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);

        //初始化Toolbar
        initToolBar();

        //获取意图 url
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String img_url = intent.getStringExtra("img_url");

        //初始化ImageView
        Glide.with(this).load(img_url).into(imageView);

        //CollapsingToolbar设置颜色
        initCollapsingToobar(img_url);
        //初始化网页视图
        initWebView(url);

    }

    /**
     *初始化CollapsingToolbar
     */
    private void initCollapsingToobar(String img_url) {
        Request<Bitmap> imageRequest = NoHttp.createImageRequest(img_url);
        NoHttpInstance.getInstance().add(0, imageRequest, new OnResponseListener<Bitmap>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<Bitmap> response) {

                int toolbarcolor = Palette.from(response.get()).generate().getMutedColor(getResources().getColor(R.color.colorPrimary));
                collapsingToolbar.setContentScrimColor(toolbarcolor);
            }

            @Override
            public void onFailed(int what, Response<Bitmap> response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });
    }

    /**
     *初始化网页视图
     */
    private void initWebView(String url) {
        WebSettings settings = webview.getSettings();
        settings.setSupportZoom(true);
        settings.setDisplayZoomControls(true);
        //settings.setJavaScriptEnabled(true);   //js广告会显示出来

        webview.loadUrl(url);
        //设置（点击图片后）在此活动页上查看图片，而非用浏览器查看
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                //toolbar设置标题
                collapsingToolbar.setTitle(title);

            }
        });

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                webview.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                pb.setVisibility(View.GONE);
                webview.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 初始化ToolBar
     */
    private void initToolBar() {
        //设置Toolbar
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();

        if (supportActionBar != null) {
            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            //supportActionBar.setTitle(title);
        }
    }
/*
    //左上角返回按钮  方法一
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }*/

    //左上角返回按钮  方法二
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
