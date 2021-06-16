package com.dealse.dealsepartner.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.models.Image;
import com.dealse.dealsepartner.Adapters.AreaListAdapter;
import com.dealse.dealsepartner.Adapters.OffersListAdapter;
import com.dealse.dealsepartner.Application.Dealsepartnerapplicationclass;
import com.dealse.dealsepartner.Entity.CheckStoreMobieNumberExistRequest;
import com.dealse.dealsepartner.Entity.CheckStoreMobieNumberExistResponse;
import com.dealse.dealsepartner.Entity.Store;
import com.dealse.dealsepartner.Interfaces.AddStore;
import com.dealse.dealsepartner.Interfaces.CheckStoreMobieNumberExist;
import com.dealse.dealsepartner.Interfaces.GetAreaAndStoreTypeResponse;
import com.dealse.dealsepartner.Managers.DealseApplicationsManager;
import com.dealse.dealsepartner.Objects.AreaListModel;
import com.dealse.dealsepartner.Objects.GetAreaAndStoreTypeList;
import com.dealse.dealsepartner.Objects.Partner;
import com.dealse.dealsepartner.Objects.StoreTypeApiModel;
import com.dealse.dealsepartner.R;
import com.dealse.dealsepartner.Utility.Constants;
import com.dealse.dealsepartner.Utility.GpsTracker;
import com.dealse.dealsepartner.Utility.LoaderDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;

/**
 * Created by Harshank Sananse on 12/2/2016.
 */
public class Merchant_signup extends AppCompatActivity implements LocationListener {

    private Toolbar mToolbar;
    private ScrollView parentactionscroll;
    private RelativeLayout relativeLayout;
    private EditText businesset, firstnameet, lastnameet, phoneet, emailet, storecontactet, addressrt, cityet, countryet;
    private RelativeLayout relativeLayoutcountry;
    private RelativeLayout relativeLayoutpassword;
    private EditText genderet;
    private Button btnRegister;
    private Button button;
    private View dottedView;
    private RelativeLayout relativeLayoutp;
    private RelativeLayout relativeLayoutpp;
    private EditText dateofbirthet;
    private TextView textView;
    private TextView textView2;

    private AutoCompleteTextView av_areaselect;
    private AutoCompleteTextView av_storetypeselect;


    private String TAG = "Merchant_signup";

    public ArrayList<AreaListModel> areaList;
    public ArrayList<StoreTypeApiModel> storeTypeList;

    public static final String MULTIPART_FORM_DATA = "multipart/form-data";


    AreaListAdapter areaListAdapter;

    //private List<AddUserResponse> addUserResponsesList;
    //private List<AddUserResponse> addUserResponses;

    public String businessetString, firstnameetString, lastnameetString, phoneetString, emailetString, storecontactetString, addressrtString, cityetSting, countryetSting;

    public static Partner partnerDetails;

    public Store store = new Store();

    private GpsTracker gpsTracker;
    private LocationManager locationManager;
    private String provider;

    ImageView image;
    ImageView removeicn;
    ProgressBar progressBar;
    TextView addtxt;
    RelativeLayout plustxt;
    TextView tv_plusbtn;

    String imagePath;

    String[] perms = {"android.permission.FINE_LOCATION"};

