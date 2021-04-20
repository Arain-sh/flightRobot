package com.example.flightrobot

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.example.flightrobot.models.operationResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_operation.*
import org.w3c.dom.Text
import rxhttp.RxHttp


/**
 * @data on 2020/9/25 9:05 AM
 * @auther arain
 * @describe Recycler使用
 */
class OperationRecyclerAdapter(private val operationList: List<operationResponse.Data>) :
    RecyclerView.Adapter<OperationRecyclerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.operationlist, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = operationList.size ?: 0

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val operationpos = operationList[position]
        holder.title.text = operationpos.name
        holder.id.text = "操作ID: " + operationpos.id.toString()
        holder.ele.text = "操作元件: " + operationpos.element
        holder.obj.text = "操作对象: " + operationpos.`object`
        holder.type.text = "操作类型: " + operationpos.type
        holder.degree.text = operationpos.degree
        var fixButton: Button = holder.itemView.findViewById(R.id.operation_degree)
        fixButton.setOnClickListener {
            MaterialDialog(holder.itemView.context).show {
                input { dialog, text ->
                    // Text submitted with the action button
                    // kotlin
                    fixButton.text = text
                    RxHttp.postForm("http://192.168.10.10/api/v1/operations/update")
                        .add("id", operationpos.id)
                        .add("degree", text)
                        .asString()
                        .subscribe({ s ->
                            try {
                                println(s)
                            } catch (e: Exception) {
                                println(e)
                            }
                        }, { throwable ->
                            println(throwable)
                            println("Sys Log: cannot get data")
                        })
                    RxHttp.postForm("http://192.168.1.104:7890")
                        .add("object", 2)
                        .add("element", 15)
                        .add("degree", 1)
                        .add("type", "push")
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
                positiveButton(R.string.agree)
                negativeButton(R.string.disagree) { dialog ->
                    // Do something
                }
            }
            Toast.makeText(holder.itemView.context, "已修改.", Toast.LENGTH_SHORT).show();
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.operation_title)
        val id: TextView = itemView.findViewById(R.id.operation_id)
        val obj: TextView = itemView.findViewById(R.id.operation_obj)
        val ele: TextView = itemView.findViewById(R.id.operation_ele)
        val type: TextView = itemView.findViewById(R.id.operation_type)
        val degree: Button = itemView.findViewById(R.id.operation_degree)

    }
}