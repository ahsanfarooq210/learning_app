package com.example.learningapp.UserAuthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.example.learningapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signup_activity extends AppCompatActivity
{

    private RelativeLayout relativeLayout,upperlayout;
    private ScrollView scrollView;
    private Handler handler;
    private Runnable runnable=new Runnable()
    {
        @Override
        public void run()
        {
            scrollView.setVisibility(View.VISIBLE);
        }
    };
    private ProgressBar progressBar;
    private Handler pHandler;
    private Runnable progressRunnable=new Runnable()
    {
        @Override
        public void run()
        {
                progressBar.setVisibility(View.GONE);
        }
    };

    private EditText username_et,password_et,cnfrm_pass_et;
    private FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_activity);

        //for the snack bar
        upperlayout=findViewById(R.id.signup_upper_layout);
        relativeLayout=findViewById(R.id.signup_rellay2);
        scrollView=findViewById(R.id.signup_rellay1);

        //progress bar
        progressBar=findViewById(R.id.signup_progress_bar);
        pHandler=new Handler();
        pHandler.postDelayed(progressRunnable,0);


        //timeout for the splash screen is 1.5 seconds
        handler=new Handler();
        handler.postDelayed(runnable,1500);

        //initializing the edit texts
        username_et=findViewById(R.id.signup_username);
        password_et=findViewById(R.id.signup_password);
        cnfrm_pass_et=findViewById(R.id.signup_cnfrm_password);

        mAuth=FirebaseAuth.getInstance();

    }

    public void signup(View view)
    {
        if(username_et.getText().toString().trim().length()==0||!(username_et.getText().toString().trim().contains("@")))
        {
            username_et.setError("please enter a valid Email");
            return;
        }

        if(password_et.getText().toString().trim().length()<6)
        {
            password_et.setError("enter valid password with minimum 6 characters");
            return;
        }
        if(!cnfrm_pass_et.getText().toString().trim().equals(password_et.getText().toString().trim()))
        {
            cnfrm_pass_et.setError("passwords donot match");
            password_et.setError("passwords donot match");
            return;
        }
        
        String email=username_et.getText().toString().trim();
        String password=password_et.getText().toString().trim();

        signupProc(email,password);

    }

    private void signupProc(String email, final String password)
    {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            pHandler.postDelayed(progressRunnable,200);
                            Snackbar.make(upperlayout, "Signed up successfully please login to continue", Snackbar.LENGTH_INDEFINITE).setAction("Login", new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {

                                    onBackPressed();
                                }
                            }).show();

                            username_et.setText("");
                            password_et.setText("");
                            cnfrm_pass_et.setText("");

                        } else {
                            // If sign in fails, display a message to the user.
                            pHandler.postDelayed(progressRunnable,200);
                            Snackbar.make(upperlayout, "Sign up failed please Retry", Snackbar.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
}