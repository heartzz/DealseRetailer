package com.dealse.dealsepartner.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dealse.dealsepartner.Activities.CreateNewOfferStepper;
import com.dealse.dealsepartner.Activities.HomeScreen;
import com.dealse.dealsepartner.Entity.CheckStoreMobieNumberExistResponse;
import com.dealse.dealsepartner.Managers.DealseApplicationsManager;
import com.dealse.dealsepartner.Objects.Offer;
import com.dealse.dealsepartner.Objects.OfferImageListData;
import com.dealse.dealsepartner.Objects.SelectedImages;
import com.dealse.dealsepartner.R;
import com.dealse.dealsepartner.Utility.Constants;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Step5detailpreviewfragment extends Fragment {




    Offer offer;
    Stack<Fragment> fragmentStack;
    ViewPager mPager;
    AdapterImageSlider pagerAdapter;
    TextView tv_offerName,tv_offerShortDescription,tv_offerValidtill,tv_offerTnc,tv_ownerNumber;
    TextView tv_title,tv_brief,tv_distance;
    CheckStoreMobieNumberExistResponse checkStoreMobieNumberExistResponse;

    public Step5detailpreviewfragment(Offer offer,Stack<Fragment> fragmentStack){

        this.fragmentStack = fragmentStack;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.step5detailpreviewfragment, null, false);
        mPager = (ViewPager) v.findViewById(R.id.pager);
        pagerAdapter = new AdapterImageSlider(getActivity(),getPickerRemovedList(CreateNewOfferStepper.selectedImagesList));
        mPager.setAdapter(pagerAdapter);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());

        tv_offerName = (TextView)v.findViewById(R.id.tv_offerName);
        tv_offerShortDescription = (TextView)v.findViewById(R.id.tv_offerShortDescription);
        tv_offerValidtill = (TextView)v.findViewById(R.id.tv_offerValidtill);
        tv_offerTnc = (TextView)v.findViewById(R.id.tv_offerTnc);
        tv_ownerNumber = (TextView)v.findViewById(R.id.tv_ownerNumber);

        tv_title = (TextView)v.findViewById(R.id.title);
        tv_brief = (TextView)v.findViewById(R.id.brief);
        tv_distance = (TextView)v.findViewById(R.id.distance);


        checkStoreMobieNumberExistResponse = new CheckStoreMobieNumberExistResponse();

        String storeDataString = DealseApplicationsManager.getInstance().getPref(getActivity()).getString("storeData","");

        if(!storeDataString.equalsIgnoreCase("")){

            checkStoreMobieNumberExistResponse = new Gson().fromJson(storeDataString, CheckStoreMobieNumberExistResponse.class);

            if(checkStoreMobieNumberExistResponse != null) {

                tv_title.setText(checkStoreMobieNumberExistResponse.getData().getName());
                tv_brief.setText(checkStoreMobieNumberExistResponse.getData().getAddress());

            }else {
                checkStoreMobieNumberExistResponse = new CheckStoreMobieNumberExistResponse();
            }
        }

        tv_offerName.setText(CreateNewOfferStepper.offer.getName());
        tv_offerShortDescription.setText(CreateNewOfferStepper.offer.getSortDescription());
        tv_offerValidtill.setText(Constants.convertDate(CreateNewOfferStepper.offer.getEndDate()));
        tv_ownerNumber.setText(checkStoreMobieNumberExistResponse.getData().getOwnerMobileNo());
        tv_offerTnc.setText(CreateNewOfferStepper.offer.getTermsAndConditions());

        return v;
    }

    public boolean saveDetails(){
        if(isValid()){
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            //ft.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_in_right);
            Step2BannerImageFragment step2BannerImageFragment = new Step2BannerImageFragment(offer,fragmentStack);
            ft.add(R.id.container,step2BannerImageFragment);
            fragmentStack.lastElement().onPause();
            ft.remove(fragmentStack.pop());
            fragmentStack.push(step2BannerImageFragment);
            ft.commit();
            return true;
        }
        return false;
    }

    public boolean isValid(){
        return true;
    }

    private static class AdapterImageSlider extends PagerAdapter {

        private Activity act;
        private List<OfferImageListData> items;

        private AdapterImageSlider.OnItemClickListener onItemClickListener;

        private interface OnItemClickListener {
            void onItemClick(View view, SelectedImages obj);
        }

        public void setOnItemClickListener(AdapterImageSlider.OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        // constructor
        private AdapterImageSlider(Activity activity, List<OfferImageListData> items) {
            this.act = activity;
            this.items = items;
        }

        @Override
        public int getCount() {
            return this.items.size();
        }

        public OfferImageListData getItem(int pos) {
            return items.get(pos);
        }

        public void setItems(List<OfferImageListData> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final OfferImageListData o = items.get(position);
            LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.offers_detailsslider, container, false);

            ImageView image = (ImageView) v.findViewById(R.id.image);


            if (o.getOfferImage().replace("\\", "/").startsWith("http://")) {
                Picasso.with(act)
                        .load(o.getOfferImage().replace("\\", "/"))
                        //       .fit()
                        .skipMemoryCache()
                        .into(image, new Callback() {
                            @Override
                            public void onSuccess() {
                                //progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {

                            }
                        });

            }else {
                File imgFile = new File(o.getOfferImage());

                Picasso.with(act)
                        .load(imgFile)
                        //       .fit()
                        .skipMemoryCache()
                        .into(image, new Callback() {
                            @Override
                            public void onSuccess() {
                                //    holder.progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {

                            }
                        });
            }


            ((ViewPager) container).addView(v);

            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((RelativeLayout) object);

        }

    }


    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }
        }
    }

    public ArrayList<OfferImageListData>  getPickerRemovedList(ArrayList<OfferImageListData> selectedImagesList){

        ArrayList<OfferImageListData> orderedListArray = new ArrayList<>();
        for(OfferImageListData offerImageListData : selectedImagesList){
            if(!offerImageListData.getOfferImage().equalsIgnoreCase("last")){
                orderedListArray.add(offerImageListData);
            }
        }

        return orderedListArray;
    }

}