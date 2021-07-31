package com.tapan.twaktotest.data.user.di

import android.app.Application
import coil.ImageLoader
import com.tapan.twaktotest.data.user.remote.UserApiService
import com.tapan.twaktotest.data.user.remote.UserRemoteSource
import com.tapan.twaktotest.data.user.remote.UserRemoteSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [UserRepoModule::class])
@InstallIn(SingletonComponent::class)
class UserModule {
    @Singleton
    @Provides
    fun provideUserApiService(retrofit: Retrofit): UserApiService {
        return retrofit.create(UserApiService::class.java)
    }

    @Singleton
    @Provides
    fun userRemoteSource(
        userApiService: UserApiService,
        context: Application,
        imageLoader: ImageLoader
    ): UserRemoteSource {
        return UserRemoteSourceImpl(userApiService, context,imageLoader)
    }

    @Singleton
    @Provides
    fun provideImageLoader(context: Application): ImageLoader {
        return ImageLoader.invoke(context)
    }

}