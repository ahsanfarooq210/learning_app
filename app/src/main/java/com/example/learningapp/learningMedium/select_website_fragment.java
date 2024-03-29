package com.example.learningapp.learningMedium;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.learningapp.R;
import com.example.learningapp.webViewes.Website_web_view;

import java.security.PrivateKey;


public class select_website_fragment extends Fragment implements View.OnClickListener
{

    private CardView tutorialsPointCard, w3Schoolscard, geeksForGeeksCard;
    private String website;
    private Bundle bundle;

    public select_website_fragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_select_website_fragment, container, false);
        tutorialsPointCard = v.findViewById(R.id.tutorials_point_card);
        w3Schoolscard = v.findViewById(R.id.w3_schools_card);
        geeksForGeeksCard = v.findViewById(R.id.geeks_for_geeks__card);

        tutorialsPointCard.setOnClickListener(this);
        w3Schoolscard.setOnClickListener(this);
        geeksForGeeksCard.setOnClickListener(this);

        bundle = getArguments();

        return v;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.tutorials_point_card:
                website = getString(R.string.bundle_website_reference_tutorials_point);
                next();
                break;

            case R.id.w3_schools_card:
                website = getString(R.string.bundle_reference_website_w3_schools);
                next();
                break;

            case R.id.geeks_for_geeks__card:
                website = getString(R.string.bundle_reference_website_geeks_for_geeeks);
                next();
                break;


        }
    }

    public void next()
    {
        Intent intent = new Intent(getContext(), Website_web_view.class);
        bundle.putString(getString(R.string.bundle_website_name_reference), website);
        intent.putExtra("Bundle", bundle);
        getActivity().startActivity(intent);
    }
}