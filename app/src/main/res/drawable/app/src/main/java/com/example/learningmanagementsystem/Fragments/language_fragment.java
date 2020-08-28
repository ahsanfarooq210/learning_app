package com.example.learningmanagementsystem.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.learningmanagementsystem.Adapters.Language_rv_adapter;
import com.example.learningmanagementsystem.Entities.LanguageDashboardEntity;
import com.example.learningmanagementsystem.R;

import java.util.ArrayList;

public class language_fragment extends Fragment
{
    private RecyclerView recyclerView;


    public language_fragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_language_fragment, container, false);
        recyclerView=v.findViewById(R.id.fragment_language_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<LanguageDashboardEntity> list=new ArrayList<>();
        LanguageDashboardEntity l1=new LanguageDashboardEntity(getString(R.string.language_cpp),"click here \n to learn c++",R.drawable.icons8_cpp_100px);
        list.add(l1);
        l1=new LanguageDashboardEntity(getString(R.string.language_java),"click here \n to learn java",R.drawable.icons8_java_64px);
        list.add(l1);
        l1=new LanguageDashboardEntity(getString(R.string.language_python),"click here \n to learn python",R.drawable.icons8_python_64px);
        list.add(l1);
        l1=new LanguageDashboardEntity(getString(R.string.language_php),"click here \\n to learn php",R.drawable.icons8_php_52px);
        list.add(l1);
        l1=new LanguageDashboardEntity(getString(R.string.language_java_script),"click here \n to learn java Script",R.drawable.icons8_javascript_60px);
        list.add(l1);
        Language_rv_adapter adapter=new Language_rv_adapter(getActivity(),list,getContext());
        recyclerView.setAdapter(adapter);


        return  v;
    }

}