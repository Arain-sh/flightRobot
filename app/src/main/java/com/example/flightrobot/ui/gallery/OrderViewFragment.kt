package com.example.flightrobot.ui.gallery

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.angcyo.dsladapter.*
import com.example.flightrobot.*
import com.example.flightrobot.R
import com.example.flightrobot.models.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_create_list.*
import kotlinx.android.synthetic.main.fileviewtitle.*
import kotlinx.android.synthetic.main.fragment_gallery.*
import kotlinx.android.synthetic.main.fragment_orderview.*
import kotlinx.android.synthetic.main.fragment_taskinfo.*
import kotlinx.android.synthetic.main.fragment_tasks.*
import kotlinx.android.synthetic.main.orderview.*
import rxhttp.RxHttp
import java.lang.Thread.sleep

class OrderViewFragment : Fragment() {

    private lateinit var orderViewModel: OrderViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        orderViewModel =
                ViewModelProviders.of(this).get(OrderViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_orderview, container, false)
        orderViewModel.text.observe(viewLifecycleOwner, Observer {
        //    textView.text = it
        })

        var nDialog: ProgressDialog
        nDialog = ProgressDialog(this.context)
        nDialog.setMessage("Loading..")
        nDialog.setTitle("数据库加载中...")
        nDialog.setCancelable(true)
        nDialog.show()

        var tasklist : MutableList<taskResponse.Data> = mutableListOf()
        var actionlist : MutableList<actionResponse.Data> = mutableListOf()
        var operationlist : MutableList<operationResponse.Data> = mutableListOf()
        var orderOperations : MutableList<operationResponse.Data> = mutableListOf()
        val operationSearch = root.findViewById<SearchView>(R.id.orderview_search)
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

                                        this.activity?.runOnUiThread {
                                            //这里面进行UI的更新操作
                                            val layoutManager = GridLayoutManager(this.context, 1)
                                            orderviewRecycler.layoutManager = layoutManager
                                            val adapter = OperationRecyclerAdapter(orderOperations)
                                            orderviewRecycler.adapter = adapter
                                            nDialog.dismiss()

                                            orderviewRecycler.layoutManager = layoutManager
                                            orderviewRecycler.adapter = DslAdapter()
                                            if (orderOperations.isNotEmpty()) {
                                                val item = DslAdapterItem()
                                                item.itemLayoutId = R.layout.operationlist
                                                item.itemBindOverride = { itemHolder, _, _, _ ->
                                                    orderviewRecycler.dslAdapter {
                                                        for (i in orderOperations.indices) {
                                                            var itm = orderOperations[i]
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
                                                                        if (itemIsSelected) {
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
                                                orderviewRecycler._dslAdapter?.addLastItem(item)
                                            }
                                            //var pushList: Button = findViewById(R.id.pushlist)
                                            var str: String = ""
                                            /*
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
                                                    })
                                                Toast.makeText(this, "发布成功", Toast.LENGTH_SHORT).show();
                                                finish()
                                            }*/
                                            Thread.sleep(50)
                                        }
                                    }
                                    var fixButton: Button = root.findViewById(R.id.orderview_fix)
                                    fixButton.setOnClickListener {
                                        MaterialDialog(this.requireContext()).show {
                                            title(R.string.fix_title)
                                            message(R.string.orderfix_mes)
                                            positiveButton(R.string.agree) {
                                                RxHttp.postForm(getString(R.string.com_url))
                                                    .add("object", "")
                                                    .add("element", "")
                                                    .add("degree", "")
                                                    .add("type", "stop")
                                                    .add("showObject", "")
                                                    .add("showdcs_id", "")
                                                    .add("showtarget_value", "")
                                                    .add("task_name", "")
                                                    .add("end", "true")
                                                    .asString()
                                                    .subscribe({ s ->
                                                        try {
                                                        } catch (e: Exception) {
                                                            println(e)
                                                        }
                                                    }, { throwable ->
                                                        println(throwable)
                                                    })
                                                RxHttp.get(getString(R.string.default_url) + "/api/v1/orders/update")
                                                    .add("id", orderLast.id)
                                                    .add("task_id", 3)
                                                    .asString()
                                                    .subscribe({ s ->
                                                        try {
                                                        } catch (e: Exception) {
                                                            println(e)
                                                        }
                                                    }, { throwable ->
                                                        println(throwable)
                                                    })
                                                val it = Intent(root.context, EditOrderActivity::class.java)
                                                val run_step: Int = orderLast.run_step
                                                val task_name: String = orderLast.status
                                                it.putExtra("run_step", run_step)
                                                it.putExtra("task_name", task_name)
                                                startActivity(it)
                                            }
                                            negativeButton(R.string.disagree) { dialog ->
                                                // Do something
                                            }
                                        }
                                    }

                                    var stopButton: Button = root.findViewById(R.id.orderview_stop)
                                    stopButton.setOnClickListener {
                                        MaterialDialog(this.requireContext()).show {
                                            title(R.string.fix_title)
                                            message(R.string.orderstop_mes)
                                            positiveButton(R.string.agree2stop) {
                                                RxHttp.postForm(getString(R.string.com_url))
                                                    .add("object", "")
                                                    .add("element", "")
                                                    .add("degree", "")
                                                    .add("type", "stop")
                                                    .add("showObject", "")
                                                    .add("showdcs_id", "")
                                                    .add("showtarget_value", "")
                                                    .add("task_name", "")
                                                    .add("end", "true")
                                                    .asString()
                                                    .subscribe({ s ->
                                                        try {
                                                        } catch (e: Exception) {
                                                            println(e)
                                                        }
                                                    }, { throwable ->
                                                        println(throwable)
                                                    })

                                                RxHttp.get(getString(R.string.default_url) + "/api/v1/orders/update")
                                                    .add("id", orderLast.id)
                                                    .add("task_id", 3)
                                                    .asString()
                                                    .subscribe({ s ->
                                                        try {
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

        return root
    }
}