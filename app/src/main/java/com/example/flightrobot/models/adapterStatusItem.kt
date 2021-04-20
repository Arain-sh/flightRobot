package com.example.flightrobot.models

import android.widget.TextView
import com.angcyo.dsladapter.DslAdapterStatusItem
import com.angcyo.dsladapter.DslViewHolder
import com.angcyo.dsladapter.R

class adapterStatusItem : DslAdapterStatusItem() {
    override fun _onBindStateLayout(itemHolder: DslViewHolder, state: Int) {
        super._onBindStateLayout(itemHolder, state)

        if (state == ADAPTER_STATUS_LOADING) {
            itemHolder.v<TextView>(R.id.text_view)?.text = "正在加载中..."
        }
    }
}