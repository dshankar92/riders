package com.bikegroup.riders.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.bikegroup.riders.R;
import com.bikegroup.riders.view.fragment.FragDoneSignup;
import com.bikegroup.riders.view.fragment.FragmentSignup;

public class SignupScreen extends FragmentActivity {
    private String TAG = SignupScreen.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        initViews();

        if (savedInstanceState == null) {
           Fragment fragment= new FragmentSignup();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.singup_container,fragment).commit();
        }
    }

    private void initViews() {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* startActivity(new Intent(LoginScreen.this, WelcomeScreen.class));
        finish();*/
    }
}
