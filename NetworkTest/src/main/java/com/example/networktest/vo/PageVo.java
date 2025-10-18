package com.example.networktest.vo;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Date：2025/10/17
 * Time：13:37
 * Author：chenshengrui
 */
public class PageVo {

    @NonNull
    @Override
    public String toString() {
        return "PageVo{" +
                "data=" + data +
                ", message='" + message + '\'' +
                ", code=" + code +
                ", success=" + success +
                '}';
    }

    @SerializedName("success")
    private Boolean success;
    @SerializedName("code")
    private Integer code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private DataDTO data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public static class DataDTO {
        @SerializedName("page")
        private String page;
        @SerializedName("keyword")
        private String keyword;
        @SerializedName("order")
        private String order;

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public String getOrder() {
            return order;
        }

        public void setOrder(String order) {
            this.order = order;
        }
    }
}
