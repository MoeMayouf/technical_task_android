package com.example.data.api

import com.example.data.entities.response.ResponseUsers
import retrofit2.http.GET
import retrofit2.http.Query

interface GoRestApiService {

    @GET("users")
    suspend fun getUsers(@Query("page") page: String): ResponseUsers
}