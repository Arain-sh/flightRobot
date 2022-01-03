package com.example.flightrobot

import android.os.Bundle
import android.os.Looper
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.example.flightrobot.models.actionResponse
import com.example.flightrobot.models.operationResponse
import com.example.flightrobot.models.taskResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_operation.*
import kotlinx.android.synthetic.main.fragment_tasks.*
import rxhttp.RxHttp
import java.util.*


class OperationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_operation)
        Objects.requireNonNull(getSupportActionBar())?.setDisplayHomeAsUpEnabled(true)
        val action_id: Int = intent.getIntExtra("action_id", 1)
        val task_id: Int = intent.getIntExtra("task_id", 1)
        val operation_search = findViewById<SearchView>(R.id.operation_search)
        var filterList : MutableList<operationResponse.Data> = mutableListOf()
        var del = 0
        // kotlin
        RxHttp.postForm(this.getString(R.string.default_url) + "/api/v1/operations")
            .add("action_id", action_id)
            .asString()
            .subscribe({ s ->
                try {
                    var s: operationResponse = Gson().fromJson(s, operationResponse::class.java)
                    val operationList = s.data
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

                            operation_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                                override fun onQueryTextSubmit(query: String?): Boolean {
                                    return false
                                }

                                override fun onQueryTextChange(newText: String?): Boolean {
                                    filterList = fil(newText!!)
                                    operationRecycler.adapter = OperationRecyclerAdapter(filterList)
                                    return false
                                }
                                fun fil(constraint: String) : MutableList<operationResponse.Data> {
                                    val charSearch = constraint
                                    if (charSearch.isEmpty()) {
                                        filterList = operationList
                                    } else {
                                        val resultList : MutableList<operationResponse.Data> = mutableListOf()
                                        for (row in operationList) {
                                            if (row.name.contains(charSearch) or row.id.toString().contains(charSearch)) {
                                                resultList.add(row)
                                            }
                                        }
                                        filterList = resultList
                                    }
                                    return filterList
                                }
                            })

                            val itemTouchHelper =
                                ItemTouchHelper(ReViewTouchCallback(object : IActionListener {
                                    override fun onItemMove(src: Int, target: Int): Boolean {
                                        Collections.swap(operationList, src, target)
                                        adapter.notifyItemMoved(src, target)
                                        return true
                                    }

                                    override fun onItemRemove(pos: Int) {
                                        //adapter.getDataList().remove(pos)
                                        operationList.removeAt(pos)
                                        adapter.notifyItemRemoved(pos)
                                        del = pos+1
                                    }
                                }))
                            itemTouchHelper.attachToRecyclerView(operationRecycler)

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
                                                    .add("run_step", del)
                                                    .add("status", (action_id).toString())
                                                    .asString()
                                                    .subscribe({ s ->
                                                        try {
                                                            Looper.prepare()
                                                            println("Delete Num: $del")
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

                                                for (op in operationList) {
                                                    println("op: $op")
                                                    RxHttp.postForm(this.context.getString(R.string.com_url))
                                                        .add("object", op.`object`)
                                                        .add("element", op.element)
                                                        .add("degree", op.degree)
                                                        .add("type", op.type)
                                                        .add("showObject", op.showObject)
                                                        .add("showdcs_id", op.showdcs_id)
                                                        .add("showtarget_value", op.showtarget_value)
                                                        .add("task_name", "")
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
                                            }
                                            negativeButton(R.string.disagree) { dialog ->
                                                // Do something
                                            }
                                        }
                                    }
                                }
                            }
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