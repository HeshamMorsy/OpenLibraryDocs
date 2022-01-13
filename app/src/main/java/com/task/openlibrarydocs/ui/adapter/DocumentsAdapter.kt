package com.task.openlibrarydocs.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.task.openlibrarydocs.R
import com.task.openlibrarydocs.data.model.domain.Document
import com.task.openlibrarydocs.ui.adapter.base.MyBaseAdapter
import com.task.openlibrarydocs.ui.adapter.base.TYPE_FOOTER
import com.task.openlibrarydocs.ui.adapter.base.TYPE_ITEM
import com.task.openlibrarydocs.ui.adapter.viewholders.DocumentViewHolder
import com.task.openlibrarydocs.ui.adapter.viewholders.LoaderViewHolder
import kotlinx.coroutines.channels.Channel

class DocumentsAdapter(
    private val documents: ArrayList<Document>,
    val actionChannel: Channel<Document>,
) : MyBaseAdapter<Document>() {

    init {
        arrayList = documents
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_FOOTER){
            val view = layoutInflater.inflate(R.layout.cell_load_item, parent, false)
            LoaderViewHolder(view)
        }else{
            val view = layoutInflater.inflate(R.layout.cell_document, parent, false)
            DocumentViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) != TYPE_FOOTER)
            (holder as DocumentViewHolder).bind(documents[position], actionChannel)
        else
            (holder as LoaderViewHolder).bind(hideFooter)
    }

    /**
     * Function to get the cell type using position
     * @return it returns @TYPE_ITEM or @TYPE_FOOTER if it's loading
     */
    override fun getItemViewType(position: Int): Int {
        return if (arrayList.size - 1 >= position) TYPE_ITEM else TYPE_FOOTER
    }

}