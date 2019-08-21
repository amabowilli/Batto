package com.techno.batto.service;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.gson.Gson;
import com.techno.batto.Activity.ChatActivity;
import com.techno.batto.App.AppConfig;
import com.techno.batto.Holder.DataHolder;
import com.techno.batto.Response.NewChatResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by pintu22 on 23/11/17.
 */

public class GetChatService extends Service {
    public static boolean isRunning = false;
    public static int count = 0;
    private static String TAG = GetChatService.class.getSimpleName();
    private MyThread mythread;

    public GetChatService() {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
      //  Log.d(TAG, "onCreate");
        mythread = new MyThread();
    }

    @Override
    public synchronized void onDestroy() {
        super.onDestroy();
       // Log.d(TAG, "onDestroy");
        if (!isRunning) {
            mythread.interrupt();
            isRunning = false;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        //Log.d(TAG, "onStart");
        if (!isRunning) {
            mythread.start();
            isRunning = true;
        }
        return START_NOT_STICKY;
    }

    @Override
    public synchronized void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

    }

//    private void GetChat() {
//        //System.out.println("ChildId: "+DataHolder.getChildId()+"\nTeacherId: "+DataHolder.getTeacherId());
//        Call<List<NewChatResponse>> call = AppConfig.loadInterface().getChat(DataHolder.getSender(), DataHolder.getReciver());
//        call.enqueue(new Callback<List<NewChatResponse>>() {
//            @Override
//            public void onResponse(Call<List<NewChatResponse>> call, Response<List<NewChatResponse>> successData) {
//                try {
//                    if (successData.isSuccessful()) {
//                        int i;
//                        i = successData.body().size();
//                        if (i > count) {
//                            DataHolder.setGetChat(successData.body());
//                            Intent broadcastIntent = new Intent();
//                            broadcastIntent.setAction(ChatActivity.mBroadcastGetChatData);
//                            broadcastIntent.putExtra("Data", "GetTeacherChatData");
//                            sendBroadcast(broadcastIntent);
//                        }
//                        count = i;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<NewChatResponse>> call, Throwable t) {
//
//                t.printStackTrace();
//            }
//        });
//    }

    private class MyThread extends Thread {
        static final long DELAY = 3000;

        @Override
        public void run() {
            while (isRunning) {
              //  Log.d(TAG, "Running");
                try {
                  //  GetChat();
                    getChat1();
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    isRunning = false;
                    e.printStackTrace();
                }
            }
        }
    }


    private void getChat1() {

        Call<ResponseBody> call = AppConfig.loadInterface().getChat1(DataHolder.getSender(), DataHolder.getReciver(),DataHolder.getProduct_id());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        System.out.println("Chat Response :- " + object);
                        if (object.getString("status").equals("1")) {
                            Gson gson = new Gson();
                            NewChatResponse requestListResponse = gson.fromJson(responseData, NewChatResponse.class);
                            int i;
                            i = requestListResponse.getResult().size();
                            if (i > count) {
                                DataHolder.setGetChat(requestListResponse.getResult());
                                Intent broadcastIntent = new Intent();
                                broadcastIntent.setAction(ChatActivity.mBroadcastGetChatData);
                                broadcastIntent.putExtra("Data", "GetTeacherChatData");
                                sendBroadcast(broadcastIntent);
                            }
                            count = i;
                        } else {
                        }
                    } else ;
                    // AppConfig.showToast("server error");
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }

}
