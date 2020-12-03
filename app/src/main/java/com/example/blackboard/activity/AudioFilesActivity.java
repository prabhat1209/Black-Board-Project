package com.example.blackboard.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.blackboard.R;
import com.example.blackboard.adapter.AudioFilesAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;

public class AudioFilesActivity extends AppCompatActivity {

    private ImageView goBack;
    public static ImageView playButton, repeat, repeatOne, shuffle ;
    private ConstraintLayout playerLayout;
    public static BottomSheetBehavior bottomSheetBehavior;
    private RecyclerView recyclerView;
    private File[] allFiles;
    private AudioFilesAdapter audioFilesAdapter;
    public static MediaPlayer mediaPlayer;
    public static boolean isPlaying = false;
    public static TextView playerHeader, playerName;
    public static SeekBar seekBar;
    public static Handler seekbarHandler;
    public static Runnable updateSeekbar;
    private ImageButton previous, next;
    public static boolean isRepeat = true;
    public static boolean isRepeatOne = false;
    public static boolean isShuffle = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_audio_files);

            recyclerView = findViewById(R.id.rec_view_audio);
            goBack = findViewById(R.id.back_from_audio);
            playerLayout = findViewById(R.id.player_layout);
            bottomSheetBehavior = BottomSheetBehavior.from(playerLayout);
            playerHeader = findViewById(R.id.player_header_title);
            playerName = findViewById(R.id.player_name);
            playButton = findViewById(R.id.player_play_btn);
            seekBar = findViewById(R.id.player_seekbar);
            next = findViewById(R.id.next_song);
            previous = findViewById(R.id.previous_song);
            repeat = findViewById(R.id.repeat);
            repeatOne = findViewById(R.id.repeat_one);
            shuffle = findViewById(R.id.shuffle);

            String path = this.getExternalFilesDir("/").getAbsolutePath();
            File directory = new File(path);
            allFiles = directory.listFiles();

            audioFilesAdapter = new AudioFilesAdapter(allFiles, this);

            recyclerView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(audioFilesAdapter);

            goBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isPlaying){
                        playerHeader.setText("Paused");
                        audioFilesAdapter.pauseAudio();
                    }else {
                        if(AudioFilesAdapter.fileToPlay!=null){
                            playerHeader.setText("Playing");
                            audioFilesAdapter.resumeAudio();
                        }
                    }
                }
            });

            bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if(newState == BottomSheetBehavior.STATE_HIDDEN){
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                }
            });

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    if(AudioFilesAdapter.fileToPlay!=null) {
                        audioFilesAdapter.pauseAudio();
                    }
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    if(AudioFilesAdapter.fileToPlay!=null) {
                        int progress = seekBar.getProgress();
                        mediaPlayer.seekTo(progress);
                        audioFilesAdapter.resumeAudio();
                    }
                }
            });

            previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    audioFilesAdapter.playPrevious();
                }
            });

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    audioFilesAdapter.playNext();
                }
            });

            repeat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isShuffle = true;
                    isRepeatOne = false;
                    isRepeat = false;
                    shuffle.setVisibility(View.VISIBLE);
                    repeat.setVisibility(View.INVISIBLE);
                }
            });

            shuffle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isShuffle = false;
                    isRepeatOne = true;
                    isRepeat = false;
                    repeatOne.setVisibility(View.VISIBLE);
                    shuffle.setVisibility(View.INVISIBLE);
                }
            });

            repeatOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isShuffle = false;
                    isRepeatOne = false;
                    isRepeat = true;
                    repeat.setVisibility(View.VISIBLE);
                    repeatOne.setVisibility(View.INVISIBLE);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if(isPlaying)
            mediaPlayer.stop();

        startActivity(new Intent(AudioFilesActivity.this, HomeActivity.class));
        finish();
    }
}