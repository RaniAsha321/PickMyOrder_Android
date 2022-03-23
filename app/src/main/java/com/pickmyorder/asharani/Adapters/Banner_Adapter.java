package com.pickmyorder.asharani.Adapters;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pickmyorder.asharani.Activities.Home;
import com.pickmyorder.asharani.Models.BannersArray;
import com.pickmyorder.asharani.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;

import static com.google.firebase.crashlytics.internal.Logger.TAG;
import static com.pickmyorder.asharani.Activities.Home.viewPager;

public class Banner_Adapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private Integer [] images = {R.drawable.image1,R.drawable.image2,R.drawable.image3};
    List<BannersArray> myAdsList;
  /*  public static ImageButton btn_play_pause;
    public static VideoView videoView;
    public static ProgressBar progressBar;*/
    int carouseltime;

    public Banner_Adapter(Context context, List<BannersArray> myAdsList, int carouseltime) {
        this.context = context;
        this.myAdsList = myAdsList;
        this.carouseltime = carouseltime;    }

    @Override
    public int getCount() {
        return myAdsList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
        return view==object;
    }

    @NonNull
    @NotNull
    @Override
    public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {

       LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.trial, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView_banner);
        VideoView videoView = (VideoView) view.findViewById(R.id.videoView_banner);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        ImageButton btn_play_pause = (ImageButton) view.findViewById(R.id.btn_play_pause);
        String type = myAdsList.get(position).getType();
        Timer timer1 = new Timer();

        btn_play_pause.setTag("play");
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (videoView.isPlaying()){

                    videoView.pause();
                    btn_play_pause.setVisibility(View.VISIBLE);
                    btn_play_pause.setBackgroundResource(R.drawable.ic_play);
                }

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (type != null && type.equals("image")){

            container.refreshDrawableState();
            imageView.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
            btn_play_pause.setVisibility(View.GONE);
           //
            // imageView.setImageResource(myAdsList.get(position).getSource());
           // progressBar.setVisibility(View.VISIBLE);
       /*     Glide.with(context).load(myAdsList.get(position).getSource()).thumbnail(Glide.with(context).load(R.drawable.animate_new))
                    .fitCenter().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);*/

            Glide.with(context).load(myAdsList.get(position).getSource()).placeholder(R.drawable.placeholder_img).fitCenter().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
           // progressBar.setVisibility(View.GONE);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!myAdsList.get(position).getUrl().equals("")){

                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(myAdsList.get(position).getUrl()));
                        context.startActivity(i);

                    }

                }
            });

        }
        else {

            container.refreshDrawableState();
            imageView.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);

           // videoView.setBackgroundResource(R.drawable.animate_new);
            btn_play_pause.setVisibility(View.VISIBLE);
            MediaController mediaController = new MediaController(context);
            mediaController.setAnchorView(videoView);
           // videoView.setMediaController(mediaController);
            videoView.setZOrderMediaOverlay(true);
            videoView.setVideoURI(Uri.parse(myAdsList.get(position).getSource()));
            videoView.setBackgroundColor(Color.TRANSPARENT);
            btn_play_pause.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("UseCompatLoadingForDrawables")
                @Override
                public void onClick(View v) {

                    if (!videoView.isPlaying()){

                        Home.timerTask.cancel();
                      //  btn_play_pause.setVisibility(View.VISIBLE);

                        videoView.start();
                        btn_play_pause.setBackgroundResource(R.drawable.ic_pause);
                        btn_play_pause.setTag("pause");


                        TimerTask timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                btn_play_pause.post(new Runnable(){

                                    @Override
                                    public void run() {
                                        btn_play_pause.setVisibility(View.GONE);
                                    }
                                });
                            }
                        };
                        Timer timer = new Timer();
                        timer.schedule(timerTask, 5000, 5000);


                    }

                    else {

                        int timer_time = Integer.parseInt(Paper.book().read("timer_time"));

                       // Timer timer1 = new Timer();
                      //  timer1.schedule(Home.timerTask, timer_time, timer_time);
                     //   btn_play_pause.setVisibility(View.VISIBLE);
                      //  timer1.schedule(Home.timerTask,carouseltime,carouseltime);

                        TimerTask timerTask1 = new TimerTask() {
                            @Override
                            public void run() {
                                viewPager.post(new Runnable(){

                                    @Override
                                    public void run() {
                                        viewPager.setCurrentItem((viewPager.getCurrentItem()+1)%myAdsList.size());
                                    }
                                });
                            }
                        };
                         timer1.schedule(timerTask1,carouseltime,carouseltime);



                        videoView.pause();
                        btn_play_pause.setBackgroundResource(R.drawable.ic_play);
                        btn_play_pause.setTag("play");


/*
                        if (btn_play_pause.getTag().equals("play"))
                        {
                            Paper.book().write("video_stopped","1");
                            Home.BannerCall();
                            // new RegisterAsyntaskNew().execute();
                        }
*/

                        TimerTask timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                btn_play_pause.post(new Runnable(){

                                    @Override
                                    public void run() {
                                        btn_play_pause.setVisibility(View.GONE);
                                    }
                                });
                            }
                        };
                        Timer timer = new Timer();
                        timer.schedule(timerTask, 5000, 5000);
                    }




                }
            });

/*            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    progressBar.setVisibility(View.VISIBLE);

                    mp.setLooping(true);

                    mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                        @Override
                        public boolean onInfo(MediaPlayer mp, int what, int extra) {
                            if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END){
                               // progressDialogs.dismiss();
                                progressBar.setVisibility(View.GONE);
                                return true;
                            } else if(what == MediaPlayer.MEDIA_INFO_BUFFERING_START){
                               // progressDialogs.show();
                                progressBar.setVisibility(View.VISIBLE);
                            }
                            return false;
                        }
                    });
                   // progressDialogs.dismiss();

                    progressBar.setVisibility(View.GONE);

                    //  videoView.start();
                    if (!videoView.isPlaying()){

                       // videoView.start();
                        btn_play_pause.setBackgroundResource(R.drawable.ic_play);


                        TimerTask timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                btn_play_pause.post(new Runnable(){

                                    @Override
                                    public void run() {
                                        btn_play_pause.setVisibility(View.GONE);
                                    }
                                });
                            }
                        };
                        Timer timer = new Timer();
                        timer.schedule(timerTask, 5000, 5000);


                    }

                    else {

                       // videoView.pause();
                        btn_play_pause.setBackgroundResource(R.drawable.ic_pause);

                        TimerTask timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                btn_play_pause.post(new Runnable(){

                                    @Override
                                    public void run() {
                                        btn_play_pause.setVisibility(View.GONE);
                                    }
                                });
                            }
                        };
                        Timer timer = new Timer();
                        timer.schedule(timerTask, 5000, 5000);
                    }

                }
            });*/

        /*    videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                  *//*  progressBar.setVisibility(View.VISIBLE);

                    mp.setLooping(true);

                    mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                        @Override
                        public boolean onInfo(MediaPlayer mp, int what, int extra) {
                            if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END){
                                // progressDialogs.dismiss();
                                progressBar.setVisibility(View.GONE);
                                return true;
                            } else if(what == MediaPlayer.MEDIA_INFO_BUFFERING_START){
                                // progressDialogs.show();
                                progressBar.setVisibility(View.VISIBLE);
                            }
                            return false;
                        }
                    });
                    // progressDialogs.dismiss();

                    progressBar.setVisibility(View.GONE);*//*
                    videoView.start();
                }
            });*/

            videoView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    btn_play_pause.setVisibility(View.VISIBLE);

                    return false;
                }
            });

        }


      //  container.addView(imageView);
        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
