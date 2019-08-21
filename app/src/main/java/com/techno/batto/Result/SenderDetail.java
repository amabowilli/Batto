package com.techno.batto.Result;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ritesh on 7/2/18.
 */

public class SenderDetail {

    @SerializedName("sender_image")
    @Expose
    private String senderImage;

    public String getSenderImage() {
        return senderImage;
    }

    public void setSenderImage(String senderImage) {
        this.senderImage = senderImage;

    }
}
