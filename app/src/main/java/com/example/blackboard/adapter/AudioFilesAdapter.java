package com.example.blackboard.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blackboard.R;
import com.example.blackboard.activity.AudioFilesActivity;
import com.example.blackboard.pojo.TimeAgo;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class AudioFilesAdapter extends RecyclerView.Adapter<AudioFilesAdapter.AudioViewHolder> {

    private File[] allFiles;
    private TimeAgo timeAgo;
    private Context context;
    public static File fileToPlay = null;
    public static int currentPos;

    public AudioFilesAdapter(File[] allFiles, Context context) {
        this.allFiles = allFiles;
        this.context = context;
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_audio,parent,false);
        timeAgo = new TimeAgo();
        return new AudioFilesAdapter.AudioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {
        holder.title.setText(allFiles[position].getName());
        holder.date.setText(timeAgo.getTimeAgo(allFiles[position].lastModified()));
    }

    @Override
    public int getItemCount() {
        return allFiles.length;
    }

    public class AudioViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView title, date;

        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.list_image_view);
            title = itemView.findViewById(R.id.list_title);
            date = itemView.findViewById(R.id.list_date);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context,allFiles[getAdapterPosition()].getName(),Toast.LENGTH_LONG).show();
                    fileToPlay = allFiles[getAdapterPosition()];
                    currentPos = getAdapterPosition();
                    if(AudioFilesActivity.isPlaying){
                        stopAudio();
                        playAudio(fileToPlay);
                    }else {
                        AudioFilesActivity.isPlaying = true;
                        playAudio(fileToPlay);
                    }
                }
            });
        }
    }

    public void playAudio(File play){
        AudioFilesActivity.isPlaying = true;
        AudioFilesActivity.mediaPlayer = new MediaPlayer();
        try {
            AudioFilesActivity.mediaPlayer.setDataSource(fileToPlay.getAbsolutePath());
            AudioFilesActivity.mediaPlayer.prepare();
            AudioFilesActivity.mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AudioFilesActivity.playButton.setImageDrawable(context.getResources().getDrawable(R.drawable.player_pause_btn));
        AudioFilesActivity.playerName.setText(fileToPlay.getName());
        AudioFilesActivity.playerHeader.setText("Playing");

        AudioFilesActivity.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        AudioFilesActivity.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopAudio();
                AudioFilesActivity.playerHeader.setText("Finished");
                playNext();
            }
        });



        AudioFilesActivity.seekBar.setMax(AudioFilesActivity.mediaPlayer.getDuration());
        AudioFilesActivity.seekbarHandler = new Handler();
        updateRunnable();
        AudioFilesActivity.seekbarHandler.postDelayed(AudioFilesActivity.updateSeekbar, 0);

    }

    public void stopAudio(){
        AudioFilesActivity.isPlaying = false;
        AudioFilesActivity.mediaPlayer.stop();
        AudioFilesActivity.playButton.setImageDrawable(context.getResources().getDrawable(R.drawable.player_play_btn));
        AudioFilesActivity.playerHeader.setText("Stop");
        AudioFilesActivity.seekbarHandler.removeCallbacks(AudioFilesActivity.updateSeekbar);
    }

    public void pauseAudio(){
        AudioFilesActivity.playButton.setImageDrawable(context.getResources().getDrawable(R.drawable.player_play_btn));
        AudioFilesActivity.mediaPlayer.stop();
        AudioFilesActivity.isPlaying = false;
        AudioFilesActivity.seekbarHandler.removeCallbacks(AudioFilesActivity.updateSeekbar);
    }

    public void resumeAudio(){
        AudioFilesActivity.playButton.setImageDrawable(context.getResources().getDrawable(R.drawable.player_pause_btn));
        AudioFilesActivity.mediaPlayer.start();
        AudioFilesActivity.isPlaying = true;
        updateRunnable();
        AudioFilesActivity.seekbarHandler.postDelayed(AudioFilesActivity.updateSeekbar,0);
    }

    public void updateRunnable(){
        AudioFilesActivity.updateSeekbar = new Runnable() {
            @Override
            public void run() {
                AudioFilesActivity.seekBar.setProgress(AudioFilesActivity.mediaPlayer.getCurrentPosition());
                AudioFilesActivity.seekbarHandler.postDelayed(this, 500);
            }
        };
    }

    public void playPrevious(){
        if(fileToPlay!=null) {
            if(AudioFilesActivity.isRepeat){
                if(currentPos==(allFiles.length-1)){
                    currentPos = 0;
                }else {
                    currentPos += 1;
                }
            }else if(AudioFilesActivity.isRepeatOne){
                //No change
            }else {
                int prev = currentPos;
                Random random = new Random();
                int randomNo = random.nextInt(allFiles.length);
                while (prev==randomNo){
                    randomNo = random.nextInt(allFiles.length);
                }
                currentPos = randomNo;
            }
            if (currentPos < (allFiles.length - 1)) {
                fileToPlay = allFiles[currentPos];
                if (AudioFilesActivity.isPlaying) {
                    stopAudio();
                    playAudio(fileToPlay);
                } else {
                    AudioFilesActivity.isPlaying = true;
                    playAudio(fileToPlay);
                }
            } else {
                fileToPlay = allFiles[currentPos];
                if (AudioFilesActivity.isPlaying) {
                    stopAudio();
                    playAudio(fileToPlay);
                } else {
                    AudioFilesActivity.isPlaying = true;
                    playAudio(fileToPlay);
                }
            }
        }
    }

    public void playNext(){

        if(fileToPlay!=null) {
            if(AudioFilesActivity.isRepeat){
                if(currentPos==0){
                    currentPos = allFiles.length-1;
                }else {
                    currentPos -= 1;
                }
            }else if(AudioFilesActivity.isRepeatOne){
                //No change
            }else {
                int prev = currentPos;
                Random random = new Random();
                int randomNo = random.nextInt(allFiles.length);
                while (prev==randomNo){
                    randomNo = random.nextInt(allFiles.length);
                }
                currentPos = randomNo;
            }
            if (currentPos > 0) {
                fileToPlay = allFiles[currentPos];
                if (AudioFilesActivity.isPlaying) {
                    stopAudio();
                    playAudio(fileToPlay);
                } else {
                    AudioFilesActivity.isPlaying = true;
                    playAudio(fileToPlay);
                }
            } else {
                fileToPlay = allFiles[currentPos];
                if (AudioFilesActivity.isPlaying) {
                    stopAudio();
                    playAudio(fileToPlay);
                } else {
                    AudioFilesActivity.isPlaying = true;
                    playAudio(fileToPlay);
                }
            }
        }
    }

}
