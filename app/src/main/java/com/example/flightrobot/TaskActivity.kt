package com.example.flightrobot

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.flightrobot.models.actionResponse
import com.example.flightrobot.models.taskResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_taskinfo.*
import kotlinx.android.synthetic.main.fragment_tasks.*
import kotlinx.android.synthetic.main.fragment_tasks.mRecycler
import rxhttp.RxHttp
import java.lang.Thread.sleep
import java.util.*


class TaskActivity : AppCompatActivity() {
    companion object {
        fun startForResult(activity: Activity, requestCode: Int = MainActivity.REQUEST_LOGIN) {
            val intent = Intent(activity, TaskActivity::class.java)
            activity.startActivityForResult(intent, requestCode)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_taskinfo)
        Objects.requireNonNull(getSupportActionBar())?.setDisplayHomeAsUpEnabled(true)
        var task_id: Int = intent.getIntExtra("task_id", 1)
        var nDialog: ProgressDialog
        val action_search = findViewById<SearchView>(R.id.action_search)
        var filterList : MutableList<actionResponse.Data> = mutableListOf()
        nDialog = ProgressDialog(this)
        nDialog.setMessage("Loading..")
        nDialog.setTitle("数据加载中...")
        nDialog.isIndeterminate = false
        nDialog.setCancelable(true)
        nDialog.show()

        // kotlin
        RxHttp.postForm(this.getString(R.string.default_url) + "/api/v1/actions")
            .add("task_id", task_id)
            .asString()
            .subscribe({ s ->
                try {
                    var s: actionResponse = Gson().fromJson(s, actionResponse::class.java)
                    var actionList = s.data
                    runOnUiThread {
                        //这里面进行UI的更新操作
                        //使用Recycler
                        val layoutManager = GridLayoutManager(this, 3)
                        actionRecycler.layoutManager = layoutManager
                        val adapter = ActionRecyclerAdapter(actionList)
                        actionRecycler.adapter = adapter
                        sleep(250)
                        nDialog.dismiss()

                        action_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                            override fun onQueryTextSubmit(query: String?): Boolean {
                                return false
                            }

                            override fun onQueryTextChange(newText: String?): Boolean {
                                filterList = fil(newText!!)
                                actionRecycler.adapter = ActionRecyclerAdapter(filterList)
                                return false
                            }
                            fun fil(constraint: String) : MutableList<actionResponse.Data> {
                                val charSearch = constraint
                                if (charSearch.isEmpty()) {
                                    filterList = actionList
                                } else {
                                    val resultList : MutableList<actionResponse.Data> = mutableListOf()
                                    for (row in actionList) {
                                        if (row.name.contains(charSearch) or row.id.toString().contains(charSearch)) {
                                            resultList.add(row)
                                        }
                                    }
                                    filterList = resultList
                                }
                                return filterList
                            }
                        })
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