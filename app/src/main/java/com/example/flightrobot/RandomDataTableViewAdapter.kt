package com.example.flightrobot

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ph.ingenuity.tableview.adapter.AbstractTableAdapter
import ph.ingenuity.tableview.adapter.recyclerview.holder.AbstractViewHolder
import ph.ingenuity.tableviewdemo.models.RandomDataCell

class RandomDataTableViewAdapter(
    private val context: Context
) : AbstractTableAdapter(context) {

    override fun getColumnHeaderItemViewType(column: Int): Int = 0

    override fun getRowHeaderItemViewType(row: Int): Int = 0

    override fun getCellItemViewType(column: Int): Int = 0

    override fun onCreateCellViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val cellView = LayoutInflater.from(context).inflate(
            // Replace this with your cell view layout
            R.layout.table_cell_text_data,
            parent,
            false
        )

        return RandomDataCellViewHolder(cellView)
    }

    override fun onBindCellViewHolder(
        holder: AbstractViewHolder,
        cellItem: Any,
        column: Int,
        row: Int
    ) {
        val cell = cellItem as RandomDataCell
        val cellViewHolder = holder as RandomDataCellViewHolder
        cellViewHolder.cellTextView.text = cell.content.toString()
    }

    override fun onCreateColumnHeaderViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val columnHeaderView = LayoutInflater.from(context).inflate(
            // Replace this with your column header view layout
            R.layout.table_column_header_text_data,
            parent,
            false
        )

        return RandomDataColumnHeaderViewHolder(columnHeaderView)
    }

    override fun onBindColumnHeaderViewHolder(
        holder: AbstractViewHolder,
        columnHeaderItem: Any,
        column: Int
    ) {
        val columnHeaderCell = columnHeaderItem as RandomDataCell
        val columnHeaderViewHolder = holder as RandomDataColumnHeaderViewHolder
        columnHeaderViewHolder.cellTextView.text = columnHeaderCell.content.toString()
    }

    override fun onCreateRowHeaderViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val rowHeaderView = LayoutInflater.from(context).inflate(
            // Replace this with your row header view layout
            R.layout.table_row_header_text_data,
            parent,
            false
        )

        return RandomDataRowHeaderViewHolder(rowHeaderView)
    }

    override fun onBindRowHeaderViewHolder(
        holder: AbstractViewHolder,
        rowHeaderItem: Any,
        row: Int
    ) {
        val rowHeaderCell = rowHeaderItem as RandomDataCell
        val rowHeaderViewHolder = holder as RandomDataRowHeaderViewHolder
        rowHeaderViewHolder.cellTextView.text = rowHeaderCell.content.toString()
    }

    override fun onCreateCornerView(): View? {
        // Replace this with your corner view layout
        val cornerView = LayoutInflater.from(context).inflate(R.layout.table_corner_view, null)
        cornerView.setOnClickListener {
            Toast.makeText(context, "CornerView has been clicked.", Toast.LENGTH_SHORT).show()
        }

        return cornerView
    }

    class RandomDataCellViewHolder(itemView: View) : AbstractViewHolder(itemView) {
        val cellTextView: TextView
            get() = itemView.findViewById(R.id.random_data_cell_data)
    }

    class RandomDataColumnHeaderViewHolder(itemView: View) : AbstractViewHolder(itemView) {
        val cellTextView: TextView
            get() = itemView.findViewById(R.id.column_header_text)
    }

    class RandomDataRowHeaderViewHolder(itemView: View) : AbstractViewHolder(itemView) {
        val cellTextView: TextView
            get() = itemView.findViewById(R.id.row_header_text)
    }
}