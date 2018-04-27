package com.bikegroup.riders.view.webServices;

/**
 * Created by Geetika on 6/16/2016.
 */

import android.app.Activity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bikegroup.riders.view.appController.AppController;
import com.bikegroup.riders.view.iHelper.GlobalKeys;
import com.bikegroup.riders.view.iHelper.WebAPIResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



public class SignupApiHandler {
    /**
     * Instance object
     */
    private Activity mActivity;
    /**
     * Debug TAG
     */
    private String TAG = SignupApiHandler.class.getSimpleName();
    /**
     * API Response Listener
     */
    private WebAPIResponseListener mResponseListener;
    public JsonObjectRequest mjsonRequest;
    public String password, username, mobile, pet_name, addr, d_o_b, bgrp, sos_1, sos_2, mail;


    public SignupApiHandler(Activity mActivity, String name, String mob, String petname, String address, String dob, String bg, String sos1, String sos2, String email, String password, WebAPIResponseListener webAPIResponseListener) {
        this.mActivity = mActivity;

        this.username = name;
        this.mobile = mob;
        this.pet_name = petname;
        this.addr = address;
        this.password = password;
        this.d_o_b = dob;
        this.bgrp = bg;
        this.sos_1 = sos1;
        this.sos_2 = sos2;
        this.mail = email;
        this.password = password;
        this.mResponseListener = webAPIResponseListener;
        postAPICall();
    }

    /**
     * Making json object request
     */
    public void postAPICall() {
        JSONObject mJsonObject = new JSONObject();
        try {
            mJsonObject.put(GlobalKeys.USERNAME, username);
            mJsonObject.put(GlobalKeys.MOBILE_NUMBER, mobile);
            mJsonObject.put(GlobalKeys.NICK_NAME, password);
            mJsonObject.put(GlobalKeys.ADDRESS, addr);
            mJsonObject.put(GlobalKeys.DOB, d_o_b);
            mJsonObject.put(GlobalKeys.BLOOD_GROUP, bgrp);
            mJsonObject.put(GlobalKeys.SOS_NUMBER_1, sos_1);
            mJsonObject.put(GlobalKeys.SOS_NUMBER_2, sos_2);
            mJsonObject.put(GlobalKeys.EMAIL, mail);
            mJsonObject.put(GlobalKeys.PASSWORD, password);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        /**
         * json Request
         */
        String url = (GlobalKeys.URL + GlobalKeys.SIGNUP_URL).trim();
        Log.d(TAG, "url: " + url);

        mjsonRequest = new JsonObjectRequest(
                Method.POST, url, mJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d(TAG, " Response :" + response);
                        parseAPIResponse(response.toString());
//                        AndroidAppUtils.hideProgressDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                WebserviceAPIErrorHandler.getInstance().VolleyErrorHandler(error, mActivity);
//                AndroidAppUtils.hideProgressDialog();
            }
        }
        )


        {

            /*
             * (non-Javadoc)
             *
             * @see com.android.volley.Request#getHeaders()
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(GlobalKeys.HEADER_KEY_CONTENT_TYPE, GlobalKeys.HEADER_VALUE_CONTENT_TYPE);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(mjsonRequest, TAG);
        // set request time-out
        mjsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    /**
     * Parse Response
     *
     * @param response
     */
    protected void parseAPIResponse(String response) {
        boolean status = WebserviceAPISuccessFailManager.getInstance()
                .getResponseStatus(response);
        if (status) {
            /* Response Success */
            try {
                JSONObject mObject = new JSONObject(response);
                if (mObject != null) {
                   /* if (mObject.has(GlobalKeys.TOKEN)) {
                        token = mObject.getString(GlobalKeys.TOKEN);
                    }*/
                }
            } catch (JSONException e) {
                e.printStackTrace();
                mResponseListener.onFailOfResponse("");
            }
            mResponseListener.onSuccessOfResponse();

        } else {
            /* Response Status is false/null API Fail */

            String msg = "";
            try {
                JSONObject mObject = new JSONObject(response);
                if (mObject != null) {
                    if (mObject.has(GlobalKeys.RESPONSE_MESSAGE)) {
                        msg = mObject.getString(GlobalKeys.RESPONSE_MESSAGE);
                        Log.d(TAG, msg);
                        mResponseListener.onFailOfResponse(msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}


