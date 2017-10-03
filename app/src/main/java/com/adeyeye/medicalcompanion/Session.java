package com.adeyeye.medicalcompanion;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ADEYEYE on 9/6/2017.
 */

public class Session {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;

    public Session(Context ctx){
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences("Medic Companion", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }
    public void setLoggedIn(boolean loggedIn){
        editor.putBoolean("loggedInmode", loggedIn);
        editor.commit();
    }
    public boolean loggedIn(){

        return prefs.getBoolean("loggedInmode", false);
    }

}





















