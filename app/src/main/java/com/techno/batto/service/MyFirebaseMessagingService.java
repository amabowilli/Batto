package com.techno.batto.service;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.techno.batto.Activity.ChatActivity;
import com.techno.batto.Activity.Home;
import com.techno.batto.Constant.Constants;
import com.techno.batto.utils.NotificationUtils;


import org.json.JSONObject;

import static com.techno.batto.Bean.MySharedPref.saveData;

/**
 * Created by Nitin on 14/12/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private NotificationUtils notificationUtils;
    String notification_status;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Constants.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());
        System.out.println("----------------------json----------------" + json);

        try {
            JSONObject data = json.getJSONObject("message");
            System.out.println("----------------------json----------------" + data);
            String key = data.getString("key");
            System.out.println("satus%%%" + key);
            Intent resultIntent = new Intent(getApplicationContext(), Home.class);
            Intent chatIntent = new Intent(getApplicationContext(), ChatActivity.class);
            resultIntent.putExtra("messgae", ""+data);

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Constants.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", key);

                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                saveData(getApplicationContext(), "message", data.toString());
                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
                if (key.equals("You have a new chat message")) {
                    chatIntent.putExtra("reciver_id", data.getString("userid"));
                    chatIntent.putExtra("product_id", data.getString("product_id"));
                    showNotificationMessage(getApplicationContext(), "Batto", "You have a new message", "Timestamp", chatIntent);
                }else if (key.equals("new following request")) {
                    showNotificationMessage(getApplicationContext(), "Batto", ""+data.getString("message"), "Timestamp", resultIntent);
                }else  if (key.equals("you have a new review")) {
                    showNotificationMessage(getApplicationContext(), "Batto", "You have a new review", "Timestamp", resultIntent);
                }else  if (key.equals("new product addedd")) {
                    showNotificationMessage(getApplicationContext(), "Batto", "New product addedd", "Timestamp", resultIntent);
                }
            }else {

                if (key.equals("You have a new chat message")) {
                    chatIntent.putExtra("reciver_id", data.getString("userid"));
                    chatIntent.putExtra("product_id", data.getString("product_id"));
                    showNotificationMessage(getApplicationContext(), "Batto", "You have a new message", "Timestamp", chatIntent);
                }else  if (key.equals("new following request")) {
                    showNotificationMessage(getApplicationContext(), "Batto", ""+data.getString("message"), "Timestamp", resultIntent);
                }else  if (key.equals("you have a new review")) {
                    showNotificationMessage(getApplicationContext(), "Batto", "You have a new review", "Timestamp", resultIntent);
                }else  if (key.equals("new product addedd")) {
                    showNotificationMessage(getApplicationContext(), "Batto", "New product addedd", "Timestamp", resultIntent);
                }


                Log.e("hello like", "else");
                resultIntent.putExtra("message", data.toString());






//                // check for image attachment
//                if (TextUtils.isEmpty(imageUrl)) {
//                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
//                } else {
//                    // image is present, show notification with image
//                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
//                }



    }
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}