package com.example.learningapp.Profile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.learningapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Edit_profile extends AppCompatActivity
{
    private FirebaseFirestore firestore, download_user;
    private FirebaseUser user;

    private ProgressBar progressBar;

    private ScrollView relllay1;
    private RelativeLayout rellay2, parent_layout;

    private Handler handler;
    private Runnable runnable = new Runnable()
    {
        @Override
        public void run()
        {
            progressBar.setVisibility(View.GONE);
        }
    };

    private Runnable runnableSplash = new Runnable()
    {
        @Override
        public void run()
        {
            relllay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };

    private EditText username_et, contact_et;
    private Spinner gender_spinner;
    private final int GALLERY_CODE = 100;
    private CircleImageView profilePhoto;

    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Handler splash = new Handler();
        splash.postDelayed(runnableSplash, 1500);


        firestore = FirebaseFirestore.getInstance();
        download_user = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        progressBar = findViewById(R.id.edit_progress_bar);

        handler = new Handler();

        handler.postDelayed(runnable, 0);

        relllay1 = findViewById(R.id.edit_rellay1);
        rellay2 = findViewById(R.id.edit_rellay2);

        parent_layout = findViewById(R.id.edit_profile_parent_layout);

        username_et = findViewById(R.id.edit_profile_name);
        contact_et = findViewById(R.id.edit_prfile_contact_et);
        gender_spinner = findViewById(R.id.edit_gender_spinner);

        profilePhoto = findViewById(R.id.edit_profile_photo);

        storageReference = FirebaseStorage.getInstance().getReference();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Genders, R.layout.spinner_text);
        adapter.setDropDownViewResource(R.layout.spinner_text_dropdown);
        gender_spinner.setAdapter(adapter);

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        String uuid = user.getUid();
        DocumentReference documentReference = download_user.collection(getString(R.string.firestore_user_reference)).document(uuid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>()
        {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error)
            {
                if (value != null)
                {
                    username_et.setText(value.getString(getString(R.string.user_reference_fname)));
                    contact_et.setText(value.getString(getString(R.string.user_reference_contact)));
                    String gender = value.getString(getString(R.string.user_reference_gender));

                    if (gender != null)
                    {


                        switch (gender)
                        {
                            case "Male":
                                gender_spinner.setSelection(0);
                                break;
                            case "Female":
                                gender_spinner.setSelection(1);
                                break;
                            case "Other":
                                gender_spinner.setSelection(2);
                                break;
                        }
                    }
                }
            }
        });

        StorageReference downloadReference = storageReference.child("users/" + user.getUid() + "/profile.jpg");
        downloadReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri uri)
            {
                //Picasso.get().load(uri).into(profilePhoto);
            }
        });
    }

    public void save(View view)
    {
        progressBar.setVisibility(View.VISIBLE);
        String usernameStr = username_et.getText().toString().trim();
        String contactStr = contact_et.getText().toString().trim();
        String gender = gender_spinner.getSelectedItem().toString();

        if (usernameStr.length() == 0)
        {
            username_et.setError("Please enter your name");
            YoYo.with(Techniques.Tada).duration(700).repeat(1).playOn(username_et);
            return;
        }

        if (contactStr.length() == 0)
        {
            contact_et.setError("Please enter your contact");
            YoYo.with(Techniques.Tada).duration(700).repeat(1).playOn(contact_et);
            return;
        }

        YoYo.with(Techniques.Tada).duration(700).repeat(1).playOn(view);
        String uuid = user.getUid();

        if (uuid.length() == 0)
        {
            uuid = "Unknown";
        }

        DocumentReference documentReference = firestore.collection(getString(R.string.firestore_user_reference)).document(uuid);

        Map<String, Object> user = new HashMap<>();
        user.put(getString(R.string.user_reference_fname), usernameStr);
        user.put(getString(R.string.user_reference_contact), contactStr);
        user.put(getString(R.string.user_reference_gender), gender);
        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>()
        {
            @Override
            public void onSuccess(Void aVoid)
            {
                handler.postDelayed(runnable, 300);
                Snackbar.make(parent_layout, "Saved Successfully!", BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });
    }

    public void changeProPic(View view)
    {
        Intent openGalleryintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openGalleryintent, GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                assert data != null;
                if (data.getData() != null)
                {


                    Uri imageUri = data.getData();

                    profilePhoto.setImageURI(imageUri);

                    uploadImageToFirebase(imageUri);
                }

            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri)
    {
        //upload image to firebase storage
        final StorageReference fileref = storageReference.child("users/" + user.getUid() + "/profile.jpg");
        fileref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                {
                    @Override
                    public void onSuccess(Uri uri)
                    {
                        Picasso.get().load(uri).into(profilePhoto);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(Edit_profile.this, "image upload failure", Toast.LENGTH_SHORT).show();
            }
        });

    }
}