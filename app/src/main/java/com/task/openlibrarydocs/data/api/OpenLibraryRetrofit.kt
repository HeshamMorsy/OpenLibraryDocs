package com.task.openlibrarydocs.data.api

import com.task.openlibrarydocs.data.model.response.ApiResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * This interface represents the endpoint apis of docs search
 */
interface OpenLibraryRetrofit {

    @GET("search.json")
    suspend fun searchWithData(@QueryMap queryMap: HashMap<String,String>): ApiResponse

}