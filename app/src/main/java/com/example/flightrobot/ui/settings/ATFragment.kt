package com.example.flightrobot.ui.settings

import android.graphics.Color
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
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import kotlinx.android.synthetic.main.activity_base_recycler.*
import kotlinx.android.synthetic.main.fragment_settings.*
import rxhttp.RxHttp
import java.time.LocalDateTime
import java.util.*


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
        val circularProgressBar = root.findViewById<CircularProgressBar>(R.id.circularProgressBar2)
        circularProgressBar.apply {
            // Set Progress
            progress = 65f
            // or with animation
            setProgressWithAnimation(65f, 1000) // =1s

            // Set Progress Max
            progressMax = 200f

            // Set ProgressBar Color
            progressBarColor = Color.BLACK
            // or with gradient
            progressBarColorStart = Color.GRAY
            progressBarColorEnd = Color.RED
            progressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM

            // Set background ProgressBar Color
            backgroundProgressBarColor = Color.GRAY
            // or with gradient
            backgroundProgressBarColorStart = Color.WHITE
            backgroundProgressBarColorEnd = Color.RED
            backgroundProgressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM

            // Set Width
            progressBarWidth = 7f // in DP
            backgroundProgressBarWidth = 3f // in DP

            // Other
            roundBorder = true
            startAngle = 180f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }

        return root
    }


    class MyTimerTask() : TimerTask() {
        override fun run() {
            //这里填上每次触发要执行的代码
        }
    }

}