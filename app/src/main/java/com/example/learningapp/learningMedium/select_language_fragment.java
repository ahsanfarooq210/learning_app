package com.example.learningapp.learningMedium;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.learningapp.R;


public class select_language_fragment extends Fragment implements View.OnClickListener
{

    private CardView cppCard,javaCard,pythonCard,javaScriptCard,cSharpCard;
    private String language;

    public select_language_fragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_select_language_fragment, container, false);

        //setting all the cards
        cppCard=v.findViewById(R.id.cpp_card);
        javaCard=v.findViewById(R.id.java_card);
        pythonCard=v.findViewById(R.id.python_card);
        javaScriptCard=v.findViewById(R.id.javascript_card);
        cSharpCard=v.findViewById(R.id.csharp_card);

        //setting all the on click listinners
        cppCard.setOnClickListener(this);
        javaCard.setOnClickListener(this);
        pythonCard.setOnClickListener(this);
        javaScriptCard.setOnClickListener(this);
        cSharpCard.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.cpp_card:
                language=getString(R.string.cpp_language);
                nextFragemnt();
                break;

            case R.id.java_card:
                language=getString(R.string.java_language);
                nextFragemnt();
                break;

            case R.id.python_card:
                language=getString(R.string.python_language);
                nextFragemnt();
                break;

            case R.id.javascript_card:
                language=getString(R.string.javascript_language);
                nextFragemnt();
                break;

            case R.id.csharp_card:
                language=getString(R.string.csharp_language);
                nextFragemnt();
                break;
        }
    }

    private void nextFragemnt()
    {
        Bundle bundle=new Bundle();
        bundle.putString(getString(R.string.bundle_language_reference),language);
        select_medium_fragment selectMediumFragment=new select_medium_fragment();
        selectMediumFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.dashboard_main_frame_layout,selectMediumFragment).commit();
    }
}