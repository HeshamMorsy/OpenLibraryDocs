package com.task.openlibrarydocs.ui.adapter.viewholders

import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.cell_isbn_item.view.*


class ISBNViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(item: String) {
        // fill isbn text
        itemView.isbnText.text = item

        // load the image from the url using Glide
        Glide.with(itemView.context).load("http://covers.openlibrary.org/b/isbn/$item.jpg")
            .addListener(object : RequestListener<Drawable>{
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    // hide the isbn image if there is no image to preview
                    itemView.isbnImage.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    // show the isbn image when image loaded successfully
                    itemView.isbnImage.visibility = View.VISIBLE
                    return false
                }

            })
            .into(itemView.isbnImage)
    }

}