package com.techno.batto.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fxn.pix.Pix;
import com.techno.batto.App.AppConfig;
import com.techno.batto.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.techno.batto.Bean.MySharedPref.getData;

public class AddPostActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView img_back, img_one;
    private EditText edt_title, edt_price;
    private Button btn_cancel, btn_submit;
    private String path, title, price, user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        findId();
        user_id = getData(this, "user_id", null);
        Pix.start(this, 100, 1);
        img_back.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        img_back.setOnClickListener(this);
    }

    private void findId() {
        img_back = findViewById(R.id.img_back);
        img_one = findViewById(R.id.img_one);
        edt_title = findViewById(R.id.edt_title);
        edt_price = findViewById(R.id.edt_price);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_submit = findViewById(R.id.btn_submit);
    }

    @Override
    public void onClick(View view) {
        if (view == img_back) {
            finish();
        } else if (view == btn_cancel) {
            finish();
        } else if (view == img_one) {
            Pix.start(this, 100, 1);
        } else if (view == btn_submit) {
            vailidate();
        }
    }

    private void vailidate() {
        title = edt_title.getText().toString();
        price = edt_price.getText().toString();
        if (path == null) {
            Toast.makeText(this, "Please Select Image", Toast.LENGTH_SHORT).show();
        } else if (title.equalsIgnoreCase("")) {
            edt_title.setError("Enter Name");
        } else if (price.equalsIgnoreCase("")) {
            edt_price.setError("Enter Price");
        } else {
            addPostCall();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            for (int i = 0; i < returnValue.size(); i++) {
                path = returnValue.get(i);
                Glide.with(this).load(path).into(img_one);
            }
        }
    }

    private void addPostCall() {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(AddPostActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        File file = null;
        if (path != null) {
            path = resizeAndCompressImageBeforeSend(this, path, "user_image");
            file = new File(path);
        }
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        Call<ResponseBody> call = AppConfig.loadInterface().addSocialPost(title, price, user_id, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        System.out.println("post" + object);
                        if (object.getString("status").equals("1")) {
                            final PrettyDialog pDialog = new PrettyDialog(AddPostActivity.this);
                            pDialog.setTitle("Success")
                                    .setIcon(R.drawable.checked)
                                    .setMessage("Your post added successfully")
                                    .addButton("Ok", R.color.pdlg_color_white, R.color.pdlg_color_green,
                                            new PrettyDialogCallback() {
                                                @Override
                                                public void onClick() {
                                                    pDialog.dismiss();
                                                    finish();
                                                }
                                            }
                                    )

                                    .addButton("Re-post", R.color.pdlg_color_white, R.color.pdlg_color_red,
                                            new PrettyDialogCallback() {
                                                @Override
                                                public void onClick() {
                                                    pDialog.dismiss();
                                                    Intent i = new Intent(AddPostActivity.this, AddPostActivity.class);
                                                    startActivity(i);
                                                    finish();
                                                }
                                            }
                                    ).show();
                        } else {
                            Toast.makeText(AddPostActivity.this, "Something is wrong....", Toast.LENGTH_SHORT).show();
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
                progressDialog.dismiss();
                Toast.makeText(AddPostActivity.this, "Please Check Connection", Toast.LENGTH_SHORT).show();
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


}