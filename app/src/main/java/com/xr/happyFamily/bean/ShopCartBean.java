package com.xr.happyFamily.bean;

import com.xr.happyFamily.bao.bean.Goods;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/10/14.
 */

public class ShopCartBean implements Serializable {


        private int id;
        private int shopId;
        private String shopName;
        private int productId;
        private String productName;
        private String details;
        private String size;
        private String price;
        private String defaultPic;
        private int count;
        private boolean isSelect = true;
        private int isFirst = 2;
        private boolean isShopSelect = true;
        private boolean isCoupon;

    /**
     * Copyright 2018 bejson.com
     */

    /**
     * Auto-generated: 2018-06-14 11:25:1
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */

        private int cartId;
        private int userId;
        private GoodsPrice goodsPrice;
        private Goods goods;
        private int quantity;
        private String isBuy;
        private int totalAmount;
        private long createTime;
        private long updateTime;
        public void setCartId(int cartId) {
            this.cartId = cartId;
        }
        public int getCartId() {
            return cartId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
        public int getUserId() {
            return userId;
        }

        public void setGoodsPrice(GoodsPrice goodsPrice) {
            this.goodsPrice = goodsPrice;
        }
        public GoodsPrice getGoodsPrice() {
            return goodsPrice;
        }

        public void setGoods(Goods goods) {
            this.goods = goods;
        }
        public Goods getGoods() {
            return goods;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
        public int getQuantity() {
            return quantity;
        }

        public void setIsBuy(String isBuy) {
            this.isBuy = isBuy;
        }
        public String getIsBuy() {
            return isBuy;
        }

        public void setTotalAmount(int totalAmount) {
            this.totalAmount = totalAmount;
        }
        public int getTotalAmount() {
            return totalAmount;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }
        public long getCreateTime() {
            return createTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }
        public long getUpdateTime() {
            return updateTime;
        }


        /**
         * Copyright 2018 bejson.com
         */
        /**
         * Auto-generated: 2018-06-14 11:25:1
         *
         * @author bejson.com (i@bejson.com)
         * @website http://www.bejson.com/java2pojo/
         */
        public static class GoodsPrice implements Serializable {

            private int priceId;
            private int goodsId;
            private int power;
            private int price;
            private int state;
            private long createTime;
            private long updateTime;
            private int adminId;
            public void setPriceId(int priceId) {
                this.priceId = priceId;
            }
            public int getPriceId() {
                return priceId;
            }

            public void setGoodsId(int goodsId) {
                this.goodsId = goodsId;
            }
            public int getGoodsId() {
                return goodsId;
            }

            public void setPower(int power) {
                this.power = power;
            }
            public int getPower() {
                return power;
            }

            public void setPrice(int price) {
                this.price = price;
            }
            public int getPrice() {
                return price;
            }

            public void setState(int state) {
                this.state = state;
            }
            public int getState() {
                return state;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }
            public long getCreateTime() {
                return createTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }
            public long getUpdateTime() {
                return updateTime;
            }

            public void setAdminId(int adminId) {
                this.adminId = adminId;
            }
            public int getAdminId() {
                return adminId;
            }

        }



        public String getDefaultPic() {
            return defaultPic;
        }

        public void setDefaultPic(String defaultPic) {
            this.defaultPic = defaultPic;
        }

        public boolean getIsShopSelect() {
            return isShopSelect;
        }

        public void setShopSelect(boolean shopSelect) {
            isShopSelect = shopSelect;
        }

        public int getIsFirst() {
            return isFirst;
        }

        public void setIsFirst(int isFirst) {
            this.isFirst = isFirst;
        }

        public boolean getIsSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getShopId() {
            return shopId;
        }

        public void setShopId(int shopId) {
            this.shopId = shopId;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public Boolean getIsCoupon(){return isCoupon;}

        public void setIsCoupon(Boolean isCoupon){this.isCoupon=isCoupon;}
    }

