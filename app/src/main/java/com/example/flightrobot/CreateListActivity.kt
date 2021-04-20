package com.example.flightrobot

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.angcyo.dsladapter.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_create_list.*
import kotlinx.android.synthetic.main.fragment_tasks.*
import rxhttp.RxHttp
import taskResponse
import java.util.*
import kotlin.collections.ArrayList

class CreateListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_list)

        Objects.requireNonNull(getSupportActionBar())?.setDisplayHomeAsUpEnabled(true)
        var dslList : MutableList<DslAdapterItem>? = null
        var jihua = ArrayList<Int>()
        // kotlin
        RxHttp.get(this.getString(R.string.default_url) + "/api/v1/tasks")
            .asString()
            .subscribe({ s ->
                try {
                    var s: taskResponse = Gson().fromJson(s, taskResponse::class.java)
                    var taskList = s.data
                    for (i in s.data.indices) {
                        //println("s = ${s.data.get(i).name}")
                    }
                    val layoutManager = GridLayoutManager(this, 3)
                    runOnUiThread {
                        //这里面进行UI的更新操作
                        //使用Recycler
                        createRecycler.layoutManager = layoutManager
                        createRecycler.adapter = DslAdapter()
                        if (taskList.isNotEmpty()) {
                            val item = DslAdapterItem()
                            item.itemLayoutId = R.layout.tasklist
                            item.itemBindOverride = { itemHolder, _, _, _ ->
                                createRecycler.dslAdapter {
                                    for (i in taskList.indices) {
                                        val itm = taskList[i]
                                        dslItem(R.layout.tasklist) {
                                            dslList?.add(this)
                                            itemGroups = mutableListOf("group${i}")
                                            itemTopInsert = 10 * dpi
                                            itemBindOverride =
                                                { itemHolder, itemPosition, adapterItem, _ ->
                                                    itemHolder.tv(R.id.mine_title)?.text =
                                                        itm.name
                                                    itemHolder.tv(R.id.mine_id)?.text =
                                                        "动作ID: " + itm.id.toString()
                                                    if (itemIsSelected) {
                                                        itemHolder.tv(R.id.mine_title)!!.setBackgroundColor(Color.GREEN)
                                                    }
                                                    itemHolder.tv(R.id.mine_title)?.apply {
                                                        setBackgroundColor(
                                                            when {
                                                                itemIsSelected -> Color.GREEN
                                                                else -> Color.WHITE
                                                            }
                                                        )
                                                    }
                                                    itemHolder.tv(R.id.mine_id)?.apply {
                                                        setBackgroundColor(
                                                            when {
                                                                itemIsSelected -> Color.GREEN
                                                                else -> Color.WHITE
                                                            }
                                                        )
                                                    }
                                                }
                                            itemClick = {
                                                itemIsSelected = !itemIsSelected
                                                updateItemDepend()
                                            }
                                        }
                                    }
                                }
                            }
                            createRecycler._dslAdapter?.addLastItem(item)
                        }
                        var pushList: Button = findViewById(R.id.pushlist)
                        var str: String = ""
                        pushList.setOnClickListener {
                            for (i in createRecycler._dslAdapter!!.dataItems.indices) {
                                if (createRecycler._dslAdapter!!.dataItems[i].itemIsSelected) {
                                    jihua.add(taskList[i].id)
                                    str = str + taskList[i].id.toString() + ","
                                }
                            }


                            // kotlin
                            RxHttp.postForm(this.getString(R.string.default_url) + "/api/v1/orderlists/store")
                                .add("tasks", str)
                                .asString()
                                .subscribe({ s ->
                                    try {

                                    } catch (e: Exception) {
                                        println(e)
                                    }
                                }, { throwable ->
                                    println(throwable)
                                    println("Sys Log: cannot get data")
                                })
                            Toast.makeText(this, "发布成功", Toast.LENGTH_SHORT).show();
                            finish()
                        }

                        Thread.sleep(250)
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