    private static final int PERMISSION_REQUEST_CODE = 200;

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_signup_screen);
        initToolbar();
        btnRegister = (Button) findViewById(R.id.btnRegister);
        findViews();


        LoaderDialog.showLoader(Merchant_signup.this);
        getAreaAndStoreTypeList();

        if(!checkPermission()){
            requestPermission();
        }



        /*Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        ArrayList<AreaListModel> areaList = (ArrayList<AreaListModel>) args.getSerializable("AreaList");*/

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        phoneet.setEnabled(false);
        if (args != null) {
            phoneet.setText(args.getCharSequence("mobileNumber"));
        } else {
            phoneet.setText(DealseApplicationsManager.getInstance().getPref(Merchant_signup.this).getString("mobileNumber", ""));
        }

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                         token = task.getResult();

                        // Log and toast
                       // String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG + "Firebase Token", token);
                    //    Toast.makeText(Merchant_signup.this, token, Toast.LENGTH_SHORT).show();
                    }
                });


        btnRegister.setOnClickListener(new View.OnClickListener() {
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
                  //  store.setMobileNo2(token);
                    store.setEmail(emailetString);
                    store.setAddress(addressrtString + " " + cityetSting + " " + countryetSting);
                    store.setMobileNo1(storecontactetString);

                    store.setAreaId(getAreaIdFromName(av_areaselect.getText().toString()));
                    store.setStoreTypeId(getTypeIdFromName(av_storetypeselect.getText().toString()));
                    //     partnerDetails.setStoreLatitude();
                    //     partnerDetails.setStoreLongitude();
                    LoaderDialog.showLoader(Merchant_signup.this);
                    addStore(imagePath);







                    /*Intent mainintent = new Intent(Merchant_signup.this, HomeScreen.class);
                    startActivity(mainintent);*/

                } else {
                    Toast.makeText(Merchant_signup.this, "Please fill requiered details", Toast.LENGTH_SHORT).show();
                }

            }
        });

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
                    hideKeyboard(Merchant_signup.this);
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
                hideKeyboard(Merchant_signup.this);
            }
        });

        plustxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPicker();
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

    public void startPicker(){
        Intent intent = new Intent(Merchant_signup.this, AlbumSelectActivity.class);
        //set limit on number of images that can be selected, default is 10
        intent.putExtra(com.darsh.multipleimageselect.helpers.Constants.INTENT_EXTRA_LIMIT, 10);
        startActivityForResult(intent, com.darsh.multipleimageselect.helpers.Constants.REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == com.darsh.multipleimageselect.helpers.Constants.REQUEST_CODE && resultCode == Merchant_signup.this.RESULT_OK && data != null) {
            //The array list has the image paths of the selected images
            ArrayList<Image> images = data.getParcelableArrayListExtra(com.darsh.multipleimageselect.helpers.Constants.INTENT_EXTRA_IMAGES);
            Log.d("Got it", images.get(0).toString());

            tv_plusbtn.setVisibility(View.GONE);
            addtxt.setVisibility(View.GONE);

            image.setVisibility(View.VISIBLE);

            imagePath = images.get(0).path;

            File imgFile = new File(imagePath);

            Picasso.with(Merchant_signup.this)
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


        }
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
        new AlertDialog.Builder(Merchant_signup.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        if(locationManager != null)
            locationManager.removeUpdates(this);
    }

    private void findViews() {


        parentactionscroll = (ScrollView) findViewById(R.id.parentactionscroll);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
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

    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        TextView toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        ImageView toolbar_back = (ImageView) findViewById(R.id.toolbar_back);
        getSupportActionBar().setTitle(null);
        //toolbar_title.setTypeface(GetFonts.getInstance().getRobotoFont(TravFeddHomeScreen.this));
        toolbar_title.setText(" ");
        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void getLocation(){
        gpsTracker = new GpsTracker(Merchant_signup.this);
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            store.setLatitude(gpsTracker.getLatitude());
            store.setLongitude(gpsTracker.getLongitude());
        }else{
            gpsTracker.showSettingsAlert();
        }
    }


    public int getAreaIdFromName(String storeName){
        for(AreaListModel alm : areaList){
            if(alm.getAreaName().equalsIgnoreCase(storeName)){
                return Integer.parseInt(alm.getAreaId());
            }
        }
        return 0;
    }

    public int getTypeIdFromName(String typeName){
        for(StoreTypeApiModel stm : storeTypeList){
            if(stm.getStoreTypeName().equalsIgnoreCase(typeName)){
                return Integer.parseInt(stm.getStoreTypeId());
            }
        }
        return 0;
    }




    public  ArrayList<String> getAreaStringList() {

        ArrayList<String> areaStringList = new ArrayList<>();
        for(AreaListModel alm : areaList){

            areaStringList.add(alm.getAreaName());
        }
        return areaStringList;

    }

    public  ArrayList<String> getStoreTypeStringList() {

        ArrayList<String> areaStringList = new ArrayList<>();
        for(StoreTypeApiModel alm : storeTypeList){

            areaStringList.add(alm.getStoreTypeName());
        }
        return areaStringList;

    }




    /*@SuppressLint("ValidFragment")
    public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        int byear, bmonth, bday;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            byear = calendar.get(Calendar.YEAR);
            bmonth = calendar.get(Calendar.MONTH);
            bday = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dpd = new DatePickerDialog(Merchant_signup.this,
                    AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, this, byear, bmonth, bday);

            // dpd.setTitle("Select Date"); // Uncomment this line to activate it

            // Return the DatePickerDialog
            return new DatePickerDialog(getActivity(), this, byear, bmonth, bday);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the chosen date
            String date = day + "/" + (month + 1) + "/" + year;

            dateofbirthet.setText(date);
        }
    }*/


    public void getAreaAndStoreTypeList(){


        final GetAreaAndStoreTypeResponse getAreaAndStoreTypeResponse = Dealsepartnerapplicationclass.retrofit.create(GetAreaAndStoreTypeResponse.class);
        Call<GetAreaAndStoreTypeList> call = getAreaAndStoreTypeResponse.getAreaAndStoreTypeList(DealseApplicationsManager.getInstance().getPref(Merchant_signup.this).getString(Constants.KEY_TOKEN,""));
        call.enqueue(new Callback<GetAreaAndStoreTypeList>() {
            @Override
            public void onResponse(Call<GetAreaAndStoreTypeList> call, Response<GetAreaAndStoreTypeList> response) {
                GetAreaAndStoreTypeList getAreaAndStoreTypeList = new GetAreaAndStoreTypeList();
                getAreaAndStoreTypeList = response.body();

                LoaderDialog.hideLoader();

                if(getAreaAndStoreTypeList != null) {
                    try {

                        if(getAreaAndStoreTypeList.getCode() == 200) {

                            areaList = new ArrayList<>();
                            storeTypeList = new ArrayList<>();
                            areaList = getAreaAndStoreTypeList.getData().getAreaListModel();
                            storeTypeList = getAreaAndStoreTypeList.getData().getStoreTypeApiModel();

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Merchant_signup.this,android.R.layout.simple_dropdown_item_1line, getAreaStringList());
                            av_areaselect.setAdapter(adapter);


                            ArrayAdapter<String> adapterType = new ArrayAdapter<String>(Merchant_signup.this, android.R.layout.simple_dropdown_item_1line, getStoreTypeStringList());
                            av_storetypeselect.setAdapter(adapterType);

                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(Merchant_signup.this,"Something went erong, Please try again later",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Merchant_signup.this,"Something went erong, Please try again later",Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<GetAreaAndStoreTypeList> call, Throwable t) {
                Toast.makeText(Merchant_signup.this,"Please again try later",Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void addStore(String fileUri) {

        AddStore service =
                Dealsepartnerapplicationclass.retrofit.create(AddStore.class);

        MultipartBody.Part body1;
        Call<CheckStoreMobieNumberExistResponse> call;

        if(fileUri != null) {
            body1 = prepareFilePart("Logo", fileUri);//prepareFilePart("Logo", "");
        }else {
            body1 = MultipartBody.Part.createFormData("Logo", "", createPartFromString(""));
        }


        RequestBody Name = createPartFromString(store.getName());
        RequestBody Email = createPartFromString(store.getEmail());
        RequestBody Address = createPartFromString(store.getAddress());
        RequestBody OwnerName = createPartFromString(store.getOwnerName());
        RequestBody OwnerMobileNo = createPartFromString(store.getOwnerMobileNo());
        RequestBody Mobile1 = createPartFromString(store.getMobileNo1());
        RequestBody FirebaseToken = createPartFromString(token);
    //    RequestBody AddedDate = createPartFromString(store.getAddedDate());

        Log.d(TAG, "addStore"+"Step1");


        call = service.addStore(DealseApplicationsManager.getInstance().getPref(Merchant_signup.this).getString(Constants.KEY_TOKEN,"")
                , store.getAreaId(),store.getStoreTypeId(),Name,Email,Address,store.getLatitude(),store.getLongitude(),OwnerName,OwnerMobileNo,Mobile1,FirebaseToken,body1);

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
                                .makeText(Merchant_signup.this, message, Toast.LENGTH_SHORT);
                        toast.show();

                        SharedPreferences.Editor editor = DealseApplicationsManager.getInstance().getEditor(Merchant_signup.this);

                        String storeDetails = new Gson().toJson(checkStoreMobieNumberExistResponse);

                        editor.putString("storeData", storeDetails);
                        editor.putString(Constants.KEY_STOREID, String.valueOf(checkStoreMobieNumberExistResponse.getData().getStoreId()));
                        editor.commit();

                        Log.d(TAG, "addStore"+"Step3");

                        Intent mainintent = new Intent(Merchant_signup.this,HomeScreen.class);
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
                        .makeText(Merchant_signup.this, t.toString(), Toast.LENGTH_SHORT);
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
}
