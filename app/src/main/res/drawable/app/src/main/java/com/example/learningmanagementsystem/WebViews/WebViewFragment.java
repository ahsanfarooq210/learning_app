package com.example.learningmanagementsystem.WebViews;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.learningmanagementsystem.R;


public class WebViewFragment extends Fragment
{


    private WebView webView;
    private Bundle bundle;
    private String language,medium;

    public WebViewFragment()
    {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_web_view, container, false);

        //initializing the web view
        webView=v.findViewById(R.id.web_view);
        webView.setWebViewClient(new WebViewClient());

        //getting the bundle
        bundle=getArguments();

        //getting the data from the bundle





        return v;
    }
}