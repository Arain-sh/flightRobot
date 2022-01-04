package com.example.flightrobot

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Looper
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.setActionButtonEnabled
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.angcyo.dsladapter.*
import com.angcyo.dsladapter.dsl.DslActionItem
import com.angcyo.dsladapter.dsl.DslDemoItem
import com.example.flightrobot.models.*
import com.google.gson.Gson
import com.tapadoo.alerter.Alerter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_order_task.*
import kotlinx.android.synthetic.main.activity_video.*
import kotlinx.android.synthetic.main.create_order_.*
import kotlinx.android.synthetic.main.edit_order.*
import kotlinx.android.synthetic.main.fragment_operation.*
import kotlinx.android.synthetic.main.fragment_orderview.*
import kotlinx.android.synthetic.main.fragment_taskinfo.*
import kotlinx.android.synthetic.main.fragment_tasks.*
import kotlinx.android.synthetic.main.operationlist.*
import kotlinx.android.synthetic.main.order_actionlist.*
import kotlinx.android.synthetic.main.orderlistmain.*
import kotlinx.android.synthetic.main.table_row_header_text_data.*
import kotlinx.android.synthetic.main.taskaction.*
import orderlist
import rxhttp.RxHttp
import rxhttp.async
import rxhttp.toClass
import java.lang.Thread.sleep
import java.util.*


class OrderHistoryDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_order)
        var list: List<Int> ?= null

        Objects.requireNonNull(getSupportActionBar())?.setDisplayHomeAsUpEnabled(true)

        var run_step: Int = intent.getIntExtra("run_step", 0)
        var task_name: String? = intent.getStringExtra("task_name")
        var id: Int = intent.getIntExtra("id",0)
        println("runstep: $run_step, task_name: $task_name, id: $id")
        //var delAction: String = intent.getStringExtra("delAction")!!

        setTitle("任务: " + task_name)
        var nDialog: ProgressDialog
        nDialog = ProgressDialog(this)
        nDialog.setMessage("Loading..")
        nDialog.setTitle("数据库加载中...")
        nDialog.setCancelable(true)
        nDialog.show()

        var tasklist : MutableList<taskResponse.Data> = mutableListOf()
        var actionlist : MutableList<actionResponse.Data> = mutableListOf()
        var operationlist : MutableList<operationResponse.Data> = mutableListOf()
        var orderOperations : MutableList<operationResponse.Data> = mutableListOf()
        val operationSearch = findViewById<SearchView>(R.id.orderview_search)
        var filterList : MutableList<orderResponse.Data> = mutableListOf()
        var dslList : MutableList<DslAdapterItem>? = null

        //获取操作
        RxHttp.get(this.getString(R.string.default_url) + "/api/v1/operations")
            .asString()
            .subscribe({ s ->
                try {
                    var s: operationResponse = Gson().fromJson(s, operationResponse::class.java)
                    operationlist =  s.data
                } catch (e: Exception) {
                    println(e)
                }
            }, { throwable ->
                println(throwable)
            })
        sleep(500)

        // kotlin
        RxHttp.postForm(this.getString(R.string.default_url) + "/api/v1/orders")
            .add("id", id)
            .asString()
            .subscribe({ s ->
                try {
                    var s: orderResponse = Gson().fromJson(s, orderResponse::class.java)
                    var orderLast = s.data[0]
                    println("orderLast: $orderLast")
                    orderLast?.let{
                        // kotlin
                        RxHttp.get(this.getString(R.string.default_url) + "/api/v1/tasks")
                            .asString()
                            .subscribe({ s ->
                                try {
                                    var s: taskResponse = Gson().fromJson(s, taskResponse::class.java)
                                    tasklist = s.data
                                    tasklist?.let{
                                        for (i in tasklist.indices) {
                                            for (j in tasklist[i].actions.indices) {
                                                for (k in tasklist[i].actions[j].operations.indices) {
                                                    if (tasklist[i].actions[j].operations[k].id.toString() in orderLast.run_status.split(",")) {
                                                        for (l in operationlist.indices) {
                                                            if (operationlist[l].id.toString() == tasklist[i].actions[j].operations[k].id.toString()) {
                                                                orderOperations.add(operationlist[l])
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        this?.runOnUiThread {
                                            //这里面进行UI的更新操作
                                            val layoutManager = GridLayoutManager(this, 1)
                                            nDialog.dismiss()

                                            editOrder.layoutManager = layoutManager
                                            editOrder.adapter = DslAdapter()
                                            if (orderOperations.isNotEmpty()) {
                                                val item = DslAdapterItem()
                                                item.itemLayoutId = R.layout.operationlist
                                                item.itemBindOverride = { itemHolder, _, _, _ ->
                                                    editOrder.dslAdapter {
                                                        for (i in orderOperations.indices) {
                                                            var itm = orderOperations[i]
                                                            if (itm.id > run_step) {
                                                                dslItem(R.layout.operationlist) {
                                                                    dslList?.add(this)
                                                                    itemTopInsert = 10 * dpi
                                                                    itemBindOverride =
                                                                        { itemHolder, itemPosition, adapterItem, _ ->
                                                                            itemHolder.tv(R.id.operation_title)?.text =
                                                                                itm.name
                                                                            //itemHolder.tv(R.id.operation_id)?.text = "动作ID: " + itm.id.toString()
                                                                            itemHolder.tv(R.id.operation_ele)?.text = "操作元件: " + itm.element
                                                                            itemHolder.tv(R.id.operation_obj)?.text = "操作对象: " + itm.`object`
                                                                            itemHolder.tv(R.id.operation_type)?.text = "操作类型: " + itm.type
                                                                            itemHolder.tv(R.id.operation_degree)?.text = itm.degree

                                                                            itemHolder.tv(R.id.operation_id)?.apply {
                                                                                setBackgroundColor(
                                                                                    when {
                                                                                        orderLast.run_step.toInt() > itemPosition -> Color.GREEN
                                                                                        else -> Color.WHITE
                                                                                    }
                                                                                )
                                                                            }
                                                                        }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                editOrder._dslAdapter?.addLastItem(item)
                                            }
                                            Thread.sleep(50)
                                        }
                                    }
                                    var psButton: Button = findViewById(R.id.editorder_push)
                                    psButton.setOnClickListener {
                                        MaterialDialog(this).show {
                                            title(R.string.fix_title)
                                            message(R.string.push_mes)
                                            positiveButton(R.string.agree) {
                                                var lst = ""
                                                for (i in orderOperations.indices) {
                                                    lst += orderOperations[i]
                                                    lst += ","
                                                }
                                                val inputField: EditText = it.getInputField()

                                                //发布任务
                                                RxHttp.postForm(getString(R.string.default_url) + "/api/v1/orders/store")
                                                    .add("user_id", 1)
                                                    .add("task_id", 0)// 0: pending; 1: running; 2: finished;
                                                    .add("status", inputField.text.toString())// order名称
                                                    .add("run_status", lst)//operations
                                                    .add("run_step", run_step)
                                                    .asString()
                                                    .subscribe({ s ->
                                                        try {
                                                            MaterialDialog(this.context).show {
                                                                title(R.string.fix_title)
                                                                message(R.string.push_success)
                                                                sleep(100)
                                                                finish()
                                                            }
                                                        } catch (e: Exception) {
                                                            println(e)
                                                        }
                                                    }, { throwable ->
                                                        println(throwable)
                                                    })

                                                //发送控制指令
                                                for (op in orderOperations) {
                                                    if (op.id.toString() in orderLast.run_status.split(",")) {
                                                        RxHttp.postForm(this.context.getString(R.string.com_url))
                                                            .add("object", op.`object`)//仪表盘ID
                                                            .add("element", op.element)//元件ID
                                                            .add("degree", op.degree)//元件期望状态值
                                                            .add("type", op.type)//操作种类
                                                            .add("showObject", op.showObject)//观测仪表盘ID
                                                            .add("showdcs_id", op.showdcs_id)//观测仪表盘元件ID
                                                            .add("showtarget_value", op.showtarget_value)//观测仪表盘元件值
                                                            .add("task_name", orderLast.status)
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
                                            }
                                            negativeButton(R.string.disagree) { dialog ->
                                                // Do something
                                            }
                                        }
                                    }
                                } catch (e: Exception) {
                                    println(e)
                                }
                            }, { throwable ->
                                println(throwable)
                            })
                    }
                } catch (e: Exception) {
                    println(e)
                }
            }, { throwable ->
                println(throwable)
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