package com.example.flightrobot

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.example.flightrobot.models.fileResponse
import com.example.flightrobot.models.operationResponse
import com.google.gson.Gson
import com.rajat.pdfviewer.PdfViewerActivity
import kotlinx.android.synthetic.main.fragment_operation.*
import rxhttp.RxHttp


/**
 * @data on 2020/9/25 9:05 AM
 * @auther arain
 * @describe Recycler使用
 */
class FileRecyclerAdapter(private val fileList: List<fileResponse.Data>) :
    RecyclerView.Adapter<FileRecyclerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.filelist, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = fileList.size ?: 0

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val filepos = fileList[position]
        holder.title.text = filepos.title
        holder.des.text = filepos.description
        val img_url: String = filepos.image
        val url: String = filepos.url
        val openButton: Button = holder.itemView.findViewById(R.id.file_open)

        openButton.setOnClickListener {
            holder.itemView.context.startActivity(
                PdfViewerActivity.buildIntent(
                    holder.itemView.context,
                    url,                                // PDF URL in String format
                    false,
                    filepos.title,                        // PDF Name/Title in String format
                    "",                  // If nothing specific, Put "" it will save to Downloads
                    enableDownload = false                    // This param is true by defualt.
                )
            )
            Toast.makeText(holder.itemView.context, "加载中....", Toast.LENGTH_SHORT).show();
        }


        holder.itemView.setOnClickListener {
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.file_title)
        val des: TextView = itemView.findViewById(R.id.file_description)
        val img: ImageView = itemView.findViewById(R.id.file_img)
    }
}