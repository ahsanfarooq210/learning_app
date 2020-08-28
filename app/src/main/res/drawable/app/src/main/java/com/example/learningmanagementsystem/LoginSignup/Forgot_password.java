package com.example.learningmanagementsystem.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.learningmanagementsystem.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_password extends AppCompatActivity
{
    //handler for the progress bar
    private ProgressBar progressBar;
    private Handler progressBarh = new Handler();
    private Runnable runnable1 = new Runnable()
    {
        @Override
        public void run()
        {

            progressBar.setVisibility(View.GONE);
        }
    };
    //email edit text
    private EditText email;
    private FirebaseAuth auth;
    private RelativeLayout upperLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //initializing the progresss bar
        progressBar=findViewById(R.id.forgot_progress_bar);
        email=findViewById(R.id.forgot_username_tf);
        auth= FirebaseAuth.getInstance();
        upperLayer=findViewById(R.id.forgot_upper_relative);
        progressBarh.postDelayed(runnable1,0);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void resetPassword(View view)
    {
        progressBar.setVisibility(View.VISIBLE);
        String emailstr=email.getText().toString().trim();
        if(emailstr.length()==0&&!(emailstr.contains("@"))&&!(emailstr.contains(".com")))
        {
            email.setError("Please enter valid email");
            return;
        }

        auth.sendPasswordResetEmail(emailstr).addOnSuccessListener(new OnSuccessListener<Void>()
        {
            @Override
            public void onSuccess(Void aVoid)
            {
                Snackbar.make(upperLayer,"Reset email successfully send. Check your mal", BaseTransientBottomBar.LENGTH_SHORT).show();
                progressBarh.postDelayed(runnable1,100);
                email.setText("");
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                progressBarh.postDelayed(runnable1,100);
                Snackbar.make(upperLayer,"Falied to send a reset email"+e.getMessage(),BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        progressBarh.postDelayed(runnable1,100);

    }
}