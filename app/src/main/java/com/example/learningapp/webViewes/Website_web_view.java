package com.example.learningapp.webViewes;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.learningapp.Entity.HistoryEntity;
import com.example.learningapp.Entity.SavedPages;
import com.example.learningapp.HelperClasses.History;
import com.example.learningapp.HelperClasses.WebCommonFunctions;
import com.example.learningapp.R;
import com.example.learningapp.notes.activities.Notes_Main_Class;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Website_web_view extends AppCompatActivity
{

    private ProgressBar progressBar;
    private ImageView websiteLogo;
    private WebView webView;
    private LinearLayout upperLayout;
    private String myUrl;
    private String link;
    private FirebaseFirestore firestore;
    private CollectionReference collectionReference;
    private FirebaseUser user;
    private String myTitle;

    private Runnable runnable = new Runnable()
    {
        @Override
        public void run()
        {
            History history = new History();
            HistoryEntity his = new HistoryEntity(myUrl);
            history.savehistory(his, Website_web_view.this);
        }
    };

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website_web_view);

        Bundle bundle = getIntent().getBundleExtra("Bundle");
        if (bundle != null)
        {
            String language = bundle.getString(getString(R.string.bundle_language_reference));
            String website = bundle.getString(getString(R.string.bundle_website_name_reference));
            assert language != null;
            link = WebCommonFunctions.decideLink(language, website, Website_web_view.this);
        } else
        {

            link = "http://www.google.com/";

        }

        firestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        collectionReference = firestore.collection(user.getUid());


        progressBar = findViewById(R.id.website_progress_bar);
        websiteLogo = findViewById(R.id.website_logo);
        webView = findViewById(R.id.website_web_view);

        progressBar.setMax(100);
        webView.loadUrl(link);  //TO_DO get the website and the language from the user and put the link accordingly


        upperLayout = findViewById(R.id.web_view_upper_layout);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient()
        {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                upperLayout.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
                myUrl = url;
                runnable.run();
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                upperLayout.setVisibility(View.GONE);
                myUrl = url;
                super.onPageFinished(view, url);
                //runnable.run();

            }
        });
        webView.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public void onProgressChanged(WebView view, int newProgress)
            {
                super.onProgressChanged(view, newProgress);

                progressBar.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title)
            {
                super.onReceivedTitle(view, title);
                myTitle = title;
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon)
            {
                super.onReceivedIcon(view, icon);
                websiteLogo.setImageBitmap(icon);
            }
        });


        webView.setDownloadListener(new DownloadListener()
        {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength)
            {
                DownloadManager.Request myRequest = new DownloadManager.Request(Uri.parse(url));
                myRequest.allowScanningByMediaScanner();
                myRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);


                DownloadManager myManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                myManager.enqueue(myRequest);
                Toast.makeText(Website_web_view.this, "Your file is downloading", Toast.LENGTH_SHORT).show();


            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.web_view_menu, menu);
        getSupportActionBar().setTitle("");
        return super.onCreateOptionsMenu(menu);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.wewb_view_menu_back:
                onBackPressed();
                break;
            case R.id.web_view_menu_forward:
                onForwardPressed();
                break;
            case R.id.web_view_menu_refresh:
                webView.reload();
                break;
            case R.id.web_view_share:
                share();
                break;
            case R.id.web_view_save:
                savePage();
                break;
            case R.id.web_view_menu_cancel:
                exitWebView();
                break;
            case R.id.web_view_menu_go_notes:
                openNotes();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openNotes()
    {
        startActivity(new Intent(getBaseContext(), Notes_Main_Class.class));
    }

    private void share()
    {
        Intent shareintent = new Intent(Intent.ACTION_SEND);
        shareintent.setType("text/plain");
        shareintent.putExtra(Intent.EXTRA_TEXT, myUrl);
        shareintent.putExtra(Intent.EXTRA_SUBJECT, "Copied Url");
        startActivity(Intent.createChooser(shareintent, "Share url with friends"));
    }

    private void exitWebView()
    {
        finish();
    }

    private void savePage()
    {

        SavedPages savedPages = new SavedPages(myTitle, myUrl);

//        boolean flag=WebCommonFunctions.savePages(savedPages,Website_web_view.this);
//        if(flag)
//        {
//            Snackbar.make(upperLayout, "Page saved successfully", BaseTransientBottomBar.LENGTH_SHORT).show();
//        }
//        else
//        {
//            Snackbar.make(upperLayout, "Failed. Try again", BaseTransientBottomBar.LENGTH_SHORT).show();
//        }
        collectionReference.document(getString(R.string.firestore_collection_saved_pages)).collection("saved").add(savedPages).addOnSuccessListener(new OnSuccessListener<DocumentReference>()
        {
            @Override
            public void onSuccess(DocumentReference documentReference)
            {
                Snackbar.make(upperLayout, "Page saved successfully", BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Snackbar.make(upperLayout, "Failed. Try again", BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });
    }

    private void onForwardPressed()
    {
        if (webView.canGoForward())
        {
            webView.goForward();
        } else
        {
            Toast.makeText(Website_web_view.this, "Can't go furhter", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed()
    {
        if (webView.canGoBack())
        {
            webView.goBack();
        } else
        {
            finish();
        }
    }
}