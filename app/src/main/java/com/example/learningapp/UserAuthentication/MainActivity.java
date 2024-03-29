package com.example.learningapp.UserAuthentication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.learningapp.Dashboard.Main_dashboard;
import com.example.learningapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
{

    private Handler handler;
    private ScrollView scrollView;
    private Runnable runnable = new Runnable()
    {
        @Override
        public void run()
        {
            scrollView.setVisibility(View.VISIBLE);

        }
    };

    private EditText username_et, password_et;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 1;

    private Handler phandler;
    private Runnable prunnable = new Runnable()
    {
        @Override
        public void run()
        {
            progressBar.setVisibility(View.GONE);
        }
    };

    private FirebaseUser user;

    private CircleImageView google_sign;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
        {
            startActivity(new Intent(this, Main_dashboard.class));
            finish();
        }

        //initializing the relative layout for the splash screen
        scrollView = findViewById(R.id.rellay1);
        handler = new Handler();
        //splash screen timeout 1.5 seconds
        handler.postDelayed(runnable, 1500);
        username_et = findViewById(R.id.login_username);
        password_et = findViewById(R.id.login_password);
        progressBar = findViewById(R.id.login_progress_bar);
        mAuth = FirebaseAuth.getInstance();
        google_sign = findViewById(R.id.sign_in_google);


        phandler = new Handler();
        phandler.postDelayed(prunnable, 0);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        try
        {
            mGoogleSignInClient.revokeAccess();
        } catch (Exception ignored)
        {

        }


    }

    public void loginButton(View view)
    {

        YoYo.with(Techniques.Bounce).duration(600).repeat(1).playOn(view);

        if (username_et.getText().toString().length() == 0)
        {
            username_et.setError("please enter the username");
            YoYo.with(Techniques.Tada).duration(700).repeat(1).playOn(username_et);
            return;
        }
        if (password_et.getText().toString().length() == 0)
        {
            password_et.setError("please enter a password");
            YoYo.with(Techniques.Tada).duration(700).repeat(1).playOn(password_et);
            return;
        }


        progressBar.setVisibility(View.VISIBLE);
        String username_str, password_str;
        username_str = username_et.getText().toString().trim();
        password_str = password_et.getText().toString().trim();

        loginUsername(username_str, password_str);


    }

    private void loginUsername(String email, String password)
    {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("login_username", "signInWithEmail:success");
                            updateUI();

                        } else
                        {
                            // If sign in fails, display a message to the user.
                            Log.w("login_username", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Sign In Failed", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                        // ...
                    }
                });
    }

    public void googleLogin(View view)
    {
        YoYo.with(Techniques.BounceIn).duration(400).repeat(1).playOn(view);
        signIn();
    }

    private void signIn()
    {
        progressBar.setVisibility(View.VISIBLE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask)
    {
        try
        {

            GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
            FirebaseGoogleAuth(acc);


        } catch (ApiException e)
        {
            Toast.makeText(MainActivity.this, "Sign In Failed", Toast.LENGTH_SHORT).show();
            try
            {
                mGoogleSignInClient.revokeAccess();
            } catch (Exception ignore)
            {

            }
            FirebaseGoogleAuth(null);
            progressBar.setVisibility(View.GONE);
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acct)
    {
        //check if the account is null
        if (acct != null)
        {
            AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {
                        // Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(MainActivity.this, user.getEmail(), Toast.LENGTH_SHORT).show();

                        updateUI();
                    } else
                    {
                        Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        try
                        {
                            mGoogleSignInClient.revokeAccess();
                        } catch (Exception ignore)
                        {

                        }
                        progressBar.setVisibility(View.GONE);

                    }
                }
            });
        } else
        {
            Toast.makeText(MainActivity.this, "acc failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void forgotPass(View view)
    {
        YoYo.with(Techniques.Flash).duration(500).repeat(1).playOn(view);
        startActivity(new Intent(MainActivity.this, Forgot_pass_activity.class));
    }

    public void signup(View view)
    {
        startActivity(new Intent(MainActivity.this, Signup_activity.class));
    }

    private void updateUI()
    {
        startActivity(new Intent(MainActivity.this, Main_dashboard.class));
        progressBar.setVisibility(View.GONE);
        finish();
    }

}