package com.techno.batto.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.techno.batto.Result.ProductListResult;

import java.util.List;

/**
 * Created by ritesh on 16/1/19.
 */

public class ProductListResponse {
    @SerializedName("result")
    @Expose
    private List<ProductListResult> result = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public List<ProductListResult> getResult() {
        return result;
    }

    public void setResult(List<ProductListResult> result) {
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
