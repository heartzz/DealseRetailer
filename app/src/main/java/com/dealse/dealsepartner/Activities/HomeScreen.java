package com.dealse.dealsepartner.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.dealse.dealsepartner.Adapters.OffersListAdapter;
import com.dealse.dealsepartner.Application.Dealsepartnerapplicationclass;
import com.dealse.dealsepartner.Entity.CheckStoreMobieNumberExistResponse;
import com.dealse.dealsepartner.Interfaces.GetOfferList;
import com.dealse.dealsepartner.Interfaces.GetRedeemedOffersList;
import com.dealse.dealsepartner.Interfaces.ScanUserOffer;
import com.dealse.dealsepartner.Objects.GetOfferRequest;
import com.dealse.dealsepartner.Objects.GetOfferResponse;
import com.dealse.dealsepartner.Objects.GetUsedOfferList;
import com.dealse.dealsepartner.Objects.Offer;
import com.dealse.dealsepartner.Objects.RedeemedOfeersResponse;
import com.dealse.dealsepartner.Objects.ScanOfferObject;
import com.dealse.dealsepartner.Objects.ScanOfferResponse;
import com.dealse.dealsepartner.Utility.Constants;
import com.dealse.dealsepartner.Utility.LoaderDialog;
import com.google.android.material.navigation.NavigationView;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.dealse.dealsepartner.Managers.DealseApplicationsManager;
import com.dealse.dealsepartner.R;
import com.dealse.dealsepartner.Utility.FirebaseConstants;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;


/**
 * Created by HARSHANK on 02-10-2018.
 */
