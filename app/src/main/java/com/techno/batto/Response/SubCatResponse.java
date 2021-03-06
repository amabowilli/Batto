package com.techno.batto.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.techno.batto.Result.SubCatResult;

import java.util.List;

/**
 * Created by ritesh on 13/2/19.
 */

public class SubCatResponse {
    @SerializedName("result")
    @Expose
    private List<SubCatResult> result = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public List<SubCatResult> getResult() {
        return result;
    }

    public void setResult(List<SubCatResult> result) {
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
