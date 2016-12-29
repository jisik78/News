package com.example.administrator.news.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.news.R;
import com.example.administrator.news.adapter.MyChatListAdapter;
import com.example.administrator.news.entity.MsgBean;
import com.example.administrator.news.entity.MyMessage;
import com.example.administrator.news.utils.Constant;
import com.example.administrator.news.utils.NoHttpInstance;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/14.
 */

public class ChatFragment extends Fragment {

    private ListView listView;
    private List<MyMessage> datas;
    private MyChatListAdapter adapter;
    private EditText editText;
    private Button button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_fragment_chat_activity_main, container, false);

        listView = (ListView) view.findViewById(R.id.listview_fragment_chat_activity_main);
        button = (Button) view.findViewById(R.id.btn_fragment_chat);
        editText = (EditText) view.findViewById(R.id.et_fragment_chat);

        datas = new ArrayList<>();

        if (datas != null) {
            adapter = new MyChatListAdapter(datas, getContext());
            listView.setAdapter(adapter);
            listView.setSelection(datas.size());
        }


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initListener();
    }

    private void initListener() {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String etStr = editText.getText().toString();
                editText.setText("");

                if (etStr != null) {

                    MyMessage myMessage = new MyMessage(1, etStr);
                    datas.add(myMessage);
                    adapter.setDatas(datas);
                    adapter.notifyDataSetChanged();
                    listView.setSelection(datas.size());


                    Request<String> requestString = NoHttp.createStringRequest(Constant.ROBOT_AHAFE + etStr + Constant.ROBOT_BHAFE);
                    NoHttpInstance.getInstance().add(7, requestString, new OnResponseListener<String>() {
                        @Override
                        public void onStart(int what) {

                        }

                        @Override
                        public void onSucceed(int what, Response<String> response) {

                            String resultStr = response.get();
                            if (resultStr != null) {

                                MsgBean msgBean = JSON.parseObject(resultStr, MsgBean.class);

                                if(msgBean.getResult()!= null){

                                String text = msgBean.getResult().getText();
                                MyMessage myMessage = new MyMessage(0, text);

                                //Log.i("22222222222", resultStr);

                                datas.add(myMessage);
                                adapter.setDatas(datas);
                                adapter.notifyDataSetChanged();
                                listView.setSelection(datas.size());
                                }
                            }
                        }

                        @Override
                        public void onFailed(int what, Response<String> response) {

                            MyMessage myMessage = new MyMessage(0, "我不知道怎么回答你");
                            datas.add(myMessage);
                            adapter.setDatas(datas);
                            adapter.notifyDataSetChanged();
                        }
                        @Override
                        public void onFinish(int what) {

                        }
                    });
                }
            }
        });

    }
}
