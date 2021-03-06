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
import kotlinx.android.synthetic.main.taskaction.*
import orderlist
import rxhttp.RxHttp
import rxhttp.async
import rxhttp.toClass
import java.lang.Thread.sleep
import java.util.*


class CreateOrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_order_)
        var list: List<Int> ?= null

        Objects.requireNonNull(getSupportActionBar())?.setDisplayHomeAsUpEnabled(true)

        //var task_id: Int = intent.getIntExtra("task_id", 1)
        //var order_id: Int = intent.getIntExtra("order_id", 1)
        //var del: Int = intent.getIntExtra("del",0)
        //var delAction: String = intent.getStringExtra("delAction")!!

        //setTitle("??????: " + task_id.toString())
        var nDialog: ProgressDialog
        nDialog = ProgressDialog(this)
        nDialog.setMessage("Loading..")
        nDialog.setTitle("??????????????????...")
        nDialog.setCancelable(true)
        nDialog.show()

        var tasks : MutableList<taskResponse.Data> = mutableListOf()
        var actions : MutableList<actionResponse.Data> = mutableListOf()
        var operations : MutableList<String> = mutableListOf()
        var actionName : MutableList<String> = mutableListOf()
        var actionFisrt : MutableList<String> = mutableListOf()
        var taskIndex : MutableList<String> = mutableListOf()

        // kotlin
        RxHttp.get(getString(R.string.default_url) + "/api/v1/tasks")
            .asString()
            .subscribe({ s ->
                try {
                    var s: taskResponse = Gson().fromJson(s, taskResponse::class.java)
                    var taskList = s.data
                    runOnUiThread {
                        //???????????????UI???????????????
                        val layoutManager = GridLayoutManager(this, 1)
                        createOrderlist.layoutManager = layoutManager
                        createOrderlist.adapter = DslAdapter()
                        createOrderlist.addItemDecoration(DslItemDecoration())
                        //HoverItemDecoration().attachToRecyclerView(createOrderlist)
                        if (taskList.isNotEmpty()) {
                            val item = DslAdapterItem()
                            var numOfOp = 0
                            item.itemLayoutId = R.layout.taskaction
                            item.itemBindOverride = { itemHolder, _, _, _ ->
                                createOrderlist.dslAdapter {
                                    for (i in taskList.indices) {
                                        val itm = taskList[i]
                                        dslItem(R.layout.taskaction) {
                                            itemIsGroupHead = true //??????????????????
                                            //itemIsHover = true //????????????
                                            itemGroups = mutableListOf("group${i}")
                                            //itemGroupExtend = false
                                            itemBindOverride =
                                                { itemHolder, itemPosition, adapterItem, _ ->
                                                    itemHolder.tv(R.id.fold_button)?.text =
                                                        if (itemGroupExtend) "??????" else "??????"
                                                    itemHolder.tv(R.id.action_title)?.text =
                                                        itm.name
                                                    itemHolder.tv(R.id.action_id)?.text =
                                                        "??????ID: " + itm.id.toString()
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
                                        //?????????????????????????????????
                                        if (i == 0) {
                                            for (a in taskList.indices) {
                                                taskIndex.add("$numOfOp")
                                                numOfOp++
                                                for (b in taskList[a]?.actions.indices) {
                                                    val nb = taskList[a].actions[b].operations.size
                                                    if (nb != 0) {
                                                        val nname = taskList[a].actions[b].name
                                                        actionFisrt.add("$numOfOp")
                                                        actionName.add("$nname")
                                                        numOfOp += nb
                                                        for (d in taskList[a].actions[b].operations.indices) {
                                                            operations.add(taskList[a].actions[b].operations[d].id.toString())
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        var actionList = itm.actions
                                        for (k in actionList.indices) {
                                            var operationList = actionList[k].operations
                                            // ??????operations
                                            for (j in operationList.indices) {
                                                val opitem = operationList[j]
                                                println(operationList[0])
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
                                                                    "????????????: " + opitem.element
                                                                itemHolder.tv(R.id.operation_obj)?.text =
                                                                    "????????????: " + opitem.`object`
                                                                itemHolder.tv(R.id.operation_type)?.text =
                                                                    "????????????: " + opitem.type
                                                                itemHolder.tv(R.id.operation_degree)?.text =
                                                                    opitem.degree
                                                                //itemHolder.tv(R.id.operation_degree)?.background = (R.color.colorPrimary)
                                                                if (opitem.degree == "") {
                                                                    //itemHolder.tv(R.id.operation_degree)?.setBackgroundColor(R.color.exo_white)
                                                                }
                                                                itemHolder.tv(R.id.action_operation_name)?.text = ""

                                                                if ("$itemPosition" in actionFisrt) {
                                                                    println("$itemPosition")
                                                                    val itemindex = actionFisrt.indexOf("$itemPosition")
                                                                    itemHolder.tv(R.id.action_operation_name)?.text = actionName[itemindex]
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
                                                                                .add("id", opitem.id)
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

                                        /*
                                        // get actions of tasks
                                        RxHttp.postForm(getString(R.string.default_url) + "/api/v1/actions")
                                            .add("task_id", itm.id)
                                            .asString()
                                            .subscribe({ s ->
                                                try {
                                                    var s: actionResponse = Gson().fromJson(s, actionResponse::class.java)
                                                    var actionList = s.data
                                                    // get operations
                                                    for (k in actionList.indices) {
                                                        RxHttp.postForm(getString(R.string.default_url) + "/api/v1/operations")
                                                            .add("action_id", actionList[k].id)
                                                            .asString()
                                                            .subscribe({ s ->
                                                                try {
                                                                    var s: operationResponse = Gson().fromJson(
                                                                        s,
                                                                        operationResponse::class.java
                                                                    )
                                                                    var operationList = s.data
                                                                    // ??????operations
                                                                    for (j in operationList.indices) {
                                                                        val item = operationList[j]
                                                                        dslItem(DslDemoItem()) {
                                                                            itemGroups = mutableListOf("group${i}")
                                                                            itemIsSelected = true
                                                                            itemBindOverride =
                                                                                { itemHolder, _, _, _ ->
                                                                                    itemGroupParams.apply {
                                                                                        itemHolder.tv(R.id.operation_title)?.text =
                                                                                            item.name
                                                                                        itemHolder.tv(R.id.operation_id)?.text =
                                                                                            item.id.toString()
                                                                                        itemHolder.tv(R.id.operation_ele)?.text =
                                                                                            "????????????: " + item.element
                                                                                        itemHolder.tv(R.id.operation_obj)?.text =
                                                                                            "????????????: " + item.`object`
                                                                                        itemHolder.tv(R.id.operation_type)?.text =
                                                                                            "????????????: " + item.type
                                                                                        itemHolder.tv(R.id.operation_degree)?.text =
                                                                                            item.degree
                                                                                        if (j == 0) {
                                                                                            itemHolder.tv(R.id.action_operation_name)?.text = actionList[k].name
                                                                                        }
                                                                                        if (j == 0) {
                                                                                            itemTopInsert = 20
                                                                                            itemDecorationColor = R.color.grayDark
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
                                                                                                "?????????.",
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
                                                                                updateItemDepend()
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
                                                    }
                                                } catch (e: Exception) {
                                                    println(e)
                                                }
                                            }, { throwable ->
                                                println(throwable)
                                            })*/


                                        if (i == taskList.size - 1) {
                                            nDialog.dismiss()
                                        }
                                    }
                                }
                            }
                            createOrderlist._dslAdapter?.addLastItem(item)
                        }
                        // push button
                        var pushButton: Button = this.findViewById(R.id.commmitlist)
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

                                    //inputField?.error = if (isValid) null else "????????????0???1!"
                                    //dialog.setActionButtonEnabled(WhichButton.POSITIVE, isValid)
                                }
                                positiveButton(R.string.push) {
                                    var lst = ""
                                    for (i in operations.indices) {
                                        lst += operations[i]
                                        lst += ","
                                    }
                                    val inputField: EditText = it.getInputField()
                                    RxHttp.postForm(getString(R.string.default_url) + "/api/v1/orderdetails/store")
                                        .add("name", inputField.text.toString()) //order??????
                                        .add("tasks", "0")//0: ????????????1:????????????2:?????????;3:??????????????????
                                        .add("actions", "0")//?????????
                                        .add("operations", lst)//????????????
                                        .add("start_index", 0)//order??????
                                        .add("desc", "0")
                                        .add("pend", "0")
                                        .add("pending","0")
                                        .add("run_times", "0")
                                        .asString()
                                        .subscribe({ s ->
                                            try {
                                                Toast.makeText(this.context, "??????????????????!", Toast.LENGTH_LONG)
                                                    .show()
                                                finish()
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