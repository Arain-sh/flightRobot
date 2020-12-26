package com.example.flightrobot

import actionResponse
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import com.example.flightrobot.models.operationResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_operation.*
import kotlinx.android.synthetic.main.fragment_taskinfo.*
import kotlinx.android.synthetic.main.fragment_taskinfo.actionRecycler
import rxhttp.RxHttp
import java.util.*

class OperationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_operation)
        Objects.requireNonNull(getSupportActionBar())?.setDisplayHomeAsUpEnabled(true)
        var action_id: Int = intent.getIntExtra("action_id", 1)

        // kotlin
        RxHttp.postForm(this.getString(R.string.default_url) + "/api/v1/operations")
            .add("action_id", action_id)
            .asString()
            .subscribe({ s ->
                try {
                    var s: operationResponse = Gson().fromJson(s, operationResponse::class.java)
                    var operationList = s.data
                    operationList?.let{runOnUiThread {
                        //这里面进行UI的更新操作
                        //使用Recycler
                        val layoutManager = GridLayoutManager(this, 3)
                        operationRecycler.layoutManager = layoutManager
                        val adapter = OperationRecyclerAdapter(operationList)
                        operationRecycler.adapter = adapter
                    }}

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