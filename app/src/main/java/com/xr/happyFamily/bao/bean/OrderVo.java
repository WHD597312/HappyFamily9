package com.xr.happyFamily.bao.bean;


import java.util.ArrayList;
import java.util.List;

public class OrderVo {

    private Integer receiveState;

    private Receive receive;

    private List<Goods> allGoods = new ArrayList<Goods>();

    private Logistics logistics;

    public Integer getReceiveState() {
        return receiveState;
    }

    public void setReceiveState(Integer receiveState) {
        this.receiveState = receiveState;
    }

    public List<Goods> getAllGoods() {
        return allGoods;
    }

    public void setAllGoods(List<Goods> allGoods) {
        this.allGoods = allGoods;
    }

    public Receive getReceive() {
        return receive;
    }

    public void setReceive(Receive receive) {
        this.receive = receive;
    }

    public Logistics getLogistics() {
        return logistics;
    }

    public void setLogistics(Logistics logistics) {
        this.logistics = logistics;
    }


}
