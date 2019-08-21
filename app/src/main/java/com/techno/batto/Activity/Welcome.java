package com.techno.batto.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techno.batto.Bean.SliderBean;
import com.techno.batto.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class Welcome extends AppCompatActivity {
    ViewPager viewPager;
    private static int currentPage = 0;
    CircleIndicator circleIndicator_indicator;
    private static int NUM_PAGES = 0;
    List<SliderBean> slist;
    TextView welcome_next_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        slist = new ArrayList<>();

        viewPager =  findViewById(R.id.vp_viewpager);
        circleIndicator_indicator =  findViewById(R.id.ci_indicator);
        welcome_next_text =  findViewById(R.id.welcome_next_text);


        SliderBean sliderBean = new SliderBean();
        sliderBean.setImage(R.drawable.welcome1);
        sliderBean.setTittle(getString(R.string.snapposttext));
        slist.add(sliderBean);

        SliderBean sliderBean1 = new SliderBean();
        sliderBean1.setImage(R.drawable.welcome2);
        sliderBean1.setTittle(getString(R.string.buysellnear));
        slist.add(sliderBean1);

        SliderBean sliderBean2 = new SliderBean();
        sliderBean2.setImage(R.drawable.welcome3);
        sliderBean2.setTittle(getString(R.string.buysellnear));
        slist.add(sliderBean2);

        SliderBean sliderBean3 = new SliderBean();
        sliderBean3.setImage(R.drawable.welcome4);
        sliderBean3.setTittle(getString(R.string.buysellnear));
        slist.add(sliderBean3);

        SliderBean sliderBean4 = new SliderBean();
        sliderBean4.setImage(R.drawable.welcome5);
        sliderBean4.setTittle(getString(R.string.buysellnear));
        slist.add(sliderBean4);

        SliderBean sliderBean5 = new SliderBean();
        sliderBean5.setImage(R.drawable.welcome6);
        sliderBean5.setTittle(getString(R.string.buysellnear));
        slist.add(sliderBean5);

        SliderBean sliderBean6 = new SliderBean();
        sliderBean6.setImage(R.drawable.welcome7);
        sliderBean6.setTittle(getString(R.string.buysellnear));
        slist.add(sliderBean6);

        SliderBean sliderBean7 = new SliderBean();
        sliderBean7.setImage(R.drawable.welcome8);
        sliderBean7.setTittle(getString(R.string.buysellnear));
        slist.add(sliderBean7);



        CustomPagerAdapter customPagerAdapter = new CustomPagerAdapter(Welcome.this,slist);
        viewPager.setAdapter(customPagerAdapter);
        circleIndicator_indicator.setViewPager(viewPager);
        NUM_PAGES = slist.size();
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2000, 2000);

        circleIndicator_indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
              //  Log.e("Curent Page :", "" + currentPage);
//                Toast.makeText(MainBuyerActivity.this, "Curent Page" + "" + currentPage, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });


        //-------------------------------------- on click -----------------------------

        welcome_next_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }
        });
    }

    public class CustomPagerAdapter extends PagerAdapter {

        // private ArrayList<Integer> IMAGES;
        public List<SliderBean> sliderBeanList;
        private LayoutInflater inflater;
        private Context context;


        public CustomPagerAdapter(Context context, List<SliderBean> sliderBeanList) {
            this.context = context;
            this.sliderBeanList = sliderBeanList;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return sliderBeanList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup view, final int position) {
            View imageLayout = inflater.inflate(R.layout.welcome_one_layout, view, false);

            assert imageLayout != null;
            final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.slider_imageview);
            final TextView slider_text = (TextView) imageLayout.findViewById(R.id.slider_text);


            Glide.with(context).load(sliderBeanList.get(position).getImage()).into(imageView);
            slider_text.setText(sliderBeanList.get(position).getTittle());
            view.addView(imageLayout, 0);


            return imageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }


    }


}
