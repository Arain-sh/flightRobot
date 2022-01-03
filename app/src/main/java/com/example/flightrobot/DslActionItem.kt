package com.angcyo.dsladapter.dsl

import com.angcyo.dsladapter.DslAdapterItem
import com.angcyo.dsladapter.DslViewHolder
import com.angcyo.dsladapter.containsPayload
import com.example.flightrobot.MainActivity
import com.example.flightrobot.R
import com.example.flightrobot.orderTaskActivity

/**
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2019/10/16
 * Copyright (c) 2019 ShenZhen O&M Cloud Co., Ltd. All rights reserved.
 */
class DslActionItem : DslAdapterItem() {

    var itemText: CharSequence? = null
        set(value) {
            _oldText = field
            field = value
        }

    //存储旧数据
    var _oldText: CharSequence? = null

    init {
        itemLayoutId = R.layout.order_actionlist
    }

    override fun onItemBind(
        itemHolder: DslViewHolder,
        itemPosition: Int,
        adapterItem: DslAdapterItem,
        payloads: List<Any>
    ) {
        super.onItemBind(itemHolder, itemPosition, adapterItem, payloads)
        itemHolder.tv(R.id.text_view)?.text = itemText

        if (payloads.containsPayload(orderTaskActivity.TAG_UPDATE_DATA)) {
            //识别到刷新标识
            itemHolder.tv(R.id.text_view)?.apply {
                text = "from:$_oldText\nto:${itemText}"
                animate().rotationBy(360f).setDuration(1000).start()
            }
        }
    }
}