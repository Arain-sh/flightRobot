package com.example.flightrobot

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.rajat.pdfviewer.PdfViewerActivity
import kotlinx.android.synthetic.main.fragment_taskinfo.*
import rxhttp.RxHttp
import java.util.*


class FileViewActivity : AppCompatActivity() {
    companion object {
        fun startForResult(activity: Activity, requestCode: Int = MainActivity.REQUEST_LOGIN) {
            val intent = Intent(activity, FileViewActivity::class.java)
            activity.startActivityForResult(intent, requestCode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_view)
        Objects.requireNonNull(getSupportActionBar())?.setDisplayHomeAsUpEnabled(true)



        window.setFlags(
            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    override fun onBackPressed() {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === android.R.id.home) {
            finish() // finish your activity
        }
        return super.onOptionsItemSelected(item)
    }
}