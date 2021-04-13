package com.dealse.dealsepartner.Managers;

import android.app.Activity;
import android.content.SharedPreferences;

import com.dealse.dealsepartner.Application.Dealsepartnerapplicationclass;

/**
 * Created by HARSHANK on 04-08-2019.
 */
public class DealseApplicationsManager {
        /* shared Preference and its editor */
        private static final String PREFERENCE_NAME = "dealsepartner";
        private SharedPreferences pref;
        private SharedPreferences.Editor editor;
        /* Constants for shared preferences */
        private static final String IS_FIRST_TIME = "isFirstTime";
        /* preference mode - private , only be accessed by JohnnyZCasino application */
        public int PRIVATE_MODE = 0;

        public boolean isFirstTime = false;


        /* Singleton Class */
        private static DealseApplicationsManager instance = new DealseApplicationsManager();

        private DealseApplicationsManager(){}


        public static DealseApplicationsManager getInstance()
        {
            return instance;
        }
        public void init()
        {

        }

        public SharedPreferences getPref(Activity activity)
        {
            if(pref==null)
            {
                pref = activity.getSharedPreferences(PREFERENCE_NAME, PRIVATE_MODE);
            }
            return pref;
        }
        public SharedPreferences.Editor getEditor(Activity activity)
        {
            if(editor==null){
                editor= getPref(activity).edit();
            }
            return editor;
        }



        public boolean isFromUser(String userID,Activity activity){
            boolean isUser = false;

            if(userID.equalsIgnoreCase(DealseApplicationsManager.getInstance().getPref(activity).getString("user_id",""))){
                return true;
            }
            return false;
        }
}
