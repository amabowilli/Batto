package com.techno.batto.Interface;


import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


/**
 * Created by nitin on 25-09-2017.
 */

public interface UserInterface {

    @POST("webservice/social_login")
    Call<ResponseBody> social_login(
            @Query("user_name") String username,
            @Query("email") String email,
            @Query("register_id") String register_id,
            @Query("social_id") String social_id,
            @Query("verify_with") String verify_with,
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("ios_register_id") String ios_register_id);


    @POST("webservice/login")
    Call<ResponseBody> login(
            @Query("email") String email,
            @Query("password") String password,
            @Query("register_id") String register_id,
            @Query("lat") String lat,
            @Query("lon") String lon
    );

    @POST("webservice/signup")
    Call<ResponseBody> signup(@Query("user_name") String username,
                              @Query("mobile") String mobile,
                              @Query("email") String email,
                              @Query("password") String password,
                              @Query("register_id") String register_id,
                              @Query("lat") String lat,
                              @Query("lon") String lon);


    @POST("webservice/forgot_password")
    Call<ResponseBody> forgot_password(
            @Query("email") String email);

    @POST("webservice/get_profile")
    Call<ResponseBody> get_profile(
            @Query("user_id") String user_id);

    @Multipart
    @POST("webservice/update_profile")
    Call<ResponseBody> update_profile(
            @Query("user_id") String user_id,
            @Query("user_name") String user_name,
            @Query("first_name") String first_name,
            @Query("last_name") String last_name,
            @Query("website") String website,
            @Query("bio") String bio,
            @Query("email") String email,
            @Query("mobile") String mobile,
            @Query("gender") String gender,
            @Part MultipartBody.Part file);

    @POST("webservice/get_category")
    Call<ResponseBody> get_category();

    @POST("webservice/get_sub_category")
    Call<ResponseBody> get_subcategory(@Query("category_id") String category_id);

    @POST("webservice/get_chaild_sub_category")
    Call<ResponseBody> get_secondsubcategory(@Query("sub_cat_id") String sub_cat_id);

    @POST("webservice/get_country")
    Call<ResponseBody> get_country();


    @POST("webservice/get_product_details")
    Call<ResponseBody> get_productDetails(@Query("product_id") String product_id);

    @POST("webservice/add_report")
    Call<ResponseBody> addReport(@Query("user_id") String user_id,
                                 @Query("product_id") String product_id,
                                 @Query("report") String report);

    @POST("webservice/add_review")
    Call<ResponseBody> addReview(@Query("user_id") String user_id,
                                 @Query("product_id") String product_id,
                                 @Query("pro_user_id") String pro_user_id,
                                 @Query("title") String title,
                                 @Query("comment") String comment,
                                 @Query("rating") String rating);

    @POST("webservice/get_product_by_category")
    Call<ResponseBody> get_product(@Query("cat_id") String cat_id,
                                   @Query("lat") String lat,
                                   @Query("lon") String lon);


    @POST("webservice/get_filter_product_list")
    Call<ResponseBody> get_filter_product(@Query("cat_id") String cat_id,
                                          @Query("from_price") String from_price,
                                          @Query("to_price") String to_price,
                                          @Query("day") String day,
                                          @Query("sort_by") String sort_by,
                                          @Query("distance") String from_distance,
                                          @Query("currency") String currency,
                                          @Query("lat") String lat,
                                          @Query("lon") String lon);


    @Multipart
    @POST("webservice/add_product_by_user")
    Call<ResponseBody> add_Post(
            @Query("name") String name,
            @Query("description") String description,
            @Query("price") String price,
            @Query("address") String address,
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("cat_id") String cat_id,
            @Query("user_id") String user_id,
            @Query("negotiable") String negotiable,
            @Query("exchange") String exchange,
            @Query("currency") String currency,
            @Query("exchange_with") String exchange_with,
            @Query("category_name") String category_name,
            @Part MultipartBody.Part file1,
            @Part MultipartBody.Part file2,
            @Part MultipartBody.Part file3,
            @Part MultipartBody.Part file4);


    @Multipart
    @POST("webservice/update_product_by_user")
    Call<ResponseBody> update_Post(
            @Query("product_id") String product_id,
            @Query("name") String name,
            @Query("description") String description,
            @Query("price") String price,
            @Query("address") String address,
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("cat_id") String cat_id,
            @Query("user_id") String user_id,
            @Query("negotiable") String negotiable,
            @Query("exchange") String exchange,
            @Query("currency") String currency,
            @Query("exchange_with") String exchange_with,
            @Query("category_name") String category_name,
            @Part MultipartBody.Part file1,
            @Part MultipartBody.Part file2,
            @Part MultipartBody.Part file3,
            @Part MultipartBody.Part file4);


