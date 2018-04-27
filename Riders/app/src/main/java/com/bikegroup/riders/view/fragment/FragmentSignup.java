package com.bikegroup.riders.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bikegroup.riders.R;

/**
 * Created by Geetika on 4/16/2018.
 */
public class FragmentSignup extends Fragment implements View.OnClickListener {

    private Button btn_signup_cont;
    private EditText et_username, et_mobile, et_petname, et_address, et_dob;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signup, container, false);
        init(v);
        assignClicks();
        return v;
    }

    private void init(View view) {
        btn_signup_cont = (Button) view.findViewById(R.id.btn_signup_cont);
    }

    private void assignClicks() {
        btn_signup_cont.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_signup_cont:

                Fragment newFragment = new FragDoneSignup();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
                transaction.replace(R.id.singup_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commitAllowingStateLoss();
                break;
            default:
                break;
        }
    }
}
