package com.example.flightrobot

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.flightrobot.models.sysinfoResponse
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import rxhttp.RxHttp


class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    var user = com.example.flightrobot.user("arain", 0, false)
    companion object {
        const val REQUEST_LOGIN = 10
        const val RESULT_LOGIN_FAILED = 20
        var isFromOrder = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //获取参数
        // kotlin
        RxHttp.get(this.getString(R.string.default_url) + "/api/v1/sysinfos")
            .asString()
            .subscribe({ s ->
                try {
                    var s: sysinfoResponse = Gson().fromJson(s, sysinfoResponse::class.java)

                    var sysinfo = s.data.get(0)
                    println("SYSLOG: " + sysinfo)
                    if (sysinfo.graph == 0) user.isLogin = true
                    else {
                        //如果需要登录，go login界面，back to main
                        if (!user.isLogin) {
                            LoginActivity.startForResult(this)
                        } else {
                            init()
                        }
                    }
                } catch (e: Exception) {
                    println(e)
                }
            }, { throwable ->
                println(throwable)
                println("Sys Log: cannot get data")
            })


        // Hide the status bar.
        window.setFlags(
            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> println("to be finished")
            R.id.logout_button -> {
                //LoginActivity.startForResult(this)
                val it = Intent()
                it.setClass((this), LoginActivity::class.java)
                startActivity(it);
                //finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_LOGIN) {
            if (resultCode == RESULT_LOGIN_FAILED) {
                finish()
            } else {
                init()
            }
        }
    }

    fun init() {
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setItemIconTintList(null)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_gallery,
                R.id.nav_slideshow,
                R.id.nav_settings,
                R.id.nav_files,
                R.id.nav_sys_monitor
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //用户登出
    }

    override fun dispatchTouchEvent(me: MotionEvent): Boolean {
        if (me.action == MotionEvent.ACTION_DOWN) {  //把操作放在用户点击的时候
            val v: View? = currentFocus //得到当前页面的焦点,ps:有输入框的页面焦点一般会被输入框占据
            if (isShouldHideKeyboard(v, me)) { //判断用户点击的是否是输入框以外的区域
                if (v != null) {
                    hideKeyboard(v.getWindowToken())
                } //收起键盘
            }
        }
        return super.dispatchTouchEvent(me)
    }

    private fun isShouldHideKeyboard(v: View?, event: MotionEvent): Boolean {
        if (v != null && v is EditText) {  //判断得到的焦点控件是否包含EditText
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            //得到输入框在屏幕中上下左右的位置
            val top = l[1]
            val bottom = top + v.getHeight()
            val right = left + v.getWidth()
            return !(event.x > left && event.x < right && event.y > top && event.y < bottom)
        } else {
            if (v != null && v is Button) {
                val l = kotlin.intArrayOf(0, 0)
                v.getLocationInWindow(l)
                val left = l[0]
                //得到输入框在屏幕中上下左右的位置
                val top = l[1]
                val bottom = top + v.getHeight()
                val right = left + v.getWidth()
                return !(event.x > left && event.x < right && event.y > top && event.y < bottom)
            }
        }
        // 如果焦点不是EditText则忽略
        return false
    }

    private fun hideKeyboard(token: IBinder?) {
        if (token != null) {
            val im: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

}