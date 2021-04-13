package com.dealse.dealsepartner.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.dealse.dealsepartner.Activities.CreateNewOfferStepper;
import com.dealse.dealsepartner.Objects.Offer;
import com.dealse.dealsepartner.Objects.OfferImageListData;
import com.dealse.dealsepartner.Objects.SelectedImages;
import com.dealse.dealsepartner.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Step2BannerImageFragment extends Fragment {

    RecyclerView imagesListView;

    SelectedDocImageAdapter selectedImageAdapter;

    Offer offer;
    Stack<Fragment> fragmentStack;

    public Step2BannerImageFragment(Offer offer,Stack<Fragment> fragmentStack){
        this.offer = offer;
        this.fragmentStack = fragmentStack;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.step2bannerimagefragment, null, false);

        findViews(v);

        imagesListView = (RecyclerView)v.findViewById(R.id.recyclerView);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        imagesListView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(),2);
        imagesListView.setLayoutManager(mLayoutManager);




        /*CreateNewOfferStepper.selectedImagesList = new ArrayList<SelectedImages>();

        SelectedImages si = new SelectedImages();
        si.path = "last";
        CreateNewOfferStepper.selectedImagesList.add(si);
        selectedImageAdapter = new SelectedDocImageAdapter(getActivity(),CreateNewOfferStepper.selectedImagesList);
        imagesListView.setAdapter(selectedImageAdapter);*/


        if(!isImagePickerAdded()) {
            OfferImageListData si = new OfferImageListData();
            si.setOfferImage("last");
            CreateNewOfferStepper.selectedImagesList.add(si);
        }


            imagesListView.setVisibility(View.VISIBLE);
            selectedImageAdapter = new SelectedDocImageAdapter(getActivity(), CreateNewOfferStepper.selectedImagesList);
            imagesListView.setAdapter(selectedImageAdapter);



        return v;
    }

    public boolean isImagePickerAdded(){
        boolean isImagePickerAdded = false;
        if(CreateNewOfferStepper.selectedImagesList.size() != 0) {

            for(OfferImageListData offerImageListData : CreateNewOfferStepper.selectedImagesList){
                if(offerImageListData.getOfferImage().equalsIgnoreCase("last")){
                    isImagePickerAdded = true;
                }else {
                    isImagePickerAdded =  false;
                }
            }

        }else {
            isImagePickerAdded =  false;
        }
        return isImagePickerAdded;
    }

    public boolean saveDetails(){
        if(isValid()){
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            //ft.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_in_right);
            Step5detailpreviewfragment step5detailpreviewfragment = new Step5detailpreviewfragment(offer,fragmentStack);
            ft.add(R.id.container,step5detailpreviewfragment);
            fragmentStack.lastElement().onPause();
            ft.remove(fragmentStack.pop());
            fragmentStack.push(step5detailpreviewfragment);
            ft.commit();
            return true;
        }
        return false;
    }

    public boolean isValid(){
        return true;
    }

    private void findViews(View view) {

    }

    public void startPicker(){
        Intent intent = new Intent(getActivity(), AlbumSelectActivity.class);
        //set limit on number of images that can be selected, default is 10
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 10);
        startActivityForResult(intent,Constants.REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE && resultCode == getActivity().RESULT_OK && data != null) {
            //The array list has the image paths of the selected images
            ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            Log.d("Got it", images.get(0).toString());

         //   CreateNewOfferStepper.selectedImagesList = new ArrayList<OfferImageListData>();

            for (int i = 0; i < images.size(); i++) {
                OfferImageListData si = new OfferImageListData();
                si.setOfferImage(images.get(i).path);
                CreateNewOfferStepper.selectedImagesList.add(si);
            }

            /*if(!isImagePickerAdded()) {
                OfferImageListData si = new OfferImageListData();
                si.setOfferImage("last");
                CreateNewOfferStepper.selectedImagesList.add(si);
            }*/

            imagesListView.setVisibility(View.VISIBLE);
            selectedImageAdapter = new SelectedDocImageAdapter(getActivity(),getLastAddList(CreateNewOfferStepper.selectedImagesList));
            imagesListView.setAdapter(selectedImageAdapter);

        }
    }

    public ArrayList<OfferImageListData>  getLastAddList(ArrayList<OfferImageListData> selectedImagesList){

        ArrayList<OfferImageListData> orderedListArray = new ArrayList<>();
        for(OfferImageListData offerImageListData : selectedImagesList){
            if(!offerImageListData.getOfferImage().equalsIgnoreCase("last")){
                orderedListArray.add(offerImageListData);
            }
        }
        OfferImageListData si = new OfferImageListData();
        si.setOfferImage("last");
        orderedListArray.add(si);
        return orderedListArray;
    }





    public class SelectedDocImageAdapter extends RecyclerView.Adapter<SelectedDocImageAdapter.MyViewHolder> {


        private Context a;
        private List<OfferImageListData> selectedImagesList;
        public SelectedDocImageAdapter(Activity a, List<OfferImageListData> selectedImagesList) {
            this.a = a;
            this.selectedImagesList = selectedImagesList;
        }
        @Override
        public SelectedDocImageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.selectedimageitem, parent, false);

            return new SelectedDocImageAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final SelectedDocImageAdapter.MyViewHolder holder, final int position) {


            if(selectedImagesList.get(position).getOfferImage().equalsIgnoreCase("last")){
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
            }/*else if(selectedImagesList.get(position).imgDrawable > 0){

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

            }*/else {

                holder.plustxt.setVisibility(View.GONE);
                holder.addtxt.setVisibility(View.GONE);

                if (selectedImagesList.get(position).getOfferImage().replace("\\", "/").startsWith("http://")) {


                    Picasso.with(a)
                            .load(selectedImagesList.get(position).getOfferImage().replace("\\", "/"))
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
                }else{
                    File imgFile = new File(selectedImagesList.get(position).getOfferImage().replace("\\", "/"));

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