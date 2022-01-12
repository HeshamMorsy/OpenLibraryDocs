package com.task.openlibrarydocs.data.model.domain

import com.google.gson.annotations.SerializedName

/**
 * This class represents the domain entity that is used within our app
 */
data class Document(

    @SerializedName("title") var title: String? = null,
    @SerializedName("isbn") var isbn: ArrayList<String> = arrayListOf(),
    @SerializedName("author_name") var authorName: ArrayList<String> = arrayListOf(),

    )
