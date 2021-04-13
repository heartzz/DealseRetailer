package com.dealse.dealsepartner.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dealse.dealsepartner.Objects.AreaListModel;
import com.dealse.dealsepartner.Objects.Deals;
import com.dealse.dealsepartner.R;

import java.util.ArrayList;
import java.util.List;

public class AreaListAdapter extends RecyclerView.Adapter<AreaListAdapter.MyViewHolder> {


    private Context a;
    private ArrayList<AreaListModel> areaList;

    public AreaListAdapter(Context a, ArrayList<AreaListModel> areaList) {
        this.a = a;
        this.areaList = areaList;
    }

    @Override
    public AreaListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_area, parent, false);

        return new AreaListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AreaListAdapter.MyViewHolder holder, final int position) {

        holder.areaNametxt.setText(areaList.get(position).getAreaName());
    }

    @Override
    public int getItemCount() {
        return areaList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView offerImage;
        TextView areaNametxt,offertext,fromtodates;



        public MyViewHolder(View view) {
            super(view);

            areaNametxt = (TextView)view.findViewById(R.id.tv_areaName);

        }
    }
}