package com.example.flightrobot

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flightrobot.models.actionResponse

/**
 * @data on 2020/9/25 9:05 AM
 * @auther arain
 * @describe Recycler使用
 */
class ActionRecyclerAdapter(private val actionList: List<actionResponse.Data>) :
    RecyclerView.Adapter<ActionRecyclerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.actionlist, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = actionList.size ?: 0

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val taskpos = actionList[position]
        holder.title.text = taskpos.name
        holder.id.text = "动作ID: " + taskpos.id.toString()
        holder.des.text = taskpos.description
        holder.itemView.setOnClickListener {
            val it = Intent()
            it.setClass((holder.itemView.context), OperationActivity::class.java)
            val action_id: Int = taskpos.id
            it.putExtra("action_id", action_id)
            it.putExtra("task_id", taskpos.task_id)
            MainActivity.isFromOrder = false
            holder.itemView.context.startActivity(it)
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.action_title)
        val id: TextView = itemView.findViewById(R.id.action_id)
        val des: TextView = itemView.findViewById(R.id.action_description)
    }
}