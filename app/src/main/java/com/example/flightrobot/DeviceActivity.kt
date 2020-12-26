package com.example.flightrobot

import android.app.Activity
import android.app.PendingIntent.getActivity
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
import com.example.flightrobot.models.airinfoResponse
import com.example.flightrobot.models.sysinfoResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_pdf_view.*
import kotlinx.android.synthetic.main.table_row_header_text_data.*
import ph.ingenuity.tableview.TableView
import ph.ingenuity.tableview.feature.filter.Filter
import ph.ingenuity.tableview.feature.pagination.Pagination
import ph.ingenuity.tableviewdemo.data.RandomDataFactory
import ph.ingenuity.tableviewdemo.listeners.TableViewListener
import rxhttp.RxHttp
import java.util.*


class DeviceActivity : AppCompatActivity() {
    companion object {
        fun startForResult(activity: Activity, requestCode: Int = MainActivity.REQUEST_LOGIN) {
            val intent = Intent(activity, DeviceActivity::class.java)
            activity.startActivityForResult(intent, requestCode)
        }
    }


    private lateinit var previousButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)
        Objects.requireNonNull(getSupportActionBar())?.setDisplayHomeAsUpEnabled(true)

        val hxj: Button = findViewById(R.id.hxj_status)
        val jxb: Button = findViewById(R.id.jxb_status)
        val sjxt: Button =findViewById(R.id.sjxt_status)
        val cam_01: Button = findViewById(R.id.cam_01_status)
        val cam_02: Button = findViewById(R.id.cam_02_status)
        val cam_03: Button =findViewById(R.id.cam_03_status)
        val cpu: Button = findViewById(R.id.cpu_status)
        val card: Button = findViewById(R.id.card_status)
        val graph: Button =findViewById(R.id.graph_status)

        //获取参数
        // kotlin
        RxHttp.get(this.getString(R.string.default_url) + "/api/v1/sysinfos")
            .asString()
            .subscribe({ s ->
                try {
                    var s: sysinfoResponse = Gson().fromJson(s, sysinfoResponse::class.java)

                    var sysinfo = s.data.get(0)
                    println("SYSLOG: " + sysinfo)

                    runOnUiThread {
                        sysinfo?.let{
                            cpu.text = sysinfo.cpu
                            card.text = sysinfo.card
                            if (sysinfo.jxb_hxj == 0) {
                                hxj.text = "离线"
                            } else hxj.text = "正常"
                            if (sysinfo.jxb_jxb == 0) jxb.text = "离线"
                            else jxb.text = "正常"
                            if (sysinfo.jxb_sj == 0) sjxt.text = "离线"
                            else sjxt.text = "正常"
                            if (sysinfo.sxt_01 == 0) cam_01.text = "离线"
                            else cam_01.text = "正常"
                            if (sysinfo.sxt_02 == 0) cam_02.text = "离线"
                            else cam_02.text = "正常"
                            if (sysinfo.sxt_03 == 0) cam_03.text = "离线"
                            else cam_03.text = "正常"
                            if (sysinfo.graph == 0) graph.text = "离线"
                            else graph.text = "正在运行"

                        }
                    }

                } catch (e: Exception) {
                    println(e)
                }
            }, { throwable ->
                println(throwable)
                println("Sys Log: cannot get data")
            })

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