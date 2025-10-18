package com.example.networktest.vo;

/**
 * Date：2025/10/14
 * Time：14:58
 * Author：chenshengrui
 */
public class CommonItem {

    @Override
    public String toString() {
        return "CommonItem{" +
                "articleId='" + articleId + '\'' +
                ", commentContent='" + commentContent + '\'' +
                '}';
    }

    private String articleId;
    private String commentContent;

    public CommonItem(String id, String comment) {
        this.articleId = id;
        this.commentContent = comment;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }
}
