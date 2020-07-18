package com.example.my_music_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {
    Intent i;
    private static final int STORAGE_PERMISSION_CODE = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        i = new Intent(this,Alltracks_fav.class);
        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE,STORAGE_PERMISSION_CODE);

    }

    public void splash(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.this.finish();
                startActivity(i);
            }
        },2000);

    }
    private void checkPermission(String readExternalStorage, int storagePermissionCode) {
        if(ContextCompat.checkSelfPermission(MainActivity.this,readExternalStorage)== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{readExternalStorage},storagePermissionCode);
        }else{
           splash();
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==STORAGE_PERMISSION_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
               splash();
            }
        }
    }
}
