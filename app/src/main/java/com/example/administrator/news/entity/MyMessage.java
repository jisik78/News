package com.example.administrator.news.entity;

/**
 * Created by Administrator on 2016/12/23.
 */

public class MyMessage {

    private int type; //0：from 收消息  1：to 发消息
    private String text; //消息内容


    public MyMessage( int type,String text) {
        this.text = text;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
