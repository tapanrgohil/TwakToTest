package com.tapan.twaktotest.data.user.di

import com.tapan.twaktotest.data.note.local.NoteLocalSource
import com.tapan.twaktotest.data.note.local.NoteLocalSourceImpl
import com.tapan.twaktotest.data.user.UserRepository
import com.tapan.twaktotest.data.user.UserRepositoryImpl
import com.tapan.twaktotest.data.user.local.UserLocalSource
import com.tapan.twaktotest.data.user.local.UserLocalSourceImpl
import com.tapan.twaktotest.data.user.remote.UserApiService
import com.tapan.twaktotest.data.user.remote.UserRemoteSource
import com.tapan.twaktotest.data.user.remote.UserRemoteSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton
import kotlin.contracts.ExperimentalContracts

@Module
@InstallIn(SingletonComponent::class)
abstract class UserRepoModule {
    @Binds
    abstract fun userLocalSource(userLocalSource: UserLocalSourceImpl): UserLocalSource


    @Binds
    abstract fun userRepo(userRepositoryImpl: UserRepositoryImpl): UserRepository


    @Binds
    abstract fun noteLocalSource(noteLocalSourceImpl: NoteLocalSourceImpl):NoteLocalSource
}