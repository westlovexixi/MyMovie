package com.example.mymovie;

import android.app.Application;

public final class UserInfo extends Application {
    private static String preID;

    public static String getPreID(){return preID;};

    public static void setPreID(String preID){UserInfo.preID = preID;}
}
