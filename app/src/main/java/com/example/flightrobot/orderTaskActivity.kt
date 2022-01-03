package com.example.flightrobot

import android.app.ProgressDialog
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.angcyo.dsladapter.*
import com.angcyo.dsladapter.dsl.DslDemoItem
import com.example.flightrobot.models.actionResponse
import com.example.flightrobot.models.operationResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_order_task.*
import kotlinx.android.synthetic.main.activity_pdf_view.*
import rxhttp.RxHttp
import java.lang.Thread.sleep
import java.util.*


class orderTaskActivity : AppCompatActivity() {
    companion object {
        val TAG_UPDATE_DATA = "update_data"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_task)
        Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)
        var nDialog: ProgressDialog
        nDialog = ProgressDialog(this)
        nDialog.setMessage("Loading..")
        nDialog.setTitle("数据加载中...")
        nDialog.isIndeterminate = false
        nDialog.setCancelable(true)
        nDialog.show()

        var task_id: Int = intent.getIntExtra("task_id", 1)
        var order_id: Int = intent.getIntExtra("order_id", 1)
        var del: Int = intent.getIntExtra("del",0)
        var delAction: String = intent.getStringExtra("delAction")!!

        setTitle("任务: " + task_id.toString())

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
                        val layoutManager = GridLayoutManager(this, 1)
                        ordertasklist.layoutManager = layoutManager
                        ordertasklist.adapter = DslAdapter()
                        if (actionList.isNotEmpty()) {
                            val item = DslAdapterItem()
                            item.itemLayoutId = R.layout.taskaction
                            item.itemBindOverride = { itemHolder, _, _, _ ->
                                ordertasklist.dslAdapter {
                                    for (i in actionList.indices) {
                                        val itm = actionList[i]
                                        dslItem(R.layout.taskaction) {
                                            itemIsGroupHead = true //启动分组折叠
                                            itemIsHover = true //关闭悬停
                                            itemGroups = mutableListOf("group${i}")
                                            itemTopInsert = 10 * dpi
                                            itemBindOverride =
                                                { itemHolder, itemPosition, adapterItem, _ ->
                                                    itemHolder.tv(R.id.fold_button)?.text =
                                                        if (itemGroupExtend) "折叠" else "展开"
                                                    itemHolder.tv(R.id.action_title)?.text =
                                                        itm.name
                                                    itemHolder.tv(R.id.action_id)?.text =
                                                        "动作ID: " + itm.id.toString()
                                                    itemHolder.tv(R.id.action_description)?.text =
                                                        itm.description

                                                    itemHolder.click(R.id.fold_button) {
                                                        itemGroupExtend = !itemGroupExtend
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
                                        }

                                        // kotlin
                                        RxHttp.postForm(getString(R.string.default_url) + "/api/v1/operations")
                                            .add("action_id", itm.id)
                                            .asString()
                                            .subscribe({ s ->
                                                try {
                                                    var s: operationResponse = Gson().fromJson(
                                                        s,
                                                        operationResponse::class.java
                                                    )
                                                    var operationList = s.data
                                                    //println("SYS LOG: " + operationList)
                                                    // 设置operations
                                                    for (j in operationList.indices) {
                                                        if (del != 0 && i == delAction.toInt()-1 && j == del-1) {

                                                        } else {
                                                            val item = operationList[j]
                                                            dslItem(DslDemoItem()) {
                                                                itemGroups = mutableListOf("group${i}")
                                                                itemBindOverride =
                                                                    { itemHolder, _, _, _ ->
                                                                        itemGroupParams.apply {
                                                                            itemHolder.tv(R.id.operation_title)?.text =
                                                                                item.name
                                                                            itemHolder.tv(R.id.operation_id)?.text =
                                                                                item.id.toString()
                                                                            itemHolder.tv(R.id.operation_ele)?.text =
                                                                                "操作元件: " + item.element
                                                                            itemHolder.tv(R.id.operation_obj)?.text =
                                                                                "操作对象: " + item.`object`
                                                                            itemHolder.tv(R.id.operation_type)?.text =
                                                                                "操作类型: " + item.type
                                                                            itemHolder.tv(R.id.operation_degree)?.text =
                                                                                item.degree

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
                                        sleep(300)
                                        nDialog.dismiss()
                                    }
                                }
                            }
                            ordertasklist._dslAdapter?.addLastItem(item)
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
