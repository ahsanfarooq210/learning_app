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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class Books_web_view extends AppCompatActivity
{
    private ProgressBar progressBar;
    private ImageView websiteLogo;
    private WebView webView;
    private LinearLayout upperLayout;
    private String myUrl;
    private Bundle bundle;
    private String link;
    private FirebaseFirestore firestore;
    private CollectionReference collectionReference;
    private FirebaseUser user;
    private String myTitle;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_web_view);

        bundle = getIntent().getBundleExtra("Bundle");

        if (bundle != null)
        {
            String languange = bundle.getString(getString(R.string.bundle_language_reference));
            assert languange != null;
            link = decideLink(languange);
        } else
        {
            link = "https://www.google.com/";
        }

        firestore = FirebaseFirestore.getInstance();
        collectionReference = firestore.collection(getString(R.string.firestore_collection_saved_pages));

        user = FirebaseAuth.getInstance().getCurrentUser();

        progressBar = findViewById(R.id.books_progress_bar);
        websiteLogo = findViewById(R.id.books_logo);
        webView = findViewById(R.id.books_web_view);


        progressBar.setMax(100);

        //webView.getSettings().setCacheMode( WebSettings.LOAD_CACHE_ELSE_NETWORK ); // to load the offline saved page
        webView.loadUrl(link);//to do add the url in the function

        upperLayout = findViewById(R.id.books_web_view_upper_layout);


        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                upperLayout.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                upperLayout.setVisibility(View.GONE);
                myUrl = url;
                super.onPageFinished(view, url);

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
            public void onReceivedIcon(WebView view, Bitmap icon)
            {
                super.onReceivedIcon(view, icon);
                websiteLogo.setImageBitmap(icon);
            }

            @Override
            public void onReceivedTitle(WebView view, String title)
            {
                super.onReceivedTitle(view, title);
                myTitle = title;
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
                Toast.makeText(Books_web_view.this, "Your file is downloading", Toast.LENGTH_SHORT).show();
            }
        });

//        webView.getSettings().setAppCachePath( getApplicationContext().getCacheDir().getAbsolutePath() );
//        webView.getSettings().setAllowFileAccess( true );
//        webView.getSettings().setAppCacheEnabled( true );
//        webView.getSettings().setCacheMode( WebSettings.LOAD_DEFAULT );


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.web_view_menu, menu);
        getSupportActionBar().setTitle("");
        return super.onCreateOptionsMenu(menu);

    }

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
        //TODO: Add the open notes acivity in books web view
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
        collectionReference.document(user.getUid()).collection(getString(R.string.saved_pages_collection_path)).add(savedPages).addOnSuccessListener(new OnSuccessListener<DocumentReference>()
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
            Toast.makeText(Books_web_view.this, "Can't go furhter", Toast.LENGTH_SHORT).show();
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

    private String decideLink(String language)
    {
        String link;


        switch (language)
        {
            case "c++":
                link = getString(R.string.pdf_drive_cpp);
                break;
            case "java":
                link = getString(R.string.pdf_drive_java);
                break;
            case "python":
                link = getString(R.string.pdf_drive_python);
                break;
            case "java script":
                link = getString(R.string.pdf_drive_javaScript);
                break;
            case "c#":
                link = getString(R.string.pdf_drive_csharp);
                break;
            default:
                link = "https://www.google.com/";
                break;
        }
        return link;
    }
}

