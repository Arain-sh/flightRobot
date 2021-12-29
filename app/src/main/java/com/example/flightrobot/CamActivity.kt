package com.example.flightrobot

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.nfc.Tag
import android.os.Bundle
import android.provider.MediaStore.Images.Media.getBitmap
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import coil.api.load
import kotlinx.android.synthetic.main.activity_cam.*
import ph.ingenuity.tableview.TableView
import ph.ingenuity.tableview.feature.filter.Filter
import ph.ingenuity.tableview.feature.pagination.Pagination
import ph.ingenuity.tableviewdemo.data.RandomDataFactory
import ph.ingenuity.tableviewdemo.listeners.TableViewListener
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.concurrent.fixedRateTimer


class CamActivity : AppCompatActivity() {
    companion object {
        fun startForResult(activity: Activity, requestCode: Int = MainActivity.REQUEST_LOGIN) {
            val intent = Intent(activity, CamActivity::class.java)
            activity.startActivityForResult(intent, requestCode)
        }
    }

    private lateinit var previousButton: Button
    lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cam)
        Objects.requireNonNull(getSupportActionBar())!!.setDisplayHomeAsUpEnabled(true)
        var nDialog: ProgressDialog
        nDialog = ProgressDialog(this)
        nDialog.setMessage("Loading..")
        nDialog.setTitle("数据库加载中...")
        nDialog.isIndeterminate = false
        nDialog.setCancelable(true)
        nDialog.show()
        Thread.sleep(250)
        nDialog.dismiss()

        var cam01_img: ImageView = findViewById(R.id.cam01_rt)
        var cam02_img: ImageView = findViewById(R.id.cam02_rt)
        //var url = "http://192.168.1.198:8080/shareVideo0/realTime.jpg"
        //var url = "http://192.168.1.198:8080/shareVideo0/realTime.jpg"
        var url = "http://n.sinaimg.cn/sinacn00/63/w640h1023/20181007/603f-hkrzvkw0220959.jpg"
        var url01 = "https://img30.51tietu.net/pic/2016-083109/20160831091954esfeb42hytk132443.jpg"
        //var url01 = "http://192.168.1.198:8080/shareVideo1/realTime.jpg"

        runOnUiThread {
            timer = fixedRateTimer("", false, 0, 500) {
                try {
                    cam01_img.load(url)
                    cam02_img.load(url01)
                } catch (e: Exception) {

                }
            }
        }

        //init
        initializeViews()
        // Retrieve your data from local storage or API
        initializeData()
        try {
        } catch(e: Exception) {
        }


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