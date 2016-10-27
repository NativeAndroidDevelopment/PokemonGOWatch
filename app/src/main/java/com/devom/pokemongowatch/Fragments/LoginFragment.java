package com.devom.pokemongowatch.Fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.devom.pokemongowatch.Managers.BackgroundManager;
import com.devom.pokemongowatch.Managers.DialogManager;
import com.devom.pokemongowatch.Managers.LoginManager;
import com.devom.pokemongowatch.Managers.NetworkManager;
import com.devom.pokemongowatch.R;
import com.pokegoapi.auth.GoogleUserCredentialProvider;

public class LoginFragment extends HomeFragment {

    private static final String TAG = LoginFragment.class.getSimpleName();

    //<editor-fold desc="Variables">
    private Context context;
    private NetworkManager networkManager;
    private LoginManager loginManager;
    //</editor-fold>

    //<editor-fold desc="Views">
    private EditText tokenInputField;
    private ImageButton tokenPasteButton;
    private TextView tokenErrorText;
    private CheckBox persistSignInCheckbox;
    private Button signInButton;
    private TextView tokenRequestText;
    //</editor-fold>

    //<editor-fold desc="Overrides">
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

        networkManager = NetworkManager.getInstance(context);
        loginManager = LoginManager.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        BackgroundManager.getInstance().setBackground(R.drawable.background_1);

        tokenInputField = (EditText) view.findViewById(R.id.fragment_login_token_input_field);
        tokenPasteButton = (ImageButton) view.findViewById(R.id.fragment_login_token_paste_button);
        tokenErrorText = (TextView) view.findViewById(R.id.fragment_login_token_error_text);
        persistSignInCheckbox = (CheckBox) view.findViewById(R.id.fragment_login_persist_sign_in_checkbox);
        signInButton = (Button) view.findViewById(R.id.fragment_login_sign_in_button);
        tokenRequestText = (TextView) view.findViewById(R.id.fragment_login_token_request_text);

        tokenPasteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData.Item item = clipboardManager.getPrimaryClip().getItemAt(0);
                if (item != null) {
                    tokenInputField.setText(item.getText().toString());
                }
            }
        });
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasInternetAccess()) {
                    String token = tokenInputField.getText().toString();
                    if (!TextUtils.isEmpty(token)) {
                        loginManager.login(context, token, persistSignInCheckbox.isChecked());
                    } else {
                        tokenErrorText.setVisibility(View.VISIBLE);
                        tokenInputField.getBackground().setColorFilter(getResources().getColor(R.color.error, null), PorterDuff.Mode.SRC_ATOP);
                    }
                }
            }
        });
        tokenRequestText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasInternetAccess()) {
                    startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(GoogleUserCredentialProvider.LOGIN_URL)));
                }
            }
        });

        return view;
    }
    //</editor-fold>

    //<editor-fold desc="Helpers">
    private boolean hasInternetAccess() {
        if (networkManager.isConnected()) {
            return true;
        }
        DialogManager.showNoNetworkDialog(context);
        return false;
    }
    //</editor-fold>
}
