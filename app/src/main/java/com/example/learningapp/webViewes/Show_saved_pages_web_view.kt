package com.example.learningapp.webViewes

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.learningapp.Entity.SavedPages
import com.example.learningapp.R
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

@Suppress("DEPRECATION")
class Show_saved_pages_web_view : AppCompatActivity()
{
    private var progressBar: ProgressBar? = null
    private var websiteLogo: ImageView? = null
    private var webView: WebView? = null
    private var upperLayout: LinearLayout? = null
    private var myUrl: String? = null
    private val bundle: Bundle? = null
    private var firestore: FirebaseFirestore? = null
    private var collectionReference: CollectionReference? = null
    private var user: FirebaseUser? = null
    private var myTitle: String? = null
    private var link: String? = null


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        //Show_saved_pages_web_view
        setContentView(R.layout.activity_show_saved_pages_web_view)

        val intent = intent;

        if (intent != null)
        {
            link = intent.getStringExtra("the url");
            if (link == null)
            {
                link = "https://www.google.com/"
            }

        }


        firestore = FirebaseFirestore.getInstance()
        collectionReference = firestore!!.collection(getString(R.string.firestore_collection_saved_pages))
        user = FirebaseAuth.getInstance().currentUser
        progressBar = findViewById(R.id.show_saved_progress_bar)
        websiteLogo = findViewById(R.id.show_saved_logo)
        webView = findViewById(R.id.show_saved_web_view)
        progressBar!!.max = 100

        //webView.getSettings().setCacheMode( WebSettings.LOAD_CACHE_ELSE_NETWORK ); // to load the offline saved page
        webView!!.loadUrl(link) //to do add the url in the function

        upperLayout = findViewById(R.id.show_saved_web_view_upper_layout)
        webView!!.settings.javaScriptEnabled = true
        webView!!.webViewClient = object : WebViewClient()
        {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?)
            {
                super.onPageStarted(view, url, favicon)
                upperLayout!!.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?)
            {
                super.onPageFinished(view, url)
                upperLayout!!.visibility = View.GONE
                myUrl = url
            }
        }
        webView!!.webChromeClient = object : WebChromeClient()
        {
            override fun onProgressChanged(view: WebView, newProgress: Int)
            {
                super.onProgressChanged(view, newProgress)
                progressBar!!.progress = newProgress
            }

            override fun onReceivedIcon(view: WebView, icon: Bitmap)
            {
                super.onReceivedIcon(view, icon)
                websiteLogo!!.setImageBitmap(icon)
            }

            override fun onReceivedTitle(view: WebView, title: String)
            {
                super.onReceivedTitle(view, title)
                myTitle = title
            }
        }
        webView!!.setDownloadListener { url, _, _, _, _ ->
            val myRequest = DownloadManager.Request(Uri.parse(url))
            myRequest.allowScanningByMediaScanner()
            myRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            val myManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            myManager.enqueue(myRequest)
            Toast.makeText(this@Show_saved_pages_web_view, "Your file is downloading", Toast.LENGTH_SHORT).show()
        }


//        webView.getSettings().setAppCachePath( getApplicationContext().getCacheDir().getAbsolutePath() );
//        webView.getSettings().setAllowFileAccess( true );
//        webView.getSettings().setAppCacheEnabled( true );
//        webView.getSettings().setCacheMode( WebSettings.LOAD_DEFAULT );
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.web_view_menu, menu)
        supportActionBar!!.title = ""
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when (item.itemId)
        {
            R.id.wewb_view_menu_back -> onBackPressed()
            R.id.web_view_menu_forward -> onForwardPressed()
            R.id.web_view_menu_refresh -> webView!!.reload()
            R.id.web_view_share -> shareFun()
            R.id.web_view_save -> savePage()
            R.id.web_view_menu_cancel -> finish()
            R.id.web_view_menu_go_notes -> openNotes()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun shareFun()
    {
        val shareintent = Intent(Intent.ACTION_SEND)
        shareintent.type = "text/plain"
        shareintent.putExtra(Intent.EXTRA_TEXT, myUrl)
        shareintent.putExtra(Intent.EXTRA_SUBJECT, "Copied Url")
        startActivity(Intent.createChooser(shareintent, "Share url with friends"))
    }

    private fun openNotes()
    {
        //TODO: Add the open notes acivity in show saved pages web view
    }

    private fun webViewExit()
    {
        finish();
    }

    private fun savePage()
    {
        val savedPages = SavedPages(myTitle, myUrl)
        collectionReference!!.document(user!!.uid).collection(getString(R.string.saved_pages_collection_path)).add(savedPages).addOnSuccessListener { Snackbar.make(upperLayout!!, "Page saved successfully", BaseTransientBottomBar.LENGTH_SHORT).show() }.addOnFailureListener { Snackbar.make(upperLayout!!, "Failed. Try again", BaseTransientBottomBar.LENGTH_SHORT).show() }
    }

    private fun onForwardPressed()
    {
        if (webView!!.canGoForward())
        {
            webView!!.goForward()
        } else
        {
            Toast.makeText(this@Show_saved_pages_web_view, "Can't go furhter", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed()
    {
        if (webView!!.canGoBack())
        {
            webView!!.goBack()
        } else
        {
            finish()
        }
    }

//    private fun decideLink(language: String): String {
//        val link: String
//        link = when (language) {
//            "c++" -> getString(R.string.pdf_drive_cpp)
//            "java" -> getString(R.string.pdf_drive_java)
//            "python" -> getString(R.string.pdf_drive_python)
//            "java script" -> getString(R.string.pdf_drive_javaScript)
//            "c#" -> getString(R.string.pdf_drive_csharp)
//            else -> "https://www.google.com/"
//        }
//        return link
//    }
}