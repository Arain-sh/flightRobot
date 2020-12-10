package com.example.flightrobot

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import ph.ingenuity.tableview.TableView
import ph.ingenuity.tableview.feature.filter.Filter
import ph.ingenuity.tableview.feature.pagination.Pagination
import ph.ingenuity.tableviewdemo.data.RandomDataFactory
import ph.ingenuity.tableviewdemo.listeners.TableViewListener
import java.util.*


class TaskActivity : AppCompatActivity() {
    companion object {
        fun startForResult(activity: Activity, requestCode: Int = MainActivity.REQUEST_LOGIN) {
            val intent = Intent(activity, TaskActivity::class.java)
            activity.startActivityForResult(intent, requestCode)
        }
    }

    private lateinit var tableView: TableView
    private lateinit var pageNumberField: EditText
    private lateinit var searchField: EditText
    private lateinit var itemsPerPage: Spinner
    private lateinit var previousButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var pagination: Pagination
    private lateinit var filter: Filter
    private lateinit var tablePaginationDetails: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_taskinfo)
        Objects.requireNonNull(getSupportActionBar())?.setDisplayHomeAsUpEnabled(true)
        //var info: String = intent.getStringExtra("info")!!

        //init
        initializeViews()
        // Retrieve your data from local storage or API
        initializeData()
        //init listener
        initializeListeners()

        window.setFlags(
            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    private fun initializeViews() {
        tableView = findViewById(R.id.task_table_view)
        itemsPerPage = findViewById(R.id.items_per_page_spinner)
        searchField = findViewById(R.id.query_string)
        previousButton = findViewById(R.id.previous_button)
        nextButton = findViewById(R.id.next_button)
        pageNumberField = findViewById(R.id.page_number_text)
        tablePaginationDetails = findViewById(R.id.table_details)
    }

    private fun initializeData() {
        val randomDataFactory = RandomDataFactory(12, 12)
        val tableAdapter = RandomDataTableViewAdapter(this)
        val cellsList = randomDataFactory.randomCellsList as List<List<Any>>
        val rowHeadersList = randomDataFactory.randomRowHeadersList as List<Any>
        val columnHeadersList = randomDataFactory.randomColumnHeadersList as List<Any>
        tableView.adapter = tableAdapter
        tableView.tableViewListener = TableViewListener(tableView)
        tableAdapter.setAllItems(cellsList, columnHeadersList, rowHeadersList)
        pagination = Pagination(tableView)
        filter = Filter(tableView)
    }

    private fun initializeListeners() {
        itemsPerPage.onItemSelectedListener = onItemsPerPageSelectedListener
        //searchField.addTextChangedListener(onSearchTextChange)
        previousButton.setOnClickListener(onPreviousPageButtonClicked)
        nextButton.setOnClickListener(onNextPageButtonClicked)
        pageNumberField.addTextChangedListener(onPageTextChanged)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === android.R.id.home) {
            finish() // finish your activity
        }
        return super.onOptionsItemSelected(item)
    }

    private val onPageTextChanged = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val page: Int = if (TextUtils.isEmpty(s)) 1 else Integer.valueOf(s.toString())
            pagination.loadPage(page)
        }
    }

    private val onItemsPerPageSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val numItems = when (parent!!.getItemAtPosition(position) as String) {
                "All" -> 0
                else -> Integer.valueOf(parent.getItemAtPosition(position) as String)
            }

            pagination.itemsPerPage = numItems
        }
    }

    private val onPreviousPageButtonClicked = View.OnClickListener {
        pagination.loadPreviousPage()
    }

    private val onNextPageButtonClicked = View.OnClickListener {
        pagination.loadNextPage()
    }

    private val onTableViewPageTurnedListener =
        object : Pagination.OnTableViewPageTurnedListener {
            override fun onPageTurned(numItems: Int, itemsStart: Int, itemsEnd: Int) {
                val currentPage = pagination.currentPage
                val pageCount = pagination.pageCount
                previousButton.visibility = View.VISIBLE
                nextButton.visibility = View.VISIBLE

                if (currentPage == 1 && pageCount == 1) {
                    previousButton.visibility = View.INVISIBLE
                    nextButton.visibility = View.INVISIBLE
                }

                if (currentPage == 1) {
                    previousButton.visibility = View.INVISIBLE
                }

                if (currentPage == pageCount) {
                    nextButton.visibility = View.INVISIBLE
                }

                tablePaginationDetails.text = getString(
                    R.string.table_pagination_details,
                    currentPage,
                    itemsStart,
                    itemsEnd
                )
            }
        }


}