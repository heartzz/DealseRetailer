package com.dealse.dealsepartner.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dealse.dealsepartner.R;

public class Step4cardpreviewfragment extends Fragment {


    public static Fragment newInstance() {
        return new Step4cardpreviewfragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.step4cardpreviewfragment, null, false);

        return v;
    }

}
