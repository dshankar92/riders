package com.bikegroup.riders.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bikegroup.riders.R;


/**
 * Created by Geetika on 4/16/2018.
 */
public class FragDoneSignup extends Fragment implements View.OnClickListener {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_signup_continue, container, false);
        init(rootView);
        assignClicks();
        return rootView;
    }

    private void init(View view) {
    }

    private void assignClicks() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            default:
                break;
        }
    }
}
