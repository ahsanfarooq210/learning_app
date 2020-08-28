package com.example.learningmanagementsystem.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.learningmanagementsystem.LoginSignup.MainActivity;
import com.example.learningmanagementsystem.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import static com.example.learningmanagementsystem.R.string.firestore_users;
import static com.example.learningmanagementsystem.R.string.user_key_dob;

public class edit_profile_activity extends AppCompatActivity
{

    private EditText name_et, phone_et;
    private Spinner gender_spinner;
    private Button dob_btn, edit;
    private String dob = "";
    private FirebaseFirestore firestore, download_user;
    private FirebaseUser user;

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

    //linear layout for the snackbars
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_activity);

        name_et = findViewById(R.id.edit_username_tf);
        phone_et = findViewById(R.id.edit_phone_tf);
        gender_spinner = findViewById(R.id.edit_profile_gender_spiner);
        dob_btn = findViewById(R.id.edit_profile_dob_btn);
        edit = findViewById(R.id.edit_profile_edit_btn);
        progressBar=findViewById(R.id.edit_profile_progress_bar);

        //initializing the linear layout for the snack bar
        linearLayout = findViewById(R.id.edit_profile_parent_layout);

        //hiding the progress ring
        progressBarh.postDelayed(runnable1, 0);

        //gender spinner array adapter and the spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Genders, R.layout.spinner_text);
        adapter.setDropDownViewResource(R.layout.spinner_text_dropdown);
        gender_spinner.setAdapter(adapter);
        //firebase auth fo the uuid
        download_user=FirebaseFirestore.getInstance();
        firestore=FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();


        //creating edit button click listinner
        edit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                progressBar.setVisibility(View.VISIBLE);
                String name, phone, gender;
                name = name_et.getText().toString().trim();
                if (name.length() == 0)
                {
                    name_et.setError(getString(R.string.please_enter_name));
                    progressBarh.postDelayed(runnable1, 0);
                    return;
                }
                phone = phone_et.getText().toString().trim();
                if (phone.length() == 0)
                {
                    phone_et.setError(getString(R.string.please_enter_phone));
                    progressBarh.postDelayed(runnable1, 0);
                    return;
                }
                gender = gender_spinner.getSelectedItem().toString();
                if (dob.length() == 0)
                {
                    dob_btn.setError(getString(R.string.date_of_birth));
                    progressBarh.postDelayed(runnable1, 0);
                    return;
                }

                if (user != null)
                {
                    String userid = user.getUid();
                    DocumentReference reference = firestore.collection(getString(firestore_users)).document(userid);
                    Map<String, Object> user = new HashMap<>();
                    user.put(getString(R.string.user_key_fullname), name);
                    user.put(getString(R.string.user_key_phone), phone);
                    user.put(getString(R.string.user_key_gender), gender);
                    user.put(getString(user_key_dob), dob);

                    reference.set(user).addOnSuccessListener(new OnSuccessListener<Void>()
                    {
                        @Override
                        public void onSuccess(Void aVoid)
                        {
                            progressBarh.postDelayed(runnable1, 0);
                            Snackbar.make(linearLayout, "Information edited successfully", BaseTransientBottomBar.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        //for opening the calender popup to get the user birth date
        dob_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
                builder.setTitleText("Date Of Birth");
                final MaterialDatePicker materialDatePicker = builder.build();
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");

                //date picker dialog click listinner
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener()
                {
                    @Override
                    public void onPositiveButtonClick(Object selection)
                    {
                        dob = materialDatePicker.getHeaderText();

                        //to show the selected date on the button
                        dob_btn.setText(materialDatePicker.getHeaderText());
                    }
                });

            }
        });

    }

    @Override
    public void onStart()
    {
        super.onStart();

        if (user != null)
        {
            String useremail=user.getEmail();
            DocumentReference documentReference = download_user.collection(getString(firestore_users)).document(user.getUid());
            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>()
            {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error)
                {
                    if(value!=null)
                    {
                        name_et.setText(value.getString(getString(R.string.user_key_fullname)));
                        phone_et.setText(value.getString(getString(R.string.user_key_phone)));
                        dob_btn.setText(value.getString(getString(user_key_dob)));
                    }

                }
            });
        } else
        {
            startActivity(new Intent(this, MainActivity.class));
        }


    }
}