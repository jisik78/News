package com.example.administrator.news.activity;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.news.App;
import com.example.administrator.news.R;
import com.example.administrator.news.Fragment.ChatFragment;
import com.example.administrator.news.Fragment.LikeFragment;
import com.example.administrator.news.Fragment.NewsFragment;
import com.example.administrator.news.Fragment.ImageFragment;
import com.example.administrator.news.utils.CacheUtil;
import com.example.administrator.news.utils.Constant;
import com.wilddog.client.DataSnapshot;
import com.wilddog.client.SyncError;
import com.wilddog.client.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_activity_main)
    Toolbar toolbarActivityMain;
    @BindView(R.id.drawerlayout_activity_main)
    DrawerLayout drawerlayoutActivityMain;
    @BindView(R.id.navigation_activity_main)
    NavigationView navigationActivityMain;
    CircleImageView circleImageView;

    private List<Fragment> fragments;
    private FragmentManager fragmentManager;
    private Fragment currFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //双击选中 alt+ insert 选butterKnife
        ButterKnife.bind(this);
        //初始化 navigation上的 圆形View
        circleImageView = (CircleImageView) navigationActivityMain.getHeaderView(0).findViewById(R.id.circleimageView_navigation_activity_main);

        //初始化Toolbar
        initToolBar();

        //初始化所有片断
        initFragments();

        //初始化导航视图
        initNavigation();

        //初始化监听
        initIconListener();

        //初始化野狗
        initWildDog();
    }

    /**
     * 初始化监听头像监听
     */
    private void initIconListener() {
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (App.user != null) {
                    //显示更换头像对话框
                    showDialog();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setItems(new String[]{"手机注册", "邮箱注册"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            switch (i) {
                                case 0:
                                    //打开注册页面
                                    RegisterPage registerPage = new RegisterPage();
                                    registerPage.setRegisterCallback(new EventHandler() {
                                        public void afterEvent(int event, int result, Object data) {
                                            // 解析注册结果
                                            if (result == SMSSDK.RESULT_COMPLETE) {
                                                @SuppressWarnings("unchecked")
                                                HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                                                String country = (String) phoneMap.get("country");
                                                String phone = (String) phoneMap.get("phone");

                                                // 提交用户信息（此方法可以不调用）
                                                registerUser(country, phone);
                                            }
                                        }
                                    });
                                    registerPage.show(MainActivity.this);
                                    break;
                                case 1:
                                    //如果没有登录用户则跳转到登录注册页面
                                    Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                                    startActivityForResult(intent, Constant.GET_IMAGE_FROM_SERVICE);
                                    break;
                            }
                        }
                    }).show();

                }
            }
        });
    }

    private void registerUser(String country, String phone) {

        Toast.makeText(this, country + "" + phone, Toast.LENGTH_SHORT).show();


    }


    /**
     * 初始化野狗
     */
    private void initWildDog() {
        if (App.user != null) {

            //野狗的的value改变监听事件
            ValueEventListener postListener = new ValueEventListener() {
                //当value改变时调用该方法
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String imgStr = (String) dataSnapshot.getValue();

                    //把string转化成bitmap
                    byte[] decode = Base64.decode(imgStr, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);

                    circleImageView.setImageBitmap(bitmap);

                }

                @Override
                public void onCancelled(SyncError syncError) {
                    // 获取数据失败，打印错误信息。
                }
            };
            //给当前数据库设置数据改变监听器
            App.ref.child(App.user.getUid()).addValueEventListener(postListener);
        }
    }


    /**
     * 初始化导航视图
     */
    private void initNavigation() {
        //默认选择新闻
        navigationActivityMain.setCheckedItem(R.id.news_navigation);
        //默认切换第一个Fragment
        currFragment = fragments.get(1);

        fragmentManager.beginTransaction().add(R.id.fl_activity_main_content, currFragment).commit();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerlayoutActivityMain, toolbarActivityMain, R.string.open, R.string.close);
        // toggle.syncState();

        //初始化侧拉菜单的点击事件  切换Fragments
        initNavigationListener();
    }

    /**
     * 初始化侧拉菜单的点击事件  切换Fragments
     */
    private void initNavigationListener() {
        //侧拉菜单的点击事件
        navigationActivityMain.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int index = 0;
                switch (item.getItemId()) {
                    case R.id.like_navigation:
                        index = 0;
                        toolbarActivityMain.setTitle("收藏");
                        break;
                    case R.id.news_navigation:
                        index = 1;
                        toolbarActivityMain.setTitle("新闻");
                        break;
                    case R.id.dialog_navigation:
                        index = 2;
                        toolbarActivityMain.setTitle("聊天");
                        break;
                    case R.id.setting_navigation:
                        index = 3;
                        toolbarActivityMain.setTitle("图片");
                        break;

                }
                //切换Fragments
                TabFragments(index);
                return true;
            }
        });
    }

    /**
     * 切换Fragments
     *
     * @param index
     */
    private void TabFragments(int index) {
        //打开管理事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment nextFragment = fragments.get(index);

        if (nextFragment != currFragment) {

            if (!nextFragment.isAdded()) {

                if (currFragment != null) {

                    transaction.hide(currFragment);
                }
                transaction.add(R.id.fl_activity_main_content, nextFragment);
            } else {

                if (currFragment != null) {
                    transaction.hide(currFragment);
                }
                transaction.show(nextFragment);
            }
            currFragment = nextFragment;
        }

        //切换fragment
        transaction.commit();
        //关闭侧拉菜单
        drawerlayoutActivityMain.closeDrawers();
    }

    /**
     * 初始化所有片断
     */
    private void initFragments() {
        fragmentManager = getSupportFragmentManager();
        fragments = new ArrayList<>();
        fragments.add(new LikeFragment());
        fragments.add(new NewsFragment());
        fragments.add(new ChatFragment());
        fragments.add(new ImageFragment());
    }

    /**
     * 初始化Toolbar
     */
    private void initToolBar() {
        toolbarActivityMain.setTitle("新闻");
        toolbarActivityMain.setLogo(R.mipmap.ic_launcher);
        toolbarActivityMain.setNavigationIcon(R.drawable.smile);
        setSupportActionBar(toolbarActivityMain);
    }

    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constant.GET_IMAGE_FROM_SERVICE && resultCode == RESULT_OK) {

            String imgStr = data.getStringExtra("data");
            // 把string 转换成bitmap
            byte[] decode = Base64.decode(imgStr, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);

            circleImageView.setImageBitmap(bitmap);
        } else if (requestCode == Constant.GET_IMAGE_FROM_CAMERA && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

            String imgStr = Base64.encodeToString(out.toByteArray(), Base64.DEFAULT);
            App.ref.child(App.user.getUid()).setValue(imgStr);

            circleImageView.setImageBitmap(bitmap);
        } else if (requestCode == Constant.GET_IMAGE_FROM_ALBUM && resultCode == RESULT_OK) {

            Uri imgUri = data.getData();

            ContentResolver contentResolver = getContentResolver();

            try {

                InputStream in = contentResolver.openInputStream(imgUri);

                Rect rect = new Rect(0, 0, 96, 96);
                BitmapFactory.Options opts = new BitmapFactory.Options();

                opts.inSampleSize = 5;
                Bitmap bitmap = BitmapFactory.decodeStream(in, rect, opts);

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

                String imgStr = Base64.encodeToString(out.toByteArray(), Base64.DEFAULT);

                App.ref.child(App.user.getUid()).setValue(imgStr);

                   /* Cursor cursor = contentResolver.query(imgUri,null,null,null,null);
                    cursor.moveToFirst();
                    String path = cursor.getString(1);
                    Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    opts.inSampleSize = 50;
                    Bitmap bitmap = BitmapFactory.decodeFile(path,opts);*/

                   /* InputStream inputStream = contentResolver.openInputStream(imgUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,out);
                    String imgStr = Base64.encodeToString(out.toByteArray(),Base64.DEFAULT);
                    App.ref.child(App.user.getUid()).setValue(imgStr);*/

                circleImageView.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * show选择头像对话框
     */
    private void showDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("选择头像")
                .setItems(new String[]{"相机", "相册"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                fromCamera();
                                break;
                            case 1:
                                fromAlbum();
                                break;
                        }
                    }
                }).show();

    }

    /**
     * 从相册中选择头像
     */
    private void fromAlbum() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");//image类型 图片，* 是 所有的后缀名  jgp，png，gif
        startActivityForResult(intent, Constant.GET_IMAGE_FROM_ALBUM);
    }

    /**
     * 从相机中选择头像
     */
    private void fromCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, Constant.GET_IMAGE_FROM_CAMERA);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CacheUtil.putStringIntoSp(MainActivity.this, CacheUtil.READED, "");
    }

    /* @BindView(R.id.tv_show_activity_main)
    TextView mtv_Show;
    @BindView(R.id.iv_show_activity_main)
    ImageView mIvShow;
    private String url = "http://v.juhe.cn/toutiao/index?type=top&key=9398ab53c96408b94f9def1a4a1cc5e6";
    private String urlbitmap = "http://i1.piimg.com/567571/2f6a1ec344713cd5.jpg";
    private RequestQueue requestQueue = NoHttpInstance.getInstance();//请求队列

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //双击选中 alt+ insert 选butterKnife
        ButterKnife.bind(this);
        requestString();
        requestImage();
    }

    private void requestImage() {
        Request<Bitmap> imageRequest = NoHttp.createImageRequest(urlbitmap, RequestMethod.GET);
        requestQueue.add(1, imageRequest, new OnResponseListener<Bitmap>() {
            @Override
            public void onStart(int what) {
            }
            @Override
            public void onSucceed(int what, Response<Bitmap> response) {
                if(what ==1){
                    mIvShow.setImageBitmap(response.get());
                }
            }
            @Override
            public void onFailed(int what, Response<Bitmap> response) {
            }
            @Override
            public void onFinish(int what) {
            }
        });
    }

    private void requestString() {
        //制造请求
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        //添加到请求队列
        requestQueue.add(0, request, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {
            }
            @Override
            public void onSucceed(int what, Response<String> response) {

                if(what ==0) {
                    String result = response.get();
                    //用fastJson解析数据
                    NewsBean newsBean = JSON.parseObject(result,NewsBean.class);
                    mtv_Show.setText(newsBean.getResult().getData().get(0).getTitle());
                   // mtv_Show.setText(response.get());
                }
            }
            @Override
            public void onFailed(int what, Response<String> response) {
            }
            @Override
            public void onFinish(int what) {
            }
        });
    }
*/

}