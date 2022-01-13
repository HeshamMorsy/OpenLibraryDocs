package com.task.openlibrarydocs.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * This class represents the main network response entity
 */
data class ApiResponse(

    @SerializedName("numFound") @Expose var numFound: Int? = null,
    @SerializedName("start") @Expose var start: Int? = null,
    @SerializedName("numFoundExact") @Expose var numFoundExact: Boolean? = null,
    @SerializedName("docs") @Expose var docs: ArrayList<NetworkDoc> = arrayListOf(),
    @SerializedName("q") @Expose var q: String? = null,
    @SerializedName("offset") @Expose var offset: String? = null

)