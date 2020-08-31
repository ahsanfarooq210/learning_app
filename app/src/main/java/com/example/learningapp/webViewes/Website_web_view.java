package com.example.learningapp.webViewes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.learningapp.R;

public class Website_web_view extends AppCompatActivity
{

    private ProgressBar progressBar;
    private ImageView websiteLogo;
    private WebView webView;
    private LinearLayout upperLayout;
    private SwipeRefreshLayout refreshLayout;
    private String myUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website_web_view);

        progressBar=findViewById(R.id.website_progress_bar);
        websiteLogo=findViewById(R.id.website_logo);
        webView=findViewById(R.id.website_web_view);

        progressBar.setMax(100);
        webView.loadUrl("http://www.google.com/");  //TO_DO get the website and the language from the user and put the link accordingly

        refreshLayout=findViewById(R.id.web_view_swip_layout);
        upperLayout=findViewById(R.id.web_view_upper_layout);

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
                myUrl=url;
                refreshLayout.setRefreshing(false);
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
            public void onReceivedTitle(WebView view, String title)
            {
                super.onReceivedTitle(view, title);

            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon)
            {
                super.onReceivedIcon(view, icon);
                websiteLogo.setImageBitmap(icon);
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                webView.reload();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.web_view_menu,menu);
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
                Intent shareintent=new Intent(Intent.ACTION_SEND);
                shareintent.setType("text/plain");
                shareintent.putExtra(Intent.EXTRA_TEXT,myUrl);
                shareintent.putExtra(Intent.EXTRA_SUBJECT,"Copied Url");
                startActivity(Intent.createChooser(shareintent,"Share url with friends"));
        }
        return super.onOptionsItemSelected(item);
    }

    private void onForwardPressed()
    {
        if(webView.canGoForward())
        {
            webView.goForward();
        }
        else
        {
            Toast.makeText(Website_web_view.this,"Can't go furhter",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed()
    {
       if(webView.canGoBack())
       {
           webView.goBack();
       }
       else
        {
            finish();
        }
    }
}