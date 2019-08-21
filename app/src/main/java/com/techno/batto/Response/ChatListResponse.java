package com.techno.batto.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.techno.batto.Result.ChatListResult;

import java.util.List;

/**
 * Created by ritesh on 17/11/18.
 */

public class ChatListResponse {
    @SerializedName("result")
    @Expose
    private List<ChatListResult> result = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private Integer status;

    public List<ChatListResult> getResult() {
        return result;
    }

    public void setResult(List<ChatListResult> result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
