package com.bikegroup.riders.view.iHelper;

/**
 * Web API Response Helper
 * 
 */
public interface WebAPIResponseListener {
	/**
	 * On Success of API Call
	 * 
	 * @param arguments
	 */
	void onSuccessOfResponse(Object... arguments);

	/**
	 * on Fail of API Call
	 * 
	 * @param arguments
	 */
	void onFailOfResponse(Object... arguments);

}
