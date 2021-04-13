package com.dealse.dealsepartner.Fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.dealse.dealsepartner.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class Step3AllImagesFragment extends Fragment {

    ImageView image;
    ImageView removeicn;
    ProgressBar progressBar;
    TextView plustxt,addtxt;

    public static Fragment newInstance() {
        return new Step3AllImagesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.step3allimagesframent, null, false);

        image = (ImageView)view.findViewById(R.id.image);
        removeicn = (ImageView)view.findViewById(R.id.removeicn);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        plustxt = (TextView) view.findViewById(R.id.plustxt);
        addtxt = (TextView) view.findViewById(R.id.addtxt);

        removeicn.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        image.setVisibility(View.GONE);
        plustxt.setVisibility(View.VISIBLE);
        addtxt.setVisibility(View.VISIBLE);

        plustxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPicker();
            }
        });

        return view;
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

            plustxt.setVisibility(View.GONE);
            addtxt.setVisibility(View.GONE);

            image.setVisibility(View.VISIBLE);

            File imgFile = new File(images.get(0).path);

            Picasso.with(getActivity())
                    .load(imgFile)
                    //       .fit()
                    .skipMemoryCache()
                    .into(image, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {

                        }
                    });


        }
    }

}
