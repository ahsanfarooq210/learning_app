package com.example.learningapp.notes.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.learningapp.Database.Database;
import com.example.learningapp.R;
import com.example.learningapp.notes.adapters.NotesAdapter;
import com.example.learningapp.notes.entities.Note;
import com.example.learningapp.notes.listiners.NotesListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.api.Distribution;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Notes_Main_Class extends AppCompatActivity implements NotesListener
{
    public static final int REQUEST_CODE_ADD_NOTE = 1;
    private static final int REQUEST_CODE_UPDATE_NOTE = 2;
    private static final int REQUESR_CODE_SELECT_IMAGE = 4;
    private static final int REQUEST_STORAGE_PERMISSIONS = 5;

    private RecyclerView notesRecyclerView;
    private List<Note> noteList;
    private NotesAdapter notesAdapter;

    private int noteClickedPosition = -1;

    private AlertDialog dialogAddUrl;

    private ConstraintLayout upperLayout;

    private FirebaseFirestore firestore;

    private CollectionReference reference;

    private FirebaseUser user;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_main_class);
        ImageView imageAddNoteMain = findViewById(R.id.imageAddNoteMain);
        imageAddNoteMain.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivityForResult(new Intent(getApplicationContext(), CreateNoteActivity.class), REQUEST_CODE_ADD_NOTE);
            }
        });
        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        upperLayout = findViewById(R.id.Notes_main_class_upper_layout);

        noteList = new ArrayList<>();
        notesAdapter = new NotesAdapter(noteList, this);
        notesRecyclerView.setAdapter(notesAdapter);
        //getNotes(REQUESR_CODE_SHOW_NOTE, false);


        firestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        reference = firestore.collection(user.getUid());

        EditText inputSearch = findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                notesAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (noteList.size() != 0)
                {
                    notesAdapter.searchNotes(s.toString());
                }
            }
        });

        findViewById(R.id.imageAddNote).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivityForResult(new Intent(getApplicationContext(), CreateNoteActivity.class), REQUEST_CODE_ADD_NOTE);
            }
        });

        findViewById(R.id.immageAddImage).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(Notes_Main_Class.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSIONS);
                } else
                {
                    selectImage();
                }
            }
        });

        findViewById(R.id.imageAddWebLink).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showAddUrlDialog();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSIONS && grantResults.length > 0)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                selectImage();
            } else
            {
                Snackbar.make(upperLayout, "permission denied", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private String getPathFromUri(Uri uri)
    {
        String filePath;

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null)
        {
            filePath = uri.getPath();
        } else
        {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("_data");
            filePath = cursor.getString(index);
            cursor.close();
        }
        return filePath;
    }

    private void selectImage()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(intent, REQUESR_CODE_SELECT_IMAGE);
        }
    }

    @Override
    public void onNoteClicked(Note note, int position)
    {
        noteClickedPosition = position;
        Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
        intent.putExtra("isViewOrUpdate", true);
        intent.putExtra("note", note);
        startActivityForResult(intent, REQUEST_CODE_UPDATE_NOTE);

    }


