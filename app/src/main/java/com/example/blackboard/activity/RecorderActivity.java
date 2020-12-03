package com.example.blackboard.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.blackboard.R;

import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RecorderActivity extends AppCompatActivity {

    private ImageView goBack, stopRecording, startRecording, showList, pause, resume;
    private TextView recordingName;
    private boolean isRecording = false;
    private String recordPermission = Manifest.permission.RECORD_AUDIO;
    private int PERMISSION_CODE = 1;
    private MediaRecorder mediaRecorder;
    private String recordFile;
    private Chronometer timer;
    private long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_recorder);

            goBack = findViewById(R.id.back_from_recording);
            recordingName = findViewById(R.id.recording_name);
            stopRecording = findViewById(R.id.stop_recording);
            startRecording = findViewById(R.id.start_recording);
            pause = findViewById(R.id.pause_recording);
            resume = findViewById(R.id.resume_recording);
            showList = findViewById(R.id.show_list);
            timer = findViewById(R.id.chronometer);

            startRecording.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(checkReordingPermission()) {
                        isRecording = false;
                        startRecording.setVisibility(View.INVISIBLE);
                        stopRecording.setVisibility(View.VISIBLE);
                        setStartRecording();
                    }
                }
            });

            stopRecording.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isRecording = true;
                    startRecording.setVisibility(View.VISIBLE);
                    stopRecording.setVisibility(View.INVISIBLE);
                    setStopRecording();
                }
            });

            showList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isRecording)
                        setStopRecording();
                    startActivity(new Intent(RecorderActivity.this, AudioFilesActivity.class));
                    finish();
                }
            });

            pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resume.setVisibility(View.VISIBLE);
                    pause.setVisibility(View.INVISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        mediaRecorder.pause();
                        timer.stop();
                        time = timer.getBase();
                    }
                }
            });

            resume.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pause.setVisibility(View.VISIBLE);
                    resume.setVisibility(View.INVISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        mediaRecorder.resume();
                        timer.setBase(time);
                        timer.start();
                    }
                }
            });

            goBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean checkReordingPermission() {
        if(ActivityCompat.checkSelfPermission(this, recordPermission) == PackageManager.PERMISSION_GRANTED){
            return true;
        }else {
            ActivityCompat.requestPermissions(this, new String[]{recordPermission}, PERMISSION_CODE);
            return false;
        }
    }

    private void setStopRecording(){
        resume.setVisibility(View.INVISIBLE);
        pause.setVisibility(View.INVISIBLE);
        showList.setVisibility(View.VISIBLE);
        timer.stop();
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        recordingName.setText("Recording stopped file saved : "+recordFile);
        isRecording = false;
    }

    private void setStartRecording(){

        showList.setVisibility(View.INVISIBLE);
        pause.setVisibility(View.VISIBLE);

        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();

        String recordPath = this.getExternalFilesDir("/").getAbsolutePath();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd hh_mm_ss");
        Date now = new Date();

        recordFile = "Recording " + formatter.format(now) + ".3gp";

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(recordPath+"/"+recordFile);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        recordingName.setText("Recording Filename : "+recordFile);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isRecording = true;
        mediaRecorder.start();

    }

    @Override
    public void onBackPressed() {
        if(isRecording){
            setStopRecording();
        }
        startActivity(new Intent(RecorderActivity.this,HomeActivity.class));
        finish();
    }
}