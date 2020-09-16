package com.example.learningapp.RvAdapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.learningapp.R
import com.example.learningapp.webViewes.Show_saved_pages_web_view

class history_rv_adapter : RecyclerView.Adapter<history_rv_adapter.ViewHolder>
{


    private var list: ArrayList<String>? = null
    private var context: Context? = null

    constructor(list: ArrayList<String>, context: Context)
    {
        this.context = context
        this.list = list

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val v = LayoutInflater.from(context).inflate(R.layout.show_history_rv_layout, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.url!!.text = list!!.get(position)
        holder.index!!.text = "$position"
    }

    override fun getItemCount(): Int
    {
        return list!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var index: TextView? = null
        var url: TextView? = null


        init
        {
            index = itemView.findViewById(R.id.show_history_layout_index)
            url = itemView.findViewById(R.id.show_history_layout_url)

            itemView.setOnClickListener(View.OnClickListener
            {
                val link: String = list!![adapterPosition]

                var intent = Intent(context, Show_saved_pages_web_view::class.java).apply {

                    putExtra("the url", link)
                }



                context!!.startActivity(intent)

            })

        }

    }
}