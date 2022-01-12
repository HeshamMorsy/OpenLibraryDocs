package com.task.openlibrarydocs.data.model.response

import com.google.gson.annotations.SerializedName

/**
 * This class represents the main network response entity
 */
data class ApiResponse(

    @SerializedName("numFound") var numFound: Int? = null,
    @SerializedName("start") var start: Int? = null,
    @SerializedName("numFoundExact") var numFoundExact: Boolean? = null,
    @SerializedName("docs") var docs: ArrayList<NetworkDoc> = arrayListOf(),
    @SerializedName("q") var q: String? = null,
    @SerializedName("offset") var offset: String? = null

)