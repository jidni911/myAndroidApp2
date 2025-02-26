package com.example.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.activity.User;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    ProgressBar progressBar;

    SeekBar seekBar;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        mediaPlayer = MediaPlayer.create(this, R.raw.somewhere_only_we_know);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button userButton = findViewById(R.id.userButton);
        userButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, User.class)));




        progressBar = findViewById(R.id.musicProgressBar);
        seekBar = findViewById(R.id.musicSeekBar);
        ImageButton playButton = findViewById(R.id.playButton);
//        mediaPlayer.setOnInfoListener((mp, what, extra) -> {
        playButton.setOnClickListener(v -> {

            if (mediaPlayer.isPlaying()){
                mediaPlayer.pause();
                playButton.setImageResource(R.drawable.baseline_play_circle_24);
                return;
            }
            mediaPlayer.start();
            playButton.setImageResource(R.drawable.baseline_pause_circle_24);
            updateProgressAndSeekBar();
        });
        mediaPlayer.setOnCompletionListener(mp -> {
            progressBar.setProgress(0);  // Reset progress bar when audio finishes
            seekBar.setProgress(0);  // Reset seek bar when audio finishes
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                mediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                mediaPlayer.start();
            }
        });

    }

    private void updateProgressAndSeekBar() {
        int totalDuration = mediaPlayer.getDuration();
        progressBar.setMax(totalDuration);
        seekBar.setMax(totalDuration);

        // Use a handler to update the progress bar every 100ms
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    progressBar.setProgress(currentPosition);
                    seekBar.setProgress(currentPosition);
                    handler.postDelayed(this, 100);  // Continue updating every 100ms
                }
            }
        }, 100);
    }
}