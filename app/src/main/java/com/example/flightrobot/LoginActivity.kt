package com.example.flightrobot

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

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
        val Login: Button = findViewById(R.id.loginbutton)
        Login.setOnClickListener {
            // 设置 setResult，触发 BaseActivity 的 onActivityResult 方法
            setResult(Activity.RESULT_OK)
            Toast.makeText(getApplicationContext(), "登录中...", Toast.LENGTH_SHORT).show();
            finish()
        }
    }

    override fun onBackPressed() {
        setResult(MainActivity.RESULT_LOGIN_FAILED)
        super.onBackPressed()
    }
}