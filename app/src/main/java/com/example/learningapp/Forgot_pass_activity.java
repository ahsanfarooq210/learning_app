package com.example.learningapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_pass_activity extends AppCompatActivity
{
    private ProgressBar progressBar;
    private RelativeLayout rally2,upperlayer;
    private Handler handler;
    private ScrollView scrollView;
    private Runnable runnable=new Runnable()
    {
        @Override
        public void run()
        {
            scrollView.setVisibility(View.VISIBLE);
            rally2.setVisibility(View.VISIBLE);
        }
    };

    private EditText username_et;
    private FirebaseAuth mAuth;
    Runnable prunnable=new Runnable()
    {
        @Override
        public void run()
        {
            progressBar.setVisibility(View.GONE);
        }
    };
    private Handler phandler;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_activity);

        username_et=findViewById(R.id.forgot_username);

        //initializing the relative layouts for the splash screen
        scrollView=findViewById(R.id.forgot_rellay1);
        rally2=findViewById(R.id.forgot_rellay2);
        //fort the snack bar
        upperlayer=findViewById(R.id.forgot_upper_layer);

        //splash screen timeout 1.5 seconds
        handler=new Handler();
        handler.postDelayed(runnable,1500);

        //progress bar
        progressBar=findViewById(R.id.forgot_progress_bar);
        phandler=new Handler();
        phandler.postDelayed(prunnable,0);

        mAuth=FirebaseAuth.getInstance();


    }

    public void send(View view)
    {
        progressBar.setVisibility(View.VISIBLE);

        String emailStr=username_et.getText().toString().trim();

        if(emailStr.length()==0&&!(emailStr.contains("@"))&&!(emailStr.contains(".com")))
        {
            username_et.setError("Please enter valid email");
            return;
        }

        mAuth.sendPasswordResetEmail(emailStr).addOnSuccessListener(new OnSuccessListener<Void>()
        {
            @Override
            public void onSuccess(Void aVoid)
            {
                Snackbar.make(upperlayer,"Reset email successfully send. Check your mal", BaseTransientBottomBar.LENGTH_SHORT).show();
                phandler.postDelayed(prunnable,100);
                username_et.setText("");
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                phandler.postDelayed(prunnable,100);
                Snackbar.make(upperlayer,"Falied to send a reset email"+e.getMessage(),BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        phandler.postDelayed(prunnable,100);
    }
}