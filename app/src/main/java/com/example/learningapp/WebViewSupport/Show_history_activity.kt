package com.example.learningapp.WebViewSupport

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learningapp.Database.Database
import com.example.learningapp.HelperClasses.History
import com.example.learningapp.R
import com.example.learningapp.RvAdapters.history_rv_adapter

class Show_history_activity : AppCompatActivity()
{
    private var upperlayout: LinearLayout? = null
    private var recyclerView: RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_history_activity)

        upperlayout = findViewById(R.id.show_history_upper_layout)
        recyclerView = findViewById(R.id.show_history_recycler_view);
        recyclerView!!.layoutManager = LinearLayoutManager(this)

    }

    override fun onStart()
    {
        super.onStart()

        val his = History()
        val list = his.getUrl(this)

        val adapter = history_rv_adapter(list as ArrayList<String>, this)
        recyclerView!!.adapter = adapter

    }

    fun clearhistory(view: View)
    {
        val his = History()
        his.historyClear(this)
        startActivity(Intent(this, Show_history_activity::class.java))
        finish()
    }
}