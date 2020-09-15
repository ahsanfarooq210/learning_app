package com.example.learningapp.RvAdapters

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.learningapp.Entity.SavedPages
import com.example.learningapp.R
import com.example.learningapp.WebViewSupport.Show_saved_pages
import com.example.learningapp.webViewes.Show_saved_pages_web_view
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class ShowSavedRvAdapter : RecyclerView.Adapter<ShowSavedRvAdapter.ViewHolder>
{
    private var list: ArrayList<SavedPages>
    private var context: Activity
    private var upperLayout: LinearLayout? = null

    constructor(list: ArrayList<SavedPages>, context: Activity)
    {
        this.list = list
        this.context = context
    }

    constructor(list: ArrayList<SavedPages>, context: Activity, upperLayout: LinearLayout?)
    {
        this.list = list
        this.context = context
        this.upperLayout = upperLayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val v = LayoutInflater.from(context).inflate(R.layout.show_saved_rv_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.title.text = list[position].title
        holder.url.text = list[position].url
    }

    override fun getItemCount(): Int
    {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var title: TextView
        var url: TextView
        var delete: ImageView

        init
        {
            title = itemView.findViewById(R.id.show_saved_layout_title)
            url = itemView.findViewById(R.id.show_saved_layout_url)
            delete = itemView.findViewById(R.id.show_saved_layout_deleteimg)
            delete.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Are you sure you want to delete this bookmark").setCancelable(true)
                        .setPositiveButton("Yes") { dialog, which ->
                            val savedPages = list[adapterPosition]
                            val user = FirebaseAuth.getInstance().currentUser
                            val firestore = FirebaseFirestore.getInstance()
                            val documentReference = firestore.collection(context.getString(R.string.firestore_collection_saved_pages))
                                    .document(user!!.uid).collection(context.getString(R.string.saved_pages_collection_path)).document(savedPages.id)
                            documentReference.delete().addOnSuccessListener {
                                Snackbar.make(upperLayout!!, "Deleted successfully", BaseTransientBottomBar.LENGTH_SHORT).show()
                                context.startActivity(Intent(context, Show_saved_pages::class.java))
                                context.finish()
                            }.addOnFailureListener { Snackbar.make(upperLayout!!, "Error while deleting", BaseTransientBottomBar.LENGTH_SHORT).show() }
                        }
                        .setNegativeButton("No") { dialog, which -> dialog.cancel() }
                val alertDialog = builder.create()
                alertDialog.show()
            }
            itemView.setOnClickListener(View.OnClickListener
            {
                var link: String = list.get(adapterPosition).url

                var intent = Intent(context, Show_saved_pages_web_view::class.java).apply {

                    putExtra("the url", link)
                }



                context.startActivity(intent)


            })
        }
    }
}