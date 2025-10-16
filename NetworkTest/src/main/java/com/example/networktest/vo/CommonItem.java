package com.example.networktest.vo;

/**
 * Date：2025/10/14
 * Time：14:58
 * Author：chenshengrui
 */
public class CommonItem {

    private String id;
    private String comment;

    public CommonItem(String id, String comment) {
        this.id = id;
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
