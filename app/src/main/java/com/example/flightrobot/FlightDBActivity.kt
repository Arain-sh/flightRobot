package com.example.flightrobot

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.example.flightrobot.models.orderResponse
import com.example.flightrobot.models.taskResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_video.*
import kotlinx.android.synthetic.main.fragment_tasks.*
import orderlist
import rxhttp.RxHttp
import java.lang.Thread.sleep
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
        var list: List<Int> ?= null

        Objects.requireNonNull(getSupportActionBar())?.setDisplayHomeAsUpEnabled(true)
        var dbid: String = intent.getStringExtra("id")!!
        var taskList: MutableList<taskResponse.Data> = mutableListOf()
        val task_search = findViewById<SearchView>(R.id.task_search)
        var filterList : MutableList<taskResponse.Data> = mutableListOf()

        var nDialog: ProgressDialog
        nDialog = ProgressDialog(this)
        nDialog.setMessage("Loading..")
        nDialog.setTitle("数据库加载中...")
        nDialog.isIndeterminate = false
        nDialog.setCancelable(true)
        nDialog.show()

        var createList: Button = findViewById(R.id.createlist)
        createList.setOnClickListener {
            val it = Intent()
            it.setClass(this, CreateListActivity::class.java)
            startActivity(it)

            Toast.makeText(this, "...", Toast.LENGTH_SHORT).show();
        }

        // kotlin
        RxHttp.get(this.getString(R.string.default_url) + "/api/v1/tasks")
            .asString()
            .subscribe({ s ->
                try {
                    var s: taskResponse = Gson().fromJson(s, taskResponse::class.java)
                    taskList = s.data
                    taskList?.let {
                        for (i in s.data.indices) {
                            //println("s = ${s.data.get(i).name}")
                        }
                        this.runOnUiThread {
                            //这里面进行UI的更新操作
                            //使用Recycler
                            val layoutManager = GridLayoutManager(this, 3)
                            mRecycler.layoutManager = layoutManager
                            val adapter = RecyclerAdapter(taskList)
                            mRecycler.adapter = adapter
                            sleep(150)
                            nDialog.dismiss()
                            task_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                                override fun onQueryTextSubmit(query: String?): Boolean {
                                    return false
                                }

                                override fun onQueryTextChange(newText: String?): Boolean {
                                    filterList = fil(newText!!)
                                    mRecycler.adapter = RecyclerAdapter(filterList)
                                    return false
                                }
                                fun fil(constraint: String) : MutableList<taskResponse.Data> {
                                    val charSearch = constraint
                                    if (charSearch.isEmpty()) {
                                        filterList = taskList
                                    } else {
                                        val resultList : MutableList<taskResponse.Data> = mutableListOf()
                                        for (row in taskList) {
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
                    }

                } catch (e: Exception) {
                    println(e)
                }
            }, { throwable ->
                println(throwable)
                println("Sys Log: cannot get data")
            })

        var loadList: Button = findViewById(R.id.loadlist)
        loadList.setOnClickListener {
            var mulTaskList: MutableList<taskResponse.Data> = mutableListOf()
            taskList?.let {
                for (it in taskList!!) {
                    mulTaskList.add(it)
                }
                println("tasklist: ${taskList!!.size}")
                println("multl: ${mulTaskList!!.size}")
            }
            // kotlin
            taskList?.let {
                RxHttp.get(this.getString(R.string.default_url) + "/api/v1/orderlists")
                    .asString()
                    .subscribe({ s ->
                        try {
                            var s: orderlist = Gson().fromJson(s, orderlist::class.java)
                            //println("s: ${s.data.last().tasks}")
                            var j = 0
                            for ( i in mulTaskList!!.indices ) {
                                if (s.data.last().tasks.contains((i+1).toString())) {
                                    //mulTaskList!!.removeAt(i)
                                    //println("indice: $i")
                                } else {
                                    //println("remove indice: $i")
                                    mulTaskList!!.removeAt(i-j)
                                    j = j + 1
                                }
                            }
                            runOnUiThread {
                                //这里面进行UI的更新操作
                                //使用Recycler
                                val layoutManager = GridLayoutManager(this, 3)
                                mRecycler.layoutManager = layoutManager
                                val adapter = RecyclerAdapter(mulTaskList!!)
                                mRecycler.adapter = adapter
                            }
                        } catch (e: Exception) {
                            println(e)
                        }
                    }, { throwable ->
                        println(throwable)
                        println("Sys Log: cannot get data")
                    })
            }
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