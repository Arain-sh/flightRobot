package com.example.flightrobot

import android.app.ProgressDialog
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class OrderListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)
        Objects.requireNonNull(getSupportActionBar())?.setDisplayHomeAsUpEnabled(true)
        var nDialog: ProgressDialog
        nDialog = ProgressDialog(this)
        nDialog.setMessage("Loading..")
        nDialog.setTitle("数据加载中...")
        nDialog.isIndeterminate = false
        nDialog.setCancelable(true)
        nDialog.show()

        setTitle("任务清单")

        window.setFlags(
            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === android.R.id.home) {
            finish() // finish your activity
        }
        return super.onOptionsItemSelected(item)
    }
}