package com.dealse.dealsepartner.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.models.Image;
import com.dealse.dealsepartner.Application.Dealsepartnerapplicationclass;
import com.dealse.dealsepartner.Entity.CheckStoreMobieNumberExistResponse;
import com.dealse.dealsepartner.Entity.Store;
import com.dealse.dealsepartner.Interfaces.AddStore;
import com.dealse.dealsepartner.Interfaces.UpdateStore;
import com.dealse.dealsepartner.Managers.DealseApplicationsManager;
import com.dealse.dealsepartner.Objects.AreaListModel;
import com.dealse.dealsepartner.Objects.StoreTypeApiModel;
import com.dealse.dealsepartner.R;
import com.dealse.dealsepartner.Utility.Constants;
import com.dealse.dealsepartner.Utility.LoaderDialog;
import com.dealse.dealsepartner.Utility.LocationPickerActivity;
import com.dealse.dealsepartner.Utility.MapUtility;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;

public class ManageStoreDetails extends AppCompatActivity implements LocationListener {

    private Toolbar mToolbar;
    private EditText businesset, firstnameet, lastnameet, phoneet, emailet, storecontactet, addressrt, cityet, countryet;
    CheckStoreMobieNumberExistResponse checkStoreMobieNumberExistResponse;
    private AutoCompleteTextView av_areaselect;
    private AutoCompleteTextView av_storetypeselect;
    private Button btnDone;
    public Store store = new Store();
    public String businessetString, firstnameetString, lastnameetString, phoneetString, emailetString, storecontactetString, addressrtString, cityetSting, countryetSting;
    private String TAG = "ManageStoreDetails";
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    private LocationManager locationManager;
    private String provider;
    ImageView image;
    ImageView removeicn;
    ProgressBar progressBar;
    TextView addtxt;
    String imagePath;
    RelativeLayout plustxt;
    TextView tv_plusbtn;

    String[] perms = {"android.permission.FINE_LOCATION"};

    private static final int PERMISSION_REQUEST_CODE = 200;

