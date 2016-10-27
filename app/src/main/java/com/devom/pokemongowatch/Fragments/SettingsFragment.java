package com.devom.pokemongowatch.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.devom.pokemongowatch.Managers.BackgroundManager;
import com.devom.pokemongowatch.Managers.DialogManager;
import com.devom.pokemongowatch.Managers.ServiceManager;
import com.devom.pokemongowatch.Managers.SettingsManager;
import com.devom.pokemongowatch.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends BaseFragment {

    private static final String TAG = SettingsFragment.class.getSimpleName();

    //<editor-fold desc="Constants">
    private static final int RINGTONE_SELECT_REQUEST_CODE = 111;
    //</editor-fold>

    //<editor-fold desc="Variables">
    private Context context;
    private SettingsManager settingsManager;
    private Intent ringtonePicker;
    //</editor-fold>

    //<editor-fold desc="Views">
    private RelativeLayout ringtoneLayout;
    private TextView ringtoneSummary;
    private Switch vibrateSwitch;
    private Switch filterSwitch;
    private Switch fastIntervalSwitch;
    //</editor-fold>

    //<editor-fold desc="Overrides">
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();
        settingsManager = SettingsManager.getInstance(context);
        buildRingtonePicker();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        BackgroundManager.getInstance().setBackground(R.drawable.background_7);

        ringtoneLayout = (RelativeLayout) view.findViewById(R.id.fragment_settings_ringtone_layout);
        ringtoneSummary = (TextView) view.findViewById(R.id.fragment_settings_ringtone_summary);
        vibrateSwitch = (Switch) view.findViewById(R.id.fragment_settings_vibrate_switch);
        filterSwitch = (Switch) view.findViewById(R.id.fragment_settings_filter_switch);
        fastIntervalSwitch = (Switch) view.findViewById(R.id.fragment_settings_fast_interval_switch);

        setStoredSettings();

        ringtoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //NOTE: This variable is needed, do not insert the return value of getRingtone() directly into putExtra(String, Parcelable)
                Uri current = settingsManager.getRingtone();
                Log.d(TAG, "Current uri: " + current);
                ringtonePicker.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, TextUtils.isEmpty(current.toString()) ? null : current);
                startActivityForResult(ringtonePicker, RINGTONE_SELECT_REQUEST_CODE);
            }
        });
        vibrateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                settingsManager.setVibrate(b);
            }
        });
        filterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                settingsManager.setFilter(b);
            }
        });
        fastIntervalSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                settingsManager.setFastInterval(b);
                if (ServiceManager.getInstance(context).isRunning(context)) {
                    DialogManager.showServiceRestartDialog(context, new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            ServiceManager.getInstance(context).restartService(context);
                        }
                    });
                }
            }
        });

        return view;
    }

    @Override
    public int getMenuItemId() {
        return R.id.nav_settings;
    }
    //</editor-fold>

    //<editor-fold desc="Helpers">
    private void buildRingtonePicker() {
        ringtonePicker = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        ringtonePicker.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select a ringtone");
        ringtonePicker.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
        ringtonePicker.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true);
        ringtonePicker.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        ringtonePicker.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
    }

    private void setStoredSettings() {
        updateRingtone(settingsManager.getRingtone());
        vibrateSwitch.setChecked(settingsManager.isVibrate());
        filterSwitch.setChecked(settingsManager.isFilter());
        fastIntervalSwitch.setChecked(settingsManager.isFastInterval());
    }

    private void updateRingtone(Uri uri) {
        Log.d(TAG, "Got uri: " + uri);
        if (uri == null) {
            settingsManager.setRingtone("");
            ringtoneSummary.setText("Silent");
        } else {
            settingsManager.setRingtone(uri.toString());
            Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
            ringtoneSummary.setText(ringtone.getTitle(context));
        }
    }
    //</editor-fold>

    //<editor-fold desc="Callbacks">
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RINGTONE_SELECT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            updateRingtone(uri);
        }
    }
    //</editor-fold>
}
