package com.tapan.twaktotest.di

import android.app.Application
import androidx.room.Room
import com.tapan.twaktotest.data.db.GitHubDatabase
import com.tapan.twaktotest.data.user.local.NoteDao
import com.tapan.twaktotest.data.user.local.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DB_NAME = "github-db"

    @Provides
    @Singleton
    fun provideDatabase(application: Application): GitHubDatabase {
        return Room.databaseBuilder(
            application, GitHubDatabase::class.java, DB_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideUserDao(database: GitHubDatabase): UserDao = database.userDao()

    @Provides
    fun provideNoteDao(database: GitHubDatabase): NoteDao = database.noteDao()

}