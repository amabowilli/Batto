package com.techno.batto.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fxn.pix.Pix;
import com.google.android.gms.maps.model.LatLng;
import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.CurrencyPickerListener;
import com.techno.batto.App.AppConfig;
import com.techno.batto.App.GPSTracker;
import com.techno.batto.App.GeoCodingLocationLatlng;
import com.techno.batto.R;
import com.techno.batto.autoaddress.GeoAutoCompleteAdapter;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

public class AddProductActivity extends AppCompatActivity implements View.OnClickListener, IPickResult {
    private ImageView img_back;
    private ImageView img_one, img_two, img_three, img_four;
    private String img1, img2, img3, img4, ttl, discription, price, exchange_with = "", user_id, negotiable = "NO", exchange = "NO", address;
    private EditText edt_title, edt_price, edt_discription;
    public static String category = "", category_name = "", currency_type = "USD";
    private Button btn_submit, btn_cancel;
    private AutoCompleteTextView edt_address;
    private Spinner sp_exchange;
    private List<String> currency_list;
    private List<String> product_list;
    private List<String> categories_list;
    private List<String> categoriesid_list;
    private Double lat, lon;
    private CheckBox chk_one, chk_two;
    private ImageView img_one_cross, img_two_cross, img_three_cross, img_four_cross;
    private int countDrop1 = 0;
    private double longitude = 0.0;
    private double latitude = 0.0;
    public static TextView txt_conditionadd, currency_name, cat_name;
    private LinearLayout lay_condition, lay_currency, lay_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); // ******for Key board hiding
        findID();
        Pix.start(this, 100, 4);
        user_id = getData(this, "user_id", null);
        img_back.setOnClickListener(this);
        img_one.setOnClickListener(this);
        img_two.setOnClickListener(this);
        img_three.setOnClickListener(this);
        img_four.setOnClickListener(this);

        img_one_cross.setOnClickListener(this);
        img_two_cross.setOnClickListener(this);
        img_three_cross.setOnClickListener(this);
        img_four_cross.setOnClickListener(this);

        btn_submit.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        lay_condition.setOnClickListener(this);
        lay_currency.setOnClickListener(this);
        lay_category.setOnClickListener(this);


        sp_exchange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                exchange_with = product_list.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        chk_two.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    getProductListCall();
                    sp_exchange.setVisibility(View.VISIBLE);
                } else {
                    sp_exchange.setVisibility(View.GONE);
                }
            }
        });


        edt_address.setThreshold(1);
        edt_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    loadDataDrop(edt_address.getText().toString());
                }
            }
        });

        GPSTracker track = new GPSTracker(this);
        if (track.canGetLocation()) {
            latitude = track.getLatitude();
            longitude = track.getLongitude();
            address = GeoCodingLocationLatlng.getAddressFromLocation(latitude, longitude, this);
            edt_address.setText(address);
        } else {
            track.showSettingsAlert();
        }


    }

    private void findID() {
        img_back = findViewById(R.id.img_back);
        img_one = findViewById(R.id.img_one);
        img_two = findViewById(R.id.img_two);
        img_three = findViewById(R.id.img_three);
        img_four = findViewById(R.id.img_four);
        edt_title = findViewById(R.id.edt_title);
        edt_discription = findViewById(R.id.edt_discription);
        edt_price = findViewById(R.id.edt_price);
        edt_address = findViewById(R.id.edt_address);
        sp_exchange = findViewById(R.id.sp_exchange);
        btn_submit = findViewById(R.id.btn_submit);
        btn_cancel = findViewById(R.id.btn_cancel);
        img_one_cross = findViewById(R.id.img_one_cross);
        img_two_cross = findViewById(R.id.img_two_cross);
        img_three_cross = findViewById(R.id.img_three_cross);
        img_four_cross = findViewById(R.id.img_four_cross);
        chk_one = findViewById(R.id.chk_one);
        chk_two = findViewById(R.id.chk_two);
        lay_condition = findViewById(R.id.lay_condition);
        txt_conditionadd = findViewById(R.id.txt_conditionadd);
        lay_currency = findViewById(R.id.lay_currency);
        currency_name = findViewById(R.id.currency_name);
        lay_category = findViewById(R.id.lay_category);
        cat_name = findViewById(R.id.cat_name);
    }

    @Override
    public void onClick(View view) {
        if (view == img_back) {
            finish();
        } else if (view == btn_cancel) {
            finish();
        } else if (view == img_one) {
            if (img1 == null) {
                pickImage();
                //  PickImageDialog.build(new PickSetup()).show(this);
            }
        } else if (view == img_two) {
            if (img2 == null) {
                pickImage();
                //   PickImageDialog.build(new PickSetup()).show(this);
            }
        } else if (view == img_three) {
            if (img3 == null) {
                pickImage();
                //   PickImageDialog.build(new PickSetup()).show(this);
            }
        } else if (view == img_four) {
            if (img4 == null) {
                pickImage();
                //  PickImageDialog.build(new PickSetup()).show(this);
            }
        } else if (view == btn_submit) {
            if (user_id != null) {
                validate();
            } else {
                // Toast.makeText(this, "Por favor ingresa primero.....", Toast.LENGTH_SHORT).show();
            }
        } else if (view == img_one_cross) {
            img1 = null;
            img_one.setImageResource(R.drawable.add_image);
        } else if (view == img_two_cross) {
            img2 = null;
            img_two.setImageResource(R.drawable.add_image);
        } else if (view == img_three_cross) {
            img3 = null;
            img_three.setImageResource(R.drawable.add_image);
        } else if (view == img_four_cross) {
            img4 = null;
            img_four.setImageResource(R.drawable.add_image);
        } else if (view == lay_condition) {
            Intent i = new Intent(AddProductActivity.this, ConditionActivity.class);
            i.putExtra("type","Add");
            startActivity(i);
        } else if (view == lay_currency) {
            Intent i = new Intent(AddProductActivity.this, CountryListActivity.class);
            i.putExtra("type","Add");
            startActivity(i);
        } else if (view == lay_category) {
            Intent i = new Intent(AddProductActivity.this, CategoryListActivity.class);
            i.putExtra("type","Add");
            startActivity(i);
        }
    }

    private void currencyPicker1() {
        final CurrencyPicker picker = CurrencyPicker.newInstance("Select Currency");  // dialog title
        picker.setListener(new CurrencyPickerListener() {
            @Override
            public void onSelectCurrency(String name, String code, String symbol, int flagDrawableResID) {
                //img_flag.setImageDrawable(flagDrawableResID);
                //img_flag.setBackgroundResource(flagDrawableResID);
                currency_name.setText(name + " : " + code);
                currency_type = code;
                picker.dismiss();
            }
        });
        picker.show(getSupportFragmentManager(), "CURRENCY_PICKER");
    }

    private void pickImage() {
        if (img1 == null) {
            Pix.start(this, 100, 4);
        } else if (img2 == null) {
            Pix.start(this, 100, 3);
        } else if (img3 == null) {
            Pix.start(this, 100, 2);
        } else if (img4 == null) {
            Pix.start(this, 100, 1);
        }
    }

    private void validate() {
        ttl = edt_title.getText().toString();
        discription = edt_discription.getText().toString();
        price = edt_price.getText().toString();
        address = edt_address.getText().toString();

        if (chk_one.isChecked()) {
            negotiable = "YES";
        } else {
            negotiable = "NO";
        }

        if (chk_two.isChecked()) {
            exchange = "YES";
        } else {
            exchange = "NO";
        }

        if (img1 == null) {
            Toast.makeText(this, "Please Select atleast one Image", Toast.LENGTH_SHORT).show();
        } else if (ttl.equalsIgnoreCase("")) {
            edt_title.setError("Enter Title");
        } else if (discription.equalsIgnoreCase("")) {
            edt_discription.setError("Enter Descrition");
        } else if (price.equalsIgnoreCase("")) {
            edt_price.setError("Enter Price");
        }
        if (currency_type.equalsIgnoreCase("Select Currency")) {
            Toast.makeText(this, " Select Currency", Toast.LENGTH_SHORT).show();
        } else if (address.equalsIgnoreCase("")) {
            edt_address.setError("Enter Location");
        } else if (category.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please Select Category", Toast.LENGTH_SHORT).show();
        } else if (exchange.equalsIgnoreCase("YES")) {
            if (exchange_with.equalsIgnoreCase("Select Exchange With")) {
                Toast.makeText(this, "Please Select Exchange With", Toast.LENGTH_SHORT).show();
            } else {
                LatLng latlg = null;
                try {
                    latlg = GeoCodingLocationLatlng.getLocationFromAddress(address, AddProductActivity.this);
                    latitude = latlg.latitude;
                    longitude = latlg.longitude;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                addPostCall();
            }
        } else {
            LatLng latlg = null;
            try {
                latlg = GeoCodingLocationLatlng.getLocationFromAddress(address, AddProductActivity.this);
                latitude = latlg.latitude;
                longitude = latlg.longitude;
            } catch (Exception e) {
                e.printStackTrace();
            }
            addPostCall();
        }
    }

    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {
            if (img1 == null) {
                img_one.setImageBitmap(r.getBitmap());
                img1 = r.getPath();
            } else if (img2 == null) {
                img_two.setImageBitmap(r.getBitmap());
                img2 = r.getPath();
            } else if (img3 == null) {
                img_three.setImageBitmap(r.getBitmap());
                img3 = r.getPath();
            } else if (img4 == null) {
                img_four.setImageBitmap(r.getBitmap());
                img4 = r.getPath();
            }

        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            for (int i = 0; i < returnValue.size(); i++) {
                if (img1 == null) {
                    img1 = returnValue.get(i);
                    Glide.with(this).load(img1).into(img_one);
                } else if (img2 == null) {
                    img2 = returnValue.get(i);
                    Glide.with(this).load(img2).into(img_two);
                } else if (img3 == null) {
                    img3 = returnValue.get(i);
                    Glide.with(this).load(img3).into(img_three);
                } else if (img4 == null) {
                    img4 = returnValue.get(i);
                    Glide.with(this).load(img4).into(img_four);
                }

            }
        }
    }

    private void getProductListCall() {
        Call<ResponseBody> call = AppConfig.loadInterface().get_product("", "" + latitude, "" + longitude);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        System.out.println("Login Data :- " + object);
                        if (object.getString("status").equals("1")) {

                            product_list = new ArrayList<>();

                            JSONArray arr = object.getJSONArray("result");
                            product_list.add("Select Exchange With");
                            for (int i = 0; i < arr.length(); i++) {
                                String s = arr.getJSONObject(i).getString("name");
                                if (s.equalsIgnoreCase("")) {

                                } else {
                                    product_list.add(s);
                                }
                            }

                            // Creating adapter for spinner
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddProductActivity.this, android.R.layout.simple_spinner_item, product_list);
                            // Drop down layout style - list view with radio button
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            // attaching data adapter to spinner
                            sp_exchange.setAdapter(dataAdapter);

                        } else {
                            Toast.makeText(AddProductActivity.this, "Category Not Found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AddProductActivity.this, "Please Check Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDataDrop(String s) {
        try {
            if (countDrop1 == 0) {
                List<String> l1 = new ArrayList<>();
                if (s == null) {

                } else {
                    l1.add(s);
                    GeoAutoCompleteAdapter ga = new GeoAutoCompleteAdapter(AddProductActivity.this, l1, "" + latitude, "" + longitude, edt_address);
                    edt_address.setAdapter(ga);
                }
            }
            countDrop1++;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void addPostCall() {
        final android.app.ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(AddProductActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        File file1, file2, file3, file4;
        RequestBody requestFile1, requestFile2, requestFile3, requestFile4;
        MultipartBody.Part body1 = null, body2 = null, body3 = null, body4 = null;
        if (img1 != null) {
            img1 = resizeAndCompressImageBeforeSend(this, img1, "user_image1");
            file1 = new File(img1);
            requestFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
            body1 = MultipartBody.Part.createFormData("image1", file1.getName(), requestFile1);
        } else {
            body1 = MultipartBody.Part.createFormData("image1", "");
        }

        if (img2 != null) {
            img2 = resizeAndCompressImageBeforeSend(this, img2, "user_image2");
            file2 = new File(img2);
            requestFile2 = RequestBody.create(MediaType.parse("multipart/form-data"), file2);
            body2 = MultipartBody.Part.createFormData("image2", file2.getName(), requestFile2);
        } else {
            body2 = MultipartBody.Part.createFormData("image2", "");
        }

        if (img3 != null) {
            img3 = resizeAndCompressImageBeforeSend(this, img3, "user_image3");
            file3 = new File(img3);
            requestFile3 = RequestBody.create(MediaType.parse("multipart/form-data"), file3);
            body3 = MultipartBody.Part.createFormData("image3", file3.getName(), requestFile3);
        } else {
            body3 = MultipartBody.Part.createFormData("image3", "");
        }

        if (img4 != null) {
            img4 = resizeAndCompressImageBeforeSend(this, img4, "user_image4");
            file4 = new File(img4);
            requestFile4 = RequestBody.create(MediaType.parse("multipart/form-data"), file4);
            body4 = MultipartBody.Part.createFormData("image4", file4.getName(), requestFile4);
        } else {
            body4 = MultipartBody.Part.createFormData("image4", "");
        }

        Call<ResponseBody> call = AppConfig.loadInterface().add_Post(ttl, discription, price, address, "" + latitude, "" + longitude, category, user_id, negotiable, exchange, currency_type, exchange_with, category_name, body1, body2, body3, body4);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        System.out.println("Add Post-->" + object);
                        if (object.getString("status").equals("1")) {
                            final PrettyDialog pDialog = new PrettyDialog(AddProductActivity.this);
                            pDialog.setTitle("Success")
                                    .setIcon(R.drawable.checked)
                                    .setMessage("Your product added successfully")
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
                                                    Intent i = new Intent(AddProductActivity.this, AddProductActivity.class);
                                                    startActivity(i);
                                                    finish();

                                                }
                                            }
                                    ).show();
                        } else {
                            Toast.makeText(AddProductActivity.this, "Something is wrong....", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AddProductActivity.this, "Please Check Connection", Toast.LENGTH_SHORT).show();
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
