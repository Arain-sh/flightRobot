package com.example.flightrobot

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.flightrobot.models.orderResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_video.*
import kotlinx.android.synthetic.main.fragment_gallery.*
import kotlinx.android.synthetic.main.table_row_header_text_data.*
import ph.ingenuity.tableview.TableView
import ph.ingenuity.tableview.feature.filter.Filter
import ph.ingenuity.tableview.feature.pagination.Pagination
import ph.ingenuity.tableviewdemo.data.RandomDataFactory
import ph.ingenuity.tableviewdemo.listeners.TableViewListener
import rxhttp.RxHttp
import java.util.*
import androidx.appcompat.widget.SearchView


class VideoActivity : AppCompatActivity() {
    companion object {
        fun startForResult(activity: Activity, requestCode: Int = MainActivity.REQUEST_LOGIN) {
            val intent = Intent(activity, VideoActivity::class.java)
            activity.startActivityForResult(intent, requestCode)
        }
    }

    private lateinit var previousButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)
        val video_search = findViewById<SearchView>(R.id.video_search)
        var filterList : MutableList<orderResponse.Data> = mutableListOf()
        //init
        initializeViews()
        // kotlin
        RxHttp.get(this.getString(R.string.default_url) + "/api/v1/orders")
            .asString()
            .subscribe({ s ->
                try {
                    var s: orderResponse = Gson().fromJson(s, orderResponse::class.java)
                    var videoList = s.data
                    videoList?.let{
                        this.runOnUiThread {
                            //这里面进行UI的更新操作
                            val layoutManager = GridLayoutManager(this, 5)
                            videoRecycler.layoutManager = layoutManager
                            val adapter = VideoRecyclerAdapter(videoList.reversed())
                            videoRecycler.adapter = adapter

                            video_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                                override fun onQueryTextSubmit(query: String?): Boolean {
                                    return false
                                }

                                override fun onQueryTextChange(newText: String?): Boolean {
                                    filterList = fil(newText!!)
                                    videoRecycler.adapter = VideoRecyclerAdapter(filterList.reversed())
                                    return false
                                }
                                fun fil(constraint: String) : MutableList<orderResponse.Data> {
                                    val charSearch = constraint
                                    if (charSearch.isEmpty()) {
                                        filterList = videoList
                                    } else {
                                        val resultList : MutableList<orderResponse.Data> = mutableListOf()
                                        for (row in videoList) {
                                            var timeString : String = row.created_at.toString().subSequence(IntRange(0, 9)) as String
                                            if (timeString.plus('\n').plus(row.created_at.toString().subSequence(IntRange(11, 15))).contains(charSearch)) {
                                                resultList.add(row)
                                            }
                                        }
                                        filterList = resultList
                                    }
                                    return filterList
                                }
                            })
                        }
                    }
                } catch (e: Exception) {
                    println(e)
                }
            }, { throwable ->
                println(throwable)
                println("Sys Log: cannot get data")
            })
        // Retrieve your data from local storage or API
        initializeData()
        //init listener

        window.setFlags(
            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    private fun initializeViews() {
    }

    private fun initializeData() {
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === android.R.id.home) {
            finish() // finish your activity
        }
        return super.onOptionsItemSelected(item)
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