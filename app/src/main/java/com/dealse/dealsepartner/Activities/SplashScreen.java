package com.dealse.dealsepartner.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.dealse.dealsepartner.Managers.DealseApplicationsManager;
import com.dealse.dealsepartner.R;
import com.dealse.dealsepartner.Utility.Constants;

/**
 * Created by HARSHANK on 04-08-2019.
 */
public class SplashScreen extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splashscreen);


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                if(!DealseApplicationsManager.getInstance().getPref(SplashScreen.this).getString(Constants.KEY_TOKEN,"").equalsIgnoreCase("")){

                    if(!DealseApplicationsManager.getInstance().getPref(SplashScreen.this).getString("storeData","").equalsIgnoreCase("")){

                        Intent mainintent = new Intent(SplashScreen.this,HomeScreen.class);
                        mainintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(mainintent);
                        finish();

                    }else{

                        Intent mainintent = new Intent(SplashScreen.this,Merchant_signup.class);
                        mainintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(mainintent);
                        finish();

                    }

                }else{
                    Intent mainscreenIntent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(mainscreenIntent);
                    finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}

