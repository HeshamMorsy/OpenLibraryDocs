package com.task.openlibrarydocs.ui.adapter.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.task.openlibrarydocs.data.model.domain.Document
import kotlinx.android.synthetic.main.cell_document.view.*
import kotlinx.coroutines.channels.Channel


class DocumentViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(
        item: Document,
        actionChannel: Channel<Document>,
    ) {
        // fill the cell with data
        itemView.documentTitleText.text = item.title
        if (item.authorName.isNotEmpty())
            itemView.authorText.text = item.authorName[0]

        // handle on click on the document cell
        itemView.setOnClickListener {
            actionChannel.trySend(item)
        }
    }

}