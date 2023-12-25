package com.demo.lovelivewallpaper.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.demo.lovelivewallpaper.R;
import com.demo.lovelivewallpaper.utils.onReady2;

import java.util.List;


public class AdapterStaticWallpapers extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    public List<String> list;
    onReady2 onItemSelected;

    @Override 
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override 
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public View parent;

        public LinearLayout linearLayout3;

        public MyViewHolder(View view) {
            super(view);
            this.parent = view;
            this.image = (ImageView) view.findViewById(R.id.image);

            linearLayout3 = (LinearLayout) view.findViewById(R.id.linearLayout3);
        }
    }

    public AdapterStaticWallpapers(List<String> list, Context context, onReady2 onready2) {
        this.list = list;
        this.context = context;
        int i = 0;
        int i2 = 0;
        while (true) {
            if (i >= list.size()) {
                break;
            }
            i2++;
            if (i2 % 5 == 0) {
                list.add(i, "");
                break;
            }
            i++;
        }
        this.onItemSelected = onready2;
    }

    @Override 
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 2) {
            return new MyViewHolder(LayoutInflater.from(this.context).inflate(R.layout.wallpaper_layout, viewGroup, false));
        }
        return new MyViewHolder(LayoutInflater.from(this.context).inflate(R.layout.wallpaper_layout, viewGroup, false));
    }

    @Override 
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int i) {
        final String str = this.list.get(i);
        if (str.length() == 0) {
            MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
            myViewHolder.linearLayout3.setVisibility(View.GONE);

            return;
        }
        RequestManager with = Glide.with(this.context);
        RequestBuilder<Drawable> load = with.load(Uri.parse("file:///android_asset/" + str));
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
        load.into(myViewHolder.image);
        myViewHolder.parent.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                if (AdapterStaticWallpapers.this.onItemSelected != null) {
                    AdapterStaticWallpapers.this.onItemSelected.ready(str, ((MyViewHolder) viewHolder).image);
                }
            }
        });
    }

    @Override 
    public int getItemCount() {
        return this.list.size();
    }

    @Override 
    public int getItemViewType(int i) {
        return this.list.get(i).length() == 0 ? 2 : 1;
    }

}
