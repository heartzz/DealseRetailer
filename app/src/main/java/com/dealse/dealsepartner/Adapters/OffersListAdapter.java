package com.dealse.dealsepartner.Adapters;

import android.content.Context;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dealse.dealsepartner.Activities.CreateNewOfferStepper;
import com.dealse.dealsepartner.Activities.ManageOffersScreen;
import com.dealse.dealsepartner.Application.Dealsepartnerapplicationclass;
import com.dealse.dealsepartner.Objects.Deals;
import com.dealse.dealsepartner.Objects.Offer;
import com.dealse.dealsepartner.R;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class OffersListAdapter extends RecyclerView.Adapter<OffersListAdapter.MyViewHolder> {


    private Context a;
    private List<Offer> createddealsList;

    public OffersListAdapter(Context a, List<Offer> createddealsList) {
        this.a = a;
        this.createddealsList = createddealsList;
    }

    @Override
    public OffersListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offerlistitem, parent, false);

        return new OffersListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final OffersListAdapter.MyViewHolder holder, final int position) {

        holder.oferidtxt.setText(String.valueOf(createddealsList.get(position).getName()));
        holder.offertext.setText(createddealsList.get(position).getSortDescription());
        holder.fromtodates.setText(createddealsList.get(position).getEffectiveDateRange());

        ImageLoader imageLoader = ImageLoader.getInstance();

        if(createddealsList.get(position).getOfferImagesLists() != null){
            if(createddealsList.get(position).getOfferImagesLists().size() > 0){
                Log.d("Image------",createddealsList.get(position).getOfferImagesLists().get(0).getOfferImage());
             //   imageLoader.displayImage(createddealsList.get(position).getOfferImagesLists().get(0).getOfferImage(), holder.offerImage);


                Picasso.with(a)
                        .load(createddealsList.get(position).getOfferImagesLists().get(0).getOfferImage().replace("\\", "/"))
                        //       .fit()
                        .skipMemoryCache()
                        .into(holder.offerImage, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {

                            }
                        });
            }
            else {
                Log.d("Image------","No image");
                holder.offerImage.setImageDrawable(a.getResources().getDrawable(R.drawable.icondefault));
            }
        }

        holder.editIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent createofferIntent = new Intent(a, CreateNewOfferStepper.class);
                createofferIntent.putExtra("offer",new Gson().toJson(createddealsList.get(position)));
                a.startActivity(createofferIntent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return createddealsList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView offerImage,editIc;
        TextView oferidtxt,offertext,fromtodates,tv_offerStatus;
        CardView card;



        public MyViewHolder(View view) {
            super(view);

            offerImage = (ImageView)view.findViewById(R.id.itemImage1);
            oferidtxt = (TextView)view.findViewById(R.id.oferidtxt);
            offertext = (TextView)view.findViewById(R.id.offertext);
            fromtodates = (TextView)view.findViewById(R.id.fromtodates);
            tv_offerStatus = (TextView)view.findViewById(R.id.tv_offerStatus);
            card = (CardView)view.findViewById(R.id.card);
            editIc = (ImageView)view.findViewById(R.id.editIc);

        }
    }
}