package com.tapan.twaktotest.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tapan.twaktotest.data.user.local.NoteDao
import com.tapan.twaktotest.data.user.local.NoteEntity
import com.tapan.twaktotest.data.user.local.UserDao
import com.tapan.twaktotest.data.user.local.UserEntity

@Database(
    entities = [UserEntity::class, NoteEntity::class],
    version = 1
)
@TypeConverters(DateTimeConverter::class)
abstract class GitHubDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun noteDao(): NoteDao

}