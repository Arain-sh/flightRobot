package com.example.flightrobot.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.flightrobot.*
import com.example.flightrobot.models.orderResponse
import com.example.flightrobot.models.taskResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_gallery.*
import kotlinx.android.synthetic.main.fragment_taskinfo.*
import kotlinx.android.synthetic.main.fragment_tasks.*
import rxhttp.RxHttp

class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)
        galleryViewModel.text.observe(viewLifecycleOwner, Observer {
        //    textView.text = it
        })

        val orderSearch = root.findViewById<SearchView>(R.id.order_search)
        var filterList : MutableList<orderResponse.Data> = mutableListOf()

        // kotlin
        RxHttp.get(this.getString(R.string.default_url) + "/api/v1/orders")
            .asString()
            .subscribe({ s ->
                try {
                    var s: orderResponse = Gson().fromJson(s, orderResponse::class.java)
                    var orderList = s.data
                    orderList?.let{
                        this.activity?.runOnUiThread {
                            //这里面进行UI的更新操作
                            val layoutManager = GridLayoutManager(this.context, 3)
                            orderRecycler.layoutManager = layoutManager
                            val adapter = OrderRecyclerAdapter(orderList.reversed())
                            orderRecycler.adapter = adapter

                            orderSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                                override fun onQueryTextSubmit(query: String?): Boolean {
                                    return false
                                }

                                override fun onQueryTextChange(newText: String?): Boolean {
                                    filterList = fil(newText!!)
                                    orderRecycler.adapter = OrderRecyclerAdapter(filterList)
                                    return false
                                }
                                fun fil(constraint: String) : MutableList<orderResponse.Data> {
                                    val charSearch = constraint
                                    if (charSearch.isEmpty()) {
                                        filterList = orderList
                                    } else {
                                        val resultList : MutableList<orderResponse.Data> = mutableListOf()
                                        for (row in orderList) {
                                            if (row.task_id.contains(charSearch) or row.id.toString().contains(charSearch)) {
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

        return root
    }
}