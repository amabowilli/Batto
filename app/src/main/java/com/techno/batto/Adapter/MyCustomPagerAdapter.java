package com.techno.batto.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.techno.batto.R;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class MyCustomPagerAdapter extends PagerAdapter {
    Context context;
    List<String> images;
    LayoutInflater layoutInflater;
    ImageView imageView;
    private ProgressBar progrss;

    public MyCustomPagerAdapter(Context context, List<String> images) {
        this.context = context;
        this.images = images;
        layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.item, container, false);

        imageView = (ImageView) itemView.findViewById(R.id.imageView);
        progrss = (ProgressBar) itemView.findViewById(R.id.progrss);

        //imageView.setImageResource(images[position]);
//        Glide.with(context).load(images.get(position)).asBitmap().into(new SimpleTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                Drawable drawable = new BitmapDrawable(context.getResources(), resource);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    imageView.setBackground(drawable);
//                }
//            }
//        });

        Picasso.with(context).load(images.get(position)).into(imageView);

//        Picasso.with(context).load(images.get(position)).into(new Target() {
//            @Override
//            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
//               // imageView.setImageBitmap(bitmap);
//                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//                    public void onGenerated(Palette p) {
//                        progrss.setVisibility(View.GONE);
//                        int titleColor = p.getVibrantColor(0);
//                        Palette.Swatch vibrantSwatch = p.getLightVibrantSwatch();
//                        if (vibrantSwatch!=null) {
//                            int backgroundColor = vibrantSwatch.getRgb();
//                            imageView.setBackgroundColor(backgroundColor);
//                        }
//
//                    }
//                });
//            }
//
//
//
//            @Override
//            public void onBitmapFailed(Drawable errorDrawable) {
//                progrss.setVisibility(View.GONE);
//                imageView.setImageResource(R.drawable.not_found);
//            }
//
//            @Override
//            public void onPrepareLoad(Drawable placeHolderDrawable) {
//                progrss.setVisibility(View.VISIBLE);
//            }
//        });


        container.addView(itemView);

//        img_view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(context, ZoomRotedImageActivity.class);
//                i.putExtra("image", "" + images.get(position));
//                context.startActivity(i);
//            }
//        });
//
//        //listening to image click
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(context, ImageSlidingActivity.class);
//                context.startActivity(i);
//            }
//        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }


}
