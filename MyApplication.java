package com.example.acikocak;

import android.app.Application;
import com.google.firebase.FirebaseApp;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Firebase'i başlat
        FirebaseApp.initializeApp(this);
    }
}