package com.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.worawat.stressrecognition.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by woraw on 10/30/2017.
 */

public class OverallFragmentViewAdater extends RecyclerView.Adapter<OverallFragmentViewAdater.MyViewHolder> {
    private ArrayList<String> values;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public MyViewHolder(View v) {
            super(v);

            mTextView = (TextView)v.findViewById(R.id.value);

        }
    }

    public OverallFragmentViewAdater(ArrayList<String> values) {
        //this.values = values;
        this.values=new ArrayList<>();
        for (int i=0; i<10;i++){
            String value =i+"";
            this.values.add(value);
            Log.d("value",value+"");
        }

    }

    //private Context mContext;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.overall_card, parent,false);
        TextView valueText = (TextView)itemView.findViewById(R.id.value);

        MyViewHolder myViewHolder=new MyViewHolder(itemView);

        Log.d("progress","onCreateViewHolder is calles");
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        String value = values.get(position);
        holder.mTextView.setText(value);
        Log.d("progress","onBindViewHolder is called");
    }

    @Override
    public int getItemCount() {
        return values.size();
    }
}
