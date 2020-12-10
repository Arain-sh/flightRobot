package com.example.flightrobot

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import ph.ingenuity.tableview.TableView
import ph.ingenuity.tableview.feature.filter.Filter
import ph.ingenuity.tableview.feature.pagination.Pagination
import ph.ingenuity.tableviewdemo.data.RandomDataFactory
import ph.ingenuity.tableviewdemo.listeners.TableViewListener
import java.util.*


class VideoActivity : AppCompatActivity() {
    companion object {
        fun startForResult(activity: Activity, requestCode: Int = MainActivity.REQUEST_LOGIN) {
            val intent = Intent(activity, VideoActivity::class.java)
            activity.startActivityForResult(intent, requestCode)
        }
    }

    private lateinit var previousButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        Objects.requireNonNull(getSupportActionBar())?.setDisplayHomeAsUpEnabled(true)

        //init
        initializeViews()
        // Retrieve your data from local storage or API
        initializeData()
        //init listener

        window.setFlags(
            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    private fun initializeViews() {
    }

    private fun initializeData() {
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === android.R.id.home) {
            finish() // finish your activity
        }
        return super.onOptionsItemSelected(item)
    }



}