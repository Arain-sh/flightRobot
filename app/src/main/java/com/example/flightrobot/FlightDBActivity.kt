package com.example.flightrobot

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_tasks.*
import rxhttp.RxHttp
import taskResponse
import java.util.*


class FlightDBActivity : AppCompatActivity() {
    companion object {
        fun startForResult(activity: Activity, requestCode: Int = MainActivity.REQUEST_LOGIN) {
            val intent = Intent(activity, FlightDBActivity::class.java)
            activity.startActivityForResult(intent, requestCode)
        }
    }

    public var mesActivity: Activity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_tasks)

        Objects.requireNonNull(getSupportActionBar())?.setDisplayHomeAsUpEnabled(true)
        var dbid: String = intent.getStringExtra("id")!!
        var taskList: List<taskResponse.Data> ?= null


        // kotlin
        RxHttp.get(this.getString(R.string.default_url) + "/api/v1/tasks")
            .asString()
            .subscribe({ s ->
                try {
                    var s: taskResponse = Gson().fromJson(s, taskResponse::class.java)
                    //println(s.data.get(1))
                    taskList = s.data
                    for (i in s.data.indices) {
                        println("s = ${s.data.get(i).name}")
                    }
                    runOnUiThread {
                        //这里面进行UI的更新操作
                        //使用Recycler
                        val layoutManager = GridLayoutManager(this, 3)
                        mRecycler.layoutManager = layoutManager
                        val adapter = RecyclerAdapter(taskList!!)
                        mRecycler.adapter = adapter
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