    @POST("webservice/get_conversation")
    Call<ResponseBody> getConversetion(@Query("receiver_id") String receiver_id);

    @POST("webservice/get_interested_product")
    Call<ResponseBody> get_interested_product(@Query("product_id") String product_id);

    @POST("webservice/get_buying_conversation")
    Call<ResponseBody> getBuingConversetion(@Query("receiver_id") String receiver_id);


    @POST("webservice/get_selling_conversation")
    Call<ResponseBody> getSalingConversetion(@Query("receiver_id") String receiver_id);


    @GET("webservice/get_chat")
    Call<ResponseBody> getChat1(@Query("sender_id") String sender_id,
                                @Query("receiver_id") String receiver_id,
                                @Query("product_id") String product_id);


    @GET("webservice/insert_chat")
    Call<ResponseBody> insertChat(@Query("sender_id") String sender_id,
                                  @Query("receiver_id") String receiver_id,
                                  @Query("product_id") String product_id,
                                  @Query("chat_message") String chat_message,
                                  @Query("type") String type,
                                  @Query("lat") String lat,
                                  @Query("lon") String lon
                                  );

    @Multipart
    @POST("webservice/insert_chat")
    Call<ResponseBody> insertChatImage(@Query("sender_id") String sender_id,
                                       @Query("receiver_id") String receiver_id,
                                       @Query("product_id") String product_id,
                                       @Query("chat_message") String chat_message,
                                       @Query("type") String type,
                                       @Query("lat") String lat,
                                       @Query("lon") String lon,
                                       @Part MultipartBody.Part file);


    @POST("webservice/add_remove_notify")
    Call<ResponseBody> addRemoveFav(@Query("user_id") String user_id,
                                    @Query("product_id") String product_id);

    @POST("webservice/follower_request")
    Call<ResponseBody> addFollow(@Query("request_id") String request_id,
                                 @Query("request_get_id") String request_get_id);

    @POST("webservice/add_product_view")
    Call<ResponseBody> addRemoveView(@Query("user_id") String user_id,
                                     @Query("product_id") String product_id);

    @POST("webservice/get_user_product_list")
    Call<ResponseBody> getMyProduct(@Query("user_id") String user_id);

    @POST("webservice/get_user_sold_product_list")
    Call<ResponseBody> getMySoldProduct(@Query("user_id") String user_id);


    @POST("webservice/delete_product")
    Call<ResponseBody> deleteproduct(@Query("product_id") String product_id);

    @POST("webservice/get_fav_product")
    Call<ResponseBody> getFavproduct(@Query("user_id") String user_id);

    @POST("webservice/get_reviews")
    Call<ResponseBody> get_reviews(@Query("user_id") String user_id);

    @POST("webservice/add_interested_product")
    Call<ResponseBody> addIntrestedUser(@Query("user_id") String user_id,
                                        @Query("product_id") String product_id);

    @POST("webservice/sale_product")
    Call<ResponseBody> saleproduct(@Query("product_id") String product_id,
                                   @Query("user_id") String user_id);

    @Multipart
    @POST("webservice/add_social_post")
    Call<ResponseBody> addSocialPost(@Query("title") String title,
                                     @Query("price") String price,
                                     @Query("user_id") String user_id,
                                     @Part MultipartBody.Part body);


    @POST("webservice/get_social_post")
    Call<ResponseBody> getPostList();

    @POST("webservice/get_my_post")
    Call<ResponseBody> get_my_post(@Query("user_id") String user_id);

    @POST("webservice/add_remove_social_post_like")
    Call<ResponseBody> addLikeSocialPost(@Query("user_id") String user_id,
                                         @Query("social_post_id") String social_post_id);

    @POST("webservice/add_view_social_post")
    Call<ResponseBody> addViewSocialPost(@Query("user_id") String user_id,
                                         @Query("social_post_id") String social_post_id);

    @POST("webservice/get_users_list")
    Call<ResponseBody> get_users_list(@Query("user_id") String user_id);

    @POST("webservice/delete_conversation")
    Call<ResponseBody> delete_chat(@Query("receiver_id") String receiver_id,
                                   @Query("sender_id") String sender_id,
                                   @Query("product_id") String product_id);

    @POST("webservice/get_following")
    Call<ResponseBody> get_following(@Query("user_id") String user_id);

    @POST("webservice/get_followers")
    Call<ResponseBody> get_followers(@Query("user_id") String user_id);

    @POST("webservice/get_af_country")
    Call<ResponseBody> getCountryList();

}
