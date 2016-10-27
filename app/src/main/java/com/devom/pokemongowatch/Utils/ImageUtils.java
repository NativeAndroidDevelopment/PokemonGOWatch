package com.devom.pokemongowatch.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Maikel on 08/16/2016.
 */
public class ImageUtils {

    private static final String TAG = ImageUtils.class.getSimpleName();

    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getAssets().open(filePath));
        } catch (IOException e) {
            Log.i(TAG, "Failed to get image from asset");
        }
        return bitmap;
    }

    public static Bitmap getPokemonSprite(Context context, int number){
        return getBitmapFromAsset(context, String.format("generation 1/pokemon_%03d.png", number));
    }

    public static int getResourceByName(Context context, String name){
        return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
    }
}
