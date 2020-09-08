package com.example.learningapp.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.example.learningapp.Profile.Edit_profile;
import com.example.learningapp.R;
import com.example.learningapp.learningMedium.select_language_fragment;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class Main_dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private DrawerLayout drawerLayout;
    private TextView userEmail;
    private CircleImageView profileImage;

    private FirebaseUser user;
    private String emailll;
    private View navHeadderView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);

        Toolbar toolbar=findViewById(R.id.nav_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout=findViewById(R.id.main_dashboard_drawer_layout);
        NavigationView navigationView=findViewById(R.id.dashboard_nav_view);


        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navHeadderView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        user= FirebaseAuth.getInstance().getCurrentUser();
        emailll=user.getEmail();
        userEmail=navHeadderView.findViewById(R.id.nav_headder_user_email);
        userEmail.setText(user.getEmail());
        getSupportFragmentManager().beginTransaction().replace(R.id.dashboard_main_frame_layout,new select_language_fragment()).commit();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.nav_drawer_profile_edit:
                startActivity(new Intent(Main_dashboard.this, Edit_profile.class));
                break;

        }
        return true;
    }

    @Override
    public void onBackPressed()
    {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }
}