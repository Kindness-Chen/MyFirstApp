package com.example.myfirstapp.model;

import java.io.Serializable;

public class ViewFlowModel implements Serializable {

    public ViewFlowModel() {

    }

    private String imgUrl;

    public ViewFlowModel(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
