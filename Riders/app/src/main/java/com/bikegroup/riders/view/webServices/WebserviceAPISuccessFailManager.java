package com.bikegroup.riders.view.webServices;

import com.bikegroup.riders.view.iHelper.GlobalKeys;

import org.json.JSONException;
import org.json.JSONObject;



/**
 * Webservice API Response is Success OR Fail
 *
 * @author Geetika
 */
public class WebserviceAPISuccessFailManager {
    /**
     * Instance of This class
     */
    public static WebserviceAPISuccessFailManager mApiSuccessFailManager;
    /**
     * Debugging TAG
     */
    @SuppressWarnings("unused")
    private String TAG = WebserviceAPISuccessFailManager.class.getSimpleName();

    private WebserviceAPISuccessFailManager() {
    }

    /**
     * Get Instance of this class
     *
     * @return
     */
    public static WebserviceAPISuccessFailManager getInstance() {
        if (mApiSuccessFailManager == null)
            mApiSuccessFailManager = new WebserviceAPISuccessFailManager();
        return mApiSuccessFailManager;

    }

    /**
     * Get Api Response Status
     *
     * @param response
     * @return
     */
    public boolean getResponseStatus(String response) {
        if (response != null && !response.isEmpty()) {
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(response);
                if (jsonObject.getBoolean(GlobalKeys.RESPONSE_STATUS)) {
                    return true;
                } else {
                    return false;
                }
            } catch (JSONException e) {
                return false;
            }
        }
        return false;
    }

    /**
     * Get Api Response Message
     *
     * @param response
     * @return
     */
//    public String getReponseMessageCode(String response) {
//        String msg = "";
//        if (response != null && !response.isEmpty()) {
//            JSONObject jsonObject;
//            try {
//                jsonObject = new JSONObject(response);
//                msg = jsonObject.getString(GlobalKeys.RESPONSE_MESSAGE);
//            } catch (JSONException e) {
//                e.printStackTrace();
//
//            }
//        }
//        return msg;
//    }
    /**
     * Get Api Respose message
     */
//	public String getReponseMessage(String response) {
//		if (response != null && !response.isEmpty()) {
//			JSONObject jsonObject;
//			try {
//				jsonObject = new JSONObject(response);
//				int msg = jsonObject.getInt(GlobalKeys.RESPONSE_MESSAGE);
//				String msgText = APIResponseControl.RESPONSE_CODE.get(msg);
//				VVDNAndroidAppUtils.showLog(TAG, "" + msg+" "+msgText);
//				return msgText;
//			} catch (JSONException e) {
//				e.printStackTrace();
//				return null;
//			}
//		}
//		return null;
//	}
    /**
     * Get Api Response Data
     *
     * @param response
     * @return
     */
//	public JSONArray getReponseData(String response) {
//		if (response != null && !response.isEmpty()) {
//			JSONObject jsonObject;
//			try {
//				jsonObject = new JSONObject(response);
//				JSONArray data = jsonObject
//						.getJSONArray(GlobalKeys.RESPONSE_DATA);
//				return data;
//			} catch (JSONException e) {
//				e.printStackTrace();
//				return null;
//			}
//		}
//		return null;
//	}
}
