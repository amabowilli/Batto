package com.techno.batto.Holder;


import com.techno.batto.Result.ChatResult;

import java.util.List;

/**
 * Created by ritesh on 7/2/18.
 */

public class DataHolder {
    private static String reciver, sender, chat_type,product_id;
    private static List<ChatResult> getChat;

    public static String getProduct_id() {
        return product_id;
    }

    public static void setProduct_id(String product_id) {
        DataHolder.product_id = product_id;
    }

    public static String getReciver() {
        return reciver;
    }

    public static void setReciver(String reciver) {
        DataHolder.reciver = reciver;
    }

    public static String getSender() {
        return sender;
    }

    public static void setSender(String sender) {
        DataHolder.sender = sender;
    }

    public static List<ChatResult> getGetChat() {
        return getChat;
    }

    public static void setGetChat(List<ChatResult> getChat) {
        DataHolder.getChat = getChat;
    }

    public static String getChat_type() {
        return chat_type;
    }

    public static void setChat_type(String chat_type) {
        DataHolder.chat_type = chat_type;
    }

}
