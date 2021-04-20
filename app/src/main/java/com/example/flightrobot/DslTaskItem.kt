package com.example.flightrobot

import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.angcyo.dsladapter.DslAdapterItem
import com.angcyo.dsladapter.DslViewHolder

class DslTaskItem : DslAdapterItem() {
    init {
        itemLayoutId = R.layout.actionlist //指定布局, 这是必须的.
    }

    //数据承载
    var itemText: CharSequence? = null

    //可以定义任意数据类型
    //var itemBean:HttpBean? = null //像这样, 只不过数据需要手动控制.
    //如:创建/赋值/释放. 如果数据的改变, 还需要更新界面, 请调用item.updateAdapterItem, 或者直接使用系统的adapter.notifyItemChanged, 均可以.

    //重写`onItemBind`方法, 实现界面的绑定
    override fun onItemBind(
        itemHolder: DslViewHolder,
        itemPosition: Int,
        adapterItem: DslAdapterItem,
        payloads: List<Any>
    ) {
        super.onItemBind(itemHolder, itemPosition, adapterItem, payloads)
        itemHolder.tv(R.id.text_view)!!.text = itemText //自定义的itemText数据, 绑定到界面.如果itemText改变了, 请调用notifyItemChanged方法更新界面.
    }

}