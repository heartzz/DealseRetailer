package com.dealse.dealsepartner.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.dealse.dealsepartner.Application.Dealsepartnerapplicationclass;
import com.dealse.dealsepartner.Entity.CheckStoreMobieNumberExistResponse;
import com.dealse.dealsepartner.Interfaces.GetAreaAndStoreTypeResponse;
import com.dealse.dealsepartner.Interfaces.GetOfferList;
import com.dealse.dealsepartner.Managers.DealseApplicationsManager;
import com.dealse.dealsepartner.Objects.GetAreaAndStoreTypeList;
import com.dealse.dealsepartner.Objects.GetOfferRequest;
import com.dealse.dealsepartner.Objects.GetOfferResponse;
import com.dealse.dealsepartner.Objects.Offer;
import com.dealse.dealsepartner.Utility.Constants;
import com.dealse.dealsepartner.Utility.LoaderDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dealse.dealsepartner.Adapters.OffersListAdapter;
import com.dealse.dealsepartner.Objects.Deals;
import com.dealse.dealsepartner.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageOffersScreen extends AppCompatActivity {

    private Toolbar mToolbar;
    RecyclerView recyclerView;
    OffersListAdapter offersListAdapter;
    private List<Offer> createddealsList;
    FloatingActionButton fab;
    private String TAG = "ManageOffersScreen";
    CheckStoreMobieNumberExistResponse checkStoreMobieNumberExistResponse;
    TextView addimagetxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manageoffers);
        initToolbar();


        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        fab = (FloatingActionButton)findViewById(R.id.fab);

        addimagetxt = (TextView)findViewById(R.id.addimagetxt);

        LinearLayoutManager llm = new LinearLayoutManager(ManageOffersScreen.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        checkStoreMobieNumberExistResponse = new CheckStoreMobieNumberExistResponse();
        String storeDataString = DealseApplicationsManager.getInstance().getPref(ManageOffersScreen.this).getString("storeData","");
        if(!storeDataString.equalsIgnoreCase("")) {

            checkStoreMobieNumberExistResponse = new Gson().fromJson(storeDataString, CheckStoreMobieNumberExistResponse.class);

            if (checkStoreMobieNumberExistResponse != null) {

                getOffersList();
            }
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent createofferIntent = new Intent(ManageOffersScreen.this,CreateNewOfferStepper.class);
                startActivity(createofferIntent);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getOffersList();
    }


    public void getOffersList(){

        GetOfferRequest getOfferRequest = new GetOfferRequest();
        getOfferRequest.setStoreId(checkStoreMobieNumberExistResponse.getData().getStoreId());
        getOfferRequest.setPageIndex(1);

        Log.d(TAG, "getOffersList"+"Step1");

        final GetOfferList getOfferList = Dealsepartnerapplicationclass.retrofit.create(GetOfferList.class);
        Call<GetOfferResponse> call = getOfferList.getGetOfferList(DealseApplicationsManager.getInstance().getPref(ManageOffersScreen.this).getString(Constants.KEY_TOKEN,""),getOfferRequest);
        call.enqueue(new Callback<GetOfferResponse>() {
            @Override
            public void onResponse(Call<GetOfferResponse> call, Response<GetOfferResponse> response) {
                GetOfferResponse getOfferResponse = new GetOfferResponse();
                getOfferResponse = response.body();

                LoaderDialog.hideLoader();
                Log.d(TAG, "getOffersList"+"Step2");

                if(getOfferResponse != null) {
                    try {

                        if(getOfferResponse.getCode() == 200) {

                            Log.d(TAG, "getOffersList"+"Step3");

                            addimagetxt.setVisibility(View.GONE);

                            createddealsList = new ArrayList<>();
                            createddealsList = getOfferResponse.getData();

                            offersListAdapter = new OffersListAdapter(ManageOffersScreen.this, createddealsList);
                            recyclerView.setAdapter(offersListAdapter);

                        }else if(getOfferResponse.getCode() == 404){
                            recyclerView.setVisibility(View.GONE);
                            addimagetxt.setVisibility(View.VISIBLE);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.d(TAG, "getOffersList"+"Step4"+e.toString());
                        Toast.makeText(ManageOffersScreen.this,"Something went erong, Please try again later",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Log.d(TAG, "getOffersList"+"Step5");
                    recyclerView.setVisibility(View.GONE);
                    addimagetxt.setVisibility(View.VISIBLE);
                 //   Toast.makeText(ManageOffersScreen.this,"Something went erong, Please try again later",Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<GetOfferResponse> call, Throwable t) {
                LoaderDialog.hideLoader();
                Log.d(TAG, "getOffersList"+"Step6"+t.toString());
                Toast.makeText(ManageOffersScreen.this,"Please again try later",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        TextView toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        ImageView toolbar_back = (ImageView) findViewById(R.id.toolbar_back);
        getSupportActionBar().setTitle(null);
        //toolbar_title.setTypeface(GetFonts.getInstance().getRobotoFont(TravFeddHomeScreen.this));
        toolbar_title.setText("Manage Offers");
        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
