package com.tapan.twaktotest.com.tapan.twaktotest.data.user.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tapan.twaktotest.data.db.GitHubDatabase
import com.tapan.twaktotest.data.user.local.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class UserLocalSourceTest {

    lateinit var userDao: UserDao
    lateinit var db: GitHubDatabase
    lateinit var userLocalSource: UserLocalSource

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, GitHubDatabase::class.java
        ).build()
        userDao = db.userDao()

        userLocalSource = UserLocalSourceImpl(userDao)
    }

    @Test
    @Throws(Exception::class)
    fun testUserInsert() {
        val userEntity = getEntities().first()
        userLocalSource.insertUser(userEntity)
        assert(userDao.getUserByLoginSingle(userEntity.login).userEntity.id == userEntity.id)

    }

    @Test
    @Throws(Exception::class)
    fun getAllUserTest() {
        db.clearAllTables()
        val users = getEntities()
        runBlocking {
            userLocalSource.upsertUsers(users)
            userLocalSource.getAllUsers()
                .take(1)
                .collectLatest {
                    assert(it.size == users.size)
                }

        }
    }

    @Test
    @Throws(Exception::class)
    fun getAllUsersSingleTime() {
        db.clearAllTables()
        val users = getEntities()
        runBlocking {
            userLocalSource.upsertUsers(users)
            assert(userLocalSource.getAllUsersSingleTime().size == users.size)


        }
    }

    @Test
    @Throws(Exception::class)
    fun testUpsertUser() {
        db.clearAllTables()
        val users = getEntities()
        runBlocking {
            userLocalSource.upsertUsers(users)
            val entity = users.first()
            assert(userLocalSource.getUserByLoginSingle(entity.login).userEntity.bio == entity.bio)
            entity.bio = "mockBio"
            userLocalSource.updateUser(entity)
            assert(userLocalSource.getUserByLoginSingle(entity.login).userEntity.bio == "mockBio")
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

}