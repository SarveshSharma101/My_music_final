package com.example.my_music_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.MediaController;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Alltracks_fav extends AppCompatActivity implements MediaController.MediaPlayerControl {
    private TabLayout tabLayout;
    private SearchView searchView;
    private ViewPager viewPager;
    private ArrayList<songs> allsongs= new ArrayList<>();
    private ArrayList<songs> favsongs= new ArrayList<>();
    private static musicservice musicservice1,musicservice2;
    private Intent playintent;
    private boolean musicbound;
    private static ServiceConnection musiccon;
    private static Musiccontroller musiccontroller;
    static boolean  exit=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alltracks_fav);


        allsongs = getAllsongs(allsongs);
        favsongs = getfavsongs();
        Log.e("favourite started", favsongs.toString());
        Collections.sort(allsongs, new Comparator<songs>() {
            @Override
            public int compare(songs o1, songs o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });
        viewPager = findViewById(R.id.viewpager);
        viewPager = addtabFrag(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                searchView.setIconified(true);
                if(position==1){
                    musicservice1.setSongslist(favsongs);
                }
                else {
                    musicservice1.setSongslist(allsongs);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        searchView = findViewById(R.id.searchview);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Yeahhhh","Yehasssss");
            }
        });


        tabLayout = findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("All Songs");
        tabLayout.getTabAt(1).setText("Favourite Songs");


        Database db=new Database(Alltracks_fav.this);



         musiccon= new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                com.example.my_music_final.musicservice.MusicBinder binder = (com.example.my_music_final.musicservice.MusicBinder) service;
                musicservice1 = binder.getservice();

                musicservice1.setSongslist(allsongs);
                //musicservice1.playsong();
                musicbound=true;
                /*musicservice1.setSong(0);
                musicservice1.playsong();*/
                //assignservice(musicservice1, musicbound);
                //musicservice2=musicservice1;

                //plzplay plzplay = new plzplay();
                //plzplay.setmplz(musicservice1);
                Log.e("bind","PPPPPPPPPPPPPPPPPPPPPPPP");

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

                Log.e("disconnected","Lol");

                musicbound=false;
            }
        };
         if(musicservice1!=null){
        if(musicservice1.isPng()){
            Toast.makeText(this,"Hello again", Toast.LENGTH_SHORT).show();
            musiccontroller.show(0);
        }}


        //hidenavBar();
        Log.e("#####################",String.valueOf(musicservice2));
        Log.e("#####################",String.valueOf(musicbound));
        setController();
        int rid = getResources().getIdentifier("navigation_bar_height","dimen","android");


    }

    public ArrayList<songs> getfavsongs() {
        Database db=new Database(Alltracks_fav.this);

        Cursor c = db.get_favSongs();
        ArrayList<songs> s = new ArrayList<>();
        int id = c.getColumnIndex("songid");
        while(c.moveToNext()){
            long i = c.getInt(id);
            Log.e("fid",String.valueOf(i));
            for(songs ss : allsongs){

                if(ss.getID()==i){
                    Log.e("ssid",String.valueOf(ss.getID()));

                    s.add(ss);
                }
            }

        }

        return s;
    }

    @Override
    protected void onStop() {
        musiccontroller.hide();
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {

        super.onResume();
        //hidenavBar();
    }

    @Override
    protected void onRestart() {
        if(musicservice1.isPng()){
            Toast.makeText(this,"Hello again", Toast.LENGTH_SHORT).show();
            musiccontroller.show(0);
        }
        super.onRestart();
    }

    private void hidenavBar() {
        Alltracks_fav.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
                View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    public void setController(){
        musiccontroller = new Musiccontroller(Alltracks_fav.this);
        musiccontroller.setEnabled(true);
        musiccontroller.setAnchorView(findViewById(R.id.a));

        musiccontroller.setMediaPlayer(this);

        musiccontroller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    playnext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playprev();

            }
        });
    }

    private void playprev() {
        musicservice1.playprev();
        musiccontroller.show(0);
    }
    private void playnext() {
        musicservice1.playnext();
        musiccontroller.show(0);
    }


    @Override
    public void onStart() {
        if(playintent==null){
            playintent = new Intent(Alltracks_fav.this,musicservice.class);


            startService(playintent);

            boolean i = bindService(playintent,musiccon,Context.BIND_AUTO_CREATE);
            if(i){
                Toast.makeText(Alltracks_fav.this,"Yeah Done",Toast.LENGTH_SHORT).show();
            }
        }
        super.onStart();

    }


    @Override
    protected void onDestroy() {
        musicservice1.onDestroy();
        super.onDestroy();
    }

    public ArrayList<songs> getAllsongs(ArrayList<songs> s){
        Log.e("hi",String.valueOf(s.size()));
        ContentResolver contentResolver =getContentResolver();
        Uri songsuri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(songsuri,null,null,null,null);
        if(cursor!=null && cursor.moveToFirst()){
            int id   =cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int title=cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artist=cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);

            do{
                long songid= cursor.getLong(id);
                String songtitle=cursor.getString(title);
                String songartist=cursor.getString(artist);
                s.add(new songs(songid,songtitle,songartist));
            }while (cursor.moveToNext());
        }
        Log.e("hi2",String.valueOf(s.size()));

        return s;
    }
    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }
    @Override
    public void onBackPressed() {
        if(!searchView.isIconified()){
            searchView.setIconified(true);
        }

        else {
            super.onBackPressed();
        }
    }

    private ViewPager addtabFrag(ViewPager v) {
        Viewpageradapter adapter = new Viewpageradapter(getSupportFragmentManager());
        adapter.addfrag(new all_tracks(Alltracks_fav.this, allsongs, musicservice1),"All Songs");
        adapter.addfrag(new fav_t(Alltracks_fav.this, favsongs, musicservice1),"Favourite Songs");
        v.setAdapter(adapter);
        return v;
    }

    @Override
    public void start() {
        musicservice1.go();
    }

    @Override
    public void pause() {
        musicservice1.pausePlayer();
    }

    @Override
    public int getDuration() {

        if(musicservice1!=null){
            return musicservice1.getDur();
        }else{
            return 0;
        }
    }

    @Override
    public int getCurrentPosition() {
        if(musicservice1!=null){
            return musicservice1.getPosn();
        }else{
        return 0;
        }
    }

    @Override
    public void seekTo(int pos) {

        musicservice1.seek(pos);

    }

    @Override
    public boolean isPlaying() {

        if(musicservice1!=null){
            return musicservice1.isPng();
        }else{
            return false;
        }

    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    public class Musiccontroller extends MediaController
    {

        public Musiccontroller(Context context) {
            super(context);
        }

        public boolean dispatchKeyEvent(KeyEvent event){
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                ((Activity) getContext()).onBackPressed();
                return true;
            }
            return super.dispatchKeyEvent(event);
        }
        @Override
        public void hide() {

        }
    }


    public class Viewpageradapter extends FragmentPagerAdapter{

        private List<Fragment> f = new ArrayList<>();
        private List<String> fn = new ArrayList<>();

        public Viewpageradapter(@NonNull FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return f.get(position);
        }

        @Override
        public int getCount() {
            return f.size();
        }

        public void addfrag(Fragment fragment, String Fragname){
            f.add(fragment);
            fn.add(Fragname);
        }
    }

    public void assignservice(musicservice m,boolean f){
        musicbound=f;
        musicservice2=m;
        Log.e("dadadadadadadada",String.valueOf(musicservice2));
    }
    public void playsongplz(int i){


        musicservice1.setSong(i);
        musicservice1.playsong();
        musiccontroller.show(0);
    }

    public static class plzplay{
        private static musicservice mplz;
        public void setmplz(musicservice m){
            this.mplz=m;
            Log.e("class",String.valueOf(this.mplz));
        }

        public void playsongplz(int i){


            this.mplz.setSong(i);
            this.mplz.playsong();
        }
    }

}