public class HomeScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar mToolbar;
    public static String TAG = "HomeScreen";
    private static final int REQUEST_CODE_QR_SCAN = 101;
    RelativeLayout scanlayout;
    NavigationView navigationView;
    ViewPager viewPager;
    RecyclerView recyclerView;
    TextView partnersnametv,partnersemailidtv;
    ImageView imageView;

    CheckStoreMobieNumberExistResponse checkStoreMobieNumberExistResponse;

    RedeemedListAdapter redeemedListAdapter;
    TextView toolbar_title;
    String[] perms = {"android.permission.FINE_LOCATION", "android.permission.CAMERA"};

    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landingscreen);

        initToolbar();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        LinearLayoutManager llm = new LinearLayoutManager(HomeScreen.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        checkStoreMobieNumberExistResponse = new CheckStoreMobieNumberExistResponse();

        String storeDataString = DealseApplicationsManager.getInstance().getPref(HomeScreen.this).getString("storeData","");





        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View headerLayout = navigationView.getHeaderView(0);
        partnersnametv = (TextView)headerLayout.findViewById(R.id.partnersnametv);
        partnersemailidtv = (TextView)headerLayout.findViewById(R.id.partnersemailidtv);
        imageView = (ImageView)headerLayout.findViewById(R.id.imageView);



        if(!storeDataString.equalsIgnoreCase("")){

            checkStoreMobieNumberExistResponse = new Gson().fromJson(storeDataString, CheckStoreMobieNumberExistResponse.class);

            if(checkStoreMobieNumberExistResponse != null) {

                toolbar_title.setText(checkStoreMobieNumberExistResponse.getData().getName());
                partnersnametv.setText(checkStoreMobieNumberExistResponse.getData().getName());
                partnersemailidtv.setText(checkStoreMobieNumberExistResponse.getData().getOwnerName()+" -  "+checkStoreMobieNumberExistResponse.getData().getOwnerMobileNo());

                if(checkStoreMobieNumberExistResponse.getData().getLogoUrl() != null) {

                    getRedeemedOfferList();

                    Picasso.with(HomeScreen.this)
                            .load(checkStoreMobieNumberExistResponse.getData().getLogoUrl().replace("\\", "/"))
                            //       .fit()
                            .skipMemoryCache()
                            .into(imageView, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {

                                }
                            });
                }
            }else {
                checkStoreMobieNumberExistResponse = new CheckStoreMobieNumberExistResponse();
            }
        }


        scanlayout = (RelativeLayout)findViewById(R.id.scanlayout1);

        scanlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkPermission()){
                    openScanner();
                }else {
                    requestPermission();
                }
            }
        });
    }

    public void openScanner(){
        Intent i = new Intent(HomeScreen.this, QrCodeActivity.class);
        startActivityForResult( i,REQUEST_CODE_QR_SCAN);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, CAMERA}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && cameraAccepted)
                        openScanner();
                    //    Snackbar.make(view, "Permission Granted, Now you can access location data and camera.", Snackbar.LENGTH_LONG).show();
                    else {

                     //   Snackbar.make(view, "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION, CAMERA},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(HomeScreen.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(0).setChecked(true);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            Log.d("MainActivity", "COULD NOT GET A GOOD RESULT.");
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if (result != null) {
                AlertDialog alertDialog = new AlertDialog.Builder(HomeScreen.this).create();
                alertDialog.setTitle("Scan Error");
                alertDialog.setMessage("QR Code could not be scanned");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            return;

        }
        if (requestCode == REQUEST_CODE_QR_SCAN) {
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
            Log.d("MainActivity", "Have scan result in your app activity :" + result);

            ScanOfferObject scanOfferObject = new ScanOfferObject();
            scanOfferObject = new Gson().fromJson(result, ScanOfferObject.class);

            scanUserOffer(scanOfferObject);

            /*AlertDialog alertDialog = new AlertDialog.Builder(HomeScreen.this).create();
            alertDialog.setTitle("Scan result");
            alertDialog.setMessage(result);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();*/
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_newnotes) {

            Intent itemintent = new Intent(HomeScreen.this, ManageOffersScreen.class);
            startActivity(itemintent);

        }else if(id == R.id.nav_setting){
            SharedPreferences.Editor editor = DealseApplicationsManager.getInstance().getEditor(HomeScreen.this);
            editor.clear().commit();
            Intent afterLogout = new Intent(HomeScreen.this,SplashScreen.class);
            afterLogout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(afterLogout);
            finish();
        }else if(id == R.id.nav_history){
            Intent redeemHistoryintent = new Intent(HomeScreen.this, UserReddemedHistory.class);
            startActivity(redeemHistoryintent);
        }else if(id == R.id.nav_profile){
            Intent itemintent = new Intent(HomeScreen.this, ManageStoreDetails.class);
            startActivity(itemintent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        ImageView toolbar_back = (ImageView) findViewById(R.id.toolbar_back);
        toolbar_title.setVisibility(View.VISIBLE);
        toolbar_back.setVisibility(View.VISIBLE);
        getSupportActionBar().setTitle(null);
        //toolbar_title.setTypeface(GetFonts.getInstance().getRobotoFont(TravFeddHomeScreen.this));
        toolbar_back.setVisibility(View.GONE);

    }


    //Scan Offer
    public void scanUserOffer(ScanOfferObject scanOfferObject){

        Log.d(TAG, "scanUserOffer"+"Step1");

        final ScanUserOffer scanUserOffer = Dealsepartnerapplicationclass.retrofit.create(ScanUserOffer.class);
        Call<ScanOfferResponse> call = scanUserOffer.scanUserOffer(DealseApplicationsManager.getInstance().getPref(HomeScreen.this).getString(Constants.KEY_TOKEN,""),scanOfferObject);
        call.enqueue(new retrofit2.Callback<ScanOfferResponse>() {
            @Override
            public void onResponse(Call<ScanOfferResponse> call, Response<ScanOfferResponse> response) {
                ScanOfferResponse scanOfferResponse = new ScanOfferResponse();
                scanOfferResponse = response.body();

                LoaderDialog.hideLoader();
                Log.d(TAG, "scanUserOffer"+"Step2");

                if(scanOfferResponse != null) {
                    try {

                        if(scanOfferResponse.getCode() == 200) {

                            Log.d(TAG, "scanUserOffer"+"Step3");


                            AlertDialog alertDialog = new AlertDialog.Builder(HomeScreen.this).create();
                            alertDialog.setTitle("Scan result");
                            alertDialog.setMessage("OfferRedeemed Successfully");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            getRedeemedOfferList();
                                        }
                                    });
                            alertDialog.show();

                        }else if(scanOfferResponse.getCode() == 404){
                            recyclerView.setVisibility(View.GONE);
                        }else if(scanOfferResponse.getCode() == 302){
                            Toast.makeText(HomeScreen.this,scanOfferResponse.getMessage(),Toast.LENGTH_SHORT).show();
                        }else if(scanOfferResponse.getCode() == 406){
                            Toast.makeText(HomeScreen.this,scanOfferResponse.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.d(TAG, "scanUserOffer"+"Step4"+e.toString());
                        Toast.makeText(HomeScreen.this,"Something went erong, Please try again later",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Log.d(TAG, "scanUserOffer"+"Step5");
                    recyclerView.setVisibility(View.GONE);
                    //   Toast.makeText(ManageOffersScreen.this,"Something went erong, Please try again later",Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<ScanOfferResponse> call, Throwable t) {
                LoaderDialog.hideLoader();
                Log.d(TAG, "scanUserOffer"+"Step6"+t.toString());
                Toast.makeText(HomeScreen.this,"Please again try later",Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void getRedeemedOfferList(){

        GetOfferRequest getOfferRequest = new GetOfferRequest();
        getOfferRequest.setStoreId(checkStoreMobieNumberExistResponse.getData().getStoreId());
        getOfferRequest.setPageIndex(1);
        getOfferRequest.setPageSize(10);

        Log.d(TAG, "getRedeemedOfferList"+"Step1");

        final GetRedeemedOffersList getRedeemedOffersList = Dealsepartnerapplicationclass.retrofit.create(GetRedeemedOffersList.class);
        Call<GetUsedOfferList> call = getRedeemedOffersList.getRedeemedOffersList(DealseApplicationsManager.getInstance().getPref(HomeScreen.this).getString(Constants.KEY_TOKEN,""),getOfferRequest);
        call.enqueue(new retrofit2.Callback<GetUsedOfferList>() {
            @Override
            public void onResponse(Call<GetUsedOfferList> call, Response<GetUsedOfferList> response) {
                GetUsedOfferList getOfferResponse = new GetUsedOfferList();
                getOfferResponse = response.body();

                LoaderDialog.hideLoader();
                Log.d(TAG, "getRedeemedOfferList"+"Step2");

                if(getOfferResponse != null) {
                    try {

                        if(getOfferResponse.getCode() == 200) {

                            Log.d(TAG, "getRedeemedOfferList"+"Step3");


                            redeemedListAdapter = new RedeemedListAdapter(HomeScreen.this, getOfferResponse.getData());
                            recyclerView.setAdapter(redeemedListAdapter);

                        }else if(getOfferResponse.getCode() == 404){
                            recyclerView.setVisibility(View.GONE);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.d(TAG, "getRedeemedOfferList"+"Step4"+e.toString());
                        Toast.makeText(HomeScreen.this,"Something went erong, Please try again later",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Log.d(TAG, "getRedeemedOfferList"+"Step5");
                    recyclerView.setVisibility(View.GONE);
                    //   Toast.makeText(ManageOffersScreen.this,"Something went erong, Please try again later",Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<GetUsedOfferList> call, Throwable t) {
                LoaderDialog.hideLoader();
                Log.d(TAG, "getRedeemedOfferList"+"Step6"+t.toString());
                Toast.makeText(HomeScreen.this,"Please again try later",Toast.LENGTH_SHORT).show();
            }
        });
    }


    public class RedeemedListAdapter extends RecyclerView.Adapter<RedeemedListAdapter.MyViewHolder> {


        private Context a;
        private List<RedeemedOfeersResponse> createddealsList;

        public RedeemedListAdapter(Context a, List<RedeemedOfeersResponse> createddealsList) {
            this.a = a;
            this.createddealsList = createddealsList;
        }

        @Override
        public RedeemedListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_lastredeemedoffers, parent, false);

            return new RedeemedListAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final RedeemedListAdapter.MyViewHolder holder, final int position) {

            holder.tv_offerId.setText(String.valueOf(createddealsList.get(position).getOfferName()));
            holder.tv_redeemDate.setText(Constants.convertDate(createddealsList.get(position).getRedeemDate()));


            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    /*Intent createofferIntent = new Intent(a, CreateNewOfferStepper.class);
                    createofferIntent.putExtra("offer",new Gson().toJson(createddealsList.get(position)));
                    a.startActivity(createofferIntent);*/

                }
            });
        }

        @Override
        public int getItemCount() {
            return createddealsList.size();
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tv_offerId,tv_redeemDate;
            CardView card;



            public MyViewHolder(View view) {
                super(view);

                tv_offerId = (TextView)view.findViewById(R.id.tv_offerId);
                tv_redeemDate = (TextView)view.findViewById(R.id.tv_redeemDate);
                card = (CardView)view.findViewById(R.id.card);

            }
        }
    }
}
