package com.bikegroup.riders.view.appController;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/*******************************************************************************
 * AppController.java
 * Created on May 24, 2016
 * Copyright (c) 2014, DoorLock, Inc.
 * 350 East Plumeria, San Jose California, 95134, U.S.A.
 * All rights reserved.
 * This software is the confidential and proprietary information of
 * DoorLock, Inc. ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with DoorLock.
 *
 * @author VVDN
 *         Class Name: AppController
 *         Description: This class is used as a controller of the application.
 ******************************************************************************/
public class AppController extends Application {
    /**
     * Debuggable TAG
     */
    public static final String TAG = AppController.class
            .getSimpleName();
    /**
     * AppController reference object
     */
    public static AppController mInstance;
    /**
     * RequestQueue reference object
     */
    private RequestQueue mRequestQueue, serialRequestQueue;
    /**
     * ImageLoader reference object
     */
    private ImageLoader mImageLoader;
//    private AbstractHttpClient mHttpClient;
    int MAX_SERIAL_THREAD_POOL_SIZE = 1;
    final int MAX_CACHE_SIZE = 2 * 1024 * 1024; // 2 MB

    public static synchronized AppController getInstance() {

        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        Fabric.with(this, new Crashlytics());
        mInstance = this;
//        mHttpClient = new DefaultHttpClient();
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());//,new HurlStack(null, ClientSSLSocketFactory.getSocketFactory()));
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    /**
     * Use to fetch the serial request queue
     */
//    public RequestQueue getSerialRequestQueue() {
//        if (serialRequestQueue == null) {
//            serialRequestQueue = prepareSerialRequestQueue();
//            serialRequestQueue.start();
//        }
//        return serialRequestQueue;
//    }

//    private RequestQueue prepareSerialRequestQueue() {
//        Cache cache = new DiskBasedCache(getApplicationContext().getCacheDir(),
//                MAX_CACHE_SIZE);
//        Network network = getNetwork();
//        return new RequestQueue(cache, network, MAX_SERIAL_THREAD_POOL_SIZE);
//    }


//    private Network getNetwork() {
//        HttpStack stack;
//         String userAgent = "volley/0";
//         if (Build.VERSION.SDK_INT >= 9) {
//         stack = new HurlStack();
//         } else {
//        stack = new HttpClientStack(mHttpClient);
//         }
//
//        return new BasicNetwork(stack);
//    }

//    public <T> void addToArrayRequestQueue(Request<T> req, String tag) {
//        // set the default tag if tag is empty
//        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
//        getSerialRequestQueue().add(req);
//    }
}
