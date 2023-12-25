package com.demo.lovelivewallpaper.Adapters;

import UEnginePackage.Views.LiveWallpaperViewCanvas;
import UEnginePackage.Models.layers.LayerManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.demo.lovelivewallpaper.R;
import com.demo.lovelivewallpaper.utils.ImageUtils2;
import com.demo.lovelivewallpaper.utils.Uscreen;
import com.demo.lovelivewallpaper.utils.onReady2;

import java.util.List;

import liveWallpaper.myapplication.App;
import liveWallpaper.myapplication.Statics;


public class LiveWallpapersEffectsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public Context context;
    public boolean firstItemEmpty;
    String imagePath;
    public List<LayerManager> list;
    public onReady2 onItemChoosed;

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
        public View parent;
        public TextView title;
        LiveWallpaperViewCanvas wallpaperView;

        public MyViewHolder(View view) {
            super(view);
            this.parent = view;
            this.title = (TextView) view.findViewById(R.id.title);
            this.cardView = (CardView) view.findViewById(R.id.cardView);
            this.bg = view.findViewById(R.id.bg);
            this.wallpaperView = (LiveWallpaperViewCanvas) view.findViewById(R.id.liveWallpaperView);
            this.imageView = (ImageView) view.findViewById(R.id.imageView);
        }
    }

    public LiveWallpapersEffectsAdapter(List<LayerManager> list, Context context, String str, onReady2 onready2) {
        this.list = list;
        this.context = context;
        this.imagePath = str;
        this.onItemChoosed = onready2;
        if (list.size() >= 4) {
            this.list.add(4, null);
        }
    }

    @Override 
    public int getItemViewType(int i) {
        if (i == 0 && this.firstItemEmpty) {
            return 1;
        }
        return this.list.get(i) == null ? 2 : 0;
    }

    @Override 
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 1) {
            return new MyViewHolder(LayoutInflater.from(this.context).inflate(R.layout.empty_particle_view_item, viewGroup, false));
        }
        if (i == 2) {
            return new MyViewHolder(LayoutInflater.from(this.context).inflate(R.layout.particle_view_item, viewGroup, false));
        }
        return new MyViewHolder(LayoutInflater.from(this.context).inflate(R.layout.particle_view_item, viewGroup, false));
    }

    @Override 
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int i) {
        if (this.list.get(i) == null) {
            MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
            myViewHolder.cardView.setVisibility(View.GONE);
            return;
        }
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
        final LayerManager layerManager = this.list.get(i);
        if (myViewHolder == null || myViewHolder.parent == null) {
            return;
        }
        myViewHolder.cardView.setRadius(Uscreen.width / 34);
        myViewHolder.wallpaperView.setLayerManager(layerManager);
        String str = Statics.wallpaperFolder + this.imagePath;
        String str2 = this.imagePath;
        if (str2 == "-1" || str2.equals("-1")) {
            str = ImageUtils2.getFolderPath("images", App.c()) + "/wallpaper.jpg";
        }
        Glide.with(this.context).load(str).into(myViewHolder.imageView);
        myViewHolder.parent.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                if (LiveWallpapersEffectsAdapter.this.onItemChoosed != null) {
                    LiveWallpapersEffectsAdapter.this.onItemChoosed.ready(layerManager, Integer.valueOf(i));
                    LiveWallpapersEffectsAdapter.this.onItemChoosed = null;
                }
            }
        });
    }

    @Override 
    public int getItemCount() {
        return this.list.size();
    }


}
