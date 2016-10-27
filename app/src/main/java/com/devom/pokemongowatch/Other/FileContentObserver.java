package com.devom.pokemongowatch.Other;

import android.os.FileObserver;
import android.util.Log;

import com.devom.pokemongowatch.Fragments.Listeners.OnFileContentChangeListener;
import com.devom.pokemongowatch.Managers.LogManager;

import java.io.File;

/**
 * Created by Maikel on 09/19/2016.
 */
public class FileContentObserver extends FileObserver {

    private static final String TAG = FileContentObserver.class.getSimpleName();

    private File file;
    private OnFileContentChangeListener onFileContentChangeListener;

    public FileContentObserver(File file, OnFileContentChangeListener onFileContentChangeListener) {
        super(file.getPath(), MODIFY);
        this.file = file;
        this.onFileContentChangeListener = onFileContentChangeListener;
    }

    @Override
    public void onEvent(int i, String s) {
        if (i == MODIFY && onFileContentChangeListener != null) {
            onFileContentChangeListener.onFileContentChange(LogManager.read(file));
        }
    }
}
