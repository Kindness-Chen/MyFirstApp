package com.example.myfirstapp.model;

import java.io.Serializable;

public class BaseAdapterModel implements Serializable {

    private String title;
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "BaseAdapterAdapter{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
