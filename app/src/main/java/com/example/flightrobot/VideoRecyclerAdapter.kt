package com.example.flightrobot

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
import com.example.flightrobot.models.taskResponse
import rxhttp.RxHttp
import java.util.*


/**
 * @data on 2020/9/25 9:05 AM
 * @auther arain
 * @describe Recycler使用
 */
class VideoRecyclerAdapter (private val orderList: List<orderResponse.Data>) :
    RecyclerView.Adapter<VideoRecyclerAdapter.MyViewHolder>() {
    var filterList : MutableList<orderResponse.Data> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.videolist, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = orderList.size ?: 0

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val orderpos = orderList[position]
        var timeString : String = orderpos.created_at.toString().subSequence(IntRange(0, 9)) as String
        holder.video_name.text = timeString.plus('\n').plus(orderpos.created_at.toString().subSequence(IntRange(11, 15)))

        var desButton: ImageButton = holder.itemView.findViewById(R.id.videobtn)
        desButton.setOnClickListener {
            RxHttp.postForm(holder.itemView.context.getString(R.string.default_url) + "/api/v1/actions")
                .add("task_id", orderpos.task_id)
                .asString()
                .subscribe({ s ->
                    try {
                        val it = Intent()
                        it.setClass((holder.itemView.context), VideoPlayer::class.java)
                        val task_id: Int = orderpos.task_id.toInt()
                        it.putExtra("url", "http://vfx.mtime.cn/Video/2018/07/06/mp4/180706094003288023.mp4")
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
//        val task_id: TextView = itemView.findViewById(R.id.order_task_id)
        val video_name: TextView = itemView.findViewById(R.id.video_name)
    }
}