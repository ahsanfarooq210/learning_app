package com.example.learningapp.notes.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

import com.example.learningapp.Database.Database;
import com.example.learningapp.R;
import com.example.learningapp.notes.entities.Note;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

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
        initMiscellaneous();
        setViewSubtitleIndicateColor();


    }

    private void saveNote()
    {
        if (inputNoteTitle.getText().toString().trim().isEmpty())
        {
            //TODO:make a snak bar here
            //TODO:make a yoyo animation
            Toast.makeText(this, "please write the title", Toast.LENGTH_SHORT).show();
            return;
        }
        if (inputNoteSubTitle.getText().toString().trim().isEmpty())
        {
            //TODO:make a snak bar here
            //TODO:make a yoyo animation
            Toast.makeText(this, "please write the subtitle", Toast.LENGTH_SHORT).show();
            return;
        }
        if (inputNoteText.getText().toString().trim().isEmpty())
        {
            //TODO:make a snak bar here
            //TODO:make a yoyo animation
            Toast.makeText(this, "please write the title", Toast.LENGTH_SHORT).show();
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
        // saveNote();
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
                //TODO:make a snack bar here
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
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
                        selectedImagePath = getPathFromUri(selectedImageUri);

                    } catch (Exception exception)
                    {
                        //TODO:make a snack bar here
                        Toast.makeText(this, "Error in selecting image", Toast.LENGTH_SHORT).show();
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
                        //TODO:make a snack bar here
                        Toast.makeText(CreateNoteActivity.this, "Ad a url", Toast.LENGTH_SHORT).show();
                    } else
                    {
                        if (!Patterns.WEB_URL.matcher(inputUrl.getText().toString().trim()).matches())
                        {
                            //TODO: make a snack bar here
                            Toast.makeText(CreateNoteActivity.this, "Enter a valid URL", Toast.LENGTH_SHORT).show();
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