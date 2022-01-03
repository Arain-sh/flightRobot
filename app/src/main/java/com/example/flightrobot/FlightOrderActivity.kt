package com.example.flightrobot

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
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
import com.example.flightrobot.models.actionResponse
import com.example.flightrobot.models.operationResponse
import com.example.flightrobot.models.orderResponse
import com.example.flightrobot.models.taskResponse
import com.google.gson.Gson
import com.tapadoo.alerter.Alerter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_order_task.*
import kotlinx.android.synthetic.main.activity_video.*
import kotlinx.android.synthetic.main.create_order_.*
import kotlinx.android.synthetic.main.fragment_operation.*
import kotlinx.android.synthetic.main.fragment_taskinfo.*
import kotlinx.android.synthetic.main.fragment_tasks.*
import kotlinx.android.synthetic.main.operationlist.*
import kotlinx.android.synthetic.main.order_actionlist.*
import kotlinx.android.synthetic.main.orderlistmain.*
import kotlinx.android.synthetic.main.orderview.*
import kotlinx.android.synthetic.main.taskaction.*
import orderdetailResponse
import orderlist
import rxhttp.RxHttp
import rxhttp.async
import rxhttp.toClass
import java.lang.Thread.sleep
import java.util.*


class FlightOrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.orderview)
        var list: List<Int> ?= null

        Objects.requireNonNull(getSupportActionBar())?.setDisplayHomeAsUpEnabled(true)

        var id: String? = intent.getStringExtra("id")
        var order_id: Int = intent.getIntExtra("order_id", 1)
        //var del: Int = intent.getIntExtra("del",0)
        //var delAction: String = intent.getStringExtra("delAction")!!

        //setTitle("任务: " + task_id.toString())
        var nDialog: ProgressDialog
        nDialog = ProgressDialog(this)
        nDialog.setMessage("Loading..")
        nDialog.setTitle("数据库加载中...")
        nDialog.setCancelable(true)
        nDialog.show()

        var tasks : MutableList<taskResponse.Data> = mutableListOf()
        var actions : MutableList<actionResponse.Data> = mutableListOf()
        var operations : MutableList<String> = mutableListOf()
        var actionName : MutableList<String> = mutableListOf()
        var actionFisrt : MutableList<String> = mutableListOf()
        var taskIndex : MutableList<String> = mutableListOf()

        var operationList : MutableList<operationResponse.Data> = mutableListOf()
        var opShow : MutableList<operationResponse.Data> = mutableListOf()
        var orders : MutableList<orderdetailResponse.Data> = mutableListOf()

        RxHttp.get(this.getString(R.string.default_url) + "/api/v1/operations")
            .asString()
            .subscribe({ s ->
                try {
                    var s: operationResponse = Gson().fromJson(s, operationResponse::class.java)
                    operationList = s.data
                } catch (e: Exception) {
                    println(e)
                }
            }, { throwable ->
                println(throwable)
            })
        sleep(300)

        RxHttp.postForm(this.getString(R.string.default_url) + "/api/v1/orderdetails")
            .add("id",id)
            .asString()
            .subscribe({ s ->
                try {
                    var s: orderdetailResponse = Gson().fromJson(s, orderdetailResponse::class.java)
                    orders = s.data
                    orders.reverse()
                } catch (e: Exception) {
                    println(e)
                }
            }, { throwable ->
                println(throwable)
            })
        sleep(300)
        // kotlin
        RxHttp.get(this.getString(R.string.default_url) + "/api/v1/tasks")
            .asString()
            .subscribe({ s ->
                try {
                    var s: taskResponse = Gson().fromJson(s, taskResponse::class.java)
                    var taskList = s.data
                    runOnUiThread {
                        //这里面进行UI的更新操作
                        val layoutManager = GridLayoutManager(this, 1)
                        orderview.layoutManager = layoutManager
                        orderview.adapter = DslAdapter()
                        orderview.addItemDecoration(DslItemDecoration())
                        //HoverItemDecoration().attachToRecyclerView(createOrderlist)
                        if (taskList.isNotEmpty()) {
                            val item = DslAdapterItem()
                            var numOfOp = 0
                            var iflag = false
                            //筛选task

                            item.itemLayoutId = R.layout.taskaction
                            item.itemBindOverride = { itemHolder, _, _, _ ->
                                orderview.dslAdapter {
                                    for (i in taskList.indices) {
                                        val itm = taskList[i]
                                        for (k in taskList[i].actions.indices) {
                                            for (j in itm.actions[k].operations.indices) {
                                                val tst = itm.actions[k].operations[j]
                                                if (tst.id.toString() in orders[0].operations.split(",")) {
                                                    operations.add(tst.id.toString())
                                                    iflag = true
                                                    println("tasks: ${tst.id}")
                                                }
                                            }
                                        }
                                        if (iflag) {
                                            //dslitem
                                            dslItem(R.layout.taskaction) {
                                                itemIsGroupHead = true //启动分组折叠
                                                //itemIsHover = true //关闭悬停
                                                itemGroups = mutableListOf("group${i}")
                                                //itemGroupExtend = false
                                                itemBindOverride =
                                                    { itemHolder, itemPosition, adapterItem, _ ->
                                                        itemHolder.tv(R.id.fold_button)?.text =
                                                            if (itemGroupExtend) "折叠" else "展开"
                                                        itemHolder.tv(R.id.action_title)?.text =
                                                            itm.name
                                                        itemHolder.tv(R.id.action_id)?.text =
                                                            "任务ID: " + itm.id.toString()
                                                        itemHolder.tv(R.id.action_description)?.text =
                                                            itm.description

                                                        itemHolder.click(R.id.fold_button) {
                                                            itemGroupExtend = !itemGroupExtend
                                                        }

                                                        if (itemIsSelected) {
                                                            itemHolder.tv(R.id.action_mark)!!.setBackgroundColor(
                                                                Color.GREEN)
                                                        }
                                                        itemHolder.tv(R.id.action_mark)!!.apply {
                                                            setBackgroundColor(
                                                                when {
                                                                    itemIsSelected -> Color.GREEN
                                                                    else -> Color.WHITE
                                                                }
                                                            )
                                                        }
                                                        itemHolder.tv(R.id.action_mark)!!.apply {
                                                            setBackgroundColor(
                                                                when {
                                                                    itemIsSelected -> Color.GREEN
                                                                    else -> Color.WHITE
                                                                }
                                                            )
                                                        }

                                                        itemGroupParams.apply {
                                                            if (isOnlyOne()) {
                                                                itemHolder.itemView.setBackgroundResource(
                                                                    R.drawable.shape_group_all
                                                                )
                                                            } else if (isFirstPosition()) {
                                                                itemHolder.itemView
                                                                    .setBackgroundResource(R.drawable.shape_group_header)
                                                            } else {
                                                                itemHolder.itemView
                                                                    .setBackgroundColor(
                                                                        resources.getColor(
                                                                            R.color.white
                                                                        )
                                                                    )
                                                            }
                                                        }
                                                    }
                                                /*
                                                itemClick = {
                                                    itemIsSelected = !itemIsSelected
                                                    updateItemDepend()
                                                }*/
                                            }
                                            //计算操作的动作起始位置
                                            if (i == 0) {
                                                for (a in taskList.indices) {
                                                    taskIndex.add("$numOfOp")
                                                    numOfOp++
                                                    for (b in taskList[a].actions.indices) {
                                                        val nb = taskList[a].actions[b].operations.size
                                                        if (nb != 0) {
                                                            val nname = taskList[a].actions[b].name
                                                            actionFisrt.add("$numOfOp")
                                                            actionName.add("$nname")
                                                            numOfOp += nb
                                                            for (d in taskList[a].actions[b].operations.indices) {
                                                                //operations.add(taskList[a].actions[b].operations[d].id.toString())
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            var actionList = itm.actions
                                            for (k in actionList.indices) {
                                                var opList = actionList[k].operations
                                                // 设置operations
                                                for (j in opList.indices) {
                                                    val opitem = opList[j]
                                                    if (opitem.id.toString() in orders[0].operations.split(",")) {
                                                        dslItem(DslDemoItem()) {
                                                            itemGroups = mutableListOf("group${i}")
                                                            itemIsSelected = true
                                                            itemBindOverride =
                                                                { itemHolder, itemPosition, _, _ ->
                                                                    itemGroupParams.apply {
                                                                        itemHolder.tv(R.id.operation_title)?.text =
                                                                            opitem.name
                                                                        itemHolder.tv(R.id.operation_id)?.text =
                                                                            opitem.id.toString()
                                                                        itemHolder.tv(R.id.operation_ele)?.text =
                                                                            "操作元件: " + opitem.element
                                                                        itemHolder.tv(R.id.operation_obj)?.text =
                                                                            "操作对象: " + opitem.`object`
                                                                        itemHolder.tv(R.id.operation_type)?.text =
                                                                            "操作类型: " + opitem.type
                                                                        itemHolder.tv(R.id.operation_degree)?.text =
                                                                            opitem.degree
                                                                        itemHolder.tv(R.id.action_operation_name)?.text = ""

                                                                        if ("$itemPosition" in actionFisrt) {
                                                                            println("$itemPosition")
                                                                            val itemindex = actionFisrt.indexOf("$itemPosition")
                                                                            //itemHolder.tv(R.id.action_operation_name)?.text = actionName[itemindex]
                                                                        }
                                                                        if (opitem == actionList[k].operations.first()) {
                                                                            itemTopInsert = 20
                                                                            itemDecorationColor = R.color.green
                                                                        }

                                                                        if (itemIsSelected) {
                                                                            itemHolder.tv(R.id.operation_mark)!!.setBackgroundColor(
                                                                                Color.GREEN)
                                                                        }
                                                                        itemHolder.tv(R.id.operation_mark)!!.apply {
                                                                            setBackgroundColor(
                                                                                when {
                                                                                    itemIsSelected -> Color.GREEN
                                                                                    else -> Color.WHITE
                                                                                }
                                                                            )
                                                                        }
                                                                        /*var fixButton: Button =
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
                                                                                    fixButton.text =
                                                                                        text
                                                                                    RxHttp.postForm(
                                                                                        "http://192.168.10.10/api/v1/operations/update"
                                                                                    )
                                                                                        .add(
                                                                                            "id",
                                                                                            item.id
                                                                                        )
                                                                                        .add(
                                                                                            "degree",
                                                                                            text
                                                                                        )
                                                                                        .asString()
                                                                                        .subscribe(
                                                                                            { s ->
                                                                                                try {
                                                                                                    println(
                                                                                                        s
                                                                                                    )
                                                                                                } catch (e: Exception) {
                                                                                                    println(
                                                                                                        e
                                                                                                    )
                                                                                                }
                                                                                            },
                                                                                            { throwable ->
                                                                                                println(
                                                                                                    throwable
                                                                                                )
                                                                                                println(
                                                                                                    "Sys Log: cannot get data"
                                                                                                )
                                                                                            })
                                                                                }
                                                                                positiveButton(R.string.agree)
                                                                                negativeButton(R.string.disagree) { dialog ->
                                                                                    // Do something
                                                                                }
                                                                            }
                                                                            Toast.makeText(
                                                                                itemHolder.itemView.context,
                                                                                "已修改.",
                                                                                Toast.LENGTH_SHORT
                                                                            ).show();
                                                                        }
                                                                        if (isLastPosition()) {
                                                                            itemHolder.itemView.setBackgroundResource(
                                                                                R.drawable.shape_group_all
                                                                            )
                                                                        } else {
                                                                            itemHolder.itemView
                                                                                .setBackgroundColor(
                                                                                    resources.getColor(
                                                                                        R.color.white
                                                                                    )
                                                                                )
                                                                        }*/
                                                                    }
                                                                }
                                                            itemClick = {
                                                                itemIsSelected = !itemIsSelected
                                                                if (!itemIsSelected) {
                                                                    operations.remove(opitem.id.toString())
                                                                } else {
                                                                    operations.add(opitem.id.toString())
                                                                }
                                                                updateItemDepend()
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            iflag = false
                                        }
                                        if (i == taskList.size - 1) {
                                            nDialog.dismiss()
                                        }
                                    }
                                }
                            }
                            orderview._dslAdapter?.addLastItem(item)
                        }
                        var delButton: Button = this.findViewById(R.id.orderview_delete)
                        delButton.setOnClickListener(){
                            MaterialDialog(this).show {
                                positiveButton(R.string.delete) {
                                    //删除清单
                                    RxHttp.postForm(getString(R.string.default_url) + "/api/v1/orderdetails/delete")
                                        .add("id", orders[0].id)
                                        .asString()
                                        .subscribe({ s ->
                                            try {
                                                Toast.makeText(this.context, "删除成功", Toast.LENGTH_SHORT).show();
                                                finish()
                                            } catch (e: Exception) {
                                                println(e)
                                            }
                                        }, { throwable ->
                                            println(throwable)
                                        })
                                }
                                negativeButton(R.string.disagree) { dialog ->
                                    // Do something
                                }
                            }
                        }

                        // push button
                        var pushButton: Button = this.findViewById(R.id.orderview_push)
                        pushButton.setOnClickListener {
                            MaterialDialog(this).show {
                                input (maxLength = 16, waitForPositiveButton = false) { dialog, text ->
                                    // Text submitted with the action button
                                    // kotlin
                                    val inputField = dialog.getInputField()
                                    var isValid = false
                                    /*when(text.toString()) {
                                        "0","1" -> isValid = true
                                        else -> {}
                                    }*/

                                    //inputField?.error = if (isValid) null else "该值应为0或1!"
                                    //dialog.setActionButtonEnabled(WhichButton.POSITIVE, isValid)
                                }
                                positiveButton(R.string.push) {
                                    var lst = ""
                                    for (i in operations.indices) {
                                        lst += operations[i]
                                        lst += ","
                                    }
                                    val inputField: EditText = it.getInputField()

                                    //发布任务
                                    RxHttp.postForm(getString(R.string.default_url) + "/api/v1/orders/store")
                                        .add("user_id", 1)
                                        .add("task_id", 0)// 0: pending; 1: running; 2: finished;
                                        .add("status", inputField.text.toString())// order名称
                                        .add("run_status", lst)//operations
                                        .add("run_step", 0)
                                        .asString()
                                        .subscribe({ s ->
                                            try {
                                                Toast.makeText(this.context, "发布成功", Toast.LENGTH_SHORT).show();
                                            } catch (e: Exception) {
                                                println(e)
                                            }
                                        }, { throwable ->
                                            println(throwable)
                                        })

                                    //发送控制指令
                                    for (op in operationList) {
                                        if (op.id.toString() in operations) {
                                            RxHttp.postForm(this.context.getString(R.string.com_url))
                                                .add("object", op.`object`)//仪表盘ID
                                                .add("element", op.element)//元件ID
                                                .add("degree", op.degree)//元件期望状态值
                                                .add("type", op.type)//操作种类
                                                .add("showObject", op.showObject)//观测仪表盘ID
                                                .add("showdcs_id", op.showdcs_id)//观测仪表盘元件ID
                                                .add("showtarget_value", op.showtarget_value)//观测仪表盘元件值
                                                .add("task_name", orders[0].name)
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