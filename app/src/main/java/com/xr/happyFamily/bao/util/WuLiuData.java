package com.xr.happyFamily.bao.util;

import com.xr.happyFamily.bean.WuLiuListBean;

import java.util.ArrayList;
import java.util.List;

//快递类别
public class WuLiuData {
    String[] shipperCode=new String[]{"SF","HTKY","ZTO","STO","YTO","YD","YZPY","EMS","HHTT","JD","UC","DBL","FAST","ZJS","TNT"};
    String[] shipperName=new String[]{"顺丰速运","百世快递","中通快递","申通快递","圆通速递","韵达速递","邮政快递包裹","EMS","天天快递","京东物流","优速快递","德邦快递","快捷快递","宅急送","TNT快递"};

    public String[] getShipperCode() {
        return shipperCode;
    }

    public String[] getShipperName() {
        return shipperName;
    }

    List<WuLiuListBean> wuLiuListBeanList = new ArrayList<>();


    public List<WuLiuListBean> getWuLiuListBeanList() {
        WuLiuData wuLiuData=new WuLiuData();
        for(int i=0;i< wuLiuData.getShipperCode().length;i++){
            WuLiuListBean wuLiuListBean=new WuLiuListBean();
            wuLiuListBean.setCode(wuLiuData.getShipperCode()[i]);
            wuLiuListBean.setName(wuLiuData.getShipperName()[i]);
            wuLiuListBeanList.add(wuLiuListBean);
        }
        return wuLiuListBeanList;
    }
}
