package com.example.flightrobot.ui.sysmonitor

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BasicGridItem
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.gridItems
import com.example.flightrobot.*
import com.example.flightrobot.models.sysinfoResponse
import com.google.gson.Gson
import rxhttp.RxHttp

class SysmonitorFragment : Fragment() {

    private lateinit var sysmonitorViewModel: SysmonitorViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        sysmonitorViewModel =
                ViewModelProviders.of(this).get(SysmonitorViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_sysmonitor, container, false)
        //val textView: TextView = root.findViewById(R.id.text_slideshow)
        sysmonitorViewModel.text.observe(viewLifecycleOwner, Observer {
            //textView.text = it
        })
        val hxj: Button = root.findViewById(R.id.hxj_status)
        val jxb: Button = root.findViewById(R.id.jxb_status)
        val sjxt: Button =root.findViewById(R.id.sjxt_status)
        val cam_01: Button = root.findViewById(R.id.cam_01_status)
        val cam_02: Button = root.findViewById(R.id.cam_02_status)
        val cam_03: Button = root.findViewById(R.id.cam_03_status)
        val cpu: Button = root.findViewById(R.id.cpu_status)
        val card: Button = root.findViewById(R.id.card_status)
        val graph: Button = root.findViewById(R.id.graph_status)

        //获取参数
        // kotlin
        RxHttp.get(this.getString(R.string.default_url) + "/api/v1/sysinfos")
            .asString()
            .subscribe({ s ->
                try {
                    var s: sysinfoResponse = Gson().fromJson(s, sysinfoResponse::class.java)

                    var sysinfo = s.data.get(0)
                    println("SYSLOG: " + sysinfo)

                    this.activity?.runOnUiThread() {
                        sysinfo?.let{
                            cpu.text = sysinfo.cpu
                            card.text = sysinfo.card
                            if (sysinfo.jxb_hxj == 0) {
                                hxj.text = "离线"
                            } else hxj.text = "正常"
                            if (sysinfo.jxb_jxb == 0) jxb.text = "离线"
                            else jxb.text = "正常"
                            if (sysinfo.jxb_sj == 0) sjxt.text = "离线"
                            else sjxt.text = "正常"
                            if (sysinfo.sxt_01 == 0) cam_01.text = "离线"
                            else cam_01.text = "正常"
                            if (sysinfo.sxt_02 == 0) cam_02.text = "离线"
                            else cam_02.text = "正常"
                            if (sysinfo.sxt_03 == 0) cam_03.text = "离线"
                            else cam_03.text = "正常"
                            if (sysinfo.graph == 0) graph.text = "离线"
                            else graph.text = "正在运行"

                        }
                    }

                } catch (e: Exception) {
                    println(e)
                }
            }, { throwable ->
                println(throwable)
                println("Sys Log: cannot get data")
            })

        val items = listOf(
            BasicGridItem(R.drawable.ic_menu_camera, "工作状态显示"),
            BasicGridItem(R.drawable.ic_menu_camera, "实时视频监控"),
            BasicGridItem(R.drawable.ic_menu_camera, "视频回放"),
        )
        var monitorButton: Button = root.findViewById(R.id.monitorButton)
        var videoButton: Button = root.findViewById(R.id.videoButton)
        monitorButton.setOnClickListener {
            this.context?.let {
                val it = Intent(root.context, CamActivity::class.java)
                startActivity(it)
            }
        }
        videoButton.setOnClickListener {
            this.context?.let {
                val it = Intent(root.context, VideoActivity::class.java)
                startActivity(it)
            }
        }
        return root
    }
}