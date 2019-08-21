package com.techno.batto.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.techno.batto.App.AppConfig;
import com.techno.batto.Interface.Config;
import com.techno.batto.Interface.UserInterface;
import com.techno.batto.R;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.techno.batto.Bean.MySharedPref.getData;

public class EditProfile extends AppCompatActivity implements View.OnClickListener {
    private ImageView edit_profile_back;
    private CircleImageView user_img;
    private EditText edt_username, edt_fname, edt_lname, edt_website, edt_bio, edt_email, edt_mobile;
    private Spinner spinner;
    private String user_id, final_path;
    private ProgressDialog progressDialog;
    private Bitmap rotatedBitmap;
    List<String> gender = new ArrayList<>();
    private Button btn_save;
    private String username, fname, lname, website, bio, email, mobile, genders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        findId();
        user_id = getData(this, "user_id", "null");
        GetProfile_call();
        gender.add("Male");
        gender.add("Female");
        gender.add("Other");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(EditProfile.this, android.R.layout.simple_spinner_item, gender);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                genders = gender.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        edit_profile_back.setOnClickListener(this);
        user_img.setOnClickListener(this);
        btn_save.setOnClickListener(this);
    }

    private void findId() {
        edit_profile_back = findViewById(R.id.edit_profile_back);
        user_img = findViewById(R.id.user_img);
        edt_username = findViewById(R.id.edt_username);
        edt_fname = findViewById(R.id.edt_fname);
        edt_lname = findViewById(R.id.edt_lname);
        edt_website = findViewById(R.id.edt_website);
        edt_bio = findViewById(R.id.edt_bio);
        edt_email = findViewById(R.id.edt_email);
        edt_mobile = findViewById(R.id.edt_mobile);
        spinner = findViewById(R.id.spinner);
        btn_save = findViewById(R.id.btn_save);
    }

    @Override
    public void onClick(View view) {
        if (view == edit_profile_back) {
            finish();
        } else if (view == user_img) {
            showImagePicker();
        } else if (view == btn_save) {
            validate();
        }
    }

    private void validate() {
        username = edt_username.getText().toString();
        fname = edt_fname.getText().toString();
        lname = edt_lname.getText().toString();
        website = edt_website.getText().toString();
        bio = edt_bio.getText().toString();
        email = edt_email.getText().toString();
        mobile = edt_mobile.getText().toString();

        updateProfileCall();

    }

    private void showImagePicker() {
        final PickImageDialog dialog = PickImageDialog.build(new PickSetup());
        dialog.setOnPickCancel(new IPickCancel() {
            @Override
            public void onCancelClick() {
                dialog.dismiss();
            }
        }).setOnPickResult(new IPickResult() {
            @Override
            public void onPickResult(PickResult r) {

                if (r.getError() == null) {

                    user_img.setImageBitmap(r.getBitmap());
                    Matrix matrix = new Matrix();

                    matrix.postRotate(0);

                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(r.getBitmap(), r.getBitmap().getWidth(), r.getBitmap().getHeight(), true);

                    rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

                    // final_path = r.getPath();

                    final_path = saveToInternalStorage(rotatedBitmap);
                    Log.e("Imagepath", final_path);

                    //r.getPath();
                } else {
                    //Handle possible errors
                    //TODO: do what you have to do with r.getError();
                    Toast.makeText(EditProfile.this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
                }

            }

        }).show(EditProfile.this);
    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String dateToStr = format.format(today);
        ContextWrapper cw = new ContextWrapper(EditProfile.this);
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, "profile_" + dateToStr + ".JPEG");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();
    }

    private void GetProfile_call() {
        progressDialog = new ProgressDialog(EditProfile.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.Base_Url).client(client)
                .addConverterFactory(GsonConverterFactory.create(new Gson())).build();
        UserInterface signupInterface = retrofit.create(UserInterface.class);
        Call<ResponseBody> resultCall = signupInterface.get_profile(user_id);
        resultCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    try {

                        String responedata = response.body().string();
                        Log.e("get profile response** ", " " + responedata);
                        JSONObject object = new JSONObject(responedata);
                        String error = object.getString("status");

                        if (error.equals("1")) {

                            JSONObject jsonObject = object.getJSONObject("result");

                            edt_username.setText(jsonObject.getString("user_name"));
                            edt_fname.setText(jsonObject.getString("first_name"));
                            edt_lname.setText(jsonObject.getString("last_name"));
                            edt_website.setText(jsonObject.getString("website"));
                            edt_bio.setText(jsonObject.getString("bio"));
                            edt_email.setText(jsonObject.getString("email"));
                            edt_mobile.setText(jsonObject.getString("mobile"));

                            Picasso.with(EditProfile.this).load(jsonObject.getString("image")).error(R.drawable.user).into(user_img);

                            gender.add(jsonObject.getString("gender"));
                            // gender = jsonObject.getString("gender");


                        } else {
                            String message = object.getString("message");
                            Toast.makeText(EditProfile.this, "" + message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("hgdhgfgdf", "dtrdfuydrfgjhjjfyt");

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditProfile.this, "Server Problem Please try Next time...!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProfileCall() {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(EditProfile.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        RequestBody requestFile;
        MultipartBody.Part body;
        if (final_path != null) {
            File file = new File(final_path);
            requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        } else {
            body = MultipartBody.Part.createFormData("image", "");
        }

        Call<ResponseBody> call = AppConfig.loadInterface().update_profile(user_id, username, fname, lname, website, bio, email, mobile, genders, body);
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
                            final PrettyDialog pDialog = new PrettyDialog(EditProfile.this);
                            pDialog.setTitle("Success")
                                    .setIcon(R.drawable.checked)
                                    .setMessage("Your profile is update successfully")
                                    .addButton("Ok", R.color.pdlg_color_white, R.color.pdlg_color_green,
                                            new PrettyDialogCallback() {
                                                @Override
                                                public void onClick() {
                                                    pDialog.dismiss();
                                                    finish();
                                                }
                                            }
                                    )

                                    .addButton("Re-edit", R.color.pdlg_color_white, R.color.pdlg_color_red,
                                            new PrettyDialogCallback() {
                                                @Override
                                                public void onClick() {
                                                    pDialog.dismiss();
                                                }
                                            }
                                    ).show();
                        } else {
                            Toast.makeText(EditProfile.this, "Something is wrong....", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(EditProfile.this, "Please Check Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
