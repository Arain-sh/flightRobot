package com.example.flightrobot

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.flightrobot.models.taskResponse


/**
 * @data on 2020/9/25 9:05 AM
 * @auther armStrong
 * @describe Recycler使用
 */
class RecyclerAdapter(private val taskList: List<taskResponse.Data>) :
    RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.tasklist, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = taskList.size ?: 0

    override fun onBindViewHolder(holder: RecyclerAdapter.MyViewHolder, position: Int) {
        val taskpos = taskList[position]
        holder.title.text = taskpos.name
        holder.id.text = "任务ID: " + taskpos.id.toString()
        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context, "${holder.title.text}", Toast.LENGTH_SHORT)
                .show()
            val it = Intent()
            it.setClass((holder.itemView.context), TaskActivity::class.java)
            val task_id: Int = taskpos.id
            it.putExtra("task_id", task_id)
            holder.itemView.context.startActivity(it)
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.mine_title)
        val id: TextView = itemView.findViewById(R.id.mine_id)
    }
}