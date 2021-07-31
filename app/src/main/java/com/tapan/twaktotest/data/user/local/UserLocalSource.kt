package com.tapan.twaktotest.data.user.local

import com.tapan.twaktotest.data.core.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface UserLocalSource {

    fun getAllUsers(): Flow<List<UserNoteJoined>>
    fun getAllUsersSingleTime(): List<UserNoteJoined>
    fun insertUser(userEntity: UserEntity): Long
    fun upsertUsers(users: List<UserEntity>)
    fun upsertPartialUsers(users: List<UserPartialDataEntity>)
    fun updateUser(user: UserEntity): Int
    fun updateUserImageCache(id:Int,isCached:Boolean): Int
    fun searchUser(keyword: String): Flow<List<UserNoteJoined>>
    fun getUserByLogin(login: String): Flow<UserNoteJoined>
    fun getUserByLoginSingle(login: String): UserNoteJoined
}

class UserLocalSourceImpl
@Inject constructor(private val userDao: UserDao) : UserLocalSource {
    override fun getAllUsers(): Flow<List<UserNoteJoined>> {
        return userDao.getAllUsers()
    }

    override fun getAllUsersSingleTime(): List<UserNoteJoined> {
        return userDao.getAllUsersSingleTime()
    }

    override fun insertUser(userEntity: UserEntity): Long {
        return userDao.insert(userEntity)
    }

    override fun upsertUsers(users: List<UserEntity>) {
        return users.forEach {
            if (userDao.insert(it) < 0) {
                userDao.update(it)
            }
        }
    }

    override fun upsertPartialUsers(users: List<UserPartialDataEntity>) {
        return users.forEach {
            if (userDao.insertPartialUser(it) < 0) {
                userDao.updatePartialUser(it)
            }
        }
    }

    override fun updateUser(user: UserEntity): Int {
        return userDao.update(user)
    }

    override fun updateUserImageCache(id: Int, isCached: Boolean): Int {
        return userDao.updateUserImageCache(id,isCached)
    }

    override fun searchUser(keyword: String): Flow<List<UserNoteJoined>> {
        return userDao.searchUser("%$keyword%")
    }

    override fun getUserByLogin(login: String): Flow<UserNoteJoined> {
        return userDao.getUserByLogin(login)
    }

    override fun getUserByLoginSingle(login: String): UserNoteJoined {
        return userDao.getUserByLoginSingle(login)
    }


}