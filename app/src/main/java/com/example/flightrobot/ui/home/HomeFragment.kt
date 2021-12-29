package com.example.flightrobot.ui.home

import android.content.Intent
import android.graphics.Color
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.flightrobot.*
import com.example.flightrobot.ui.tasks.TasksFragment
import com.tapadoo.alerter.Alerter


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        //val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            //textView.text = it
        })

        val imgButton737: ImageView = root.findViewById(R.id.db737)
        val imgButton787: ImageView = root.findViewById(R.id.db787)
        val imgButton919: ImageView = root.findViewById(R.id.db919)

        imgButton737.setOnClickListener {
            val it = Intent(root.context, FlightDBActivity::class.java)
            val dbid: String = "737"
            it.putExtra("id", dbid)
            startActivity(it)
        }

        imgButton787.setOnClickListener {
            Alerter.create(this.activity)
                .setTitle("提示")
                .setText("是否选择该数据库")
                .setBackgroundColorRes(R.color.colorBar)
                .addButton("加载数据库", R.style.AlertButton, View.OnClickListener {
                    Toast.makeText(this.context, "加载成功", Toast.LENGTH_LONG).show()
                    val it = Intent(root.context, FlightDBActivity::class.java)
                    val dbid: String = "787"
                    it.putExtra("id", dbid)
                    startActivity(it)
                })
                .addButton("仅查看数据库", R.style.AlertButton, View.OnClickListener {
                    //Toast.makeText(this.context, "取消", Toast.LENGTH_LONG).show()
                    val it = Intent(root.context, FlightDBActivity::class.java)
                    val dbid: String = "787"
                    it.putExtra("id", dbid)
                    startActivity(it)
                })
                .show()
        }
        imgButton919.setOnClickListener {
            Alerter.create(this.activity)
                .setTitle("提示")
                .setText("是否选择该数据库")
                .setBackgroundColorRes(R.color.colorBar)
                .addButton("加载数据库", R.style.AlertButton, View.OnClickListener {
                    Toast.makeText(this.context, "加载成功", Toast.LENGTH_LONG).show()
                    val it = Intent(root.context, FlightDBActivity::class.java)
                    val dbid: String = "919"
                    it.putExtra("id", dbid)
                    startActivity(it)
                })
                .addButton("查看数据库", R.style.AlertButton, View.OnClickListener {
                    //Toast.makeText(this.context, "No Clicked", Toast.LENGTH_LONG).show()
                    val it = Intent(root.context, FlightDBActivity::class.java)
                    val dbid: String = "919"
                    it.putExtra("id", dbid)
                    startActivity(it)
                })
                .show()
        }
        return root
    }
}