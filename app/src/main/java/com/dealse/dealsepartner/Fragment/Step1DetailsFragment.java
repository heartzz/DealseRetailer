package com.dealse.dealsepartner.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.dealse.dealsepartner.Activities.CreateNewOfferStepper;
import com.dealse.dealsepartner.Activities.ManageOffersScreen;
import com.dealse.dealsepartner.Activities.Merchant_signup;
import com.dealse.dealsepartner.Entity.CheckStoreMobieNumberExistResponse;
import com.dealse.dealsepartner.Managers.DealseApplicationsManager;
import com.dealse.dealsepartner.Objects.Deals;
import com.dealse.dealsepartner.Objects.Offer;
import com.dealse.dealsepartner.R;
import com.dealse.dealsepartner.Utility.Constants;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Stack;

public class Step1DetailsFragment extends Fragment {


    EditText tv_offerTitle,tv_shortDescription,tv_longDescription,tv_validtill,tv_terms;
    String title,shortDescription,longDescription,validFromDate,validTillDate,terms;
    EditText tv_validfrom;

    Stack<Fragment> fragmentStack;
    Calendar calendar ;
    int Year, Month, Day ;
    Offer offer;

    RelativeLayout rl_validFrom;


    public Step1DetailsFragment(Offer offer,Stack<Fragment> fragmentStack){
        this.fragmentStack = fragmentStack;
        this.offer = offer;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.createofferscreen, null, false);
        findViews(v);

        return v;
    }

    private void findViews(View view) {
        tv_offerTitle = (EditText)view.findViewById(R.id.tv_offerTitle);
        tv_shortDescription = (EditText)view.findViewById(R.id.tv_shortDescription);
        tv_longDescription = (EditText)view.findViewById(R.id.tv_longDescription);
        tv_validfrom  = (EditText)view.findViewById(R.id.tv_validfrom);
        tv_validtill = (EditText)view.findViewById(R.id.tv_validtill);
        tv_terms = (EditText)view.findViewById(R.id.tv_terms);

        rl_validFrom = (RelativeLayout)view.findViewById(R.id.rl_validFrom);


        calendar = Calendar.getInstance();

        Year = calendar.get(Calendar.YEAR) ;
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);


        tv_offerTitle.setText(offer.getName());
        tv_shortDescription.setText(offer.getSortDescription());
        tv_longDescription.setText(offer.getLongDescription());
        tv_terms.setText(offer.getTermsAndConditions());
        tv_validfrom.setText(Constants.convertDate(offer.getStartDate()));
        tv_validtill.setText(Constants.convertDate(offer.getEndDate()));

        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();

        // now define the properties of the
        // materialDateBuilder that is title text as SELECT A DATE
        materialDateBuilder.setTitleText("SELECT FROM DATE");


        // now create the instance of the material date
        // picker
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();


        MaterialDatePicker.Builder materialDateBuilder1 = MaterialDatePicker.Builder.datePicker();

        // now define the properties of the
        // materialDateBuilder that is title text as SELECT A DATE
        materialDateBuilder1.setTitleText("SELECT TILL DATE");


        // now create the instance of the material date
        // picker
        final MaterialDatePicker materialDatePicker1 = materialDateBuilder.build();

        // handle select date button which opens the
        // material design date picker
        tv_validfrom.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // getSupportFragmentManager() to
                        // interact with the fragments
                        // associated with the material design
                        // date picker tag is to get any error
                        // in logcat
                        materialDatePicker.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                });

        // now handle the positive button click from the
        // material design date picker
        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {

                        // if the user clicks on the positive
                        // button that is ok button update the
                        // selected date
                        DateFormat df = new SimpleDateFormat("dd MMM yyyy");
                        Date currentDate = new Date();
                        if(Constants.isValidDate(df.format(currentDate),Constants.convertDateToSend(selection.toString()))){
                            tv_validfrom.setText(Constants.convertDateToSend(selection.toString()));
                        }else {
                            Toast.makeText(getActivity(),"Please select new dates.",Toast.LENGTH_SHORT).show();
                        }

                        // in the above statement, getHeaderText
                        // is the selected date preview from the
                        // dialog
                    }
                });


// handle select date button which opens the
        // material design date picker
        tv_validtill.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // getSupportFragmentManager() to
                        // interact with the fragments
                        // associated with the material design
                        // date picker tag is to get any error
                        // in logcat
                        materialDatePicker1.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                });

        // now handle the positive button click from the
        // material design date picker
        materialDatePicker1.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {

                        // if the user clicks on the positive
                        // button that is ok button update the
                        // selected date
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        Date currentDate = new Date();
                        if(Constants.isValidDate(df.format(currentDate),Constants.convertDateToSend(selection.toString()))){
                            tv_validtill.setText(Constants.convertDateToSend(selection.toString()));
                        }else {
                            Toast.makeText(getActivity(),"Please select new dates.",Toast.LENGTH_SHORT).show();
                        }

                        // in the above statement, getHeaderText
                        // is the selected date preview from the
                        // dialog
                    }
                });
    }



    public boolean saveDetails(){
        if(isValid()){

            if(CreateNewOfferStepper.offer.getOfferId() != 0){
                CreateNewOfferStepper.offer.setActive(true);
            }


            CreateNewOfferStepper.offer.setStoreId(new Gson().fromJson(DealseApplicationsManager.getInstance().getPref(getActivity()).getString("storeData",""), CheckStoreMobieNumberExistResponse.class).getData().getStoreId());
            CreateNewOfferStepper.offer.setName(tv_offerTitle.getText().toString());
            CreateNewOfferStepper.offer.setSortDescription(tv_shortDescription.getText().toString());
            CreateNewOfferStepper.offer.setLongDescription(tv_longDescription.getText().toString());
            CreateNewOfferStepper.offer.setStartDate(Constants.dateToSendString(tv_validfrom.getText().toString()));
            CreateNewOfferStepper.offer.setEndDate(Constants.dateToSendString(tv_validtill.getText().toString()));
            CreateNewOfferStepper.offer.setTermsAndConditions(tv_terms.getText().toString());

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            //ft.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_in_right);
            Step2BannerImageFragment step2BannerImageFragment = new Step2BannerImageFragment(CreateNewOfferStepper.offer,fragmentStack);
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

        title = tv_offerTitle.getText().toString();
        shortDescription = tv_shortDescription.getText().toString();
        longDescription = tv_longDescription.getText().toString();
        validFromDate = tv_validfrom.getText().toString();
        validTillDate = tv_validtill.getText().toString();
        terms = tv_terms.getText().toString();


        if (!title.equalsIgnoreCase("") && !shortDescription.equalsIgnoreCase("") &&
                !longDescription.equalsIgnoreCase("") &&
                !validFromDate.equalsIgnoreCase("") &&
                !validTillDate.equalsIgnoreCase("")){

            return true;

        }else {
            Toast.makeText(getActivity(),"Please enter required  fields", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
