package com.example.learningapp.learningMedium;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.learningapp.R;
import com.example.learningapp.webViewes.Youtube_web_view;


public class select_medium_fragment extends Fragment implements View.OnClickListener
{

    private CardView youtube_card, website_card, books_card;
    private Bundle bundle;
    private String medium;


    public select_medium_fragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_select_medium_fragment, container, false);

        youtube_card = v.findViewById(R.id.youtube_card);
        website_card = v.findViewById(R.id.website_card);
        books_card = v.findViewById(R.id.books_card);

        bundle = getArguments();

        youtube_card.setOnClickListener(this);
        website_card.setOnClickListener(this);
        books_card.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.youtube_card:
                medium = getString(R.string.youtube_medium);
                bundle.putString(getString(R.string.bundle_medium_reference), medium);
                Intent intent = new Intent(getContext(), Youtube_web_view.class);
                intent.putExtra("Bundle", bundle);
                getActivity().startActivity(intent);
                break;

            case R.id.website_card:
                medium = getString(R.string.website_medium);
                select_website_fragment selectWebsiteFragment = new select_website_fragment();
                bundle.putString(getString(R.string.bundle_medium_reference), medium);
                selectWebsiteFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.dashboard_main_frame_layout, selectWebsiteFragment);
                break;

            case R.id.books_card:
                medium = getString(R.string.book_medium);

                break;
        }
    }


}