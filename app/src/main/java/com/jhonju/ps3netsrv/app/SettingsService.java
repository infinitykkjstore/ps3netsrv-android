package com.jhonju.infinitysrv.app;

import android.content.SharedPreferences;
import android.os.Environment;

import com.jhonju.infinitysrv.R;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class SettingsService {
    private static final String settings = "settings";
    private static SharedPreferences spPort = infinitysrvApp.getAppContext().getSharedPreferences("PORT",0);
    private static SharedPreferences spFolder = infinitysrvApp.getAppContext().getSharedPreferences("FOLDER",0);
    private static SharedPreferences spIps = infinitysrvApp.getAppContext().getSharedPreferences("IPS",0);
    private static SharedPreferences spListType = infinitysrvApp.getAppContext().getSharedPreferences("LIST_TYPE",0);
    private static SharedPreferences spMaxConnections = infinitysrvApp.getAppContext().getSharedPreferences("MAX_CONNECTIONS",0);
    private static SharedPreferences spReadOnly = infinitysrvApp.getAppContext().getSharedPreferences("READ_ONLY",0);

    public static int getPort() { return spPort.getInt(settings, infinitysrvApp.getAppContext().getResources().getInteger(R.integer.defaultPort)); }

    public static Set<String> getIps() { return spIps.getStringSet(settings, new HashSet<String>()); }

    public static String getFolder() {
        return spFolder.getString(settings, getDefaultFolder());
    }

    public static int getListType() { return spListType.getInt(settings, 0); }

    public static int getMaxConnections() { return spMaxConnections.getInt(settings, 0); }

    public static boolean isReadOnly() { return spReadOnly.getBoolean(settings, false); }

    private static String getDefaultFolder() {
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)) {
            File baseDirFile = infinitysrvApp.getAppContext().getExternalFilesDir(null);
            if(baseDirFile == null) {
                return infinitysrvApp.getAppContext().getFilesDir().getAbsolutePath();
            } else {
                return baseDirFile.getAbsolutePath();
            }
        } else {
            return infinitysrvApp.getAppContext().getFilesDir().getAbsolutePath();
        }
    }

    public static void setPort(int port) {
        SharedPreferences.Editor editor = spPort.edit();
        editor.putInt(settings, port);
        editor.apply();
    }

    public static void setFolder(String folder) {
        SharedPreferences.Editor editor = spFolder.edit();
        editor.putString(settings, folder);
        editor.apply();
    }

    public static void setIps(Set<String> ips) {
        SharedPreferences.Editor editor = spIps.edit();
        editor.putStringSet(settings, ips);
        editor.apply();
    }

    public static void setListType(int listType) {
        SharedPreferences.Editor editor = spListType.edit();
        editor.putInt(settings, listType);
        editor.apply();
    }

    public static void setMaxConnections(int maxConnections) {
        SharedPreferences.Editor editor = spMaxConnections.edit();
        editor.putInt(settings, maxConnections);
        editor.apply();
    }

    public static void setReadOnly(boolean readOnly) {
        SharedPreferences.Editor editor = spReadOnly.edit();
        editor.putBoolean(settings, readOnly);
        editor.apply();
    }
}
