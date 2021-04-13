package com.dealse.dealsepartner.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.dealse.dealsepartner.Objects.SelectedImages;
import com.dealse.dealsepartner.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.darsh.multipleimageselect.models.Image;


public class Createofferscreen extends AppCompatActivity {

    private Toolbar mToolbar;
    RecyclerView imagesListView;
    public ArrayList<SelectedImages> selectedImagesList;
    SelectedDocImageAdapter selectedImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createofferscreen);
        initToolbar();

        imagesListView = (RecyclerView)findViewById(R.id.recyclerView);


        LinearLayoutManager llm = new LinearLayoutManager(Createofferscreen.this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        imagesListView.setLayoutManager(llm);
        imagesListView.setItemAnimator(new DefaultItemAnimator());
        imagesListView.setNestedScrollingEnabled(false);


        selectedImagesList = new ArrayList<SelectedImages>();

        SelectedImages si = new SelectedImages();
        si.path = "last";
        selectedImagesList.add(si);
        selectedImageAdapter = new SelectedDocImageAdapter(Createofferscreen.this,selectedImagesList);
        imagesListView.setAdapter(selectedImageAdapter);

    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        TextView toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        ImageView toolbar_back = (ImageView) findViewById(R.id.toolbar_back);
        getSupportActionBar().setTitle(null);
        //toolbar_title.setTypeface(GetFonts.getInstance().getRobotoFont(TravFeddHomeScreen.this));
        toolbar_title.setText("Create Offer");
        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void startPicker(){
        Intent intent = new Intent(Createofferscreen.this, AlbumSelectActivity.class);
        //set limit on number of images that can be selected, default is 10
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 10);
        startActivityForResult(intent,Constants.REQUEST_CODE);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            //The array list has the image paths of the selected images
            ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            Log.d("Got it", images.get(0).toString());

            selectedImagesList = new ArrayList<SelectedImages>();

            for (int i = 0; i < images.size(); i++) {
                SelectedImages si = new SelectedImages();
                si.path = images.get(i).path;
                selectedImagesList.add(si);
            }

            SelectedImages si = new SelectedImages();
            si.path = "last";
            selectedImagesList.add(si);

            imagesListView.setVisibility(View.VISIBLE);
            selectedImageAdapter = new SelectedDocImageAdapter(Createofferscreen.this,selectedImagesList);
            imagesListView.setAdapter(selectedImageAdapter);

        }
    }


    public class SelectedDocImageAdapter extends RecyclerView.Adapter<SelectedDocImageAdapter.MyViewHolder> {


        private Context a;
        private List<SelectedImages> selectedImagesList;
        public SelectedDocImageAdapter(Activity a, List<SelectedImages> selectedImagesList) {
            this.a = a;
            this.selectedImagesList = selectedImagesList;
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.selectedimageitem, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {


            if(selectedImagesList.get(position).path.equalsIgnoreCase("last")){
                holder.removeicn.setVisibility(View.GONE);
                holder.progressBar.setVisibility(View.GONE);
                holder.image.setVisibility(View.GONE);
                holder.plustxt.setVisibility(View.VISIBLE);
                holder.addtxt.setVisibility(View.VISIBLE);

                holder.plustxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startPicker();
                    }
                });
            }else if(selectedImagesList.get(position).imgDrawable > 0){

                holder.plustxt.setVisibility(View.GONE);
                holder.addtxt.setVisibility(View.GONE);

                Picasso.with(a)
                        .load(selectedImagesList.get(position).imgDrawable)
                        //       .fit()
                        .skipMemoryCache()
                        .into(holder.image, new Callback() {
                            @Override
                            public void onSuccess() {
                                holder.progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {

                            }
                        });

            }else {

                holder.plustxt.setVisibility(View.GONE);
                holder.addtxt.setVisibility(View.GONE);

                File imgFile = new File(selectedImagesList.get(position).path);

                Picasso.with(a)
                        .load(imgFile)
                        //       .fit()
                        .skipMemoryCache()
                        .into(holder.image, new Callback() {
                            @Override
                            public void onSuccess() {
                                holder.progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {

                            }
                        });

            }

            holder.removeicn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    selectedImagesList.remove(position);
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return selectedImagesList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {



            ImageView image;
            ImageView removeicn;
            ProgressBar progressBar;
            TextView plustxt,addtxt;

            public MyViewHolder(View view) {
                super(view);
                image = (ImageView)view.findViewById(R.id.image);
                removeicn = (ImageView)view.findViewById(R.id.removeicn);
                progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
                plustxt = (TextView) view.findViewById(R.id.plustxt);
                addtxt = (TextView) view.findViewById(R.id.addtxt);
            }
        }
    }

}
