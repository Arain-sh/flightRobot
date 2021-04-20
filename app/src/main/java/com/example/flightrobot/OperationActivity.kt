package com.example.flightrobot

import actionResponse
import android.os.Bundle
import android.os.Looper
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.example.flightrobot.models.operationResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_operation.*
import kotlinx.android.synthetic.main.fragment_taskinfo.*
import rxhttp.RxHttp
import java.util.*


class OperationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_operation)
        Objects.requireNonNull(getSupportActionBar())?.setDisplayHomeAsUpEnabled(true)
        var action_id: Int = intent.getIntExtra("action_id", 1)
        val task_id: Int = intent.getIntExtra("task_id", 1)

        // kotlin
        RxHttp.postForm(this.getString(R.string.default_url) + "/api/v1/operations")
            .add("action_id", action_id)
            .asString()
            .subscribe({ s ->
                try {
                    var s: operationResponse = Gson().fromJson(s, operationResponse::class.java)
                    var operationList = s.data
                    println("SYS LOG: " + operationList)
                    // kotlin
                    operationList?.let {
                        runOnUiThread {
                            //这里面进行UI的更新操作
                            //使用Recycler
                            val layoutManager = LinearLayoutManager(this)
                            operationRecycler.layoutManager = layoutManager
                            val adapter = OperationRecyclerAdapter(operationList)
                            operationRecycler.adapter = adapter

                            val itemTouchHelper =
                                ItemTouchHelper(ReViewTouchCallback(object : IActionListener {
                                    override fun onItemMove(src: Int, target: Int): Boolean {
                                        Collections.swap(operationList, src, target)
                                        adapter.notifyItemMoved(src, target)
                                        return true
                                    }

                                    override fun onItemRemove(pos: Int) {
                                        //adapter.getDataList().remove(pos)
                                        adapter.notifyItemRemoved(pos)
                                    }
                                }))
                            itemTouchHelper.attachToRecyclerView(operationRecycler)

                        }
                    }

                } catch (e: Exception) {
                    println(e)
                }
            }, { throwable ->
                println(throwable)
                println("Sys Log: cannot get data")
            })
        val push: com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton = findViewById(
            R.id.custom_fab
        )
        if (MainActivity.isFromOrder) {
            runOnUiThread {
                push.isVisible = false
            }
        } else {
            push.setOnClickListener {
                this.let { it1 ->
                    MaterialDialog(it1).show {
                        title(R.string.fix_title)
                        message(R.string.push_mes)
                        positiveButton(R.string.push) { dialog ->
                            // Do something
                            // kotlin
                            RxHttp.postForm(getString(R.string.default_url) + "/api/v1/orders/store")
                                .add("user_id", 1)
                                .add("task_id", task_id)
                                .add("run_status", "finished")
                                .asString()
                                .subscribe({ s ->
                                    try {
                                        Looper.prepare()
                                        Toast.makeText(this.context, "任务发布成功!", Toast.LENGTH_LONG)
                                            .show()
                                        Looper.loop()
                                    } catch (e: Exception) {
                                        println(e)
                                    }
                                }, { throwable ->
                                    println(throwable)
                                    println("Sys Log: cannot get data")
                                })

                            RxHttp.postForm(this.context.getString(R.string.default_url) + "/api/v1/actions")
                                .add("task_id", task_id)
                                .asString()
                                .subscribe({ s ->
                                    try {
                                        var s: actionResponse = Gson().fromJson(s, actionResponse::class.java)
                                        var actionList = s.data
                                        for (item in actionList) {
                                            RxHttp.postForm(this.context.getString(R.string.default_url) + "/api/v1/operations")
                                                .add("action_id", item.id)
                                                .asString()
                                                .subscribe({ s ->
                                                    try {
                                                        var s: operationResponse = Gson().fromJson(s, operationResponse::class.java)
                                                        var operationList = s.data
                                                        println("SYS LOG: " + operationList)
                                                        for (op in operationList) {
                                                            RxHttp.postForm("http://192.168.1.104:7890")
                                                                .add("object", op.`object`)
                                                                .add("element", op.element)
                                                                .add("degree", op.degree)
                                                                .add("type", op.type)
                                                                .add("end", "true")
                                                                .asString()
                                                                .subscribe({ s ->
                                                                    try {
                                                                        println("SYS LOG: " + s)
                                                                    } catch (e: Exception) {
                                                                        println(e)
                                                                    }
                                                                }, { throwable ->
                                                                    println(throwable)
                                                                    println("Sys Log: cannot connect")
                                                                })
                                                        }
                                                    } catch (e: Exception) {
                                                        println(e)
                                                    }
                                                }, { throwable ->
                                                    println(throwable)
                                                    println("Sys Log: cannot get data")
                                                })
                                        }
                                    } catch (e: Exception) {
                                        println(e)
                                    }
                                }, { throwable ->
                                    println(throwable)
                                    println("Sys Log: cannot get data")
                                })
                        }
                        negativeButton(R.string.disagree) { dialog ->
                            // Do something
                        }
                    }
                }
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

    interface IActionListener {
        fun onItemMove(pos: Int, targetPos: Int): Boolean
        fun onItemRemove(pos: Int)
    }

    inner class ReViewTouchCallback(IActionListener: IActionListener) :
        ItemTouchHelper.Callback() {
        private val mIActionListener: IActionListener
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN // 上下拖动
            val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT // 向左滑动
            return makeMovementFlags(dragFlags, swipeFlags)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return mIActionListener.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            mIActionListener.onItemRemove(viewHolder.adapterPosition)
        }

        init {
            mIActionListener = IActionListener
        }
    }

}