//    private void getNotes(final int requestCode, final boolean isNoteDeleted)
//    {
//        class GetNotes extends AsyncTask<Void, Void, List<Note>>
//        {
//
//            @Override
//            protected List<Note> doInBackground(Void... voids)
//            {
//                return Database.Companion.getDatabase(getApplicationContext()).getNotesDao().getAllNotes();
//            }
//
//            @Override
//            protected void onPostExecute(List<Note> notes)
//            {
//                super.onPostExecute(notes);
//                if (requestCode == REQUESR_CODE_SHOW_NOTE)
//                {
//                    noteList.clear();
//                    noteList.addAll(notes);
//                    notesAdapter.notifyDataSetChanged();
//                } else
//                {
//                    if (requestCode == REQUEST_CODE_ADD_NOTE)
//                    {
//                        noteList.add(0, notes.get(0));
//                        notesAdapter.notifyItemInserted(0);
//                        notesRecyclerView.smoothScrollToPosition(0);
//                    } else
//                    {
//                        if (requestCode == REQUEST_CODE_UPDATE_NOTE)
//                        {
//                            noteList.remove(noteClickedPosition);
//
//                            if (isNoteDeleted)
//                            {
//                                notesAdapter.notifyItemRemoved(noteClickedPosition);
//                            } else
//                            {
//                                noteList.add(noteClickedPosition, notes.get(noteClickedPosition));
//                                notesAdapter.notifyItemChanged(noteClickedPosition);
//                            }
//                        }
//                    }
//                }
//
//            }
//        }
//        new GetNotes().execute();
//    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        //getNotes(REQUESR_CODE_SHOW_NOTE, false);
        getNotesOnline();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK)
        {
            //getNotes(REQUEST_CODE_ADD_NOTE, false);
        } else
        {
            if (requestCode == REQUEST_CODE_UPDATE_NOTE && resultCode == RESULT_OK)
            {
                // getNotes(REQUEST_CODE_UPDATE_NOTE, data.getBooleanExtra("isNoteDeleted", false));
            } else
            {
                if (requestCode == REQUESR_CODE_SELECT_IMAGE && resultCode == RESULT_OK)
                {
                    if (data != null)
                    {
                        Uri selectedImageUri = data.getData();
                        if (selectedImageUri != null)
                        {
                            try
                            {
                                String selectedImagePath = getPathFromUri(selectedImageUri);
                                Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
                                intent.putExtra("isFromQuickAction", true);
                                intent.putExtra("quickActionType", "image");
                                intent.putExtra("imagePath", selectedImagePath);
                                startActivityForResult(intent, REQUEST_CODE_ADD_NOTE);
                            } catch (Exception exception)
                            {
                                Snackbar.make(upperLayout, exception.getMessage(), Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        }

    }

    private void showAddUrlDialog()
    {
        if (dialogAddUrl == null)
        {
            Context context;
            AlertDialog.Builder builder = new AlertDialog.Builder(Notes_Main_Class.this);
            View view = LayoutInflater.from(this).inflate(R.layout.layout_add_url, (ViewGroup) findViewById(R.id.layoutAddUrlContainer));

            builder.setView(view);
            dialogAddUrl = builder.create();
            if (dialogAddUrl.getWindow() != null)
            {
                dialogAddUrl.getWindow().setBackgroundDrawable(new ColorDrawable(0));

            }

            final EditText inputUrl = view.findViewById(R.id.inputURL);
            inputUrl.findFocus();
            view.findViewById(R.id.textAdd).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (inputUrl.getText().toString().trim().isEmpty())
                    {

                        Snackbar.make(upperLayout, "Add a URL", Snackbar.LENGTH_SHORT).show();
                    } else
                    {
                        if (!Patterns.WEB_URL.matcher(inputUrl.getText().toString().trim()).matches())
                        {
                            Snackbar.make(upperLayout, "Enter a valid URL", Snackbar.LENGTH_SHORT).show();
                        } else
                        {
                            dialogAddUrl.dismiss();

                            Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
                            intent.putExtra("isFromQuickAction", true);
                            intent.putExtra("quickActionType", "URL");
                            intent.putExtra("URL", inputUrl.getText().toString());
                            startActivityForResult(intent, REQUEST_CODE_ADD_NOTE);
                        }

                    }
                }
            });
            view.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    dialogAddUrl.dismiss();
                }
            });
        }

        dialogAddUrl.show();

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        getNotesOnline();

    }

    private void getNotesOnline()
    {
        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                reference.document(getString(R.string.saved_notes_document_reference)).collection(getString(R.string.notes_collection)).addSnapshotListener(new EventListener<QuerySnapshot>()
                {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error)
                    {
                        if (error != null)
                        {
                            Snackbar.make(upperLayout, "Error in downloading the data", BaseTransientBottomBar.LENGTH_SHORT).show();
                            return;
                        }

                        noteList.clear();
                        assert value != null;
                        for (QueryDocumentSnapshot snapshot : value)
                        {
                            Note note = snapshot.toObject(Note.class);
                            note.setId(snapshot.getId());
                            noteList.add(note);
                        }

                        notesAdapter.notifyDataSetChanged();


                    }
                });
            }
        };
        runnable.run();

    }
}
