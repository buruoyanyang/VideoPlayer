package biezhi.videoplayer;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
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


    //控制器相关
    ImageButton playOrPauseButton;
    ImageButton fullScreenButton;
    SeekBar progressBar;
    TextView totalTimeTv;
    TextView currentTimeTv;
    RelativeLayout controllerRl;

    //来源相关
    //所有来源不初始化，需要一个初始化一个
    Spinner qualitySpinner;
    private int[] sourceBitIds;
    ImageButton downloadAddButton;
    ImageButton favorateAddButton;


    //视频信息相关
    RelativeLayout videoInfoRl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        initClass();
        initVideo();

    }

    //加载,初始化所有资源
    private void initClass() {
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
        videoInfoRl = (RelativeLayout)findViewById(R.id.video_info_rl);
        //根据videoId请求播放地址
        //默认使用老接口处理
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
        playOrPauseButton = (ImageButton) findViewById(R.id.media_play_pause);
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

    }

    @Override
    public void onBufferingUpdate(IMediaPlayer mp, int percent) {
    }

    @Override
    public void onClick(View v) {
        if (controllerRl.getVisibility() == View.INVISIBLE) {
            Animation animation = AnimationUtils.loadAnimation(videoPlay.this, R.anim.video_controller_in);
            controllerRl.setAnimation(animation);
            controllerRl.setVisibility(View.VISIBLE);
        } else {
            Animation animation = AnimationUtils.loadAnimation(videoPlay.this, R.anim.video_controller_out);
            controllerRl.setAnimation(animation);
            controllerRl.setVisibility(View.INVISIBLE);
        }
        if (v == fullScreenButton)
        {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.GONE,LinearLayout.GONE);
//            lp.addRule(RelativeLayout.GONE);
            videoInfoRl.setLayoutParams(lp);
        }
    }

    @Override
    public boolean onError(IMediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public boolean onInfo(IMediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(IMediaPlayer mp) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

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

}
