package com.jhonju.infinitysrv.app;

import android.app.Application;
import android.content.Context;

public class infinitysrvApp extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        infinitysrvApp.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return infinitysrvApp.context;
    }

}
