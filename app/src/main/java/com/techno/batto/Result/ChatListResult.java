package com.techno.batto.Result;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ritesh on 17/11/18.
 */

public class ChatListResult {
    @SerializedName("chat_message")
    @Expose
    private String chatMessage;
    @SerializedName("user_details")
    @Expose
    private UserDetails userDetails;
    @SerializedName("product_id")
    @Expose
    private String productId;

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
