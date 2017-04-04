package com.example.omarchh.pruebafirebase;

import android.app.Application;
import android.support.v7.widget.LinearLayoutCompat;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
/**
 * Created by OMAR CHH on 03/04/2017.
 */

public class PruebaFirebaseApp extends Application {

    @Override
    public void onCreate(){

        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }
}
