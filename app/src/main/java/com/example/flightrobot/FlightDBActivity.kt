package com.example.flightrobot

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class FlightDBActivity : AppCompatActivity() {
    companion object {
        fun startForResult(activity: Activity, requestCode: Int = MainActivity.REQUEST_LOGIN) {
            val intent = Intent(activity, FlightDBActivity::class.java)
            activity.startActivityForResult(intent, requestCode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_tasks)
        Objects.requireNonNull(getSupportActionBar())?.setDisplayHomeAsUpEnabled(true)
        var dbid: String = intent.getStringExtra("id")!!
        val taskbutton001: Button = findViewById(R.id.task001)
        val taskbutton002: Button = findViewById(R.id.task002)
        val taskbutton003: Button = findViewById(R.id.task003)
        taskbutton001.setOnClickListener {
            val it = Intent(this, TaskActivity::class.java)
            val taskInfo: String = taskbutton001.text.toString()
            it.putExtra("info", taskInfo)
            startActivity(it)
        }

        taskbutton002.setOnClickListener {
            val it = Intent(this, TaskActivity::class.java)
            val taskInfo: String = taskbutton001.text.toString()
            it.putExtra("info", taskInfo)
            startActivity(it)
        }

        taskbutton003.setOnClickListener {
            val it = Intent(this, TaskActivity::class.java)
            val taskInfo: String = taskbutton001.text.toString()
            it.putExtra("info", taskInfo)
            startActivity(it)
        }

        window.setFlags(
            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === android.R.id.home) {
            finish() // finish your activity
        }
        return super.onOptionsItemSelected(item)
    }
}