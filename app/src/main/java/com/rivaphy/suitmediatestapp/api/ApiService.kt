package com.rivaphy.suitmediatestapp.api

import com.rivaphy.suitmediatestapp.response.UserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("users")
    suspend fun getUsers(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): UserResponse
}