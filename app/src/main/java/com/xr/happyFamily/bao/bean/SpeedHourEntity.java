package com.xr.happyFamily.bao.bean;


import java.util.List;

/**
 * Created by win7 on 2018/5/21.
 */

//用于首页顶部横向选择条
public class SpeedHourEntity {

    public TopicBean topic;

    public static class TopicBean {
        public long nextupdatetime;
        public List<ItemsBean> items;

        public static class ItemsBean {
            public int id;
            public String theme;
            public int products;
            public int users;
            public String href;
            public boolean follow;
            public int topictype;

            public List<ListBean> list;

            public static class ListBean {
                public String id;
                public int price;
                public String pic;
            }
        }
    }
}