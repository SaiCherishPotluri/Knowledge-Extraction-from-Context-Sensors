package com.example.amine.learngesture;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.stetho.common.LogUtil;

import static com.example.amine.learngesture.LoginActivity.INTENTEMAIL;
import static com.example.amine.learngesture.LoginActivity.INTENTID;
import static com.example.amine.learngesture.LoginActivity.INTENT_TIMEWATCHED;
import static com.example.amine.learngesture.LoginActivity.INTENT_TIMEWATCHEDVIDEO;
import static com.example.amine.learngesture.LoginActivity.INTENTURI;
import static com.example.amine.learngesture.LoginActivity.INTENTWORD;

public class VideoOperations extends Activity implements SurfaceHolder.Callback {

    private MediaRecorder mediaRecorder;
    private Camera camera;
    private SurfaceView surfaceView;
    private SurfaceHolder holder;
    private Button toggleButton;
    private TextView tvTimer;
    private TextView tvTime;
    Intent return_Intent;
    String return_File;
    VideoOperations activity;
    String word;
    String groupName="group4";
    private boolean initSuccesful;
    SharedPreferences shared_Preferences;
    CountDownTimer timer;
    CountDownTimer time;
    long timeWatched;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        activity = this;
        return_Intent = new Intent();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
        if(getIntent().hasExtra(INTENTWORD)) {
            word = getIntent().getStringExtra(INTENTWORD);
        }
        if(getIntent().hasExtra(INTENT_TIMEWATCHED)) {
            timeWatched = getIntent().getLongExtra(INTENT_TIMEWATCHED,0);
        }
        surfaceView = (SurfaceView) findViewById(R.id.sv_camera);
        holder = surfaceView.getHolder();
        holder.addCallback(this);
        tvTimer = (TextView) findViewById(R.id.tv_timer);
        tvTime = (TextView) findViewById(R.id.tv_time);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        shared_Preferences =  this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        toggleButton = (Button) findViewById(R.id.bt_start);
        time = new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long l) {
                int a = (int) (l / 1000);
                tvTime.setText(a + " ");
            }

            @Override
            public void onFinish() {
                mediaRecorder.stop();
                mediaRecorder.reset();
                if(time!=null) {
                    time.cancel();
                }
                return_Intent.putExtra(INTENTURI, return_File);
                return_Intent.putExtra(INTENT_TIMEWATCHEDVIDEO, timeWatched);
                activity.setResult(8888, return_Intent);
                activity.finish();
            }
        };
        toggleButton.setOnClickListener(new OnClickListener() {
            @Override
            // toggle video recording
            public void onClick(final View v) {
                HashSet<String> buttonClickSet = (HashSet<String>) shared_Preferences.getStringSet("START_STOP_BUTTON_CLICK", new HashSet<String>());
                buttonClickSet.add("START_STOP_BUTTON_CLICK_" + shared_Preferences.getString(INTENTID, "") + "_" + shared_Preferences.getString(INTENTEMAIL, "") + "_" + String.valueOf(System.currentTimeMillis()));
                shared_Preferences.edit().putStringSet("START_STOP_BUTTON_CLICK", buttonClickSet).apply();

                timer = new CountDownTimer(5000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        int a = (int) (millisUntilFinished / 1000);
                        tvTimer.setText(a + " ");
                        ((Button) v).setEnabled(false);
                    }
                    public void onFinish() {
                        tvTimer.setVisibility(View.GONE);
                        ((Button) v).setText("Stop Recording");
                        ((Button) v).setEnabled(true);
                        mediaRecorder.start();
                        time.start();
                    }
                };
                if (((Button) v).getText().toString().equals("Start Recording")) {
                    timer.start();
                }
                else if (((Button) v).getText().toString().equals("Stop Recording")) {
                    mediaRecorder.stop();
                    mediaRecorder.reset();
                    ((Button) v).setText("Start Recording");
                    if(time!=null) {
                        time.cancel();
                    }
                    return_Intent.putExtra(INTENTURI, return_File);
                    return_Intent.putExtra(INTENT_TIMEWATCHEDVIDEO, timeWatched);

                    activity.setResult(8888, return_Intent);
                    activity.finish();

                }
            }
        });
    }

    boolean fileCreated = false;
    private void initRecorder(Surface surface) throws IOException {


        if(camera == null) {
            camera = Camera.open(1);
            camera.setDisplayOrientation(90);
            camera.setPreviewDisplay(holder);
            camera.startPreview();
            camera.unlock();

        }

        if(mediaRecorder == null)
            mediaRecorder = new MediaRecorder();
        mediaRecorder.setPreviewDisplay(surface);
        mediaRecorder.setCamera(camera);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        int i=0;
        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss", Locale.US);
        String format = s.format(new Date());
        File file;
        if(shared_Preferences.getString("mode","learn").equalsIgnoreCase("learn")) {
            file = new File(Environment.getExternalStorageDirectory().getPath() + "/Learn2Sign/"
                    + shared_Preferences.getString(INTENTID, "0000") + "_" + word + "_0_" + format + ".mp4");
            while (file.exists()) {
                i++;
                file = new File(Environment.getExternalStorageDirectory().getPath() + "/Learn2Sign/"
                        + shared_Preferences.getString(INTENTID, "0000") + "_" + word + "_" + i + "_" + format + ".mp4");
            }
        }
        else {
            file = new File(Environment.getExternalStorageDirectory().getPath() + "/Learn2Sign/"
                    + groupName + "_" + word + "_" + shared_Preferences.getInt("rating",10) + "_" + shared_Preferences.getString("lastName","cherish") +".mp4");
        }

        if(file.createNewFile()) {
            fileCreated = true;
            Log.e("file path",file.getPath());
            return_File = file.getPath();
        }


        mediaRecorder.setOutputFile(file.getPath());
        mediaRecorder.setMaxDuration(3000);
        mediaRecorder.setVideoSize(320,240);
        mediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
            @Override
            public void onInfo(MediaRecorder mediaRecorder, int i, int i1) {
                if (i == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {    //finish after max duration has been reached
                    VideoOperations.this.mediaRecorder.stop();
                    VideoOperations.this.mediaRecorder.reset();
                    if(time!=null) {
                        time.cancel();
                    }
                    return_Intent.putExtra(INTENTURI, return_File);
                    return_Intent.putExtra(INTENT_TIMEWATCHEDVIDEO, timeWatched);
                    activity.setResult(8888, return_Intent);
                    activity.finish();
                }

            }
        });

        mediaRecorder.setOrientationHint(270);
        mediaRecorder.setVideoFrameRate(30);
        mediaRecorder.setVideoEncodingBitRate(3000000);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);// MPEG_4_SP


        try {

            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        initSuccesful = true;
    }


    @Override
    public void onBackPressed() {
        HashSet<String> backCLickSet = (HashSet<String>) shared_Preferences.getStringSet("BACK_CLICK", new HashSet<String>());
        backCLickSet.add("BACK_CLICK_" + shared_Preferences.getString(INTENTID, "") + "_" + shared_Preferences.getString(INTENTEMAIL, "") + "_" + String.valueOf(System.currentTimeMillis()));
        shared_Preferences.edit().putStringSet("BACK_CLICK", backCLickSet).apply();

        if(timer!=null)
            timer.cancel();
        if(time!=null)
            time.cancel();

        return_Intent.putExtra(INTENTURI, return_File);
        return_Intent.putExtra(INTENT_TIMEWATCHEDVIDEO, timeWatched);
        activity.setResult(7777, return_Intent);
        activity.finish();

        super.onBackPressed();
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        try {
            if(!initSuccesful)
                initRecorder(this.holder.getSurface());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {}

    private void shutdown() {
        mediaRecorder.reset();
        mediaRecorder.release();
        camera.release();
        mediaRecorder = null;
        return_Intent.putExtra(INTENTURI, return_File);
        return_Intent.putExtra(INTENT_TIMEWATCHEDVIDEO, timeWatched);
        activity.setResult(7777, return_Intent);
        camera = null;
        timer.cancel();
        finish();
    }
}