package com.example.learningapp.Dashboard;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.learningapp.Profile.Edit_profile;
import com.example.learningapp.Profile.Show_profile;
import com.example.learningapp.R;
import com.example.learningapp.UserAuthentication.MainActivity;
import com.example.learningapp.WebViewSupport.Show_history_activity;
import com.example.learningapp.WebViewSupport.Show_saved_pages;
import com.example.learningapp.learningMedium.select_language_fragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Main_dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    private TextView userEmail;
    private CircleImageView profileImage;

    private FirebaseUser user;
    private String emailll;
    private View navHeadderView;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private StorageReference storageReference;
    private FirebaseFirestore firestore;
    private int STORAGE_PERMISSION_CODE = 420;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);

        //setting up the tool bar on the top of our activity
        toolbar = findViewById(R.id.dashboard_toolbar);
        setSupportActionBar(toolbar);

        storageReference = FirebaseStorage.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();


        //setting up navigation drawer

        drawerLayout = findViewById(R.id.dashboard_drawer_layout);
        navigationView = findViewById(R.id.dashboard_navigation);
        navHeadderView = navigationView.getHeaderView(0);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav_drawer, R.string.close_nav_drawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //starting the dashboard fragment in the oncreate method
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
        {
            emailll = user.getEmail();
        }
        userEmail = navHeadderView.findViewById(R.id.nav_headder_user_email);
        profileImage = navHeadderView.findViewById(R.id.nav_hadder_profille_image);
        if (user != null)
        {
            userEmail.setText(user.getEmail());
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.dashboard_main_frame_layout, new select_language_fragment()).commit();

        checkPermissionGranted();

    }

    public void checkPermissionGranted()
    {
        if (!(ContextCompat.checkSelfPermission(Main_dashboard.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED))
        {
            requestPermission();
        }
    }

    public void requestPermission()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
        {
            new AlertDialog.Builder(this).setTitle("Permission required").setMessage("Permisson is necessary for the app operations")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            ActivityCompat.requestPermissions(Main_dashboard.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.dismiss();
                    finish();
                    System.exit(0);
                }
            }).create().show();
        } else
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == STORAGE_PERMISSION_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED)
            {
                checkPermissionGranted();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.nav_drawer_profile_edit:
                startActivity(new Intent(Main_dashboard.this, Edit_profile.class));
                break;

            case R.id.nav_drawer_profile:
                startActivity(new Intent(Main_dashboard.this, Show_profile.class));
                break;

            case R.id.nav_drawer_saved:
                startActivity(new Intent(Main_dashboard.this, Show_saved_pages.class));
                break;

            case R.id.nav_drawer_profile_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Main_dashboard.this, MainActivity.class));
                finish();
                break;

            case R.id.nav_drawer_history:
                startActivity(new Intent(this, Show_history_activity.class));
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

    @Override
    protected void onStart()
    {
        super.onStart();

        StorageReference downloadReference = storageReference.child("users/" + user.getUid() + "/profile.jpg");
        downloadReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri uri)
            {
                Picasso.get().load(uri).into(profileImage);
            }
        });

    }
}