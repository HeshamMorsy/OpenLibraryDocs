package com.task.openlibrarydocs.data.model.domain

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * This class represents the domain entity that is used within our app
 */
@Parcelize
data class Document(

    @SerializedName("title") var title: String? = null,
    @SerializedName("isbn") var isbn: ArrayList<String> = arrayListOf(),
    @SerializedName("author_name") var authorName: ArrayList<String> = arrayListOf(),

    ) : Parcelable
