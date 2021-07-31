package com.tapan.twaktotest.data.user.remote

import android.content.Context
import coil.Coil
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.tapan.twaktotest.data.apimodels.DetailsRS
import com.tapan.twaktotest.data.apimodels.UserListRS
import com.tapan.twaktotest.data.user.local.UserEntity
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

interface UserRemoteSource {
    suspend fun getUsers(since: Int): Response<UserListRS>
    suspend fun startCatchingUserMedia(image: String): Boolean
    suspend fun getUserDetails(login:String):Response<DetailsRS>
}

class UserRemoteSourceImpl @Inject constructor(
    private val userApiService: UserApiService,
    private val context: Context,
    private val imageLoader: ImageLoader
) : UserRemoteSource {
    override suspend fun getUsers(since: Int): Response<UserListRS> {
        return userApiService.getUsers(since)
    }

    @ExperimentalCoilApi
    override suspend fun startCatchingUserMedia(image: String): Boolean {
        var success = false;
        val request = ImageRequest.Builder(context)
            .data(image)
            .diskCachePolicy(CachePolicy.ENABLED)
            .listener(
                onError = { _, _ ->
                  success = false
                },
                onSuccess = { _, _ ->
                    success = true
                },

            )
            .build()

        imageLoader.enqueue(request).await()
        return success
    }

    override suspend fun getUserDetails(login: String): Response<DetailsRS> {
        return userApiService.getUserDetails(login)
    }

}