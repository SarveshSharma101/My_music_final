package com.example.my_music_final;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

public class rC_adapter extends RecyclerView.Adapter<rC_adapter.rC_viewholder>  {

    private final musicservice musicservice;
    Context context;
    ArrayList<songs> strings;
    ArrayList<songs> allstrings;
    Database db;
    public rC_adapter(Context context, ArrayList<songs> strings, musicservice musicservice) {
        this.context = context;
        this.strings = strings;
        this.allstrings = strings;
        this.musicservice = musicservice;
        db = new Database(this.context);
    }

    @NonNull
    @Override
    public rC_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_item,parent,false);
        rC_viewholder rC_viewholder = new rC_viewholder(view, context,this.allstrings);
        return rC_viewholder;
    }



    @Override
    public void onBindViewHolder(@NonNull rC_viewholder holder, int position) {
        songs s = strings.get(position);
        holder.vv.setTag(position);
        holder.songs.setText(s.getTitle());

    }

    public void setfilter(ArrayList<songs> s){
        strings.clear();
        strings.addAll(s);
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

   /* @Override
    public Filter getFilter() {
        return filter;
    }*/
/*
    Filter filter =new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<String> fl = new ArrayList<>();
            if(constraint.toString().isEmpty()){
                Log.e("allString",allstrings.toString());
                fl.addAll(allstrings);

            }else{
                for(String s: strings){
                    if(s.toLowerCase().contains(constraint.toString().toLowerCase())){
                        fl.add(s);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values=fl;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            strings.clear();
            strings.addAll((Collection<? extends String>) results.values);
            notifyDataSetChanged();

        }
    };
*/



    public class rC_viewholder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        RelativeLayout vv;
        TextView songs;
        Context context;
        Alltracks_fav alltracks_fav;
        AlertDialog.Builder builder;
        ArrayList<songs> allsongs;
        Alltracks_fav.plzplay plzplay;
        public rC_viewholder(@NonNull final View itemView, final Context context, ArrayList<songs> s) {
            super(itemView);
            this.context = context;
            vv =itemView.findViewById(R.id.vv);
            songs = itemView.findViewById(R.id.songs);
            allsongs = s;
            vv.setOnClickListener(this);
             alltracks_fav= new Alltracks_fav();

             vv.setOnLongClickListener(this);
           builder= new AlertDialog.Builder(context);
            builder.setMessage("Add this song to favourite");



        }


        @Override
        public void onClick(View v) {
            alltracks_fav.playsongplz(Integer.parseInt(v.getTag().toString()));
        }


        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean onLongClick(final View v) {
            songs.setTranslationZ(200);
            songs.setTranslationX(200);
            builder.setPositiveButton("Yess", new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    songs.setTranslationX(0);

                    songs.setTranslationZ(0);
                    songs song= allsongs.get(Integer.parseInt(v.getTag().toString()));
                    long id = song.getID();
                    if(db.insertsong(id)){
                    Toast.makeText(context,"Done",Toast.LENGTH_SHORT).show();
                }
                else{
                        Toast.makeText(context,"!Done",Toast.LENGTH_SHORT).show();

                    }
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(context,"Ok",Toast.LENGTH_SHORT).show();
                    songs.setTranslationX(0);

                    songs.setTranslationZ(0);
                }
            });
            builder.show();
            return true;
        }
    }


}
