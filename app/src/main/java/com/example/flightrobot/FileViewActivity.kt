package com.example.flightrobot

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class FileViewActivity : AppCompatActivity() {
    private var webView: WebView? = null
    private var exitTime: Long = 0

    companion object {
        fun startForResult(activity: Activity, requestCode: Int = MainActivity.REQUEST_LOGIN) {
            val intent = Intent(activity, LoginActivity::class.java)
            activity.startActivityForResult(intent, requestCode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_view)
        Objects.requireNonNull(getSupportActionBar())?.setDisplayHomeAsUpEnabled(true);

        webView = WebView(this)
        webView!!.setWebViewClient(object : WebViewClient() {
            //设置在webView点击打开的新网页在当前界面显示,而不跳转到新的浏览器中
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        })
        webView!!.getSettings().setJavaScriptEnabled(true) //设置WebView属性,运行执行js脚本
        var url2load: String = intent.getStringExtra("url")!!
        webView!!.loadUrl(url2load) //调用loadUrl方法为WebView加入链接

        setContentView(webView)
        window.setFlags(
            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    override fun onBackPressed() {
        if (webView!!.canGoBack()) {
            webView!!.goBack()
        } else {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(
                    applicationContext, "再按一次退出程序",
                    Toast.LENGTH_SHORT
                ).show()
                exitTime = System.currentTimeMillis()
            } else {
                super.onBackPressed()
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === android.R.id.home) {
            finish() // finish your activity
        }
        return super.onOptionsItemSelected(item)
    }
}