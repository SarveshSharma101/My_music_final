package com.example.my_music_final;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import androidx.annotation.Nullable;

public class musicservice extends Service implements Serializable,MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    private static MediaPlayer player;
    private ArrayList<songs> songslist;
    private static int songspos;
    private String songTitle;

    private MusicBinder musicBind;
    private static final int NOTIFY_ID=1;

    @Override
    public void onCreate() {
        player = new MediaPlayer();
        songspos=0;
        initMusicplayer();
         musicBind= new MusicBinder();

        super.onCreate();

    }

    public void playnext(){
        songspos++;
        if(songspos>=songslist.size()){
            songspos=0;
        }
        playsong();
    }
    public void playprev(){
        songspos--;
        if(songspos<=0){
            songspos=songslist.size()-1;
        }
        playsong();
    }

    public int getPosn(){
        return player.getCurrentPosition();
    }

    public int getDur(){
        return player.getDuration();
    }

    public boolean isPng(){
        return player.isPlaying();
    }

    public void pausePlayer(){
        player.pause();
    }

    public void seek(int posn){
        player.seekTo(posn);
    }

    public void go(){
        player.start();
    }

    public void setSongslist(ArrayList<songs> s){
        songslist=s;
    }

    public void setSong(int songIndex){
        songspos=songIndex;
    }

    public void initMusicplayer(){
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnErrorListener(this);
        player.setOnCompletionListener(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("onbind","binded");

        return musicBind;
    }


    @Override
    public boolean onUnbind(Intent intent) {


        Log.e("onunbind","unbinded");
        return super.onUnbind(intent);
    }

    public void playsong(){
        player.reset();
        songs song = songslist.get(songspos);
        long songid = song.getID();
        songTitle = song.getTitle();
        Uri trackuri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,songid);
        try{
            player.setDataSource(getApplicationContext(),trackuri);

        } catch (IOException e) {
            Log.e("MUSIC SERVICE", "Error setting data source", e);
            e.printStackTrace();
        }
        player.prepareAsync();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {


    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {

        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        Intent notIntent = new Intent(this, MainActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.ic_play_arrow_black_24dp)
                .setTicker(songTitle)
                .setOngoing(true)
                .setContentTitle("Playing")
                .setContentText(songTitle);
        Notification not = builder.build();

        startForeground(NOTIFY_ID, not);
    }

    @Override
    public void onDestroy() {
        Log.e("MUSIC ==", "E____________________");
        player.stop();
        stopForeground(true);

        super.onDestroy();
    }

    public class MusicBinder extends Binder implements Serializable{
        musicservice getservice(){
            return musicservice.this;
        }

    }
}
