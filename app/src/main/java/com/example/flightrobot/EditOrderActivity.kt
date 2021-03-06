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


class EditOrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_order)
        var list: List<Int> ?= null

        Objects.requireNonNull(getSupportActionBar())?.setDisplayHomeAsUpEnabled(true)

        var run_step: Int = intent.getIntExtra("run_step", 0)
        var task_name: String? = intent.getStringExtra("task_name")
        //var del: Int = intent.getIntExtra("del",0)
        //var delAction: String = intent.getStringExtra("delAction")!!

        setTitle("??????: " + task_name)
        var nDialog: ProgressDialog
        nDialog = ProgressDialog(this)
        nDialog.setMessage("Loading..")
        nDialog.setTitle("??????????????????...")
        nDialog.setCancelable(true)
        nDialog.show()

        var tasklist : MutableList<taskResponse.Data> = mutableListOf()
        var actionlist : MutableList<actionResponse.Data> = mutableListOf()
        var operationlist : MutableList<operationResponse.Data> = mutableListOf()
        var orderOperations : MutableList<operationResponse.Data> = mutableListOf()
        val operationSearch = findViewById<SearchView>(R.id.orderview_search)
        var filterList : MutableList<orderResponse.Data> = mutableListOf()
        var dslList : MutableList<DslAdapterItem>? = null

        //????????????
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
        RxHttp.get(this.getString(R.string.default_url) + "/api/v1/orders/current")
            .asString()
            .subscribe({ s ->
                try {
                    var s: ctOrderResponse = Gson().fromJson(s, ctOrderResponse::class.java)
                    var orderLast = s.data
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
                                            //???????????????UI???????????????
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
                                                                            //itemHolder.tv(R.id.operation_id)?.text = "??????ID: " + itm.id.toString()
                                                                            itemHolder.tv(R.id.operation_ele)?.text = "????????????: " + itm.element
                                                                            itemHolder.tv(R.id.operation_obj)?.text = "????????????: " + itm.`object`
                                                                            itemHolder.tv(R.id.operation_type)?.text = "????????????: " + itm.type
                                                                            itemHolder.tv(R.id.operation_degree)?.text = itm.degree
                                                                            var fixButton: Button =
                                                                                itemHolder.itemView.findViewById(
                                                                                    R.id.operation_degree
                                                                                )
                                                                            fixButton.setOnClickListener {
                                                                                MaterialDialog(
                                                                                    itemHolder.itemView.context
                                                                                ).show {
                                                                                    input { dialog, text ->
                                                                                        // Text submitted with the action button
                                                                                        // kotlin
                                                                                    }
                                                                                    positiveButton(R.string.agree) {
                                                                                        val inputField: EditText = it.getInputField()
                                                                                        fixButton.text = inputField.text
                                                                                        RxHttp.postForm(getString(R.string.default_url) + "/api/v1/operations/update")
                                                                                            .add("id", itm.id)
                                                                                            .add("degree", inputField.text)
                                                                                            .asString()
                                                                                            .subscribe({ s ->
                                                                                                try {
                                                                                                    println(s)
                                                                                                } catch (e: Exception) {
                                                                                                    println(e)
                                                                                                } }, { throwable ->
                                                                                                println(throwable)
                                                                                            })
                                                                                    }
                                                                                    negativeButton(R.string.disagree) { dialog ->
                                                                                        // Do something
                                                                                    }
                                                                                }
                                                                                Toast.makeText(
                                                                                    itemHolder.itemView.context,
                                                                                    "?????????.",
                                                                                    Toast.LENGTH_SHORT
                                                                                ).show();
                                                                            }
                                                                            if (itemIsSelected) {
                                                                                itemHolder.tv(R.id.operation_type)?.setBackgroundColor(Color.GREEN)
                                                                            }
                                                                            itemHolder.tv(R.id.operation_type)?.apply {
                                                                                setBackgroundColor(
                                                                                    when {
                                                                                        itemIsSelected  -> Color.GREEN
                                                                                        else -> Color.WHITE
                                                                                    }
                                                                                )
                                                                            }
                                                                            itemHolder.tv(R.id.operation_id)?.apply {
                                                                                setBackgroundColor(
                                                                                    when {
                                                                                        orderLast.run_step.toInt() > itemPosition -> Color.GREEN
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

                                                //????????????
                                                RxHttp.postForm(getString(R.string.default_url) + "/api/v1/orders/store")
                                                    .add("user_id", 1)
                                                    .add("task_id", 0)// 0: pending; 1: running; 2: finished;
                                                    .add("status", inputField.text.toString())// order??????
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

                                                //??????????????????
                                                for (op in orderOperations) {
                                                    if (op.id.toString() in orderLast.run_status.split(",")) {
                                                        RxHttp.postForm(this.context.getString(R.string.com_url))
                                                            .add("object", op.`object`)//?????????ID
                                                            .add("element", op.element)//??????ID
                                                            .add("degree", op.degree)//?????????????????????
                                                            .add("type", op.type)//????????????
                                                            .add("showObject", op.showObject)//???????????????ID
                                                            .add("showdcs_id", op.showdcs_id)//?????????????????????ID
                                                            .add("showtarget_value", op.showtarget_value)//????????????????????????
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