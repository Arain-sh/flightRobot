package com.example.flightrobot

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flightrobot.models.generalResponse
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.gson.Gson
import com.tapadoo.alerter.Alerter
import rxhttp.RxHttp
import java.time.LocalDate
import java.time.LocalDateTime


class LoginActivity : AppCompatActivity() {
    private var username: EditText? = null
    private var password: EditText? = null

    companion object {
        fun startForResult(activity: Activity, requestCode: Int = MainActivity.REQUEST_LOGIN) {
            val intent = Intent(activity, LoginActivity::class.java)
            activity.startActivityForResult(intent, requestCode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login)
        username = findViewById(R.id.userinput)
        password = findViewById(R.id.pwdinput)
        val login: Button = findViewById(R.id.loginbutton)
        bindProgressButton(login)
        login.attachTextChangeAnimator()
        finish()


        login.setOnClickListener {
            // 设置 setResult，触发 BaseActivity 的 onActivityResult 方法
            setResult(Activity.RESULT_OK)
            login.showProgress {
                buttonTextRes = R.string.loading
                progressColor = Color.WHITE
            }
            if (username?.text.toString() == null) {

            } else if (password?.text == null) {

            } else {
                // kotlin
                RxHttp.postForm(this.getString(R.string.default_url) + "/api/v1/login")
                    .add("email", username?.text.toString())
                    .add("password", password?.text.toString())
                    .asString()
                    .subscribe({ s ->
                        try {
                            var s: generalResponse = Gson().fromJson(s, generalResponse::class.java)
                            //println(s.data.get(1))
                            val codeStatus: Int = s.code
                            if (codeStatus == 200) {
                                finish()
                            } else {
                                println("SYS LOG: $codeStatus")
                            }
                        } catch (e: Exception) {
                        }
                    }, { throwable ->
                        if (username?.text.toString() == "") {
                            Alerter.create(this@LoginActivity)
                                .setTitle("用户名错误")
                                .setText("请正确填写用户名称...")
                                .setBackgroundColorRes(R.color.colorAccent) // or setBackgroundColorInt(Color.CYAN)
                                .show()
                        } else if (password?.text.toString() == "") {
                            Alerter.create(this@LoginActivity)
                                .setTitle("密码错误")
                                .setText("请正确填写密码...")
                                .setBackgroundColorRes(R.color.colorAccent) // or setBackgroundColorInt(Color.CYAN)
                                .show()
                        } else {
                            Alerter.create(this@LoginActivity)
                                .setTitle("验证失败")
                                .setText("请查证后再次登录...")
                                .setBackgroundColorRes(R.color.colorAccent) // or setBackgroundColorInt(Color.CYAN)
                                .show()
                        }
                    })
                Handler().postDelayed({
                    login.hideProgress(R.string.loginSuccess)
                }, 800)
            }
            Handler().postDelayed({
                login.hideProgress(R.string.login)
            }, 800)

        }

    }

    override fun onBackPressed() {
        setResult(MainActivity.RESULT_LOGIN_FAILED)
        super.onBackPressed()
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