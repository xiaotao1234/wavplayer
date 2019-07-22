package com.example.wavplayer;

import android.app.Application;
import com.example.wavplayer.FloderManager.FileOsImpl;
import com.example.wavplayer.tools.PermissionManager;
import com.example.wavplayer.tools.TimeTools;

public class IAppliation extends Application {
    public static FileOsImpl fileOs;
    public static TimeTools timeTools;
    public static PermissionManager permissionManager;
    @Override
    public void onCreate() {
        super.onCreate();
        fileOs = FileOsImpl.getInstance();
        fileOs.getOsDicteoryPath(getApplicationContext());
        timeTools = TimeTools.getInstance();
        permissionManager = PermissionManager.getInastance();
    }
}
