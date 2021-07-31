package com.tapan.twaktotest.data.user.remote

import com.tapan.twaktotest.data.apimodels.DetailsRS
import com.tapan.twaktotest.data.apimodels.UserListRS
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApiService {
    @GET("/users")
    suspend fun getUsers(@Query("since") since: Int): Response<UserListRS>

    @GET("/users/{id}")
    suspend fun getUserDetails(@Path("id")login: String): Response<DetailsRS>
}