package com.example.learningapp.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.example.learningapp.R;
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
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navHeadderView=navigationView.getHeaderView(0);

        user= FirebaseAuth.getInstance().getCurrentUser();
        emailll=user.getEmail();
        userEmail=navHeadderView.findViewById(R.id.nav_headder_user_email);
        userEmail.setText(user.getEmail());
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            
        }


        return true;
    }
}