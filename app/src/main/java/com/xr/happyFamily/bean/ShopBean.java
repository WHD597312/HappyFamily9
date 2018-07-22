package com.xr.happyFamily.bean;

import java.util.List;

/**
 * Auto-generated: 2018-06-11 15:34:3
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class ShopBean {

    private String returnCode;
    private String returnMsg;
    private ReturnData returnData;
    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }
    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }
    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnData(ReturnData returnData) {
        this.returnData = returnData;
    }
    public ReturnData getReturnData() {
        return returnData;
    }



    /**
     * Auto-generated: 2018-06-11 15:34:3
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */
    public class ReturnData {

        private int totalPage;
        private List<MyList> list;
        private int totalCount;
        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }
        public int getTotalPage() {
            return totalPage;
        }

        public void setList(List<MyList> list) {
            this.list = list;
        }
        public List<MyList> getList() {
            return list;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }
        public int getTotalCount() {
            return totalCount;
        }

        /**
         * Auto-generated: 2018-06-11 15:34:3
         *
         * @author bejson.com (i@bejson.com)
         * @website http://www.bejson.com/java2pojo/
         */
        public class MyList {

            private String image;
            private int goodsId;
            private int isMarketable;
            private int weight;
            private String simpleDescribe;
            private int recommend;
            private long updateTime;
            private String colour;
            private long createTime;
            private List<GoodsPrice> goodsPrice;
            private String nickname;
            private String detailDescribe;
            private int delState;
            private int adminId;
            private String goodsName;
            private int categoryId;
            public void setImage(String image) {
                this.image = image;
            }
            public String getImage() {
                return image;
            }

            public void setGoodsId(int goodsId) {
                this.goodsId = goodsId;
            }
            public int getGoodsId() {
                return goodsId;
            }

            public void setIsMarketable(int isMarketable) {
                this.isMarketable = isMarketable;
            }
            public int getIsMarketable() {
                return isMarketable;
            }

            public void setWeight(int weight) {
                this.weight = weight;
            }
            public int getWeight() {
                return weight;
            }

            public void setSimpleDescribe(String simpleDescribe) {
                this.simpleDescribe = simpleDescribe;
            }
            public String getSimpleDescribe() {
                return simpleDescribe;
            }

            public void setRecommend(int recommend) {
                this.recommend = recommend;
            }
            public int getRecommend() {
                return recommend;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }
            public long getUpdateTime() {
                return updateTime;
            }

            public void setColour(String colour) {
                this.colour = colour;
            }
            public String getColour() {
                return colour;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }
            public long getCreateTime() {
                return createTime;
            }

            public void setGoodsPrice(List<GoodsPrice> goodsPrice) {
                this.goodsPrice = goodsPrice;
            }
            public List<GoodsPrice> getGoodsPrice() {
                return goodsPrice;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }
            public String getNickname() {
                return nickname;
            }

            public void setDetailDescribe(String detailDescribe) {
                this.detailDescribe = detailDescribe;
            }
            public String getDetailDescribe() {
                return detailDescribe;
            }

            public void setDelState(int delState) {
                this.delState = delState;
            }
            public int getDelState() {
                return delState;
            }

            public void setAdminId(int adminId) {
                this.adminId = adminId;
            }
            public int getAdminId() {
                return adminId;
            }

            public void setGoodsName(String goodsName) {
                this.goodsName = goodsName;
            }
            public String getGoodsName() {
                return goodsName;
            }

            public void setCategoryId(int categoryId) {
                this.categoryId = categoryId;
            }
            public int getCategoryId() {
                return categoryId;
            }


            public class GoodsPrice {

                private int priceId;
                private int goodsId;
                private String power;
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

                public void setPower(String power) {
                    this.power = power;
                }
                public String getPower() {
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

        }


    }
}