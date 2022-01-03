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


class ATFragment : Fragment() {

    private lateinit var atViewModel: ATViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        atViewModel =
                ViewModelProviders.of(this).get(ATViewModel::class.java)
        val root = inflater.inflate(R.layout.autodetect, container, false)

        //获取参数
        // kotlin
        RxHttp.get(this.getString(R.string.default_url) + "/api/v1/sysinfos")
            .asString()
            .subscribe({ s ->
                try {
                    var s: sysinfoResponse = Gson().fromJson(s, sysinfoResponse::class.java)

                    var sysinfo = s.data.get(0)
                } catch (e: Exception) {
                    println(e)
                }
            }, { throwable ->
                println(throwable)
            })

        return root
    }

}