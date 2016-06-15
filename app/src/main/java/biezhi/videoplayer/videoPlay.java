package biezhi.videoplayer;


import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;


import com.rey.material.widget.ImageButton;
import com.rey.material.widget.ProgressView;
import com.rey.material.widget.Spinner;
import com.rey.material.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import biezhi.videoplayer.MessageBox.SeekBarChangeMessage;
import biezhi.videoplayer.MessageBox.SeekBarChangedMessage;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class videoPlay extends AppCompatActivity implements IMediaPlayer.OnPreparedListener, View.OnClickListener, IMediaPlayer.OnInfoListener, IMediaPlayer.OnErrorListener,
        IMediaPlayer.OnBufferingUpdateListener, IMediaPlayer.OnSeekCompleteListener, SeekBar.OnSeekBarChangeListener, Spinner.OnItemSelectedListener {


    //标题栏相关
    //返回按钮
    ImageButton titleButton;
    //标题栏
    TextView titleTv;
    //搜索按钮
    ImageButton searchButton;
    //下载按钮
    ImageButton downloadButton;
    //历史记录按钮
    ImageButton historyButton;

    //菊花
    ProgressView progressView;

    //播放器相关
    SurfaceView videoView;
    //ijkPlayer
    private IjkMediaPlayer mediaPlayer;
    //holder
    private SurfaceHolder surfaceHolder;
    //播放器布局
    private LinearLayout videoViewLL;
    private SurfaceHolder holder;
    long totalTime = 0;
    long currentTime = 0;


    //控制器相关
    ImageButton playOrPauseButton;
    ImageButton fullScreenButton;
    SeekBar progressBar;
    TextView totalTimeTv;
    TextView currentTimeTv;
    RelativeLayout controllerRl;
    RelativeLayout titleControllerRl;
    ImageButton lockVideo;
    boolean isLocked = false;
    boolean seekBarAutoFlag = true;

    //来源相关
    //所有来源不初始化，需要一个初始化一个
    Spinner qualitySpinner;
    private int[] sourceBitIds;
    ImageButton downloadAddButton;
    ImageButton favorateAddButton;


    //视频信息相关
    RelativeLayout videoInfoRl;

    //工具栏相关
    LinearLayout titleLayoutLl;

    Data appData;

    RelativeLayout rootRl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        appData = (Data) this.getApplication();
        initClass();
        initVideo();

    }

    //加载,初始化所有资源
    private void initClass() {
        EventBus.getDefault().register(this);
        titleButton = (ImageButton) findViewById(R.id.title_app_button);
        titleTv = (TextView) findViewById(R.id.title_app_tv);
        titleButton.setOnClickListener(this);
        searchButton = (ImageButton) findViewById(R.id.title_app_search);
        downloadButton = (ImageButton) findViewById(R.id.title_app_download);
        historyButton = (ImageButton) findViewById(R.id.title_app_history);
        searchButton.setOnClickListener(this);
        downloadButton.setOnClickListener(this);
        historyButton.setOnClickListener(this);

        qualitySpinner = (Spinner) findViewById(R.id.video_quality_spinner);
        sourceBitIds = new int[]{R.drawable.movie_source_1, R.drawable.movie_source_2, R.drawable.movie_source_3, R.drawable.movie_source_4, R.drawable.movie_source_5};
        downloadAddButton = (ImageButton) findViewById(R.id.video_download_add);
        favorateAddButton = (ImageButton) findViewById(R.id.video_favorate_add);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.quality, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qualitySpinner.setAdapter(adapter);
        qualitySpinner.setOnItemSelectedListener(this);
        progressView = (ProgressView) findViewById(R.id.video_loading_on);
        videoInfoRl = (RelativeLayout) findViewById(R.id.video_info_rl);
        titleLayoutLl = (LinearLayout) findViewById(R.id.video_play_title);
        rootRl = (RelativeLayout) findViewById(R.id.video_root_rl);

    }

    private void initVideo() {
        videoView = (SurfaceView) findViewById(R.id.video_view);
        if (videoView != null) {
            videoView.setOnClickListener(this);
        }
        mediaPlayer = new IjkMediaPlayer();
        mediaPlayer.setKeepInBackground(false);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnInfoListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        controllerRl = (RelativeLayout) findViewById(R.id.video_controller_rl);
        titleControllerRl = (RelativeLayout) findViewById(R.id.video_title_controller_rl);
        assert titleControllerRl != null;
        titleControllerRl.setVisibility(View.INVISIBLE);
        playOrPauseButton = (ImageButton) findViewById(R.id.media_play_pause);
        lockVideo = (ImageButton) findViewById(R.id.video_lockOrUnlock);
        assert lockVideo != null;
        lockVideo.setVisibility(View.INVISIBLE);
        lockVideo.setOnClickListener(this);
        if (playOrPauseButton != null) {
            playOrPauseButton.setOnClickListener(this);
        }
        fullScreenButton = (ImageButton) findViewById(R.id.media_full_screen);
        if (fullScreenButton != null) {
            fullScreenButton.setOnClickListener(this);
        }
        progressBar = (SeekBar) findViewById(R.id.media_seek_bar);
        if (progressBar != null) {
            progressBar.setOnSeekBarChangeListener(this);
        }
        totalTimeTv = (TextView) findViewById(R.id.media_time_total);
        currentTimeTv = (TextView) findViewById(R.id.media_time_current);
        holder = videoView.getHolder();
        try {
            mediaPlayer.setDataSource(this, Uri.parse("http://lecloud.educdn.huan.tv/mediadns/ts/AK/CDN2016051800171.mp4"));
            holder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {

                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                    mediaPlayer.setDisplay(holder);
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {

                }
            });
            mediaPlayer.prepareAsync();
            //硬解1 软解0
            mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void steepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer mp, int percent) {
        Log.e("buffer", percent + "");
    }

    @Override
    public void onClick(View v) {
        if (v == videoView) {
            if (appData.isFullScreen()) {
                if (isLocked) {
                    if (lockVideo.getVisibility() == View.INVISIBLE) {
                        lockVideo.bringToFront();
                        Animation animation2 = AnimationUtils.loadAnimation(videoPlay.this, R.anim.video_lock_in);
                        lockVideo.startAnimation(animation2);
                        lockVideo.setVisibility(View.VISIBLE);
                    } else {
                        Animation animation2 = AnimationUtils.loadAnimation(videoPlay.this, R.anim.video_lock_out);
                        lockVideo.setAnimation(animation2);
                        lockVideo.setVisibility(View.INVISIBLE);
                    }
                } else {
                    if (controllerRl.getVisibility() == View.INVISIBLE) {
                        Animation animation = AnimationUtils.loadAnimation(videoPlay.this, R.anim.video_controller_in);
                        controllerRl.setAnimation(animation);
                        controllerRl.setVisibility(View.VISIBLE);
                        Animation animation1 = AnimationUtils.loadAnimation(videoPlay.this, R.anim.video_title_in);
                        titleControllerRl.setAnimation(animation1);
                        titleControllerRl.setVisibility(View.VISIBLE);
                        Animation animation2 = AnimationUtils.loadAnimation(videoPlay.this, R.anim.video_lock_in);
                        lockVideo.startAnimation(animation2);
                        lockVideo.setVisibility(View.VISIBLE);
                        if (android.os.Build.VERSION.SDK_INT >= 19) {
                            steepStatusBar();
                            rootRl.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                        }
                    } else {
                        Animation animation = AnimationUtils.loadAnimation(videoPlay.this, R.anim.video_controller_out);
                        controllerRl.setAnimation(animation);
                        controllerRl.setVisibility(View.INVISIBLE);
                        Animation animation1 = AnimationUtils.loadAnimation(videoPlay.this, R.anim.video_title_out);
                        titleControllerRl.setAnimation(animation1);
                        titleControllerRl.setVisibility(View.INVISIBLE);
                        Animation animation2 = AnimationUtils.loadAnimation(videoPlay.this, R.anim.video_lock_out);
                        lockVideo.setAnimation(animation2);
                        lockVideo.setVisibility(View.INVISIBLE);
                        if (Build.VERSION.SDK_INT >= 19) {
                            rootRl.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
                        }
                    }
                }
            } else {
                if (controllerRl.getVisibility() == View.INVISIBLE) {
                    Animation animation = AnimationUtils.loadAnimation(videoPlay.this, R.anim.video_controller_in);
                    controllerRl.setAnimation(animation);
                    controllerRl.setVisibility(View.VISIBLE);
                } else {
                    Animation animation = AnimationUtils.loadAnimation(videoPlay.this, R.anim.video_controller_out);
                    controllerRl.setAnimation(animation);
                    controllerRl.setVisibility(View.INVISIBLE);
                }
            }
        }
        if (v == fullScreenButton) {
            if (appData.isFullScreen()) {
                videoPlay.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                WindowManager.LayoutParams attrs = getWindow().getAttributes();
                attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getWindow().setAttributes(attrs);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                appData.setFullScreen(false);
            } else {
                videoPlay.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                appData.setFullScreen(true);
            }
        }
        if (v == lockVideo) {
            if (isLocked) {
                //不考虑非全屏状态，因为不可能点到
                //显示所有控件
                Animation animation = AnimationUtils.loadAnimation(videoPlay.this, R.anim.video_controller_in);
                controllerRl.setAnimation(animation);
                controllerRl.setVisibility(View.VISIBLE);
                Animation animation1 = AnimationUtils.loadAnimation(videoPlay.this, R.anim.video_title_in);
                titleControllerRl.setAnimation(animation1);
                titleControllerRl.setVisibility(View.VISIBLE);
                lockVideo.setVisibility(View.VISIBLE);
                //大于19情况下，全部正常
                if (android.os.Build.VERSION.SDK_INT >= 19) {
                    steepStatusBar();
                    rootRl.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                }
                isLocked = !isLocked;
                lockVideo.setImageResource(R.drawable.unlock);

            } else {
                Animation animation = AnimationUtils.loadAnimation(videoPlay.this, R.anim.video_controller_out);
                controllerRl.setAnimation(animation);
                controllerRl.setVisibility(View.INVISIBLE);
                Animation animation1 = AnimationUtils.loadAnimation(videoPlay.this, R.anim.video_title_out);
                titleControllerRl.setAnimation(animation1);
                titleControllerRl.setVisibility(View.INVISIBLE);
                lockVideo.setVisibility(View.INVISIBLE);
                if (Build.VERSION.SDK_INT >= 19) {
                    rootRl.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
                }
                isLocked = !isLocked;
                lockVideo.setImageResource(R.drawable.lock);
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (appData.isFullScreen()) {
            videoInfoRl.setVisibility(View.GONE);
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(dm.widthPixels, dm.heightPixels);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            videoView.setLayoutParams(layoutParams);
            titleLayoutLl.setVisibility(View.GONE);
            titleControllerRl.setVisibility(View.VISIBLE);
            lockVideo.setVisibility(View.INVISIBLE);
            if (Build.VERSION.SDK_INT >= 19) {
                rootRl.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
            } else {
                WindowManager.LayoutParams attrs = getWindow().getAttributes();
                attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                getWindow().setAttributes(attrs);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

            }
            controllerRl.setVisibility(View.INVISIBLE);
            titleControllerRl.setVisibility(View.INVISIBLE);
        } else {
            videoInfoRl.setVisibility(View.VISIBLE);
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(dm.widthPixels, dm.heightPixels);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            videoView.setLayoutParams(layoutParams);
            titleLayoutLl.setVisibility(View.VISIBLE);
            titleControllerRl.setVisibility(View.INVISIBLE);
            lockVideo.setVisibility(View.INVISIBLE);
        }
        super.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onError(IMediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public boolean onInfo(IMediaPlayer mp, int what, int extra) {
        switch (what) {
            case IjkMediaPlayer.MEDIA_INFO_BUFFERING_START:
                //开始缓冲
                progressView.setVisibility(View.VISIBLE);
                break;
            case IjkMediaPlayer.MEDIA_INFO_BUFFERING_END:
                //缓冲结束
                progressView.setVisibility(View.INVISIBLE);
                mediaPlayer.start();
                break;
        }
        return false;
    }

    @Override
    public void onPrepared(IMediaPlayer mp) {
        mediaPlayer.start();
        totalTime = mediaPlayer.getDuration();
        currentTime = 0;
        totalTimeTv.setText(getTime(totalTime));
        currentTimeTv.setText(getTime(currentTime));
        //隐藏菊花
        progressView.setVisibility(View.INVISIBLE);
        progressBar.setMax((int) mediaPlayer.getDuration());
        videoView.setKeepScreenOn(true);
        mediaPlayer.setScreenOnWhilePlaying(true);
        EventBus.getDefault().post(new SeekBarChangeMessage());
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            if (mediaPlayer != null) {
                mediaPlayer.seekTo(progress);
            }
//            currentTimeTv.setText(getTime(progress));
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onSeekComplete(IMediaPlayer mp) {

    }

    @Override
    public void onItemSelected(Spinner parent, View view, int position, long id) {

    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void changeSeekBar(SeekBarChangeMessage message) {
        while (seekBarAutoFlag) {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    EventBus.getDefault().post(new SeekBarChangedMessage());
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeSeekBarByTime(SeekBarChangedMessage message) {
        progressBar.setProgress((int) mediaPlayer.getCurrentPosition());
        currentTimeTv.setText(getTime(mediaPlayer.getCurrentPosition()));
    }

    private String getTime(long timeSeconds) {
        String formatTime;
        String hour;
        String minute;
        String second;
        timeSeconds = timeSeconds / 1000;
        hour = String.valueOf(timeSeconds / 3600);
        if (hour.length() < 2) {
            hour = "0" + hour;
        }
        minute = String.valueOf(timeSeconds % 3600 / 60);
        if (minute.length() < 2) {
            minute = "0" + minute;
        }
        second = String.valueOf(timeSeconds % 3600 % 60);
        if (second.length() < 2) {
            second = "0" + second;
        }
        if (hour.equals("00")) {
            formatTime = minute + ":" + second;
        } else {
            formatTime = hour + ":" + minute + ":" + second;
        }
        return formatTime;
    }

}
