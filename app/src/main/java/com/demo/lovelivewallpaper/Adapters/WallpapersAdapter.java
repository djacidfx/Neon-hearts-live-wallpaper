package com.demo.lovelivewallpaper.Adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.demo.lovelivewallpaper.R;
import com.demo.lovelivewallpaper.utils.AppUtils;
import com.demo.lovelivewallpaper.utils.Uscreen;
import com.demo.lovelivewallpaper.utils.onReady2;

import java.util.List;

import liveWallpaper.myapplication.App;


public class WallpapersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Activity activity;
    public List<String> list;
    public onReady2 onItemSelected;

    public void destroy() {
    }

    @Override 
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override 
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        View bg;
        CardView cardView;
        ImageView imageView;
        ImageView lock;
        public View parent;
        public TextView title;

        public MyViewHolder(View view) {
            super(view);
            this.parent = view;
            this.title = (TextView) view.findViewById(R.id.title);
            this.cardView = (CardView) view.findViewById(R.id.cardView);
            this.lock = (ImageView) view.findViewById(R.id.lock);
            this.bg = view.findViewById(R.id.bg);
            this.imageView = (ImageView) view.findViewById(R.id.close);
        }
    }

    @Override 
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 2) {
            return new MyViewHolder(LayoutInflater.from(this.activity).inflate(R.layout.wallpaper_view_item, viewGroup, false));
        }
        return new MyViewHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.wallpaper_view_item, viewGroup, false));
    }

    @Override 
    public int getItemViewType(int i) {
        return this.list.get(i) == null ? 2 : 1;
    }

    public WallpapersAdapter(List<String> list, onReady2 onready2, Activity activity) {
        this.list = list;
        this.activity = activity;
        if (list.size() >= 4) {
            this.list.add(4, null);
        }
        this.onItemSelected = onready2;
    }

    @Override 
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder viewHolder) {
        Log.e("tag3333", "wallpapersAdapter faild to recycle view " + viewHolder.getClass().getName());
        return super.onFailedToRecycleView(viewHolder);
    }

    @Override 
    public void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
        super.onViewRecycled(viewHolder);
        Log.e("tag3333", "wallpapersAdapter view recycled " + viewHolder.getClass().getName());
    }

    @Override 
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder,  final int i) {
        final String str = this.list.get(i);
        if (str == null) {

            MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
            myViewHolder.cardView.setVisibility(View.GONE);

            return;
        }
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
        myViewHolder.parent.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                if (WallpapersAdapter.this.onItemSelected != null) {
                    WallpapersAdapter.this.onItemSelected.ready(str, Integer.valueOf(i));
                }
            }
        });
        myViewHolder.cardView.setRadius(Uscreen.width / 34);
        RequestManager with = Glide.with(App.getInstance());
        with.load("file:///android_asset/wallpapers/" + str).into(myViewHolder.imageView);
        if (AppUtils.wallpaperUnlocked(str, i)) {
            myViewHolder.lock.setVisibility(View.GONE);
        } else {
            myViewHolder.lock.setVisibility(View.VISIBLE);
        }
    }



    @Override 
    public int getItemCount() {
        List<String> list = this.list;
        if (list != null) {
            return list.size();
        }
        return 0;
    }
}
