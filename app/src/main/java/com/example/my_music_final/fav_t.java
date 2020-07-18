package com.example.my_music_final;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class fav_t extends Fragment {
    private ArrayList<songs> a = new ArrayList<>();
    private Context c;
    private ArrayList<songs> fs;
    musicservice m;
    public fav_t(Context c, ArrayList<songs> favsongs, musicservice musicservice1) {
        this.c = c;
        this.fs=favsongs;
        this.m=musicservice1;

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_fav_t, container, false);

        RecyclerView rc = v.findViewById(R.id.fav_rc);
        rc.setLayoutManager(new LinearLayoutManager(getActivity()));
        rc.setHasFixedSize(true);
        final rC_adapter1 rC_adapter1 = new rC_adapter1(getActivity(),this.fs, this.m);
        rc.setAdapter(rC_adapter1);

        //final ArrayList<String> a1 = new ArrayList<>();
        /*a1.addAll(a);



        SearchView searchView = ((Activity)this.c).findViewById(R.id.searchview);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                ArrayList<String>  newa = new ArrayList<>();
                newText = newText.toLowerCase();
                if(newText.length()==0){
                    //Log.e("Emppty query",newText+"???????????????????????????????");
                    newa.addAll(a1);
                    //Log.e("Emppty query newa",newa.toString()+"???????????????????????????????");
                    //Log.e("Emppty query a",a.toString()+"???????????????????????????????");


                }else {
                    //Log.e("filled query",newText+">>>>>>>>>>>>>>>");

                    for (String s : a) {
                        if (s.contains(newText)) {
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
        });*/

        return v;
    }
}
