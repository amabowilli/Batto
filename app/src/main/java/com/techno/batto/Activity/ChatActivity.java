package com.techno.batto.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fxn.pix.Pix;
import com.techno.batto.Adapter.ChatLVAdapter;
import com.techno.batto.App.AppConfig;
import com.techno.batto.Holder.DataHolder;
import com.techno.batto.R;
import com.techno.batto.service.GetChatService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.techno.batto.Bean.MySharedPref.getData;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton imgLeftMenu;
    ImageView send_chat_image_view;
    String reciver_id, user_id, product_id;
    EditText send_chat_edit_text;
    String msg;
    Boolean isSent = true;
    public static Double latitude, longitude;
    private RelativeLayout lay_camvideo, lay_meet;
    public static final String mBroadcastGetChatData = "GetTeacherChatData";
    private IntentFilter mIntentFilter;
    TextView txt_clearchat;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(mBroadcastGetChatData)) {
                try {
                    ListView list = (ListView) findViewById(R.id.chatRecyclerView);
                    Collections.reverse(DataHolder.getGetChat());
                    ChatLVAdapter adapter = new ChatLVAdapter(ChatActivity.this, DataHolder.getGetChat());
                    list.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        user_id = getData(this, "user_id", null);

        if (getIntent().getExtras() != null) {
            reciver_id = getIntent().getExtras().getString("reciver_id");
            product_id = getIntent().getExtras().getString("product_id");
            Log.e("reciver_id", reciver_id);
            Log.e("sender_id", user_id);
            Log.e("product_id", product_id);
            DataHolder.setReciver(reciver_id);
            DataHolder.setSender(user_id);
            DataHolder.setProduct_id(product_id);
        }

        findId();
        send_chat_image_view.setOnClickListener(this);
        imgLeftMenu.setOnClickListener(this);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(mBroadcastGetChatData);
        GetChatService.count = 0;
        startService(new Intent(this, GetChatService.class));
        txt_clearchat.setOnClickListener(this);
        lay_camvideo.setOnClickListener(this);
        lay_meet.setOnClickListener(this);
    }

    private void findId() {
        imgLeftMenu = (ImageButton) findViewById(R.id.imgLeftMenu);
        send_chat_image_view = (ImageView) findViewById(R.id.send_chat_image_view);
        send_chat_edit_text = (EditText) findViewById(R.id.send_chat_edit_text);
        txt_clearchat = (TextView) findViewById(R.id.txt_clearchat);
        lay_camvideo = (RelativeLayout) findViewById(R.id.lay_camvideo);
        lay_meet = (RelativeLayout) findViewById(R.id.lay_meet);

    }

    @Override
    public void onClick(View view) {
        if (view == imgLeftMenu) {
            stopService(new Intent(this, GetChatService.class));
            finish();
        } else if (view == send_chat_image_view) {
            msg = send_chat_edit_text.getText().toString();
            if (msg.equalsIgnoreCase("")) {
                //do nathing
            } else {
                if (isSent) {
                    isSent = false;
                    send_chat_edit_text.setText("");
                    sendMsgCall();
                }
            }
        } else if (view == lay_camvideo) {
            Pix.start(this, 100, 1);
        } else if (view == lay_meet) {
            Intent i = new Intent(getApplicationContext(), GetLocationForChat.class);
            i.putExtra("reciver_id", reciver_id);
            i.putExtra("sender_id", user_id);
            i.putExtra("product_id", product_id);
            startActivity(i);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            for (int i = 0; i < returnValue.size(); i++) {
                String path = returnValue.get(i);
//              Glide.with(this).load(path).into(img_one);
                sendImageCall(path);
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    public void onPause() {
        unregisterReceiver(mReceiver);
        super.onPause();
    }

    private void sendMsgCall() {
        Call<ResponseBody> call = AppConfig.loadInterface().insertChat(user_id, reciver_id, product_id, msg, "Text", "", "");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        System.out.println("Login Data :- " + object);
                        if (object.getString("result").equals("successful")) {
                            Toast.makeText(ChatActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            isSent = true;
                        } else {
                            Toast.makeText(ChatActivity.this, "Not Send", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ChatActivity.this, "Please Check Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void sendImageCall(String path) {
        File file = null;
        if (path != null) {
            path = resizeAndCompressImageBeforeSend(this, path, "user_image");
            file = new File(path);
        }
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("chat_image", file.getName(), requestFile);
        Call<ResponseBody> call = AppConfig.loadInterface().insertChatImage(user_id, reciver_id, product_id, "Image", "Image", "", "", body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        System.out.println("Login Data :- " + object);
                        if (object.getString("result").equals("successful")) {
                            Toast.makeText(ChatActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            isSent = true;
                        } else {
                            Toast.makeText(ChatActivity.this, "Not Send", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("Error Response", "" + response.message());
                    }
                    // AppConfig.showToast("server error");
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(ChatActivity.this, "Please Check Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static String resizeAndCompressImageBeforeSend(Context context, String filePath, String fileName) {
        try {
            final int MAX_IMAGE_SIZE = 700 * 1024; // max final file size in kilobytes

            // First decode with inJustDecodeBounds=true to check dimensions of image
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);

            // Calculate inSampleSize(First we are going to resize the image to 800x800 image, in order to not have a big but very low quality image.
            //resizing the image will already reduce the file size, but after resizing we will check the file size and start to compress image
            options.inSampleSize = calculateInSampleSize(options, 600, 700);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            Bitmap bmpPic = BitmapFactory.decodeFile(filePath, options);


            int compressQuality = 100; // quality decreasing by 5 every loop.
            int streamLength;
            do {
                ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
                Log.d("compressBitmap", "Quality: " + compressQuality);
                bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);
                byte[] bmpPicByteArray = bmpStream.toByteArray();
                streamLength = bmpPicByteArray.length;
                compressQuality -= 5;
                Log.d("compressBitmap", "Size: " + streamLength / 1024 + " kb");
            } while (streamLength >= MAX_IMAGE_SIZE);

            try {
                //save the resized and compressed file to disk cache
                Log.d("compressBitmap", "cacheDir: " + context.getCacheDir());
                FileOutputStream bmpFile = new FileOutputStream(context.getCacheDir() + fileName);
                bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpFile);
                bmpFile.flush();
                bmpFile.close();
            } catch (Exception e) {
                Log.e("compressBitmap", "Error on saving file");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return the path of resized and compressed file
        return context.getCacheDir() + fileName;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        String debugTag = "MemoryInformation";
        // Image nin islenmeden onceki genislik ve yuksekligi
        final int height = options.outHeight;
        final int width = options.outWidth;
        Log.d(debugTag, "image height: " + height + "---image width: " + width);
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        Log.d(debugTag, "inSampleSize: " + inSampleSize);
        return inSampleSize;
    }

    @Override
    public void onBackPressed() {
        stopService(new Intent(this, GetChatService.class));
        finish();
        super.onBackPressed();
    }


}
