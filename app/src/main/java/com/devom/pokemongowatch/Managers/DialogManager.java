package com.devom.pokemongowatch.Managers;

import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by Maikel on 09/19/2016.
 */
public class DialogManager {

    private static final String TAG = DialogManager.class.getSimpleName();

    public static void showNoNetworkDialog(Context context) {
        new MaterialDialog.Builder(context)
                .title("Network Unavailable")
                .titleGravity(GravityEnum.CENTER)
                .content("No internet connectivity")
                .contentGravity(GravityEnum.CENTER)
                .show();
    }

    public static void showServiceNoLocationDialog(Context context) {
        new MaterialDialog.Builder(context)
                .title("Service Unavailable")
                .titleGravity(GravityEnum.CENTER)
                .content("No GPS location")
                .contentGravity(GravityEnum.CENTER)
                .show();
    }

    public static void showServiceNoNetworkDialog(Context context) {
        new MaterialDialog.Builder(context)
                .title("Service Unavailable")
                .titleGravity(GravityEnum.CENTER)
                .content("No internet connectivity")
                .contentGravity(GravityEnum.CENTER)
                .show();
    }

    public static void showServiceRestartDialog(Context context, MaterialDialog.SingleButtonCallback actionCallback) {
        new MaterialDialog.Builder(context)
                .title("Service Restart")
                .content("This options requires a service restart. Would you like to restart it now?")
                .positiveText("Yes")
                .onPositive(actionCallback)
                .canceledOnTouchOutside(false)
                .negativeText("No")
                .show();
    }
}
