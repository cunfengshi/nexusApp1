package com.example.cunfe.nexusapp.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.cunfe.nexusapp.R;

/**
 * Created by cunfe on 2015-11-24.
 */
public class SoundService extends Service {
    @Nullable
    private MediaPlayer mp;
    private int songid=-1;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mp!=null)
        mp.release();
       // audiomanager.abandonAudioFocus(afChangeListener);
        stopSelf();
    }

    ///1 mp为空 songid=-1 返回不播放 songid！=-1 播放songid
    ///2 mp不为空 songid=-1 按play执行 songid 不为-1 等于原songid 按play播放 songid 不等于原songid 播放新歌曲
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int id = intent.getIntExtra("songid", -1);
        if(id==-1&&mp==null){return -1;}
        if(id!=-1&&(mp==null||(mp!=null&&songid!=id))){
            songid=id;
            mp = MediaPlayer.create(this, songid);
        }
        if(songid!=-1){
            String play = intent.getStringExtra("play");
            if (play.equals("play")) {
                mp.start();
            } else if(play.equals("pause")) {
                mp.pause();
            }else if(play.equals("stop"))
            {
                mp.stop();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
