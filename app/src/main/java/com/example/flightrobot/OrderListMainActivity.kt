package com.example.flightrobot

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.angcyo.dsladapter.*
import com.angcyo.dsladapter.ItemSelectorHelper.Companion.MODEL_SINGLE
import com.example.flightrobot.models.operationResponse
import com.example.flightrobot.models.orderResponse
import com.example.flightrobot.models.taskResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_create_list.*
import kotlinx.android.synthetic.main.fragment_operation.*
import kotlinx.android.synthetic.main.fragment_tasks.*
import kotlinx.android.synthetic.main.orderlist.*
import kotlinx.android.synthetic.main.orderlistmain.*
import orderdetailResponse
import rxhttp.RxHttp
import java.util.*
import kotlin.collections.ArrayList

class OrderListMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.orderlistmain)

        Objects.requireNonNull(getSupportActionBar())?.setDisplayHomeAsUpEnabled(true)
        var dslList : MutableList<DslAdapterItem>? = null
        var operationList : MutableList<operationResponse.Data> = mutableListOf()
        var orders : MutableList<orderdetailResponse.Data> = mutableListOf()
        var ol : MutableList<orderResponse.Data> = mutableListOf()
        var order_id = "1"

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
        // kotlin
        RxHttp.get(this.getString(R.string.default_url) + "/api/v1/orderdetails")
            .asString()
            .subscribe({ s ->
                try {
                    var s: orderdetailResponse = Gson().fromJson(s, orderdetailResponse::class.java)
                    orders = s.data
                    orders.reverse()
                    val layoutManager = GridLayoutManager(this, 4)
                    runOnUiThread {
                        //这里面进行UI的更新操作
                        //使用Recycler
                        orderlistRecycler.layoutManager = layoutManager
                        orderlistRecycler.adapter = DslAdapter()
                        if (orders.isNotEmpty()) {
                            val item = DslAdapterItem()
                            item.itemLayoutId = R.layout.orderlistmainitm
                            item.itemBindOverride = { itemHolder, _, _, _ ->
                                orderlistRecycler.dslAdapter {
                                    for (i in orders.indices) {
                                        if (i < 10) {
                                            val itm = orders[i]
                                            dslItem(R.layout.orderlistmainitm) {
                                                dslList?.add(this)
                                                itemTopInsert = 5 * dpi
                                                itemBindOverride =
                                                    { itemHolder, itemPosition, adapterItem, _ ->
                                                        itemHolder.tv(R.id.orderlisttitle)?.text = itm.name
                                                        itemHolder.tv(R.id.orderlistid)?.text =
                                                            "任务清单ID: " + itm.id.toString()
                                                        if (itemIsSelected) {
                                                            itemHolder.tv(R.id.orderlisttitle)!!.setBackgroundColor(Color.GREEN)
                                                        }
                                                        itemHolder.tv(R.id.orderlisttitle)?.apply {
                                                            setBackgroundColor(
                                                                when {
                                                                    itemIsSelected -> Color.GREEN
                                                                    else -> Color.WHITE
                                                                }
                                                            )
                                                        }
                                                        itemHolder.tv(R.id.orderlistid)?.apply {
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
                                                    if (itemIsSelected) {
                                                        order_id = itm.id.toString()
                                                    }
                                                    updateItemDepend()
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            orderlistRecycler._dslAdapter?.addLastItem(item)
                        }

                        var pushList: Button = findViewById(R.id.ordermain_push)
                        var str: String = ""
                        pushList.setOnClickListener {
                            RxHttp.postForm(this.getString(R.string.default_url) + "/api/v1/orderdetails")
                                .add("id", order_id)
                                .asString()
                                .subscribe({ s ->
                                    try {
                                        var s: orderdetailResponse = Gson().fromJson(s, orderdetailResponse::class.java)
                                        var orderlist = s.data
                                        str = orderlist[0].operations
                                        // kotlin
                                        RxHttp.postForm(this.getString(R.string.default_url) + "/api/v1/orders/store")
                                            .add("user_id", 1)
                                            .add("task_id", 0)// 0: pending; 1: running; 2: finished;
                                            .add("status", orderlist[0].name)// order名称
                                            .add("run_status", str)//operations
                                            .add("run_step", 0)
                                            .asString()
                                            .subscribe({ s ->
                                                try {
                                                    //发送指令
                                                    val strs = str.split(",")
                                                    println("strs: $strs")
                                                    for (i in operationList.indices) {
                                                        var op = operationList[i]
                                                        if (op.id.toString() in strs) {
                                                            println(op.name)
                                                            RxHttp.postForm(getString(R.string.com_url))
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
                                                                })
                                                        }

                                                    }
                                                    Toast.makeText(this, "发布成功", Toast.LENGTH_SHORT).show();
                                                    finish()
                                                } catch (e: Exception) {
                                                    println(e)
                                                }
                                            }, { throwable ->
                                                println(throwable)
                                            })
                                    } catch (e: Exception) {
                                        println(e)
                                    }
                                }, { throwable ->
                                    println(throwable)
                                })
                        }
                        var viewList: Button = findViewById(R.id.ordermain_view)
                        viewList.setOnClickListener {
                            val it = Intent(this, FlightOrderActivity::class.java)
                            it.putExtra("id", order_id)
                            startActivity(it)
                        }
                        Thread.sleep(50)
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

        var addList: Button = findViewById(R.id.ordermain_add)
        addList.setOnClickListener {
            val it = Intent(this, CreateOrderActivity::class.java)
            val dbid: String = "787"
            it.putExtra("id", dbid)
            startActivity(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === android.R.id.home) {
            finish() // finish your activity
        }
        return super.onOptionsItemSelected(item)
    }
}