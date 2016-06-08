package biezhi.videoplayer;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.rey.material.widget.ImageButton;
import com.rey.material.widget.ProgressView;
import com.rey.material.widget.Spinner;
import com.rey.material.widget.TextView;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.widget.media.IjkVideoView;

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

    //控制器相关
    ImageButton playOrPauseButton;
    ImageButton fullScreenButton;
    SeekBar progressBar;
    TextView totalTimeTv;
    TextView currentTimeTv;
    RelativeLayout controllerRl;

    //来源相关
    Spinner qualitySpinner;
    private int[] sourceBitIds;
    ImageButton downloadAddButton;
    ImageButton favorateAddButton;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    //所有来源不初始化，需要一个初始化一个

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        initClass();
        initVideo();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
        //根据videoId请求播放地址
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

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "videoPlay Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://biezhi.videoplayer/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "videoPlay Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://biezhi.videoplayer/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
