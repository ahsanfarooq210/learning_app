package com.example.learningapp.WebViewSupport;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learningapp.Entity.SavedPages;
import com.example.learningapp.R;
import com.example.learningapp.RvAdapters.ShowSavedRvAdapter;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Show_saved_pages extends AppCompatActivity
{

    private FirebaseFirestore firestore;

    private FirebaseUser user;
    private ArrayList<SavedPages> list;
    private LinearLayout upperlayout;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_saved_pages);

        firestore = FirebaseFirestore.getInstance();


        user = FirebaseAuth.getInstance().getCurrentUser();
        list = new ArrayList<>();

        upperlayout = findViewById(R.id.show_saved_upper_layout);
        recyclerView = findViewById(R.id.show_saved_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        CollectionReference reference = firestore.collection(user.getUid()).document(getString(R.string.firestore_collection_saved_pages)).collection("saved");
        reference.addSnapshotListener(new EventListener<QuerySnapshot>()
        {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error)
            {
                if (error != null)
                {
                    //error message
                    Snackbar.make(upperlayout, "Error in downloading the data", BaseTransientBottomBar.LENGTH_SHORT).show();

                    return;
                }

                list.clear();
                for (QueryDocumentSnapshot snapshot : value)
                {
                    SavedPages savedPages = snapshot.toObject(SavedPages.class);
                    savedPages.setId(snapshot.getId());
                    list.add(savedPages);
                }

                ShowSavedRvAdapter savedRvAdapter = new ShowSavedRvAdapter(list, Show_saved_pages.this, upperlayout);
                recyclerView.setAdapter(savedRvAdapter);

            }
        });
    }
}