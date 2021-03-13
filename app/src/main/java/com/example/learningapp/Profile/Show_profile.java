package com.example.learningapp.Profile;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.learningapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Show_profile extends AppCompatActivity
{
    private TextView name, email, contact, gender;

    private CircleImageView profilePhoto;

    private FirebaseUser user;
    private FirebaseFirestore firestore;

    private ScrollView scrollView;

    private Runnable runnable = new Runnable()
    {
        @Override
        public void run()
        {
            scrollView.setVisibility(View.VISIBLE);
        }
    };

    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);

        name = findViewById(R.id.show_profile_name);
        contact = findViewById(R.id.show_prfile_contact_et);
        gender = findViewById(R.id.show_gender_spinner);
        email = findViewById(R.id.email_tv);

        scrollView = findViewById(R.id.show_rellay1);

        Handler handler = new Handler();
        handler.postDelayed(runnable, 1500);

        firestore = FirebaseFirestore.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference();

        profilePhoto = findViewById(R.id.show_profile_photo);

        email.setText(user.getEmail());


    }

    @Override
    protected void onStart()
    {
        super.onStart();

        String uuid = user.getUid();
        DocumentReference documentReference = firestore.collection(uuid).document(getString(R.string.firestore_user_reference));
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>()
        {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error)
            {
                if (value != null)
                {
                    name.setText(value.getString(getString(R.string.user_reference_fname)));
                    contact.setText(value.getString(getString(R.string.user_reference_contact)));
                    gender.setText(value.getString(getString(R.string.user_reference_gender)));


                }
            }
        });


        StorageReference downloadReference = storageReference.child("users/" + user.getUid() + "/profile.jpg");
        downloadReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri uri)
            {
                Picasso.get().load(uri).into(profilePhoto);
            }
        });
    }
}