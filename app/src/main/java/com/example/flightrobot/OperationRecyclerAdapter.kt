package com.example.flightrobot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.setActionButtonEnabled
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.example.flightrobot.models.operationResponse
import kotlinx.android.synthetic.main.fragment_operation.*
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
                input (maxLength = 12, waitForPositiveButton = false) { dialog, text ->
                    // Text submitted with the action button
                    // kotlin
                    val inputField = dialog.getInputField()
                    var isValid = false
                    when(text.toString()) {
                        "0","1" -> isValid = true
                        else -> {}
                    }

                    inputField?.error = if (isValid) null else "该值应为0或1!"
                    dialog.setActionButtonEnabled(WhichButton.POSITIVE, isValid)
                }
                positiveButton(R.string.agree) {
                    val inputField: EditText = it.getInputField()
                    fixButton.text = inputField.text
                    RxHttp.postForm(holder.itemView.context.getString(R.string.default_url) + "/api/v1/operations/update")
                        .add("id", operationpos.id)
                        .add("degree", inputField.text)
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
                }
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