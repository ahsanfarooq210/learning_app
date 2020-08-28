package com.example.learningmanagementsystem.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;


import com.example.learningmanagementsystem.Fragments.NavigationDrawer.Edit_profile_fragment;
import com.example.learningmanagementsystem.Fragments.dashboard_fragment;
import com.example.learningmanagementsystem.R;
import com.google.android.material.navigation.NavigationView;

public class Main_dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);

        //setting up the tool bar on the top of our activity
        toolbar=findViewById(R.id.dashboard_toolbar);
        setSupportActionBar(toolbar);

        //setting up navigation drawer
        drawerLayout=findViewById(R.id.dashboard_drawer_layout);
        navigationView=findViewById(R.id.dashboard_navigation);
        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_nav_drawer,R.string.close_nav_drawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        //starting the dashboard fragment in the oncreate method
        getSupportFragmentManager().beginTransaction().replace(R.id.main_dashboard_frame_layout,new dashboard_fragment()).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.dashbard_nav_edit_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_dashboard_frame_layout,new Edit_profile_fragment()).addToBackStack("").commit();
                break;
        }
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture)
    {

    }
}