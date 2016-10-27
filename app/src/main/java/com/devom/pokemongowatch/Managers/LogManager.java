package com.devom.pokemongowatch.Managers;

import android.content.Context;
import android.os.FileObserver;
import android.util.Log;

import com.devom.pokemongowatch.Fragments.Listeners.OnFileContentChangeListener;
import com.devom.pokemongowatch.Other.FileContentObserver;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Maikel on 09/07/2016.
 */
public class LogManager {

    private static final String TAG = LogManager.class.getSimpleName();

    //<editor-fold desc="Constants">
    private static File logFile;
    private static FileObserver fileObserver;
    private static final SimpleDateFormat LOG_SDF = new SimpleDateFormat("HH:mm:ss");
    private static final SimpleDateFormat FILE_SDF = new SimpleDateFormat("dd_mm_yyyy");
    //</editor-fold>

    //<editor-fold desc="Methods">
    public static void log(Context context, String message) {
        //TODO delete file (content) if 24h past or on 00:00
        createFileIfNotExist(context);
        String log = String.format("[%s]: %s\n", getCurrentTimeStamp(LOG_SDF), message);
        try {
            Files.append(log, logFile, Charsets.UTF_8);
        } catch (IOException e) {
            Log.e(TAG, "Failed to write log file");
            e.printStackTrace();
        }
    }

    public static String readLog(Context context) {
        createFileIfNotExist(context);
        return read(logFile);
    }

    public static String read(File file) {
        try {
            return Files.toString(file, Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return "Something went wrong during read ...";
        }
    }

    public static void clear() {
        if (logFile != null) {
            try {
                Files.write("", logFile, Charsets.UTF_8);
            } catch (IOException e) {
                logFile.delete();
                logFile = null;
            }
        }
    }

    public static void registerFileContentObserver(OnFileContentChangeListener onFileContentChangeListener) {
        if (logFile != null) {
            fileObserver = new FileContentObserver(logFile, onFileContentChangeListener);
            fileObserver.startWatching();
        }
    }

    public static void unregisterFileContentObserver(){
        if(fileObserver != null){
            fileObserver.stopWatching();
        }
    }
    //</editor-fold>

    //<editor-fold desc="Helpers">
    private static void createFileIfNotExist(Context context) {
        if (logFile == null) {
            logFile = new File(context.getFilesDir(), "log.txt");
        }
    }

    private static String getCurrentTimeStamp(SimpleDateFormat simpleDateFormat) {
        return simpleDateFormat.format(new Date(System.currentTimeMillis()));
    }
    //</editor-fold>
}