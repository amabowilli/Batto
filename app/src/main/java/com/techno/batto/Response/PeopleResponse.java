package com.techno.batto.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.techno.batto.Result.PeopleResult;

import java.util.List;

public class PeopleResponse {
    @SerializedName("result")
    @Expose
    private List<PeopleResult> result = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public List<PeopleResult> getResult() {
        return result;
    }

    public void setResult(List<PeopleResult> result) {
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
