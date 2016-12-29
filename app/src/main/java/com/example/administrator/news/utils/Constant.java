package com.example.administrator.news.utils;

/**
 * Created by Administrator on 2016/12/15.
 */

public interface Constant {

    String BASE_URL = "http://v.juhe.cn/toutiao/index?";
    String AHALF ="http://v.juhe.cn/toutiao/index?type=";
    String BHALF = "&key=ac7536129f9ea7eba635634409c6b435";
    String[] NEWS_TYPES = {"top","shehui","guonei","guoji","yule","tiyu","junshi","keji","caijing","shishang"};
    String[] TITLES = {"头条","社会","国内","国际","娱乐","体育","军事","科技","财经","时尚"};

    String ROBOT_AHAFE = "http://op.juhe.cn/robot/index?info=";
    String ROBOT_BHAFE = "&key=f2df603d44117927d9e165249f483acd";


   /* String NEWS_TOP_QUERY_STRING = "type=top&key=ac7536129f9ea7eba635634409c6b435";
    String NEWS_SHEHUI_QUERY_STRING = "type=shehui&key=ac7536129f9ea7eba635634409c6b435";
    String NEWS_GUONEI_QUERY_STRING = "type=guonei&key=ac7536129f9ea7eba635634409c6b435";
    String NEWS_GUOJI_QUERY_STRING = "type=guoji&key=ac7536129f9ea7eba635634409c6b435";
    String NEWS_YULE_QUERY_STRING = "type=yule&key=ac7536129f9ea7eba635634409c6b435";
    String NEWS_TIYU_QUERY_STRING = "type=tiyu&key=ac7536129f9ea7eba635634409c6b435";
    String NEWS_JUNSHI_QUERY_STRING = "type=junshi&key=ac7536129f9ea7eba635634409c6b435";
    String NEWS_KEJI_QUERY_STRING = "type=keji&key=ac7536129f9ea7eba635634409c6b435";
    String NEWS_CAIJING_QUERY_STRING = "type=caijing&key=ac7536129f9ea7eba635634409c6b435";
    String NEWS_SHISHANG_QUERY_STRING = "type=shishang&key=ac7536129f9ea7eba635634409c6b435"*/;

    int WHAT_NEWS_REQUEST = 1;

    int GET_IMAGE_FROM_CAMERA = 0;
    int GET_IMAGE_FROM_ALBUM = 1;
    int GET_IMAGE_FROM_SERVICE = 2;






}
