package com.example.cunfe.nexusapp;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {

    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        videoView=(VideoView)findViewById(R.id.videoView);
    }

    static final int REQUEST_VIDEO_CAPTURE = 1;

    public void dispatchTakeVideoIntent(View view)
    {
        Intent video = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if(video.resolveActivity(getPackageManager())!=null)
        {
            startActivityForResult(video,REQUEST_VIDEO_CAPTURE);
        }
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode==REQUEST_VIDEO_CAPTURE&&resultCode==RESULT_OK)
        {
            Uri videoUri = data.getData();
            videoView.setVideoURI(videoUri);
            videoView.start();
            Button Play=(Button)findViewById(R.id.btnPlay);
            Play.setText(R.string.pause_button);
        }
    }
    public void PlayOrPauseVideo(View view)
    {
        Button Play=(Button)findViewById(R.id.btnPlay);
        if(videoView.isPlaying())
        {
            videoView.pause();
            Play.setText(R.string.play_button);
        }else
        {
            videoView.start();
            Play.setText(R.string.pause_button);
        }
    }
}
