package com.example.saverapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.saverapp.Model.TransModel;
import com.example.saverapp.R;

import java.util.List;


public class TransAdapter extends RecyclerView.Adapter<TransAdapter.MyViewHolder> {

    List<TransModel> transModelList;


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.translist, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(TransAdapter.MyViewHolder holder, int position) {

        holder.textView.setText(transModelList.get(position).getTrans());


    }

    @Override
    public int getItemCount() {
        return transModelList.size() ;
    }

    public void setTransModelList(List<TransModel> transModelList){
        this.transModelList = transModelList;
    }

    class MyViewHolder extends ViewHolder{


        TextView textView;


        public MyViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.trans);
        }
    }
}
