package com.bikegroup.riders.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;

import com.bikegroup.riders.R;
import com.bikegroup.riders.view.control.PrefManager;
import com.bikegroup.riders.view.iHelper.WebAPIResponseListener;
import com.bikegroup.riders.view.utils.AndroidAppUtils;
import com.bikegroup.riders.view.webServices.LoginApiHandler;


public class LoginScreen extends AppCompatActivity implements View.OnClickListener, OnFocusChangeListener {
    private String TAG = LoginScreen.class.getSimpleName();
    private Button btnLogin, btnSignUp;
    private TextInputEditText etUsername, etPassword;
    private Activity mActivity;
    private String strPassword = "", strName = "";
    private TextInputLayout layout_username, layout_password;
    private View focusview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        initViews();
        assignListeners();
    }

    /**
     * Method Name :assignListeners
     * Description : This method is used for assigning listener to view component
     */
    private void assignListeners() {
        btnLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        etUsername.setOnFocusChangeListener(this);
        etPassword.setOnFocusChangeListener(this);
    }

    /**
     * Method Name :initViews
     * Description : This method is used for initializing the view component
     */
    private void initViews() {
        mActivity = this;
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        etUsername = (TextInputEditText) findViewById(R.id.et_username);
        etPassword = (TextInputEditText) findViewById(R.id.et_pswd);
        layout_password = (TextInputLayout) findViewById(R.id.layout_password);
        layout_username = (TextInputLayout) findViewById(R.id.layout_username);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:

                /*hit login api*/
                strPassword = etPassword.getText().toString();
                strName = etUsername.getText().toString();
                if (validateFields()) {
                    new LoginApiHandler(mActivity, strName, strPassword, FetchLoginResponse());
                } else {
                    AndroidAppUtils.showLog(TAG, "Fill all the fields");
                }
                startActivity(new Intent(LoginScreen.this, HomeScreen.class));
                break;
            case R.id.btnSignUp:
                startActivity(new Intent(LoginScreen.this, SignupScreen.class));
                break;
            default:
                break;
        }
    }

    /**
     * @return
     */
    private WebAPIResponseListener FetchLoginResponse() {
        final WebAPIResponseListener mListener = new WebAPIResponseListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onSuccessOfResponse(Object... arguments) {
                String auth_token = arguments[0].toString();
                AndroidAppUtils.showLog(TAG, "token: " + auth_token);
                if (mActivity != null && !mActivity.isFinishing()) {
                    /*save token in pref*/
                    new PrefManager().setStringValue("token", auth_token);
                    /*navigate to home screen*/
                }
            }

            @Override
            public void onFailOfResponse(Object... arguments) {
                /*show alert dialog*/
            }
        };
        return mListener;
    }

    /*****************************************************************
     * Function Name - validateFields
     * Description - this method validates the user entered Email ID
     * and password and manages the error field
     *****************************************************************/
    private boolean validateFields() {
        boolean is_Emailvalid = false, is_Passwordvalid = false;
        removeErrorField();
        if (TextUtils.isEmpty(strName)) {
            AndroidAppUtils.showLog(TAG, "email_id : " + strName);
            layout_username.setHintEnabled(false);
            layout_username.setError(mActivity.getResources().getString(R.string.login_valid_email));
            focusview = etUsername;
            is_Emailvalid = false;
        } else if (!AndroidAppUtils.checkEmail(strName)) {
            layout_username.setHintEnabled(true);
            layout_username.setError(mActivity.getResources().getString(R.string.invalid_email));
            focusview = etUsername;
            is_Emailvalid = false;
        } else {
            etUsername.setBackground(etUsername.getBackground().mutate());
            layout_username.setErrorEnabled(false);
            layout_username.setError(null);
            is_Emailvalid = true;
        }
        if (TextUtils.isEmpty(strPassword)) {
            layout_password.setHintEnabled(false);
            layout_password.setError(mActivity.getResources().getString(R.string.enter_password));
            focusview = etPassword;
            is_Passwordvalid = false;
        } else if (!AndroidAppUtils.checkPassword(etPassword.getText().toString().trim()) || !etPassword.getText().toString().matches("[a-zA-Z0-9!@#$%^&*()]*")) {
            layout_password.setHintEnabled(true);
            layout_password.setError(mActivity.getResources().getString(R.string.password_validation));
            focusview = etPassword;
            is_Passwordvalid = false;
        } else {
            etPassword.setBackground(etPassword.getBackground().mutate());
            layout_password.setErrorEnabled(false);
            layout_password.setError(null);
            is_Passwordvalid = true;
        }
        if (is_Passwordvalid && is_Emailvalid)
            return true;
        else
            return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        removeErrorField();
    }

    /***********************************
     * Function Name:removeErrorField
     * Description:  Remove error field
     ************************************/
    private void removeErrorField() {
        etUsername.setBackground(etUsername.getBackground().mutate());
        layout_username.setErrorEnabled(false);
        layout_username.setError(null);
        etUsername.clearFocus();
        layout_password.setErrorEnabled(false);
        etPassword.setBackground(etPassword.getBackground().mutate());
        layout_password.setError(null);
        etPassword.clearFocus();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.et_username:
                if (hasFocus) {
                    etPassword.clearFocus();
                    etUsername.setBackground(etUsername.getBackground().mutate());
                    layout_username.setErrorEnabled(false);
                    layout_username.setError(null);
                } else {
                    validateEmail();
                }
                break;

            case R.id.et_password:
                if (hasFocus) {
                    etUsername.clearFocus();
                    etPassword.setBackground(etPassword.getBackground().mutate());
                    layout_password.setErrorEnabled(false);
                    layout_password.setError(null);
                } else {
                    validatePassword();
                }
                break;
        }
    }

    /**
     * Function Name : validateEmail
     * Description : This function used for validating email
     *
     * @return
     */
    private boolean validateEmail() {
        String Email = etUsername.getText().toString().trim();
        if (Email.isEmpty()) {
            layout_username.setHintEnabled(true);
            layout_username.setError(mActivity.getResources().getString(R.string.login_valid_email));
//            requestFocus(login_username);
            return false;
        } else if (!AndroidAppUtils.checkEmail(Email)) {
            layout_username.setHintEnabled(true);
            layout_username.setError(mActivity.getResources().getString(R.string.invalid_email));
//            requestFocus(login_username);
        } else {
            etUsername.setBackground(etUsername.getBackground().mutate());
            layout_username.setError(null);
            layout_username.setErrorEnabled(false);
        }
        return true;
    }

    /**
     * Function Name : validatePassword
     * Description : This function used for validating password
     *
     * @return
     */
    private boolean validatePassword() {
        String strPassword = etPassword.getText().toString().trim();
        if (strPassword.isEmpty()) {
            layout_password.setHintEnabled(true);
            layout_password.setError(mActivity.getResources().getString(R.string.password_validation));
//            requestFocus(login_password);
            return false;
        } else if (!AndroidAppUtils.checkPassword(strPassword) || !strPassword.matches("[a-zA-Z0-9!@#$%^&*()]*")) {
            layout_password.setHintEnabled(true);
            layout_password.setError(mActivity.getResources().getString(R.string.password_validation));
//            requestFocus(login_password);
            return false;
        } else {
            etPassword.setBackground(etPassword.getBackground().mutate());
            layout_password.setError(null);
            layout_password.setErrorEnabled(false);
        }
        return true;
    }
}
