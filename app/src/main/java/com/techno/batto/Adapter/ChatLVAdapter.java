package com.techno.batto.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.squareup.picasso.Picasso;
import com.techno.batto.App.GPSTracker;
import com.techno.batto.Holder.DataHolder;
import com.techno.batto.R;
import com.techno.batto.Result.ChatResult;

import java.util.List;

import static com.techno.batto.Bean.MySharedPref.getData;


/**
 * Created by Techno122 on 8/17/2017.
 */

public class ChatLVAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    List<ChatResult> results;
    private Activity activity;
    private String id = "", user_id = "";

    public ChatLVAdapter(Activity activity, List<ChatResult> results) {
        this.activity = activity;
        this.results = results;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return results.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (convertView == null)
            itemView = inflater.inflate(R.layout.item_chat, null);
        LinearLayout receiverLayout = itemView.findViewById(R.id.receiver_chat_box);
        TextView receiverText = itemView.findViewById(R.id.receiver_chat_message);
        TextView receiveTimeText = itemView.findViewById(R.id.receive_time);
        ImageView receiverPic = itemView.findViewById(R.id.receiver_profile_pic);
        final ImageView receiver_img_chat = itemView.findViewById(R.id.receiver_img_chat);

        LinearLayout senderLayout = itemView.findViewById(R.id.sender_chat_box);
        TextView senderText = itemView.findViewById(R.id.sender_chat_message);
        TextView sendTimeText = itemView.findViewById(R.id.send_time);
        ImageView senderPic = itemView.findViewById(R.id.sender_profile_pic);
        final ImageView sender_img_chat = itemView.findViewById(R.id.sender_img_chat);
        final ChatResult chatResponse = this.results.get(position);
        try {
            user_id = getData(activity, "user_id", null);
            if (chatResponse.getSenderId().equals(DataHolder.getSender())) {
                senderLayout.setVisibility(View.VISIBLE);
                receiverLayout.setVisibility(View.GONE);

                Picasso.with(activity).load(chatResponse.getSenderDetail().getSenderImage()).error(R.drawable.user).into(senderPic);
                //  Picasso.with(activity).load(Constant.IMAGE_URL + chatResponse.getTeacher().getImage()).error(R.drawable.example_default_teacher).into(senderPic);
                if (chatResponse.getType().equals("Image")) {
                    sender_img_chat.setVisibility(View.VISIBLE);
                    senderText.setVisibility(View.GONE);
                    Picasso.with(activity).load(chatResponse.getChatImage()).error(R.drawable.user).into(sender_img_chat);
                    sender_img_chat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ImagePopup imagePopup = new ImagePopup(activity);
                            imagePopup.setWindowHeight(800); // Optional
                            imagePopup.setWindowWidth(800); // Optional
                            imagePopup.setBackgroundColor(Color.BLACK);  // Optional
                            imagePopup.setFullScreen(true); // Optional
                            imagePopup.setHideCloseIcon(true);  // Optional
                            imagePopup.setImageOnClickClose(true);  // Optional
                            imagePopup.initiatePopup(sender_img_chat.getDrawable());
                            imagePopup.viewPopup();
                        }
                    });
                } else if (chatResponse.getType().equals("Location")) {
                    sender_img_chat.setVisibility(View.VISIBLE);
                    senderText.setVisibility(View.GONE);
                    Picasso.with(activity).load(R.drawable.location_chat).error(R.drawable.user).into(sender_img_chat);
                    sender_img_chat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            GPSTracker track = new GPSTracker(activity);
                            if (track.canGetLocation()) {
                                try {
                                    Double lat, lon;
                                    lat = Double.parseDouble(chatResponse.getLat());
                                    lon = Double.parseDouble(chatResponse.getLon());
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + track.getLatitude() + "," + track.getLongitude() + "&daddr=" + lat + "," + lon));
                                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                                    activity.startActivity(intent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                track.showSettingsAlert();
                            }

                        }
                    });
                } else {
                    sender_img_chat.setVisibility(View.GONE);
                    senderText.setVisibility(View.VISIBLE);
                    senderText.setText(chatResponse.getChatMessage());
                }
                sendTimeText.setText(chatResponse.getDate());
            } else {
                receiverLayout.setVisibility(View.VISIBLE);
                senderLayout.setVisibility(View.GONE);
                if (chatResponse.getChatMessage().equals("Image")) {
                    receiver_img_chat.setVisibility(View.VISIBLE);
                    receiverText.setVisibility(View.GONE);
                    Picasso.with(activity).load(chatResponse.getChatImage()).error(R.drawable.user).into(receiver_img_chat);
                    receiver_img_chat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ImagePopup imagePopup = new ImagePopup(activity);
                            imagePopup.setWindowHeight(800); // Optional
                            imagePopup.setWindowWidth(800); // Optional
                            imagePopup.setBackgroundColor(Color.BLACK);  // Optional
                            imagePopup.setFullScreen(true); // Optional
                            imagePopup.setHideCloseIcon(true);  // Optional
                            imagePopup.setImageOnClickClose(true);  // Optional
                            imagePopup.initiatePopup(receiver_img_chat.getDrawable());
                            imagePopup.viewPopup();
                        }
                    });
                } else if (chatResponse.getType().equals("Location")) {
                    receiver_img_chat.setVisibility(View.VISIBLE);
                    receiverText.setVisibility(View.GONE);
                    Picasso.with(activity).load(R.drawable.location_chat).error(R.drawable.user).into(receiver_img_chat);
                    receiver_img_chat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            GPSTracker track = new GPSTracker(activity);
                            if (track.canGetLocation()) {
                                try {
                                    Double lat, lon;
                                    lat = Double.parseDouble(chatResponse.getLat());
                                    lon = Double.parseDouble(chatResponse.getLon());
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + track.getLatitude() + "," + track.getLongitude() + "&daddr=" + lat + "," + lon));
                                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                                    activity.startActivity(intent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                track.showSettingsAlert();
                            }
                        }
                    });
                } else {
                    receiver_img_chat.setVisibility(View.GONE);
                    receiverText.setVisibility(View.VISIBLE);
                    receiverText.setText(chatResponse.getChatMessage());
                }

                receiveTimeText.setText(chatResponse.getDate());
                try {

                    Picasso.with(activity).load(chatResponse.getSenderDetail().getSenderImage()).error(R.drawable.user).into(senderPic);
                    // Picasso.with(activity).load(Constant.IMAGE_URL + chatResponse.getChild().getImage()).error(R.drawable.example_default_chat).into(receiverPic);
                } catch (Exception e) {
                    //receiverPic.setImageResource(R.drawable.example_default_chat);
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return itemView;
    }

}