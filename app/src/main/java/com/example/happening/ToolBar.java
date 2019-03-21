package com.example.happening;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.share.DeviceShareDialog;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ToolBar extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseAuth auth;
    FirebaseUser user;
    private static FragmentManager fragmentManager;
    ShareDialog shareDialog = new ShareDialog(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_bar);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        fragmentManager = getSupportFragmentManager();

        if (findViewById(R.id.fragment_holder) != null) {
            if (savedInstanceState != null) {
                return;
            }

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            MainHappenings mainHappenings = new MainHappenings();
            fragmentTransaction.add(R.id.fragment_holder, mainHappenings, null);
            fragmentTransaction.commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView nv = (NavigationView) findViewById(R.id.nav_view);
        View view = nv.getHeaderView(0);
        TextView nameTextView = (TextView) view.findViewById(R.id.nameTextView);
        nameTextView.setText(user.getEmail());


        getSupportActionBar().setTitle("Happenings");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tool_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_createhappenings) {
            if (findViewById(R.id.fragment_holder) != null) {

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                CreateHappening createHappening = new CreateHappening();
                fragmentTransaction.replace(R.id.fragment_holder, createHappening, null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

        } else if (id == R.id.nav_calendar) {
            if (findViewById(R.id.fragment_holder) != null) {

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                MainHappenings mainHappenings = new MainHappenings();
                fragmentTransaction.replace(R.id.fragment_holder, mainHappenings, null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        } else if (id == R.id.nav_myhappenings) {
            if (findViewById(R.id.fragment_holder) != null) {

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                MyHappenings myHappenings = new MyHappenings();
                fragmentTransaction.replace(R.id.fragment_holder, myHappenings, null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        } else if (id == R.id.nav_happenings) {
            if (findViewById(R.id.fragment_holder) != null) {

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                MainHappenings mainHappenings = new MainHappenings();
                fragmentTransaction.replace(R.id.fragment_holder, mainHappenings, null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        } else if (id == R.id.nav_settings) {
            if (findViewById(R.id.fragment_holder) != null) {

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                SettingsFragment settingsFragment = new SettingsFragment();
                fragmentTransaction.replace(R.id.fragment_holder, settingsFragment, null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        } else if (id == R.id.nav_share) {

            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            if (accessToken != null) {

                if (ShareDialog.canShow(ShareLinkContent.class)) {

                    ShareLinkContent content = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse("https://developers.facebook.com"))
                            .build();
                    shareDialog.show(content);
                }
                } else {
                Toast.makeText(this, "Must be logged in with Facebook! Or ass crack", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.nav_send) {
            if (findViewById(R.id.fragment_holder) != null) {

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                MainHappenings mainHappenings = new MainHappenings();
                fragmentTransaction.replace(R.id.fragment_holder, mainHappenings, null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            updateUI();
        }
    }

    private void updateUI() {

        Toast.makeText(ToolBar.this, "You're logged out ", Toast.LENGTH_LONG).show();
        Intent myIntent = new Intent(ToolBar.this, MainActivity.class);
        startActivityForResult(myIntent, 0);
    }
}
