package com.dealse.dealsepartner.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dealse.dealsepartner.Application.Dealsepartnerapplicationclass;
import com.dealse.dealsepartner.Entity.CheckStoreMobieNumberExistResponse;
import com.dealse.dealsepartner.Fragment.Step1DetailsFragment;
import com.dealse.dealsepartner.Fragment.Step2BannerImageFragment;
import com.dealse.dealsepartner.Fragment.Step3AllImagesFragment;
import com.dealse.dealsepartner.Fragment.Step4cardpreviewfragment;
import com.dealse.dealsepartner.Fragment.Step5detailpreviewfragment;
import com.dealse.dealsepartner.Interfaces.AddOffer;
import com.dealse.dealsepartner.Interfaces.AddStore;
import com.dealse.dealsepartner.Interfaces.GetAreaAndStoreTypeResponse;
import com.dealse.dealsepartner.Interfaces.UpdateOffer;
import com.dealse.dealsepartner.Interfaces.UploadImage;
import com.dealse.dealsepartner.Managers.DealseApplicationsManager;
import com.dealse.dealsepartner.Objects.CreateOfferResponse;
import com.dealse.dealsepartner.Objects.GetAreaAndStoreTypeList;
import com.dealse.dealsepartner.Objects.Offer;
import com.dealse.dealsepartner.Objects.OfferImageListData;
import com.dealse.dealsepartner.Objects.SelectedImages;
import com.dealse.dealsepartner.R;
import com.dealse.dealsepartner.Utility.Constants;
import com.dealse.dealsepartner.Utility.LoaderDialog;
import com.dealse.dealsepartner.Utility.ViewAnimation;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Stack;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateNewOfferStepper extends AppCompatActivity {

    private static final int MAX_STEP = 3;
    private int current_step = 1;
    private TextView status;
    private Toolbar mToolbar;
    TextView newxtbtntv;
    Step1DetailsFragment step1DetailsFragment;
    Step2BannerImageFragment step2BannerImageFragment;
    Step3AllImagesFragment step3AllImagesFragment;
    Step4cardpreviewfragment step4cardpreviewfragment;
    Step5detailpreviewfragment step5detailpreviewfragment;
    FragmentManager fragmentManager;
    private Stack<Fragment> fragmentStack;
    public static Offer offer = new Offer();
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    private String TAG = "CreateNewOfferStepper";

    public static ArrayList<OfferImageListData> selectedImagesList = new ArrayList<>();

    EditText tv_offerTitle,tv_shortDescription,tv_longDescription,tv_validfrom,tv_validtill,tv_terms;
    ArrayList<OfferImageListData> finalImagesList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createofferstepper);



        offer = new Gson().fromJson(getIntent().getStringExtra("offer"), Offer.class);

        if(offer != null) {
            selectedImagesList = offer.getOfferImagesLists();
            initToolbar("Update Offer");
        }else {
            offer = new Offer();
            selectedImagesList = new ArrayList<>();
            initToolbar("Create Offer");
        }

        initComponent();



        step1Fragment();
    }

 /*@Override
 protected void onResume() {
 super.onResume();
 selectedImagesList = new ArrayList<>();
 }*/

    public void step1Fragment()
    {

        fragmentManager = getSupportFragmentManager();
        fragmentStack = new Stack<Fragment>();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        step1DetailsFragment = new Step1DetailsFragment(offer,fragmentStack);
        ft.add(R.id.container, step1DetailsFragment);
        fragmentStack.push(step1DetailsFragment);
        ft.commit();

    }

    public void step2Fragment()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        //ft.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_in_right);
        step2BannerImageFragment = new Step2BannerImageFragment(offer,fragmentStack);
        ft.add(R.id.container,step2BannerImageFragment);
        fragmentStack.lastElement().onPause();
        ft.remove(fragmentStack.pop());
        fragmentStack.push(step2BannerImageFragment);
        ft.commit();
    }

    /*public void step3Fragment()
    {
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction ft = fragmentManager.beginTransaction();
    //ft.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_in_right);
    ft.add(R.id.container,Step3AllImagesFragment.newInstance());
    fragmentStack.lastElement().onPause();
    ft.remove(fragmentStack.pop());
    fragmentStack.push(Step3AllImagesFragment.newInstance());
    ft.commit();
    }

    public void step4Fragment()
    {
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction ft = fragmentManager.beginTransaction();
    //ft.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_in_right);
    ft.add(R.id.container,Step4cardpreviewfragment.newInstance());
    fragmentStack.lastElement().onPause();
    ft.remove(fragmentStack.pop());
    fragmentStack.push(Step4cardpreviewfragment.newInstance());
    ft.commit();
    }*/
    public void step5Fragment()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        //ft.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_in_right);
        step5detailpreviewfragment = new Step5detailpreviewfragment(offer,fragmentStack);
        ft.add(R.id.container,step5detailpreviewfragment);
        fragmentStack.lastElement().onPause();
        ft.remove(fragmentStack.pop());
        fragmentStack.push(step5detailpreviewfragment);
        ft.commit();
    }



    private void initToolbar(String title) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        TextView toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        ImageView toolbar_back = (ImageView) findViewById(R.id.toolbar_back);
        getSupportActionBar().setTitle(null);
        //toolbar_title.setTypeface(GetFonts.getInstance().getRobotoFont(TravFeddHomeScreen.this));
        toolbar_title.setText(title);
        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initComponent() {
        status = (TextView) findViewById(R.id.status);
        newxtbtntv = (TextView) findViewById(R.id.newxtbtn);
        ((LinearLayout) findViewById(R.id.lyt_back)).setVisibility(View.INVISIBLE);
        ((LinearLayout) findViewById(R.id.lyt_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Back Prassed----------",current_step+"");
                if(current_step == 2){
                    ((LinearLayout) findViewById(R.id.lyt_back)).setVisibility(View.INVISIBLE);
                    step1Fragment();
                }else if(current_step == 3){
                    step2Fragment();
                }/*else if(current_step == 4){
 // step3Fragment();
 }else if(current_step == 5){
 // step4Fragment();

 }*/
                backStep(current_step);
                bottomProgressDots(current_step);
                newxtbtntv.setText("NEXT");
            }
        });

        ((LinearLayout) findViewById(R.id.lyt_next)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Next Prassed----------",current_step+"");


                if(current_step == 1){
                    if(step1DetailsFragment.saveDetails()){
                        ((LinearLayout) findViewById(R.id.lyt_back)).setVisibility(View.VISIBLE);
                        nextStep(current_step);
                        bottomProgressDots(current_step);
                    }
                    // step2Fragment();
                }else if(current_step == 2){

                    // step2Fragment();
                    // step2BannerImageFragment = new Step2BannerImageFragment(offer,fragmentStack);
                    // if(step2BannerImageFragment.saveDetails()){
                    step5Fragment();
                    // if(step5detailpreviewfragment.saveDetails()){.
                    newxtbtntv.setText("DONE");
                    nextStep(current_step);
                    bottomProgressDots(current_step);

                    // }
                }else if(current_step == 3){
                    // selectedImagesList = new ArrayList<>();
                    createOffer();
                    // }
                }/*else if(current_step == 4){
 step5Fragment();
 newxtbtntv.setText("DONE");
 nextStep(current_step);
 bottomProgressDots(current_step);
 }else if(current_step == 5){

 }*/

            }
        });

        String str_progress = String.format(getString(R.string.step_of), current_step, MAX_STEP);
        status.setText(str_progress);
        bottomProgressDots(current_step);
    }

    private void nextStep(int progress) {
        if (progress < MAX_STEP) {
            progress++;
            current_step = progress;
            ViewAnimation.fadeOutIn(status);
        }
        String str_progress = String.format(getString(R.string.step_of), current_step, MAX_STEP);
        //status.setText(str_progress);
    }

    private void backStep(int progress) {
        if (progress > 1) {
            progress--;
            current_step = progress;
            ViewAnimation.fadeOutIn(status);
        }
        String str_progress = String.format(getString(R.string.step_of), current_step, MAX_STEP);
        //status.setText(str_progress);
    }

    private void bottomProgressDots(int current_index) {
        current_index--;
        LinearLayout dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        ImageView[] dots = new ImageView[MAX_STEP];

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle);
            dots[i].setColorFilter(getResources().getColor(R.color.grey_20), PorterDuff.Mode.SRC_IN);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current_index].setImageResource(R.drawable.shape_circle);
            dots[current_index].setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        }
    }


    public void createOffer(){

        LoaderDialog.showLoader(CreateNewOfferStepper.this);

        Call<CreateOfferResponse> call;
        if(offer.getOfferId() != 0){

            UpdateOffer updateOffer = Dealsepartnerapplicationclass.retrofit.create(UpdateOffer.class);
            call = updateOffer.updateOffer(DealseApplicationsManager.getInstance().getPref(CreateNewOfferStepper.this).getString(Constants.KEY_TOKEN,""),offer);
        }else{
            AddOffer addOffer = Dealsepartnerapplicationclass.retrofit.create(AddOffer.class);
            call = addOffer.createOffer(DealseApplicationsManager.getInstance().getPref(CreateNewOfferStepper.this).getString(Constants.KEY_TOKEN,""),offer);
        }

        call.enqueue(new Callback<CreateOfferResponse>() {
            @Override
            public void onResponse(Call<CreateOfferResponse> call, Response<CreateOfferResponse> response) {
                CreateOfferResponse createOfferResponse = new CreateOfferResponse();
                createOfferResponse = response.body();



                if(createOfferResponse != null) {
                    try {

                        if(createOfferResponse.getCode() == 200) {

                            if(!selectedImagesList.isEmpty()){
                                //selectedImagesList = new ArrayList<>();

                                finalImagesList = getPickerRemovedList(selectedImagesList);

                                if(finalImagesList.size() > 0) {
                                    if (!finalImagesList.get(0).getOfferImage().startsWith("http://") &&
                                            !finalImagesList.get(0).getOfferImage().equalsIgnoreCase("last")) {
                                        uploadImage(createOfferResponse.getData().getOfferId(), finalImagesList.get(0).getOfferImage(), 0);
                                    }
                                }else {
                                     /*Toast toast = Toast
                                     .makeText(CreateNewOfferStepper.this, createOfferResponse.getMessage(), Toast.LENGTH_SHORT);
                                     toast.show();*/
                                     onBackPressed();
                                     }

                            }
                            LoaderDialog.hideLoader();
                            // Toast.makeText(CreateNewOfferStepper.this,createOfferResponse.getData().getOfferId(),Toast.LENGTH_SHORT).show();
                            // onBackPressed();

                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(CreateNewOfferStepper.this,"Something went erong, Please try again later",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(CreateNewOfferStepper.this,"Something went erong, Please try again later",Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<CreateOfferResponse> call, Throwable t) {
                Toast.makeText(CreateNewOfferStepper.this,"Please again try later",Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void uploadImage(final String OfferId, String fileUri, final int imgPosition){

        LoaderDialog.showLoader(CreateNewOfferStepper.this);

        UploadImage service =
                Dealsepartnerapplicationclass.retrofit.create(UploadImage.class);

        MultipartBody.Part body1;
        Call<CheckStoreMobieNumberExistResponse> call;

        body1 = prepareFilePart("OfferImage", fileUri);

        RequestBody OfferIdBody = createPartFromString(OfferId);
        Log.d(TAG, "uploadImage"+"Step1");


        call = service.addImage(DealseApplicationsManager.getInstance().getPref(CreateNewOfferStepper.this).getString(Constants.KEY_TOKEN,"")
                , OfferIdBody,body1);

        call.enqueue(new Callback<CheckStoreMobieNumberExistResponse>() {
            @Override
            public void onResponse(Call<CheckStoreMobieNumberExistResponse> call, Response<CheckStoreMobieNumberExistResponse> response) {
                CheckStoreMobieNumberExistResponse checkStoreMobieNumberExistResponse = new CheckStoreMobieNumberExistResponse();
                checkStoreMobieNumberExistResponse = response.body();

                String code = checkStoreMobieNumberExistResponse.getCode();
                String message = checkStoreMobieNumberExistResponse.getMessage();

                LoaderDialog.hideLoader();

                Log.d(TAG, "uploadImage"+"Step2");

                if (code.equalsIgnoreCase("200")) {

                    {

                        Log.d(TAG, "uploadImage"+"Step3");


                        if(imgPosition == 0 && finalImagesList.size() > 2){

                            uploadImage(OfferId, finalImagesList.get(1).getOfferImage(), 1);

                        }else if(imgPosition == 1 && finalImagesList.size() > 3){

                            uploadImage(OfferId, finalImagesList.get(2).getOfferImage(), 2);

                        }else if(imgPosition == 2 && finalImagesList.size() > 4){

                            uploadImage(OfferId, finalImagesList.get(3).getOfferImage(), 3);

                        }else if(imgPosition == 3 && finalImagesList.size() > 5){

                            uploadImage(OfferId, finalImagesList.get(4).getOfferImage(), 4);

                        }else if(imgPosition == 4 && finalImagesList.size() > 6){

                            uploadImage(OfferId, finalImagesList.get(5).getOfferImage(), 5);

                        }else {
                            Toast toast = Toast
                                    .makeText(CreateNewOfferStepper.this, message, Toast.LENGTH_SHORT);
                            toast.show();
                            LoaderDialog.hideLoader();
                            onBackPressed();
                        }

 /*Toast toast = Toast
 .makeText(CreateNewOfferStepper.this, message, Toast.LENGTH_SHORT);
 toast.show();

 SharedPreferences.Editor editor = DealseApplicationsManager.getInstance().getEditor(Merchant_signup.this);
 editor.putString("storeId", String.valueOf(checkStoreMobieNumberExistResponse.getData().getStoreId()));
 editor.commit();



 Intent mainintent = new Intent(CreateNewOfferStepper.this,HomeScreen.class);
 mainintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
 startActivity(mainintent);
 finish();*/


                    }
                }
            }

            @Override
            public void onFailure(Call<CheckStoreMobieNumberExistResponse> call, Throwable t) {
                LoaderDialog.hideLoader();
                Toast toast = Toast
                        .makeText(CreateNewOfferStepper.this, t.toString(), Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    public ArrayList<OfferImageListData> getPickerRemovedList(ArrayList<OfferImageListData> selectedImagesList){

        ArrayList<OfferImageListData> orderedListArray = new ArrayList<>();
        for(OfferImageListData offerImageListData : selectedImagesList){
            if(!offerImageListData.getOfferImage().startsWith("http://") && !offerImageListData.getOfferImage().equalsIgnoreCase("last") ){
                orderedListArray.add(offerImageListData);
            }
        }



        return orderedListArray;
    }
    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, String fileUri) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = new File(fileUri);

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    //Post feed related
    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                MediaType.parse(MULTIPART_FORM_DATA), descriptionString);
    }

}