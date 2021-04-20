package com.example.flightrobot

import actionResponse
import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.example.flightrobot.models.operationResponse
import com.example.flightrobot.models.orderResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_operation.*
import kotlinx.android.synthetic.main.fragment_taskinfo.*
import org.w3c.dom.Text
import rxhttp.RxHttp


/**
 * @data on 2020/9/25 9:05 AM
 * @auther arain
 * @describe Recycler使用
 */
class OrderRecyclerAdapter (private val orderList: List<orderResponse.Data>) :
    RecyclerView.Adapter<OrderRecyclerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.orderlist, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = orderList.size ?: 0

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val orderpos = orderList[position]
        holder.order_id.text = orderpos.id.toString()
        holder.task_id.text = "任务ID: " + orderpos.task_id
        holder.order_created.text = orderpos.created_at
        if (orderpos.run_status == "pending") {
            holder.order_status.text = "等待中..."
        }
        if (orderpos.run_status == "running") {
            holder.order_status.text = "正在执行..."
        }
        if (orderpos.run_status == "finished") {
            holder.order_status.text = "已完成."
            holder.order_status.setTextColor(holder.itemView.context.getColor(R.color.colorCyanButton))
        }

        var desButton: Button = holder.itemView.findViewById(R.id.order_des)
        desButton.setOnClickListener {
            RxHttp.postForm("http://192.168.10.10/api/v1/actions")
                .add("task_id", orderpos.task_id)
                .asString()
                .subscribe({ s ->
                    try {
                        val it = Intent()
                        it.setClass((holder.itemView.context), orderTaskActivity::class.java)
                        val task_id: Int = orderpos.task_id.toInt()
                        it.putExtra("task_id", task_id)
                        it.putExtra("isOrder", true)
                        MainActivity.isFromOrder = true
                        print("SYS: " + MainActivity.isFromOrder)
                        holder.itemView.context.startActivity(it)
                    } catch (e: Exception) {
                        println(e)
                    }
                }, { throwable ->
                    println(throwable)
                    println("Sys Log: cannot get data")
                })
            Toast.makeText(holder.itemView.context, ".", Toast.LENGTH_SHORT).show()
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val task_id: TextView = itemView.findViewById(R.id.order_task_id)
        val order_id: TextView = itemView.findViewById(R.id.order_id)
        val order_created: TextView = itemView.findViewById(R.id.order_created)
        val order_status: TextView = itemView.findViewById(R.id.order_run_status)
    }
}