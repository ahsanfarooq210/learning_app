package com.example.learningapp.webViewes;

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

import com.example.learningapp.R;

public class Youtube_web_view extends AppCompatActivity
{
    private ProgressBar progressBar;
    private ImageView websiteLogo;
    private WebView webView;
    private LinearLayout upperLayout;
    private String myUrl;
    private Bundle bundle;
    private String link;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_web_view);

        bundle = getIntent().getBundleExtra("Bundle");
        if (bundle != null)
        {
            String language = bundle.getString(getString(R.string.bundle_language_reference));
            switch (language)
            {
                case "c++":
                    link = getString(R.string.youtube_tutorial_cpp);
                    break;

                case "java":
                    link = getString(R.string.youtube_tutorial_java);
                    break;

                case "python":
                    link = getString(R.string.youtube_tutorial_python);
                    break;

                case "java script":
                    link = getString(R.string.youtube_tutorial_javascript);
                    break;

                case "c#":
                    link = getString(R.string.youtube_tutorial_csharp);
                    break;
            }

        } else
        {
            link = "http://www.google.com/";
        }

        progressBar = findViewById(R.id.youtube_website_progress_bar);
        websiteLogo = findViewById(R.id.youtube_website_logo);
        webView = findViewById(R.id.youtube_web_view);

        progressBar.setMax(100);
        webView.loadUrl(link);


        upperLayout = findViewById(R.id.youtube_web_view_upper_layout);

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
                Toast.makeText(Youtube_web_view.this, "Your file is downloading", Toast.LENGTH_SHORT).show();
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
                Intent shareintent = new Intent(Intent.ACTION_SEND);
                shareintent.setType("text/plain");
                shareintent.putExtra(Intent.EXTRA_TEXT, myUrl);
                shareintent.putExtra(Intent.EXTRA_SUBJECT, "Copied Url");
                startActivity(Intent.createChooser(shareintent, "Share url with friends"));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onForwardPressed()
    {
        if (webView.canGoForward())
        {
            webView.goForward();
        } else
        {
            Toast.makeText(Youtube_web_view.this, "Can't go furhter", Toast.LENGTH_SHORT).show();
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