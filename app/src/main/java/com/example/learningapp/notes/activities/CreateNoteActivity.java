package com.example.learningapp.notes.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.learningapp.Database.Database;
import com.example.learningapp.R;
import com.example.learningapp.notes.entities.Note;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateNoteActivity extends AppCompatActivity
{


    private EditText inputNoteTitle, inputNoteSubTitle, inputNoteText;
    private TextView textDateTime;
    private View viewSubtitleIndicator;
    private static final int REQUEST_STORAGE_PERMISSIONS = 1;
    private static final int REQUEST_CODE_SELECT_IMAGW = 2;
    private ImageView imageNote;
    private String selectedImagePath;

    private String selectedNoteColor;
    private TextView textWebUrl;
    private LinearLayout layoutWebUrl;
    private AlertDialog dialogAddUrl;
    private AlertDialog dialogDeleteNote;

    private Note alreadyAvalableNote;

    private CoordinatorLayout upperlayout;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });

        inputNoteTitle = findViewById(R.id.inputNoteTitle);
        inputNoteSubTitle = findViewById(R.id.inputNoteSubTitle);
        inputNoteText = findViewById(R.id.inputNote);
        viewSubtitleIndicator = findViewById(R.id.viewSubtitleIndicator);

        imageNote = findViewById(R.id.imageNote);
        textDateTime = findViewById(R.id.textDateTime);
        textDateTime.setText(new SimpleDateFormat("EEEE,dd MMMM yyyy HH:mm a", Locale.getDefault()).format(new Date()));

        //upper layout for the snack bar
        upperlayout = findViewById(R.id.create_note_upper_layout);

        ImageView imageSave = findViewById(R.id.imageSave);
        imageSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                saveNote();
            }
        });
        textWebUrl = findViewById(R.id.textWebUrl);
        layoutWebUrl = findViewById(R.id.layoutWebUrl);

        selectedNoteColor = "#333333";
        selectedImagePath = "";

        if (getIntent().getBooleanExtra("isViewOrUpdate", false))
        {
            alreadyAvalableNote = (Note) getIntent().getSerializableExtra("note");
            setViewOrUpdateNote();
        }

        findViewById(R.id.imageRemoveWebUrl).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                textWebUrl.setText(null);
                layoutWebUrl.setVisibility(View.GONE);
            }
        });

        findViewById(R.id.imageRemoveImage).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                imageNote.setImageBitmap(null);
                imageNote.setVisibility(View.GONE);
                findViewById(R.id.imageRemoveImage).setVisibility(View.GONE);
                selectedImagePath = "";
            }
        });

        if (getIntent().getBooleanExtra("isFromQuickAction", false))
        {
            String type = getIntent().getStringExtra("quickActionType");
            if (type != null)
            {
                if (type.equals("image"))
                {
                    selectedImagePath = getIntent().getStringExtra("imagePath");
                    imageNote.setImageBitmap(BitmapFactory.decodeFile(selectedImagePath));
                    imageNote.setVisibility(View.VISIBLE);
                    findViewById(R.id.imageRemoveImage).setVisibility(View.VISIBLE);
                } else
                {
                    if (type.equals("URL"))
                    {
                        textWebUrl.setText(getIntent().getStringExtra("URL"));
                        layoutWebUrl.setVisibility(View.VISIBLE);
                    }
                }
            }
        }

        initMiscellaneous();
        setViewSubtitleIndicateColor();


    }

    private void setViewOrUpdateNote()
    {
        inputNoteTitle.setText(alreadyAvalableNote.getTitle());
        inputNoteSubTitle.setText(alreadyAvalableNote.getSubTitle());
        inputNoteText.setText(alreadyAvalableNote.getNoteText());
        textDateTime.setText(alreadyAvalableNote.getDateTime());
        if (alreadyAvalableNote.getImagePath() != null && !alreadyAvalableNote.getImagePath().trim().isEmpty())
        {
            imageNote.setImageBitmap(BitmapFactory.decodeFile(alreadyAvalableNote.getImagePath()));
            imageNote.setVisibility(View.VISIBLE);
            findViewById(R.id.imageRemoveImage).setVisibility(View.VISIBLE);

            selectedImagePath = alreadyAvalableNote.getImagePath();
        }
        if (alreadyAvalableNote.getWebLink() != null && !alreadyAvalableNote.getWebLink().trim().isEmpty())
        {
            textWebUrl.setText(alreadyAvalableNote.getWebLink());
            layoutWebUrl.setVisibility(View.VISIBLE);
        }
    }

    private void saveNote()
    {
        if (inputNoteTitle.getText().toString().trim().isEmpty())
        {

            Snackbar.make(upperlayout, "Title cannot be empty", Snackbar.LENGTH_SHORT).show();

            YoYo.with(Techniques.Tada).duration(500).repeat(1).playOn(inputNoteTitle);
            return;
        }
        if (inputNoteSubTitle.getText().toString().trim().isEmpty())
        {

            Snackbar.make(upperlayout, "Sub title cannot be empty", Snackbar.LENGTH_SHORT).show();

            YoYo.with(Techniques.Tada).duration(500).repeat(1).playOn(inputNoteSubTitle);
            return;
        }
        if (inputNoteText.getText().toString().trim().isEmpty())
        {

            Snackbar.make(upperlayout, "note cannot be empty", Snackbar.LENGTH_SHORT).show();

            YoYo.with(Techniques.Tada).duration(500).repeat(1).playOn(inputNoteText);

            return;
        }

        final Note note = new Note();
        note.setTitle(inputNoteTitle.getText().toString().trim());
        note.setSubTitle(inputNoteSubTitle.getText().toString().trim());
        note.setNoteText(inputNoteText.getText().toString().trim());
        note.setDateTime(textDateTime.getText().toString().trim());
        note.setColor(selectedNoteColor);
        note.setImagePath(selectedImagePath);
        if (layoutWebUrl.getVisibility() == View.VISIBLE)
        {
            note.setWebLink(textWebUrl.getText().toString());
        }

        if (alreadyAvalableNote != null)
        {
            note.setId(alreadyAvalableNote.getId());
        }

        class SaveNoteTask extends AsyncTask<Void, Void, Void>
        {

            @Override
            protected Void doInBackground(Void... voids)
            {
                Database.Companion.getDatabase(getApplicationContext()).getNotesDao().insertNote(note);
                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid)
            {
                super.onPostExecute(aVoid);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }

        new SaveNoteTask().execute();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        //saveNote();
    }

    private void initMiscellaneous()
    {

        final LinearLayout layoutmiscellaneous = findViewById(R.id.layoutMiscellaneous);
        final BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(layoutmiscellaneous);
        layoutmiscellaneous.findViewById(R.id.textMiscellaneous).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED)
                {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else
                {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        final ImageView imageColor1 = layoutmiscellaneous.findViewById(R.id.imageColor1);
        final ImageView imageColor2 = layoutmiscellaneous.findViewById(R.id.imageColor2);
        final ImageView imageColor3 = layoutmiscellaneous.findViewById(R.id.imageColor3);
        final ImageView imageColor4 = layoutmiscellaneous.findViewById(R.id.imageColor4);
        final ImageView imageColor5 = layoutmiscellaneous.findViewById(R.id.imageColor5);

        layoutmiscellaneous.findViewById(R.id.viewColor1).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                selectedNoteColor = "#333333";
                imageColor1.setImageResource(R.drawable.ic_done);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setViewSubtitleIndicateColor();
            }
        });

        layoutmiscellaneous.findViewById(R.id.viewColor2).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                selectedNoteColor = "#FDBE38";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(R.drawable.ic_done);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setViewSubtitleIndicateColor();
            }
        });
        layoutmiscellaneous.findViewById(R.id.viewColor3).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                selectedNoteColor = "#FF4842";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(R.drawable.ic_done);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setViewSubtitleIndicateColor();
            }
        });
        layoutmiscellaneous.findViewById(R.id.viewColor4).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                selectedNoteColor = "#3a52FC";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(R.drawable.ic_done);
                imageColor5.setImageResource(0);
                setViewSubtitleIndicateColor();
            }
        });
        layoutmiscellaneous.findViewById(R.id.viewColor5).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                selectedNoteColor = "#000000";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(R.drawable.ic_done);
                setViewSubtitleIndicateColor();
            }
        });

        if (alreadyAvalableNote != null && alreadyAvalableNote.getColor() != null && !alreadyAvalableNote.getColor().trim().isEmpty())
        {
            switch (alreadyAvalableNote.getColor())
            {
                case "#FDBE38":
                    layoutmiscellaneous.findViewById(R.id.viewColor2).performClick();
                    break;
                case "#FF4842":
                    layoutmiscellaneous.findViewById(R.id.viewColor3).performClick();
                    break;
                case "#3a52FC":
                    layoutmiscellaneous.findViewById(R.id.viewColor4).performClick();
                    break;
                case "#000000":
                    layoutmiscellaneous.findViewById(R.id.viewColor5).performClick();
                    break;
            }
        }

        layoutmiscellaneous.findViewById(R.id.layoutAddImage).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(CreateNoteActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSIONS);
                } else
                {
                    selectImage();
                }
            }
        });

        layoutmiscellaneous.findViewById(R.id.layoutAddUrl).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                showAddUrlDialog();
            }
        });

        if (alreadyAvalableNote != null)
        {
            layoutmiscellaneous.findViewById(R.id.layoutDeleteNote).setVisibility(View.VISIBLE);
            layoutmiscellaneous.findViewById(R.id.layoutDeleteNote).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    showDeleteNoteDialog();
                }
            });
        }

    }

    private void showDeleteNoteDialog()
    {
        if (dialogDeleteNote == null)
        {
            Context context;
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateNoteActivity.this);
            View view = LayoutInflater.from(this).inflate(R.layout.layout_delete_note, (ViewGroup) findViewById(R.id.layoutDeleteNoteConteiner));
            builder.setView(view);
            dialogDeleteNote = builder.create();
            if (dialogDeleteNote.getWindow() != null)
            {
                dialogDeleteNote.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            view.findViewById(R.id.textDeleteNote).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    class DeleteNoteTask extends AsyncTask<Void, Void, Void>
                    {

                        @Override
                        protected Void doInBackground(Void... voids)
                        {
                            Database.Companion.getDatabase(getApplicationContext()).getNotesDao().deleteNote(alreadyAvalableNote);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid)
                        {
                            super.onPostExecute(aVoid);
                            Intent intent = new Intent();
                            intent.putExtra("isNoteDeleted", true);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }

                    new DeleteNoteTask().execute();
                }
            });

            view.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    dialogDeleteNote.dismiss();
                }
            });
        }

        dialogDeleteNote.show();
    }

    private void setViewSubtitleIndicateColor()
    {
        GradientDrawable gradientDrawable = (GradientDrawable) viewSubtitleIndicator.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectedNoteColor));
    }

    private void selectImage()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGW);
        }
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

                Snackbar.make(upperlayout, "Permission failed", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SELECT_IMAGW && resultCode == RESULT_OK)
        {
            if (data != null)
            {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null)
                {
                    try
                    {

                        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        imageNote.setImageBitmap(bitmap);
                        imageNote.setVisibility(View.VISIBLE);

                        findViewById(R.id.imageRemoveImage).setVisibility(View.VISIBLE);
                        selectedImagePath = getPathFromUri(selectedImageUri);

                    } catch (Exception exception)
                    {
                        Snackbar.make(upperlayout, "Error in selecting the image", Snackbar.LENGTH_SHORT).show();
                    }
                }
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

    private void showAddUrlDialog()
    {
        if (dialogAddUrl == null)
        {
            Context context;
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateNoteActivity.this);
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
                        Snackbar.make(upperlayout, "Add a URL", Snackbar.LENGTH_SHORT).show();
                    } else
                    {
                        if (!Patterns.WEB_URL.matcher(inputUrl.getText().toString().trim()).matches())
                        {
                            Snackbar.make(upperlayout, "Enter a valid URL", Snackbar.LENGTH_SHORT).show();
                        } else
                        {
                            textWebUrl.setText(inputUrl.getText().toString().trim());
                            layoutWebUrl.setVisibility(View.VISIBLE);
                            dialogAddUrl.dismiss();
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
}