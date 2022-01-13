package com.task.openlibrarydocs.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.task.openlibrarydocs.R
import com.task.openlibrarydocs.ui.adapter.base.MyBaseAdapter
import com.task.openlibrarydocs.ui.adapter.viewholders.ISBNViewHolder

/**
 * class to represent the isbn of the selected document in {DocumentDetailsFragment}
 */
class ISBNsAdapter (isbns: ArrayList<String>)
    : MyBaseAdapter<String>() {

    init {
        arrayList = isbns
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.cell_isbn_item, parent, false)
        return ISBNViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = arrayList[position]
        (holder as ISBNViewHolder).bind(item)
    }

}