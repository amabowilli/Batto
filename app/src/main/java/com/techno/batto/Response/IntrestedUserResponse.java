package com.techno.batto.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.techno.batto.Result.IntrestedUserResult;

import java.util.List;

/**
 * Created by ritesh on 23/1/19.
 */

public class IntrestedUserResponse {
    @SerializedName("result")
    @Expose
    private List<IntrestedUserResult> result = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public List<IntrestedUserResult> getResult() {
        return result;
    }

    public void setResult(List<IntrestedUserResult> result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
