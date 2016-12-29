package com.example.administrator.news.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.news.R;
import com.example.administrator.news.activity.WebActivity;
import com.example.administrator.news.adapter.MyNewsListDataAdapter;
import com.example.administrator.news.entity.NewsBean;
import com.example.administrator.news.entity.NewsCollect;
import com.example.administrator.news.utils.CacheUtil;
import com.example.administrator.news.utils.Constant;
import com.example.administrator.news.utils.GlideImageLoader;
import com.example.administrator.news.utils.MyOrmliteOpenHelper;
import com.example.administrator.news.utils.NoHttpInstance;
import com.j256.ormlite.dao.Dao;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.youth.banner.Banner;
import com.youth.banner.Transformer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;


/**
 * Created by Administrator on 2016/12/14.
 */

public class NewsBeanFragment extends Fragment {

    private List<NewsBean.ResultBean.DataBean> datas;
    private String type;
    private ListView listView;
    private SwipeRefreshLayout layout; //下拉刷新视图
    private Banner banner;
    private MyNewsListDataAdapter adapter;
    private Dao<NewsCollect, Long> dao;

    public NewsBeanFragment(String type) {
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layout = (SwipeRefreshLayout) inflater.inflate(R.layout.layout_listview_fragment_news_activity_main, null);

        listView = (ListView) layout.findViewById(R.id.listview_fragment_news_activity_main);

        //registerForContextMenu(listView); //注册上下文菜单  listview item 长按 弹出上下文菜单

        initLayoutListener(); //初始化布局的监听器

        initBanner(); //初始化上方轮播图

        return layout;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getURLDatas();//获取网络数据
    }


    /**
     * 初始化轮播图
     */
    private void initBanner() {
        //广告轮播图
        banner = new Banner(getContext());
        //设置banner大小
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 370);
        banner.setLayoutParams(params);

        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置播放动画
        banner.setBannerAnimation(Transformer.Accordion);

        List<String> urls = new ArrayList<>();
        urls.add("http://v.999.com/uploads/2014021114220434304.jpg");
        urls.add("http://ent.coco90.com/Uploads/Article/20150831145207_12095.jpeg");
        urls.add("http://ent.coco90.com/Uploads/Article/20150619174044_10823.jpg");
        urls.add("http://img4.imgtn.bdimg.com/it/u=3867410966,3307117030&fm=214&gp=0.jpg");

        banner.setImages(urls);
        listView.addHeaderView(banner);
        banner.start();
    }


    /**
     * 初始化布局的监听器
     */
    private void initLayoutListener() {
        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        SystemClock.sleep(2000);

                        getURLDatas();//下拉刷新时重新获取数据

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                                layout.setRefreshing(false);
                            }
                        });
                    }
                }).start();
            }
        });
    }


    /**
     * 获取聚合数据的Url
     *
     * @param type
     * @return
     */
    public String getStringURL(String type) {
        String StringURL = Constant.AHALF + type + Constant.BHALF;
        return StringURL;
    }


    /**
     * 获取聚合数据 ListView数据
     */
    private void getURLDatas() {
        Request<String> topNewsStringRequest = NoHttp.createStringRequest(getStringURL(this.type));

        NoHttpInstance.getInstance().add(Constant.WHAT_NEWS_REQUEST, topNewsStringRequest, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {
            }

            //匹配数据
            @Override
            public void onSucceed(int what, Response<String> response) {

                String result = response.get();

                NewsBean newsBean = JSON.parseObject(result, NewsBean.class);

                datas = newsBean.getResult().getData();

                adapter = new MyNewsListDataAdapter(getContext(), datas);

                listView.setAdapter(adapter);
            }

            @Override
            public void onFailed(int what, Response<String> response) {

                Toast.makeText(getContext(), "response.getException():" + response.getException(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish(int what) {
            }
        });

        initListViewListener();  //listView 设置监听
    }

    /**
     * ListView设置监听器
     */
    private void initListViewListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String url = datas.get(i - 1).getUrl();
                String img_url = datas.get(i - 1).getThumbnail_pic_s();
                String title = datas.get(i - 1).getTitle();

                String readUrl = CacheUtil.getStringFromSp(getContext(), CacheUtil.READED);
                if (!readUrl.contains(url)) {
                    readUrl = readUrl + url + ",";
                }
                CacheUtil.putStringIntoSp(getContext(), CacheUtil.READED, readUrl);

                adapter.notifyDataSetChanged();

                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("title", title);
                intent.putExtra("img_url", img_url);
                startActivity(intent);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final NewsBean.ResultBean.DataBean dataBean = datas.get(i - 1);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setItems(new String[]{"分享", "收藏"}, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        switch (which) {
                            case 0:
                                OnekeyShare oks = new OnekeyShare();
                                //关闭sso授权
                                oks.disableSSOWhenAuthorize();
                                // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
                                oks.setTitle(dataBean.getTitle());
                                // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
                                oks.setTitleUrl(dataBean.getUrl());
                                // text是分享文本，所有平台都需要这个字段
                                oks.setText("新闻分享");
                                //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
                                oks.setImageUrl(dataBean.getThumbnail_pic_s());
                                // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                                //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
                                // url仅在微信（包括好友和朋友圈）中使用
                                oks.setUrl(dataBean.getUrl());
                                // comment是我对这条分享的评论，仅在人人网和QQ空间使用
                                oks.setComment("我是测试评论文本");
                                // site是分享此内容的网站名称，仅在QQ空间使用
                                oks.setSite("ShareSDK");
                                // siteUrl是分享此内容的网站地址，仅在QQ空间使用
                                oks.setSiteUrl(dataBean.getUrl());

                                // 启动分享GUI
                                oks.show(getActivity());

                                break;

                            case 1:
                                //获取设置ID
                                String url = dataBean.getUrl();
                                int indexStart = url.indexOf("mobile/");
                                int indexEnd = url.indexOf(".html");
                                String newIdStr = url.substring(indexStart + 7, indexEnd);
                                long IdLong = Long.parseLong(newIdStr);

                                NewsCollect newsCollect = new NewsCollect(IdLong, dataBean.getThumbnail_pic_s(), dataBean.getTitle(), dataBean.getDate(), dataBean.getUrl());
                                try {
                                    dao = MyOrmliteOpenHelper.getInstance(getContext()).getDao(NewsCollect.class);
                                    dao.createIfNotExists(newsCollect);

                                    //List<NewsCollect> newsCollects = dao.queryForAll();

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

                                break;
                        }
                    }
                }).show();

                return true;
            }
        });
    }


    /**上下文菜单 *//*   //要在上面onCreate 注册！！！！
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getActivity().getMenuInflater().inflate(R.menu.menu_news_fragment_collect_activity_main,menu);
    }

    */

    /**
     * 上下文菜单点击事件
     *//*
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_menu_collect_fragment_news:
                Toast.makeText(getContext(), "222222222", Toast.LENGTH_SHORT).show();

                break;
        }
        return super.onContextItemSelected(item);
    }*/
    @Override
    public void onResume() {
        super.onResume();
        banner.startAutoPlay(); //轮播图开始自动播放
    }

    @Override
    public void onPause() {
        super.onPause();
        banner.stopAutoPlay(); //轮播图停止自动播放
    }
}
