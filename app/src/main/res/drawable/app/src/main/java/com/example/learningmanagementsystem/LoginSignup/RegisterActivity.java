package com.example.learningmanagementsystem.LoginSignup;

import android.os.Bundle;

import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.learningmanagementsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity
{

    //relative layout fot the animation screen
    private RelativeLayout rlayout;
    //animation ??
    private Animation animation;
    //edit text for signup
    private EditText email_et, password_et, confirmPassword;
    //firebase auth
    private FirebaseAuth mAuth;
    //relative layout for the snack bar
    private RelativeLayout relativeLayout;
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.bgHeader);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rlayout = findViewById(R.id.rlayout);
        animation = AnimationUtils.loadAnimation(this, R.anim.uptodowndiagonal);
        rlayout.setAnimation(animation);
        email_et = findViewById(R.id.signup_username_tf);
        password_et = findViewById(R.id.signup_password_tf);
        confirmPassword = findViewById(R.id.signup_cnfrm_password_tf);
        mAuth = FirebaseAuth.getInstance();
        relativeLayout = findViewById(R.id.signup_upper_relatative);
        progressBar=findViewById(R.id.my_progress_bar);
        progressBarh.postDelayed(runnable1, 0);
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

    private void signupEmail(String email, String password)
    {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "createUserWithEmail:success");
                            //FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            Snackbar.make(relativeLayout, "Signed up successfully please login to continue", Snackbar.LENGTH_INDEFINITE).setAction("Login", new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {

                                    onBackPressed();
                                }
                            }).show();
                            email_et.setText("");
                            password_et.setText("");
                            confirmPassword.setText("");
                            progressBarh.postDelayed(runnable1, 0);

                        } else
                        {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Snackbar.make(relativeLayout, "Signup failed", Snackbar.LENGTH_SHORT).show();
                            progressBarh.postDelayed(runnable1, 0);
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    public void Signup(View view)
    {
        String em, pas, cnfrmpas;
        em = email_et.getText().toString();
        pas = password_et.getText().toString();
        cnfrmpas = confirmPassword.getText().toString();
        if (!(em.contains("@") && em.contains(".com")))
        {
            email_et.setError("Please enter a valid email adress");
            return;
        }

        if (em.length() == 0)
        {
            email_et.setError("Please enter the email");
            return;
        }
        if (pas.length() == 0 || pas.length() < 6)
        {
            password_et.setError("please enter valid a password");
            return;
        }
        if (!(pas.equals(cnfrmpas)))
        {
            password_et.setError("passwords donot match");
            confirmPassword.setError("passwords donot match");
            return;
        }

        signupEmail(em, pas);

    }
}
