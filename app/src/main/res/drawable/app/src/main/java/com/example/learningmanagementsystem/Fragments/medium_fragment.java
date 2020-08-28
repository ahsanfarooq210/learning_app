package com.example.learningmanagementsystem.Fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.learningmanagementsystem.R;
import com.example.learningmanagementsystem.WebViews.WebViewFragment;

import static com.example.learningmanagementsystem.R.string.*;


public class medium_fragment extends Fragment
{
    private Bundle bundle;
    private String language;
    private CardView youtube_card,website_card,books_card;
    private WebViewFragment webViewFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_medium_fragment, container, false);
        bundle=getArguments();

        language=bundle.getString(getString(R.string.bundle_language));

        //initializing the medium cards
        youtube_card=v.findViewById(R.id.youtube_card);
        website_card=v.findViewById(R.id.websites_card);
        books_card=v.findViewById(R.id.books_card);
        webViewFragment=new WebViewFragment();

        //initializing hte cards on click listinner
        youtube_card.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                bundle.putString(getString(R.string.medium),getString(R.string.medium_value_youtube));
                launchFragment();

            }
        });

        website_card.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                bundle.putString(getString(R.string.medium),getString(R.string.medium_value_website));
                launchFragment();

            }
        });

        books_card.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                bundle.putString(getString(R.string.medium),getString(R.string.medium_value_books));
                launchFragment();

            }
        });


        

        return v;
    }

    //function to launch the web view fragment and to send the bundle containing language and the medium
    private void launchFragment()
    {
        webViewFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_dashboard_frame_layout,webViewFragment)
                .addToBackStack("webViewFragment").commit();
    }
}