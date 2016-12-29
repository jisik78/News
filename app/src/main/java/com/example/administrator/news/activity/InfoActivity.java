package com.example.administrator.news.activity;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.administrator.news.App;
import com.example.administrator.news.R;
import com.example.administrator.news.utils.Constant;
import com.wilddog.wilddogauth.core.Task;
import com.wilddog.wilddogauth.core.listener.OnCompleteListener;
import com.wilddog.wilddogauth.core.request.UserProfileChangeRequest;
import com.wilddog.wilddogauth.core.result.AuthResult;
import com.wilddog.wilddogauth.model.WilddogUser;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InfoActivity extends AppCompatActivity {

    @BindView(R.id.edittext_email_activity_info)
    TextInputEditText EditTextEmail;
    @BindView(R.id.textinputlayout_email_activity_info)
    TextInputLayout TextInputLayoutEmail;
    @BindView(R.id.edittext_password_activity_info)
    TextInputEditText EditTextPassword;
    @BindView(R.id.textinputlayout_password_activity_info)
    TextInputLayout TextInputLayoutPassword;
    @BindView(R.id.register_activity_info)
    Button btn_register;
    @BindView(R.id.login_activity_info)
    Button btn_login;
    private WilddogUser currUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);

        initListener();

    }

    /**
     * 初始化监听器
     */
    public void initListener(){
        EditTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().trim().length() < 6) {
                    TextInputLayoutEmail.setError("邮箱不能少于六个字符");
                } else {
                    TextInputLayoutEmail.setErrorEnabled(false);
                }
            }
        });

        EditTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().length() < 6) {
                    TextInputLayoutPassword.setError("密码不能少于六个字符");
                } else {
                    TextInputLayoutPassword.setErrorEnabled(false);
                }
            }
        });
    }

    /**
     * 在野狗用邮箱注册  点击事件
     */
    public void register(View v) {

        String email = EditTextEmail.getText().toString().trim();
        String password = EditTextPassword.getText().toString().trim();

        App.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 获取用户
                            currUser = task.getResult().getWilddogUser();
                            App.user = currUser;
                            Toast.makeText(InfoActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        } else {
                            // 错误处理
                            Log.d("result", task.getException().toString());
                            Toast.makeText(InfoActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 登录 点击事件
     * @param v
     */
    public void login(View v) {

        String email = EditTextEmail.getText().toString().trim();
        String password = EditTextPassword.getText().toString().trim();

        App.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        Log.d(InfoActivity.class.getName(), "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.d("result", currUser.toString());
                            Toast.makeText(InfoActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            showDialog();
                        }
                    }
                });
    }

    /**
     * 显示选择头像对话框
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

    private void fromAlbum() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");//image类型 图片，* 是 所有的后缀名  jgp，png，gif
        startActivityForResult(intent,Constant.GET_IMAGE_FROM_ALBUM);
    }

    private void fromCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, Constant.GET_IMAGE_FROM_CAMERA);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constant.GET_IMAGE_FROM_CAMERA && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();

            Bitmap bitmap = (Bitmap) extras.get("data");
            //bitmap不能直接上传到云端，因为网络传输图片时，有些字符会无法识别
            //首先要把图片进行base64转化
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            final String imgStr = Base64.encodeToString(out.toByteArray(), Base64.DEFAULT);

            if(App.user != null){

                App.ref.child(App.user.getUid()).setValue(imgStr);

                WilddogUser user = App.user;
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setPhotoUri(Uri.parse(currUser.getUid()))
                        .build();
                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent();
                                    intent.putExtra("data", imgStr);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                    // 更新成功
                                } else {
                                    // 发生错误
                                }
                            }
                        });
            }
        }
    }
}
