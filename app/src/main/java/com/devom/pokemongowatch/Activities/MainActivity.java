package com.devom.pokemongowatch.Activities;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.devom.pokemongowatch.Fragments.BaseFragment;
import com.devom.pokemongowatch.Fragments.ErrorFragment;
import com.devom.pokemongowatch.Fragments.FilterFragment;
import com.devom.pokemongowatch.Fragments.Listeners.OnBackgroundRequestListener;
import com.devom.pokemongowatch.Fragments.LoadingFragment;
import com.devom.pokemongowatch.Fragments.LogFragment;
import com.devom.pokemongowatch.Fragments.LoginFragment;
import com.devom.pokemongowatch.Fragments.ServiceFragment;
import com.devom.pokemongowatch.Fragments.SettingsFragment;
import com.devom.pokemongowatch.Managers.BackgroundManager;
import com.devom.pokemongowatch.Managers.Listeners.OnAuthenticationListener;
import com.devom.pokemongowatch.Managers.LoginManager;
import com.devom.pokemongowatch.Managers.SettingsManager;
import com.devom.pokemongowatch.Constants.AppConstants;
import com.devom.pokemongowatch.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnAuthenticationListener, OnBackgroundRequestListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    //<editor-fold desc="Variables">
    private FragmentManager fragmentManager;
    private DrawerLayout navigationDrawer;
    private NavigationView navigationView;
    //</editor-fold>

    //<editor-fold desc="Views">
    private ImageView background;
    //</editor-fold>

    //<editor-fold desc="Create">
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        askLocationPermission();

        fragmentManager = getSupportFragmentManager();

        setupToolbar();
        setupListeners();

        displayHome();
    }
    //</editor-fold>

    //<editor-fold desc="Setup">
    private void askLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, AppConstants.LOCATION_PERMISSION_REQUEST_CODE);
    }


    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.navigation_drawer_view);
        navigationDrawer = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, navigationDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        navigationDrawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setupListeners() {
        //Navigation Listener
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_drawer_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Authentication Listener
        LoginManager.getInstance().setOnAuthenticationListener(this);

        //Background Listener
        background = (ImageView) findViewById(R.id.fragment_container_background);
        BackgroundManager.getInstance().setOnBackgroundRequestListener(this);
    }
    //</editor-fold>

    //<editor-fold desc="Fragments">
    private void switchFragment(BaseFragment fragment, boolean backstack) {
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (currentFragment != null && currentFragment.getClass().getSimpleName().equals(fragment.getClass().getSimpleName()))
            return;

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        if (backstack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();

        //Set the corresponding menu item
        if (navigationView != null) {
            navigationView.setCheckedItem(fragment.getMenuItemId());
        }
    }

    private void switchFragment(BaseFragment fragment) {
        switchFragment(fragment, true);
    }

    private void displayHome() {
        while (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
        }
        //If already authenticated or can be authenticated without user interaction, show service display
        boolean showService = LoginManager.getInstance().isAuthenticated() || SettingsManager.getInstance(this).hasRefreshToken();
        switchFragment(showService ? new ServiceFragment() : new LoginFragment(), false);
    }

    private void displayPrevious() {
        fragmentManager.popBackStackImmediate();
        BaseFragment currentFragment = (BaseFragment) fragmentManager.findFragmentById(R.id.fragment_container);
        navigationView.setCheckedItem(currentFragment.getMenuItemId());
    }
    //</editor-fold>

    //<editor-fold desc="Overrides">
    @Override
    public void onBackPressed() {
        if (navigationDrawer.isDrawerOpen(GravityCompat.START)) {
            navigationDrawer.closeDrawer(GravityCompat.START);
        } else if (fragmentManager.getBackStackEntryCount() > 0) {
            displayPrevious();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == AppConstants.LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        finish();
                    }
                    //All permissions were granted
                }
            } else {
                finish();
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="Interface Implementation">
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (!item.isChecked()) {
            switch (item.getItemId()) {
                case R.id.nav_home: {
                    displayHome();
                    break;
                }
                case R.id.nav_settings: {
                    switchFragment(new SettingsFragment());
                    break;
                }
                case R.id.nav_filter: {
                    switchFragment(new FilterFragment());
                    break;
                }
                case R.id.nav_log: {
                    switchFragment(new LogFragment());
                    break;
                }
            }
        }
        navigationDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    @Override
//    public void onFragmentChangeRequest(Class<? extends BaseFragment> fragmentClass) {
//        try {
//            switchFragment(fragmentClass.newInstance(), false);
//        } catch (java.lang.InstantiationException | IllegalAccessException e) {
//            Log.d(TAG, "Unable to create new instance of " + fragmentClass.getSimpleName());
//        }
//    }

    @Override
    public void onAuthenticationStart() {
        switchFragment(new LoadingFragment(), false);
    }

    @Override
    public void onAuthenticationComplete(boolean isAuthenticated) {
        switchFragment(isAuthenticated ? new ServiceFragment() : new ErrorFragment(), false);
    }

    @Override
    public void onAuthenticationRetryRequest() {
        if (SettingsManager.getInstance(this).hasRefreshToken()) {
            LoginManager.getInstance().login(this);
        } else {
            switchFragment(new LoginFragment(), false);
        }
    }

    @Override
    public void onBackgroundRequest(int resId) {
        if (background != null) {
            background.setImageResource(resId);
        }
    }
    //</editor-fold>
}
