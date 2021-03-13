package com.example.learningapp.HelperClasses;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.learningapp.Entity.SavedPages;
import com.example.learningapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class WebCommonFunctions
{
    public static boolean savePages(SavedPages savedPages, Context context)
    {
        final boolean[] flag = new boolean[1];
        flag[0] = false;
        FirebaseFirestore firestore;
        CollectionReference collectionReference;
        firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        collectionReference = firestore.collection(user.getUid());
        collectionReference.document(context.getApplicationContext().getString(R.string.firestore_collection_saved_pages)).collection("saved").add(savedPages).addOnSuccessListener(new OnSuccessListener<DocumentReference>()
        {
            @Override
            public void onSuccess(DocumentReference documentReference)
            {
                flag[0] = true;
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                flag[0] = false;
            }
        });

        return flag[0];
    }

    public static String decideLink(String language, String website, Context context)
    {
        String link;

        switch (language)
        {
            case "c++":
                switch (website)
                {
                    case "tutorials point":
                        link = context.getString(R.string.tutorial_point_cpp);
                        break;
                    case "w3 Schools":
                        link = context.getString(R.string.w3_schools_cpp);
                        break;
                    case "geeks for geeks":
                        link = context.getString(R.string.geeks_for_geeks_cpp);
                        break;

                    default:
                        link = "http://www.google.com/";
                        break;

                }
                break;

            case "java":
                switch (website)
                {
                    case "tutorials point":
                        link = context.getString(R.string.tutorial_point_java);
                        break;
                    case "w3 Schools":
                        link = context.getString(R.string.w3_schools_java);
                        break;
                    case "geeks for geeks":
                        link = context.getString(R.string.geeks_for_geeks_java);
                        break;

                    default:
                        link = "http://www.google.com/";
                        break;
                }
                break;

            case "python":
                switch (website)
                {
                    case "tutorials point":
                        link = context.getString(R.string.tutorial_point_python);
                        break;
                    case "w3 Schools":
                        link = context.getString(R.string.w3_schools_python);
                        break;

                    case "geeks for geeks":
                        link = context.getString(R.string.geeks_for_geeks_python);
                        break;
                    default:
                        link = "http://www.google.com/";
                        break;
                }
                break;

            case "java script":
                switch (website)
                {
                    case "tutorials point":
                        link = context.getString(R.string.tutorial_point_java_script);
                        break;
                    case "w3 Schools":
                        link = context.getString(R.string.w3_schools_java_script);
                        break;

                    case "geeks for geeks":
                        link = context.getString(R.string.geeks_for_geeks_javascript);
                        break;

                    default:
                        link = "http://www.google.com/";
                        break;
                }
                break;

            case "c#":
                switch (website)
                {
                    case "tutorials point":
                        link = context.getString(R.string.tutorial_point_csharp);
                        break;
                    case "w3 Schools":
                        link = context.getString(R.string.w3_schools_csharp);
                        break;

                    case "geeks for geeks":
                        link = context.getString(R.string.geeks_for_geeks_csharp);
                        break;
                    default:
                        link = "http://www.google.com/";
                        break;
                }
                break;

            default:
                link = "http://www.google.com/";
                break;

        }

        return link;
    }
}
