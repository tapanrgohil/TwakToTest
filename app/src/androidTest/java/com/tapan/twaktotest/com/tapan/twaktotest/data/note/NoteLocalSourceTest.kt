package com.tapan.twaktotest.com.tapan.twaktotest.data.note

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tapan.twaktotest.com.tapan.twaktotest.data.user.local.getEntities
import com.tapan.twaktotest.data.db.GitHubDatabase
import com.tapan.twaktotest.data.note.local.NoteLocalSource
import com.tapan.twaktotest.data.note.local.NoteLocalSourceImpl
import com.tapan.twaktotest.data.user.local.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class NoteLocalSourceTest {

    lateinit var noteDao: NoteDao
    lateinit var userDao: UserDao

    lateinit var db: GitHubDatabase
    lateinit var noteLocalSource: NoteLocalSource

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, GitHubDatabase::class.java
        ).build()
        noteDao = db.noteDao()
        userDao = db.userDao()

        noteLocalSource = NoteLocalSourceImpl(noteDao)
    }

    @Test
    @Throws(IOException::class)
    fun testInsert() {
        val userEntity = getEntities().first()
        userDao.insert(userEntity)
        noteLocalSource.insertNote(NoteEntity(userEntity.id, "test"))
        userDao.getUserByLoginSingle(userEntity.login).let {
            assert(it.note?.note == "test")
        }
    }

    @Test
    @Throws(IOException::class)
    fun testReInsert() {
        val userEntity = getEntities().first()
        userDao.insert(userEntity)
        noteLocalSource.insertNote(NoteEntity(userEntity.id, "test"))
        userDao.getUserByLoginSingle(userEntity.login).let {
            assert(it.note?.note == "test")
        }
        noteLocalSource.insertNote(NoteEntity(userEntity.id, "test2"))

        userDao.getUserByLoginSingle(userEntity.login).let {
            assert(it.note?.note == "test2")
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

}