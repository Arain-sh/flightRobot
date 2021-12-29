package com.example.flightrobot.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bitvale.switcher.SwitcherX
import com.example.flightrobot.R
import com.example.flightrobot.models.sysinfoResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_settings.*
import rxhttp.RxHttp
import java.time.LocalDateTime


class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_settings, container, false)
        var hxj: SwitcherX = root.findViewById(R.id.hxj_switch)
        var jxb: SwitcherX = root.findViewById(R.id.jxb_switch)
        var sj: SwitcherX = root.findViewById(R.id.sjxi_switch)

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
                            if (sysinfo.jxb_hxj == 0) {
                                hxj.setChecked(false)
                            } else hxj.setChecked(true)
                            if (sysinfo.jxb_jxb == 0) jxb.setChecked(false)
                            else jxb.setChecked(true)
                            if (sysinfo.sxt_01 == 0) sj.setChecked(false)
                            else sj.setChecked(true)
                        }
                    }
                } catch (e: Exception) {
                    println(e)
                }
            }, { throwable ->
                println(throwable)
            })

        hxj.setOnCheckedChangeListener { checked ->
            if (checked) {
                RxHttp.postForm(this.getString(R.string.com_url))
                    .add("group", 1)
                    .add("state", 1) //a up
                    .add("end", "true")
                    .asString()
                    .subscribe({ s ->
                        try {
                            println("SYS LOG: $s")
                        } catch (e: Exception) {
                            println(e)
                        }
                    }, { throwable ->
                        println(throwable)
                    })
                RxHttp.postForm(requireParentFragment().getString(R.string.default_url) + "/api/v1/sysinfos")
                    .add("id", 1)
                    .add("jxb_hxj", 1)
                    .asString()
                    .subscribe({ s ->
                        try {
                        } catch (e: Exception) {
                            println(e)
                        }
                    }, { throwable ->
                        println(throwable)
                    })
            } else {
                RxHttp.postForm(this.getString(R.string.com_url))
                    .add("group", 1)
                    .add("state", 0) //a down
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

                RxHttp.postForm(requireParentFragment().getString(R.string.default_url) + "/api/v1/sysinfos")
                    .add("id", 1)
                    .add("jxb_hxj", 0)
                    .asString()
                    .subscribe({ s ->
                        try {
                        } catch (e: Exception) {
                            println(e)
                        }
                    }, { throwable ->
                        println(throwable)
                    })
            }
        }

        sj.setOnCheckedChangeListener { checked ->
            if (checked) {
                RxHttp.postForm(this.getString(R.string.com_url))
                    .add("group", 2)
                    .add("state", 1) //b up
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

                RxHttp.postForm(requireParentFragment().getString(R.string.default_url) + "/api/v1/sysinfos")
                    .add("id", 1)
                    .add("sxt_01", 1)
                    .add("sxt_02",1)
                    .asString()
                    .subscribe({ s ->
                        try {
                        } catch (e: Exception) {
                            println(e)
                        }
                    }, { throwable ->
                        println(throwable)
                    })
            } else {
                RxHttp.postForm(this.getString(R.string.com_url))
                    .add("group", 2)
                    .add("state", 0) //b down
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

                RxHttp.postForm(requireParentFragment().getString(R.string.default_url) + "/api/v1/sysinfos")
                    .add("id", 1)
                    .add("sxt_01", 0)
                    .add("sxt_02", 0)
                    .asString()
                    .subscribe({ s ->
                        try {
                        } catch (e: Exception) {
                            println(e)
                        }
                    }, { throwable ->
                        println(throwable)
                        println("Sys Log: cannot get data")
                    })
            }
        }
        jxb.setOnCheckedChangeListener { checked ->
            if (checked) {
                RxHttp.postForm(this.getString(R.string.com_url))
                    .add("group", 3)
                    .add("state", 1) //c up
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
                RxHttp.postForm(requireParentFragment().getString(R.string.default_url) + "/api/v1/sysinfos")
                    .add("id", 1)
                    .add("jxb_jxb", 1)
                    .asString()
                    .subscribe({ s ->
                        try {
                        } catch (e: Exception) {
                            println(e)
                        }
                    }, { throwable ->
                        println(throwable)
                        println("Sys Log: cannot get data")
                    })
            } else {
                RxHttp.postForm(this.getString(R.string.com_url))
                    .add("group", 3)
                    .add("state", 0) //c down
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
                RxHttp.postForm(requireParentFragment().getString(R.string.default_url) + "/api/v1/sysinfos")
                    .add("id", 1)
                    .add("jxb_jxb", 0)
                    .asString()
                    .subscribe({ s ->
                        try {
                        } catch (e: Exception) {
                            println(e)
                        }
                    }, { throwable ->
                        println(throwable)
                        println("Sys Log: cannot get data")
                    })
            }

        }
        return root
    }

}