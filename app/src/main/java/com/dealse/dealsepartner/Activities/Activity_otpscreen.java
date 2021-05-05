package com.dealse.dealsepartner.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.dealse.dealsepartner.Application.Dealsepartnerapplicationclass;
import com.dealse.dealsepartner.Entity.AuthRequest;
import com.dealse.dealsepartner.Entity.AuthResponse;
import com.dealse.dealsepartner.Entity.CheckStoreMobieNumberExistRequest;
import com.dealse.dealsepartner.Entity.CheckStoreMobieNumberExistResponse;
import com.dealse.dealsepartner.Interfaces.CheckStoreMobieNumberExist;
import com.dealse.dealsepartner.Interfaces.GetAuthToken;
import com.dealse.dealsepartner.Managers.DealseApplicationsManager;
import com.dealse.dealsepartner.R;
import com.dealse.dealsepartner.Utility.Constants;
import com.dealse.dealsepartner.Utility.LoaderDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_otpscreen extends AppCompatActivity {



    Toolbar mToolbar;
    TextView text1;
    Button submit;
    public static String otp = "",authtoken;
    public static PinEntryEditText pinEntry;
    public static Button verifyotp, back, save;
    ProgressDialog pd;
    private String mobileNumber;
    private String TAG = "Activity_otpscreen";
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    FirebaseAuth mAuth;
    PhoneAuthCredential credential;
    String mVerificationId;
    TextView resen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpscreen);
        initToolbar();

        Intent mainintent = getIntent();
        mobileNumber = mainintent.getStringExtra("mobile");

        resen = (TextView)findViewById(R.id.resen);


        mAuth = FirebaseAuth.getInstance();

        submit = (Button)findViewById(R.id.submit);
        pinEntry = (PinEntryEditText)findViewById(R.id.txt_pin_entry);
        text1 = (TextView)findViewById(R.id.text1);
        text1.setText(mobileNumber);
        if (pinEntry != null) {
            pinEntry.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {
                    otp = str.toString();
                }
            });
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otp.length() >= 6) {
                 //   authtoken = getInstance().getPref().getString("loginauthtoken", "");
                    pd = new ProgressDialog(Activity_otpscreen.this);
                    pd.setMessage(getResources().getString(R.string.pleasewait));
                    pd.setCancelable(false);
                    pd.show();
                //    performotpcall(otp, "register");
                    if(mVerificationId != null ) {
                        verifyPhoneNumberWithCode(mVerificationId, otp);
                    }else {
                        pd.dismiss();
                        Toast.makeText(Activity_otpscreen.this,"Invalid credentials entered",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(v, "Please enter OTP", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        resen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(Activity_otpscreen.this,"New OTP Code Sent",Toast.LENGTH_SHORT).show();
                requestOtp();
                Snackbar.make(submit, "New OTP Code Sent", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.
            //    Log.d(TAG, "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                    pinEntry.setText("");
                    pd.dismiss();
                    Toast.makeText(Activity_otpscreen.this,"Invalid credentials entered",Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;



                // ...
            }
        };

        requestOtp();
    }

    public void requestOtp(){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+mobileNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                Activity_otpscreen.this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }
    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                        //    addUserAndUpdateUI();
                            LoaderDialog.showLoader(Activity_otpscreen.this);
                            getAuthToken();


                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            pinEntry.setText("");
                            pd.dismiss();
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(submit, "Code is invalid please try again!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                pinEntry.setText("");
                                pd.dismiss();
                                Snackbar.make(submit, "Code is invalid please try again!", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }
                    }
                });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Verification");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getAuthToken() {

        final GetAuthToken getTokenToken = Dealsepartnerapplicationclass.retrofit.create(GetAuthToken.class);
        Call<AuthResponse> call = getTokenToken.getAuthToken(new AuthRequest(Constants.VALUE_FIRSTAPPKEY));
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                AuthResponse authResponse = new AuthResponse();
                authResponse = response.body();

                if(authResponse != null) {
                    try {
                        Log.d("++++++token", "Bearer  "+authResponse.getData().getToken());

                            SharedPreferences.Editor editor = DealseApplicationsManager.getInstance().getEditor(Activity_otpscreen.this);
                            editor.putString(Constants.KEY_TOKEN,"Bearer  "+authResponse.getData().getToken());
                            editor.commit();
                            editor.apply();

                    //        LoaderDialog.hideLoader();

                        checkStoreNumberExist();

                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(Activity_otpscreen.this,"Something went erong, Please try again later",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Activity_otpscreen.this,"Something went erong, Please try again later",Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(Activity_otpscreen.this,"Please again try later",Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void checkStoreNumberExist() {

        final CheckStoreMobieNumberExist checkStoreMobieNumberExist = Dealsepartnerapplicationclass.retrofit.create(CheckStoreMobieNumberExist.class);
        Call<CheckStoreMobieNumberExistResponse> call = checkStoreMobieNumberExist.checkStoreMobileNumberExist(DealseApplicationsManager.getInstance().getPref(Activity_otpscreen.this).getString(Constants.KEY_TOKEN,""),new CheckStoreMobieNumberExistRequest(mobileNumber));
        call.enqueue(new Callback<CheckStoreMobieNumberExistResponse>() {
            @Override
            public void onResponse(Call<CheckStoreMobieNumberExistResponse> call, Response<CheckStoreMobieNumberExistResponse> response) {
                CheckStoreMobieNumberExistResponse checkStoreMobieNumberExistResponse = new CheckStoreMobieNumberExistResponse();
                checkStoreMobieNumberExistResponse = response.body();

                LoaderDialog.hideLoader();

                if(checkStoreMobieNumberExistResponse != null) {
                    try {

                        if(checkStoreMobieNumberExistResponse.getData().getStoreId() != 0 || checkStoreMobieNumberExistResponse.getData().getStoreId() > 0) {

                            String storeDetails = new Gson().toJson(checkStoreMobieNumberExistResponse);

                            SharedPreferences.Editor editor = DealseApplicationsManager.getInstance().getEditor(Activity_otpscreen.this);

                            editor.putString("storeData", storeDetails);
                            editor.putInt(Constants.KEY_STOREID, checkStoreMobieNumberExistResponse.getData().getStoreId());
                            editor.apply();
                            editor.commit();

                            Log.d("++++++StoreId", String.valueOf(checkStoreMobieNumberExistResponse.getData().getStoreId()));



                            Intent mainintent = new Intent(Activity_otpscreen.this,HomeScreen.class);
                            mainintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(mainintent);
                            finish();

                        }else{



                            Intent mainintent = new Intent(Activity_otpscreen.this,Merchant_signup.class);
                            mainintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            Bundle args = new Bundle();
                            args.putSerializable("AreaList",checkStoreMobieNumberExistResponse.getData().getAreaListModel());
                            args.putCharSequence("mobileNumber",mobileNumber);
                            mainintent.putExtra("BUNDLE",args);

                            SharedPreferences.Editor editor = DealseApplicationsManager.getInstance().getEditor(Activity_otpscreen.this);
                            editor.putString("mobileNumber", mobileNumber);
                            editor.apply();
                            editor.commit();

                            startActivity(mainintent);
                            finish();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(Activity_otpscreen.this,"Something went erong, Please try again later",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Activity_otpscreen.this,"Something went erong, Please try again later",Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<CheckStoreMobieNumberExistResponse> call, Throwable t) {
                Toast.makeText(Activity_otpscreen.this,"Please again try later",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
