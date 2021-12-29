package com.example.flightrobot

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.potyvideo.library.AndExoPlayerView
import com.potyvideo.library.globalEnums.EnumResizeMode
import com.potyvideo.library.globalInterfaces.AndExoPlayerListener
import com.potyvideo.library.utils.PublicValues
import kotlinx.android.synthetic.main.activity_video_player.*
import java.util.*


class VideoPlayer : AppCompatActivity(), AndExoPlayerListener, View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)
        Objects.requireNonNull(supportActionBar)!!.setDisplayHomeAsUpEnabled(true)

        var url: String = intent.getStringExtra("url")!!

        val andExoPlayerView = findViewById<AndExoPlayerView>(R.id.andExoPlayerView)
        andExoPlayerView.setResizeMode(EnumResizeMode.FIT) // sync with attrs
        andExoPlayerView.setAndExoPlayerListener(this)
        andExoPlayerView.setSource(url)

        window.setFlags(
            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    override fun onClick(v: View?) {
        when (v?.id) {
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === android.R.id.home) {
            andExoPlayerView.stopPlayer()
            andExoPlayerView.releasePlayer()
            finish() // finish your activity
        }
        return super.onOptionsItemSelected(item)
    }
}
