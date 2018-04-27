package com.bikegroup.riders.view.iHelper;

/**
 * Created by Geetika on 6/16/2016.
 */
public interface GlobalKeys {

    /**
     * Response keys
     */
    String RESPONSE_STATUS = "status";
    String RESPONSE_MESSAGE = "message";
    String RESPONSE_ERROR = "errors";
    String RESPONSE_DATA = "data";
    String RESPONSE_SUCCESS = "success";

    /**
     * Application API Keys
     */
    String OS_TYPE = "Android";
    String HEADER_KEY_AUTH_TOKEN = "AuthToken";
    String HEADER_KEY_CONTENT_TYPE = "Content-Type";
    String HEADER_VALUE_CONTENT_TYPE = "application/json";
    String HEADER_KEY_COOKIES = "Cookie";

    /*server url*/
    String URL = "http://13.232.35.140:8000/";
    String LOGIN_URL = "accounts/login";
    String SIGNUP_URL = "accounts/signup";


    /*Login keys*/
    String TOKEN = "token";
    String USERNAME = "username";
    String PASSWORD = "password";

    /*Sign-up keys*/
    String EMAIL = "email";
    String MOBILE_NUMBER = "mobile_number";
    String NICK_NAME = "nick_name";
    String ADDRESS = "address";
    String DOB = "dob";
    String BLOOD_GROUP = "blood_group";
    String SOS_NUMBER_1 = "sos_number_1";
    String SOS_NUMBER_2 = "sos_number_2";
    String IS_ACTIVE = "is_active";
}
