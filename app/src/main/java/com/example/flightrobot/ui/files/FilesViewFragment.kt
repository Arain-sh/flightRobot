package com.example.flightrobot.ui.files

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.flightrobot.FileRecyclerAdapter
import com.example.flightrobot.R
import com.example.flightrobot.VideoRecyclerAdapter
import com.example.flightrobot.models.fileResponse
import com.example.flightrobot.models.orderResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_video.*
import kotlinx.android.synthetic.main.fragment_files.*
import rxhttp.RxHttp


class FilesFragment : Fragment() {

    private lateinit var filesViewModel: FilesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        filesViewModel =
                ViewModelProviders.of(this).get(FilesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_files, container, false)
        //val textView: TextView = root.findViewById(R.id.text_slideshow)
        filesViewModel.text.observe(viewLifecycleOwner, Observer {
            //textView.text = it
        })
        val file_search = root.findViewById<SearchView>(R.id.file_search)
        var filterList : MutableList<fileResponse.Data> = mutableListOf()

        // kotlin
        RxHttp.get(this.getString(R.string.default_url) + "/api/v1/files")
            .asString()
            .subscribe({ s ->
                try {
                    var s: fileResponse = Gson().fromJson(s, fileResponse::class.java)
                    var files = s.data
                    files?.let {
                        this.activity?.runOnUiThread() {
                            //这里面进行UI的更新操作
                            //使用Recycler
                            val layoutManager = GridLayoutManager(this.context, 3)
                            fileRecycler.layoutManager = layoutManager
                            val adapter = FileRecyclerAdapter(files)
                            fileRecycler.adapter = adapter
                        }
                    }

                    file_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            return false
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            filterList = fil(newText!!)
                            fileRecycler.adapter = FileRecyclerAdapter(filterList.reversed())
                            return false
                        }
                        fun fil(constraint: String) : MutableList<fileResponse.Data> {
                            val charSearch = constraint
                            if (charSearch.isEmpty()) {
                                filterList = files
                            } else {
                                val resultList : MutableList<fileResponse.Data> = mutableListOf()
                                for (row in files) {
                                    if (row.title.toLowerCase().contains(charSearch.toLowerCase())) {
                                        resultList.add(row)
                                    }
                                }
                                filterList = resultList
                            }
                            return filterList
                        }
                    })
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