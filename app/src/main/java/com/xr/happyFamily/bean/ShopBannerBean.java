/**
 * Copyright 2018 bejson.com
 */
package com.xr.happyFamily.bean;

/**
 * Auto-generated: 2018-06-26 16:5:10
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class ShopBannerBean {

    private int adId;
    private int adPositionId;
    private String image;
    private String description;
    private String url;
    private int state;
    private String createTime;
    private String updateTime;
    private int delState;
    private AdPosition adPosition;
    public void setAdId(int adId) {
        this.adId = adId;
    }
    public int getAdId() {
        return adId;
    }

    public void setAdPositionId(int adPositionId) {
        this.adPositionId = adPositionId;
    }
    public int getAdPositionId() {
        return adPositionId;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getImage() {
        return image;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrl() {
        return url;
    }

    public void setState(int state) {
        this.state = state;
    }
    public int getState() {
        return state;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getCreateTime() {
        return createTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    public String getUpdateTime() {
        return updateTime;
    }

    public void setDelState(int delState) {
        this.delState = delState;
    }
    public int getDelState() {
        return delState;
    }

    public void setAdPosition(AdPosition adPosition) {
        this.adPosition = adPosition;
    }
    public AdPosition getAdPosition() {
        return adPosition;
    }


    /**
     * Auto-generated: 2018-06-26 16:5:10
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */
    public class AdPosition {

        private int adPositionId;
        private String name;
        private String position;
        private String measure;
        private int state;
        private String createTime;
        private String updateTime;
        public void setAdPositionId(int adPositionId) {
            this.adPositionId = adPositionId;
        }
        public int getAdPositionId() {
            return adPositionId;
        }

        public void setName(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }

        public void setPosition(String position) {
            this.position = position;
        }
        public String getPosition() {
            return position;
        }

        public void setMeasure(String measure) {
            this.measure = measure;
        }
        public String getMeasure() {
            return measure;
        }

        public void setState(int state) {
            this.state = state;
        }
        public int getState() {
            return state;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
        public String getCreateTime() {
            return createTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
        public String getUpdateTime() {
            return updateTime;
        }

    }
}