    private static final int ADDRESS_PICKER_REQUEST = 1020;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managestoredetails);
        initToolbar();
        findViews();
        checkStoreMobieNumberExistResponse = new CheckStoreMobieNumberExistResponse();
        if(!checkPermission()){
            requestPermission();
        }

        String storeDataString = DealseApplicationsManager.getInstance().getPref(ManageStoreDetails.this).getString("storeData", "");

        if (!storeDataString.equalsIgnoreCase("")) {

            checkStoreMobieNumberExistResponse = new Gson().fromJson(storeDataString, CheckStoreMobieNumberExistResponse.class);

            if (checkStoreMobieNumberExistResponse != null) {

                if (!checkStoreMobieNumberExistResponse.getData().getLogoUrl().equalsIgnoreCase("http://vayotsavfoundation.org/images\\default.jpg")) {
                    tv_plusbtn.setVisibility(View.GONE);
                    addtxt.setVisibility(View.GONE);

                    image.setVisibility(View.VISIBLE);

                    Picasso.with(ManageStoreDetails.this)
                            .load(checkStoreMobieNumberExistResponse.getData().getLogoUrl().replace("\\", "/"))
                            //.fit()
                            .skipMemoryCache()
                            .into(image, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {

                                }
                            });
                }


                phoneet.setEnabled(false);

                businesset.setText(checkStoreMobieNumberExistResponse.getData().getName());
                String[] fullNameString = checkStoreMobieNumberExistResponse.getData().getOwnerName().split("\\s+");
                firstnameet.setText(fullNameString[0]);
                lastnameet.setText(fullNameString[1]);
                phoneet.setText(checkStoreMobieNumberExistResponse.getData().getOwnerMobileNo());
                emailet.setText(checkStoreMobieNumberExistResponse.getData().getEmail());
                storecontactet.setText(checkStoreMobieNumberExistResponse.getData().getMobileNo1());
                addressrt.setText(checkStoreMobieNumberExistResponse.getData().getAddress1());
                av_areaselect.setText(getSelectedArea(checkStoreMobieNumberExistResponse.getData().getAreaId()));
                av_storetypeselect.setText(getSelectedType(checkStoreMobieNumberExistResponse.getData().getStoreTypeId()));


                if (checkStoreMobieNumberExistResponse.getData().getAreaListModel() != null) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ManageStoreDetails.this, android.R.layout.simple_dropdown_item_1line, getAreaStringList());
                    av_areaselect.setAdapter(adapter);
                }

                if (checkStoreMobieNumberExistResponse.getData().getStoreTypeApiModel() != null) {
                    ArrayAdapter<String> adapterType = new ArrayAdapter<String>(ManageStoreDetails.this, android.R.layout.simple_dropdown_item_1line, getStoreTypeStringList());
                    av_storetypeselect.setAdapter(adapterType);
                }

            }
        }


        storecontactet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length() == 10)
                hideKeyboard(ManageStoreDetails.this);
            }
        });

        addressrt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                hideKeyboard(ManageStoreDetails.this);
            }
        });




        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                businessetString = businesset.getText().toString();
                firstnameetString = firstnameet.getText().toString();
                lastnameetString = lastnameet.getText().toString();
                phoneetString = phoneet.getText().toString();
                emailetString = emailet.getText().toString();
                storecontactetString = storecontactet.getText().toString();
                addressrtString = addressrt.getText().toString();
                cityetSting = cityet.getText().toString();
                countryetSting = countryet.getText().toString();

                if (!businessetString.equalsIgnoreCase("") && !firstnameetString.equalsIgnoreCase("") && !lastnameetString.equalsIgnoreCase("")
                        && !phoneetString.equalsIgnoreCase("")
                        && !addressrtString.equalsIgnoreCase("")
                ) {


                    store.setName(businessetString);
                    store.setOwnerName(firstnameetString + " " + lastnameetString);
                    store.setOwnerMobileNo(phoneetString);
                    store.setEmail(emailetString);
                    store.setOldLogo(checkStoreMobieNumberExistResponse.getData().getOldLogo());
                    store.setAddress1(addressrtString + " " + cityetSting + " " + countryetSting);
                    store.setAreaId(getAreaIdFromName(av_areaselect.getText().toString()));
                    store.setMobileNo1(storecontactetString);
                    store.setStoreTypeId(getTypeIdFromName(av_storetypeselect.getText().toString()));
                    store.setAddedDate(checkStoreMobieNumberExistResponse.getData().getAddedDate());
                    //     partnerDetails.setStoreLatitude();
                    //     partnerDetails.setStoreLongitude();
                    LoaderDialog.showLoader(ManageStoreDetails.this);
                    addStore(imagePath);

                    /*Intent mainintent = new Intent(Merchant_signup.this, HomeScreen.class);
                    startActivity(mainintent);*/

                } else {
                    Toast.makeText(ManageStoreDetails.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                }

            }
        });

        plustxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPicker();
            }
        });

        MapUtility.apiKey = getResources().getString(R.string.googlemap_api_key);

        addressrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ManageStoreDetails.this, LocationPickerActivity.class);
                startActivityForResult(i, ADDRESS_PICKER_REQUEST);
            }
        });

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int result0 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
                && result0 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, CAMERA}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && cameraAccepted) {
                        // Get the location manager
                        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        // Define the criteria how to select the locatioin provider -> use
                        // default
                        Criteria criteria = new Criteria();
                        provider = locationManager.getBestProvider(criteria, false);


                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        Location location = locationManager.getLastKnownLocation(provider);

                        // Initialize the location fields
                        if (location != null) {
                            System.out.println("Provider " + provider + " has been selected.");
                            onLocationChanged(location);
                        }
                        locationManager.requestLocationUpdates(provider, 400, 1, this);
                        //openScanner();
                        //    Snackbar.make(view, "Permission Granted, Now you can access location data and camera.", Snackbar.LENGTH_LONG).show();
                    }else {

                        //   Snackbar.make(view, "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow access to the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION, CAMERA},
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
        new AlertDialog.Builder(ManageStoreDetails.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public void startPicker(){
        Intent intent = new Intent(ManageStoreDetails.this, AlbumSelectActivity.class);
        //set limit on number of images that can be selected, default is 10
        intent.putExtra(com.darsh.multipleimageselect.helpers.Constants.INTENT_EXTRA_LIMIT, 10);
        startActivityForResult(intent, com.darsh.multipleimageselect.helpers.Constants.REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == com.darsh.multipleimageselect.helpers.Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            //The array list has the image paths of the selected images
            ArrayList<Image> images = data.getParcelableArrayListExtra(com.darsh.multipleimageselect.helpers.Constants.INTENT_EXTRA_IMAGES);
            Log.d("Got it", images.get(0).toString());

            tv_plusbtn.setVisibility(View.GONE);
            addtxt.setVisibility(View.GONE);

            image.setVisibility(View.VISIBLE);

            imagePath = images.get(0).path;

            File imgFile = new File(imagePath);

            Picasso.with(ManageStoreDetails.this)
                    .load(imgFile)
                    //       .fit()
                    .skipMemoryCache()
                    .into(image, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {

                        }
                    });


        }else if (requestCode == ADDRESS_PICKER_REQUEST) {
            try {
                if (data != null && data.getStringExtra(MapUtility.ADDRESS) != null) {
                    // String address = data.getStringExtra(MapUtility.ADDRESS);
                    double currentLatitude = data.getDoubleExtra(MapUtility.LATITUDE, 0.0);
                    double currentLongitude = data.getDoubleExtra(MapUtility.LONGITUDE, 0.0);
                    Bundle completeAddress =data.getBundleExtra("fullAddress");
                    /* data in completeAddress bundle
                    "fulladdress"
                    "city"
                    "state"
                    "postalcode"
                    "country"
                    "addressline1"
                    "addressline2"
                     */
                    addressrt.setText(/*new StringBuilder().append("addressline2: ").append*/
                            (completeAddress.getString("addressline2"))/*.append("\ncity: ").append
                            (completeAddress.getString("city")).append("\npostalcode: ").append
                            (completeAddress.getString("postalcode")).append("\nstate: ").append
                            (completeAddress.getString("state")).toString()*/);

                /*    countryet.setText(new StringBuilder().append("Lat:").append(currentLatitude).append
                            ("  Long:").append(currentLongitude).toString());*/

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public String getSelectedArea(String id){
        try {
            for (AreaListModel areaListModel : checkStoreMobieNumberExistResponse.getData().getAreaListModel()) {
                if (areaListModel.getAreaId().equalsIgnoreCase(id)) {
                    return areaListModel.getAreaName();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public String getSelectedType(String id){
        try {
        for(StoreTypeApiModel storeTypeApiModel : checkStoreMobieNumberExistResponse.getData().getStoreTypeApiModel()){
            if(storeTypeApiModel.getStoreTypeId().equalsIgnoreCase(id)){
                return storeTypeApiModel.getStoreTypeName();
            }
        }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public ArrayList<String> getAreaStringList() {

        ArrayList<String> areaStringList = new ArrayList<>();
        for(AreaListModel alm : checkStoreMobieNumberExistResponse.getData().getAreaListModel()){

            areaStringList.add(alm.getAreaName());
        }
        return areaStringList;

    }

    public  ArrayList<String> getStoreTypeStringList() {

        ArrayList<String> areaStringList = new ArrayList<>();
        for(StoreTypeApiModel alm : checkStoreMobieNumberExistResponse.getData().getStoreTypeApiModel()){

            areaStringList.add(alm.getStoreTypeName());
        }
        return areaStringList;

    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        TextView toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        ImageView toolbar_back = (ImageView) findViewById(R.id.toolbar_back);
        getSupportActionBar().setTitle(null);
        //toolbar_title.setTypeface(GetFonts.getInstance().getRobotoFont(TravFeddHomeScreen.this));
        toolbar_title.setText("Manage Store Details");
        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public int getAreaIdFromName(String storeName){
        for(AreaListModel alm : checkStoreMobieNumberExistResponse.getData().getAreaListModel()){
            if(alm.getAreaName().equalsIgnoreCase(storeName)){
                return Integer.parseInt(alm.getAreaId());
            }
        }
        return 0;
    }

    public int getTypeIdFromName(String typeName){
        for(StoreTypeApiModel stm : checkStoreMobieNumberExistResponse.getData().getStoreTypeApiModel()){
            if(stm.getStoreTypeName().equalsIgnoreCase(typeName)){
                return Integer.parseInt(stm.getStoreTypeId());
            }
        }
        return 0;
    }

    private void findViews(){
        businesset = (EditText) findViewById(R.id.businesset);
        firstnameet = (EditText) findViewById(R.id.firstnameet);
        lastnameet = (EditText) findViewById(R.id.lastnameet);
        phoneet  = (EditText) findViewById(R.id.phoneet);
        emailet = (EditText) findViewById(R.id.emailet);
        storecontactet = (EditText) findViewById(R.id.storecontactet);
        addressrt = (EditText) findViewById(R.id.addressrt);
        cityet = (EditText) findViewById(R.id.cityet);
        countryet = (EditText) findViewById(R.id.countryet);
        av_areaselect = (AutoCompleteTextView) findViewById(R.id.av_areaselect);
        av_storetypeselect = (AutoCompleteTextView) findViewById(R.id.av_storetypeselect);
        btnDone = (Button)findViewById(R.id.btnRegister);

        image = (ImageView)findViewById(R.id.image);
        removeicn = (ImageView)findViewById(R.id.removeicn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        plustxt = (RelativeLayout) findViewById(R.id.plustxt);
        tv_plusbtn = (TextView)findViewById(R.id.tv_plusbtn);
        addtxt = (TextView) findViewById(R.id.addtxt);

        removeicn.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        image.setVisibility(View.GONE);
        plustxt.setVisibility(View.VISIBLE);
        tv_plusbtn.setVisibility(View.VISIBLE);
        addtxt.setVisibility(View.VISIBLE);
    }

    /* Request updates at startup */
    @Override
    protected void onResume() {
        super.onResume();


    }



    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        if(locationManager != null)
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        store.setLatitude(location.getLatitude());
        store.setLongitude(location.getLongitude());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }




    public void addStore(String fileUri) {

        UpdateStore service =
                Dealsepartnerapplicationclass.retrofit.create(UpdateStore.class);

        MultipartBody.Part body1;
        Call<CheckStoreMobieNumberExistResponse> call;

        if(fileUri != null) {
            body1 = prepareFilePart("Logo", fileUri);//prepareFilePart("Logo", "");
        }else {
            body1 = MultipartBody.Part.createFormData("LogoTemp", "", createPartFromString(""));
        }


        RequestBody Name = createPartFromString(store.getName());
        RequestBody Email = createPartFromString(store.getEmail());
        RequestBody Address1 = createPartFromString(store.getAddress1());
        RequestBody OwnerName = createPartFromString(store.getOwnerName());
        RequestBody OwnerMobileNo = createPartFromString(store.getOwnerMobileNo());
        RequestBody Mobile1 = createPartFromString(store.getMobileNo1());
        RequestBody AddedDate = createPartFromString(store.getAddedDate());
        RequestBody OldLogo;
        if(store.getOldLogo() != null) {
            OldLogo = createPartFromString(store.getOldLogo());
        }else {
            OldLogo = createPartFromString("");
        }


        Log.d(TAG, "addStore"+"Step1");


        call = service.updateStore(DealseApplicationsManager.getInstance().getPref(ManageStoreDetails.this).getString(Constants.KEY_TOKEN,"")
                ,checkStoreMobieNumberExistResponse.getData().getStoreId(), store.getAreaId(),store.getStoreTypeId(),Name,Email,Address1,store.getLatitude(),store.getLongitude(),OwnerName,OwnerMobileNo,Mobile1,AddedDate,OldLogo,body1);

        call.enqueue(new Callback<CheckStoreMobieNumberExistResponse>() {
            @Override
            public void onResponse(Call<CheckStoreMobieNumberExistResponse> call, Response<CheckStoreMobieNumberExistResponse> response) {
                CheckStoreMobieNumberExistResponse checkStoreMobieNumberExistResponse = new CheckStoreMobieNumberExistResponse();
                checkStoreMobieNumberExistResponse = response.body();

                String code = checkStoreMobieNumberExistResponse.getCode();
                String message = checkStoreMobieNumberExistResponse.getMessage();

                LoaderDialog.hideLoader();

                Log.d(TAG, "addStore"+"Step2");

                if (code.equalsIgnoreCase("200")) {

                    {
                        Toast toast = Toast
                                .makeText(ManageStoreDetails.this, message, Toast.LENGTH_SHORT);
                        toast.show();

                        SharedPreferences.Editor editor = DealseApplicationsManager.getInstance().getEditor(ManageStoreDetails.this);

                        String storeDetails = new Gson().toJson(checkStoreMobieNumberExistResponse);

                        editor.putString("storeData", storeDetails);
                        editor.putString(Constants.KEY_STOREID, String.valueOf(checkStoreMobieNumberExistResponse.getData().getStoreId()));
                        editor.commit();

                        Log.d(TAG, "addStore"+"Step3");

                        Intent mainintent = new Intent(ManageStoreDetails.this,HomeScreen.class);
                        mainintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(mainintent);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<CheckStoreMobieNumberExistResponse> call, Throwable t) {
                LoaderDialog.hideLoader();
                Toast toast = Toast
                        .makeText(ManageStoreDetails.this, t.toString(), Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, String fileUri) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = new  File(fileUri);

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
