package com.example.blackboard.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.blackboard.R;

public class HomeActivity extends AppCompatActivity {

    private TextView record, audioFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);

            record = findViewById(R.id.record);
            audioFile = findViewById(R.id.audio_files);

            record.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(HomeActivity.this,RecorderActivity.class));
                    finish();
                }
            });

            audioFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(HomeActivity.this,AudioFilesActivity.class));
                    finish();
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}