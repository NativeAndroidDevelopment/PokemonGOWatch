package com.devom.pokemongowatch.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.devom.pokemongowatch.Fragments.Listeners.OnFileContentChangeListener;
import com.devom.pokemongowatch.Managers.LogManager;
import com.devom.pokemongowatch.R;

public class LogFragment extends BaseFragment implements OnFileContentChangeListener {

    //<editor-fold desc="Variables">
    private Context context;
    //</editor-fold>

    //<editor-fold desc="Views">
    private TextView content;
    private Button clearButton;
    //</editor-fold>

    //<editor-fold desc="Overrides">
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log, container, false);

        content = (TextView) view.findViewById(R.id.fragment_log_content);
        clearButton = (Button) view.findViewById(R.id.fragment_log_clear);

        content.setMovementMethod(new ScrollingMovementMethod());
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content.setText("");
                LogManager.clear();
            }
        });

        LogManager.registerFileContentObserver(this);
        showVisibleText(LogManager.readLog(context));

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogManager.unregisterFileContentObserver();
    }
    //</editor-fold>

    //<editor-fold desc="Helpers">
    private void showVisibleText(String text) {
        content.setText(text);
        Layout layout = content.getLayout();
        if (layout != null) {
            final int scrollAmount = content.getLayout().getLineTop(content.getLineCount()) - content.getHeight();
            // if there is no need to scroll, scrollAmount will be <=0
            if (scrollAmount > 0)
                content.scrollTo(0, scrollAmount);
            else
                content.scrollTo(0, 0);
        }
    }
    //</editor-fold>

    //<editor-fold desc="Interface Implementation">
    @Override
    public int getMenuItemId() {
        return R.id.nav_log;
    }

    @Override
    public void onFileContentChange(final String text) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showVisibleText(text);
            }
        });
    }
    //</editor-fold>
}
