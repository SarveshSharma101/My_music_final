package com.example.my_music_final;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ServiceConfigurationError;


/**
 * A simple {@link Fragment} subclass.
 */
public class all_tracks extends Fragment{
    private ArrayList<songs> a ;

    final ArrayList<songs> a1 = new ArrayList<>();
    private final musicservice musicservice;
    private Context c;

    public all_tracks(Context context, ArrayList<songs> atemp, musicservice musicservice) {
        this.c = context;
        this.a=atemp;
        this.musicservice = musicservice;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all_tracks, container, false);
        // Inflate the layout for this fragment
        Database db=new Database(this.c);

        RecyclerView rc = v.findViewById(R.id.all_rc);
        rc.setLayoutManager(new LinearLayoutManager(getActivity()));
        rc.setHasFixedSize(true);
        final rC_adapter rC_adapter = new rC_adapter(getActivity(),a, musicservice);
        rc.setAdapter(rC_adapter);




        a1.addAll(a);
        SearchView searchView = ((Activity)this.c).findViewById(R.id.searchview);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("search query",newText+"???????????????????????????????");
                ArrayList<songs>  newa = new ArrayList<>();
                newText = newText.toLowerCase();
                if(newText.length()==0){
                    //Log.e("Emppty query",newText+"???????????????????????????????");
                    newa.addAll(a1);
                    //Log.e("Emppty query newa",newa.toString()+"???????????????????????????????");
                    //Log.e("Emppty query a",a.toString()+"???????????????????????????????");


                }else {
                    //Log.e("filled query",newText+">>>>>>>>>>>>>>>");

                    for (songs s : a) {
                        if (s.getTitle().contains(newText)) {
                            newa.add(s);
                        }
                    }
                    //Log.e("filled query newa",newa.toString()+">>>>>>>>>>>>>>>");
                    //Log.e("filled query a",a.toString()+">>>>>>>>>>>>>>>>>>>>>>>>");


                }
                //Log.e("New a", newa.toString());

                rC_adapter.setfilter(newa);

                return false;
            }
        });


        return v;
    }







}
