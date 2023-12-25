package com.demo.lovelivewallpaper.Adapters;

import UEnginePackage.touchEffectsPackage.TouchEffectsBase;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.lovelivewallpaper.R;
import com.demo.lovelivewallpaper.utils.onReady2;

import java.util.List;


public class TouchEffectsAdapter extends RecyclerView.Adapter<TouchEffectsAdapter.MyViewHolder> {
    Context context;
    public List<TouchEffectsBase> list;
    onReady2 onItemSelected;
    public int selectedPosition;

    @Override 
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override 
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View checkMark;
        public View parent;
        public View separator;
        public TextView title;

        public MyViewHolder(View view) {
            super(view);
            this.parent = view;
            this.title = (TextView) view.findViewById(R.id.title);
            this.checkMark = view.findViewById(R.id.checkMark);
            this.separator = view.findViewById(R.id.separator);
        }
    }

    public TouchEffectsAdapter(List<TouchEffectsBase> list, int i, Context context, onReady2 onready2) {
        this.list = list;
        this.selectedPosition = i;
        this.context = context;
        this.onItemSelected = onready2;
    }

    
    @Override 
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(this.context).inflate(R.layout.touch_effect_line, viewGroup, false));
    }

    @Override 
    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
        final TouchEffectsBase touchEffectsBase = this.list.get(i);
        myViewHolder.title.setText(touchEffectsBase.title);
        myViewHolder.parent.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                if (TouchEffectsAdapter.this.onItemSelected != null) {
                    TouchEffectsAdapter.this.onItemSelected.ready(touchEffectsBase, Integer.valueOf(i));
                }
            }
        });
        if (i == this.list.size() - 1) {
            myViewHolder.separator.setVisibility(View.GONE);
        } else {
            myViewHolder.separator.setVisibility(View.VISIBLE);
        }
        if (this.selectedPosition == i) {
            myViewHolder.checkMark.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.checkMark.setVisibility(View.GONE);
        }
    }

    @Override 
    public int getItemCount() {
        return this.list.size();
    }
}
