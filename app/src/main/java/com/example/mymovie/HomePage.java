package com.example.mymovie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mymovie.fragment.MainPage;
import com.example.mymovie.fragment.Map;
import com.example.mymovie.fragment.Memoir;
import com.example.mymovie.fragment.MovieSearch;
import com.example.mymovie.fragment.Reports;
import com.example.mymovie.fragment.WatchList;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private TextView textView;
    private String fname, id;
    private Map mapFragment = new Map();
    private Reports reportsFragment = new Reports();
    private Bundle bundle = new Bundle();


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id1 = item.getItemId();
        switch (id1) {
            case R.id.it_home:
                replaceFragment(new MainPage());
                break;
            case R.id.it_movie_search:
                replaceFragment(new MovieSearch());
                break;
            case R.id.it_movie_memoir:
                replaceFragment(new Memoir());
                break;
            case R.id.it_watchlist:
                replaceFragment(new WatchList());
                break;
            case R.id.it_report:
                reportsFragment.setArguments(bundle);
                replaceFragment(reportsFragment);
                break;
            case R.id.it_map:
                mapFragment.setArguments(bundle);
                replaceFragment(mapFragment);
                break;
        }
        //this code closes the drawer after you selected an item from the menu, otherwise stay open
        drawerLayout.closeDrawer(GravityCompat.START); return true; }

    private void replaceFragment(Fragment nextFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, nextFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        //adding the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView = findViewById(R.id.welcome);

        Intent intent = getIntent();
        fname = intent.getStringExtra("fname");
        id = intent.getStringExtra("id");
        bundle.putString("id", id);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        textView.setText("Welcome, " + fname + " ! " + "\n" + "Today is " + simpleDateFormat.format(date));
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nv);
        toggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.Open,R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        SharedPreferences settings = getSharedPreferences("setting", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("pid", id);
        editor.commit();

        //these two lines of code show the navicon drawer icon top left hand side
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(new MainPage());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
}
