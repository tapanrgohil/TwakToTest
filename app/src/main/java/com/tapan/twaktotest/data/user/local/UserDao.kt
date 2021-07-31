package com.tapan.twaktotest.data.user.local

import androidx.room.*
import com.tapan.twaktotest.data.core.Resource
import com.tapan.twaktotest.data.db.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class UserDao : BaseDao<UserEntity>() {

    @Transaction
    @Query("SELECT * FROM USERS ORDER BY ID")
    abstract fun getAllUsers(): Flow<List<UserNoteJoined>>

    @Transaction
    @Query("SELECT * FROM USERS ORDER BY ID")
    abstract fun getAllUsersSingleTime(): List<UserNoteJoined>

    @Transaction
    @Query("SELECT * FROM USERS WHERE LOGIN =:id ")
    abstract fun getUserByLogin(id: String): Flow<UserNoteJoined>

    @Transaction
    @Query("SELECT * FROM USERS WHERE LOGIN =:login ")
    abstract fun getUserByLoginSingle(login: String): UserNoteJoined

    @Transaction
    @Query("SELECT * FROM USERS LEFT OUTER JOIN NOTE ON USERS.ID = NOTE.USER_ID WHERE USERS.LOGIN LIKE :keyword  OR NOTE.NOTE LIKE :keyword --case-insensitive")
    abstract fun searchUser(keyword: String): Flow<List<UserNoteJoined>>

    @Update(entity = UserEntity::class)
    abstract fun updatePartialUser(user: UserPartialDataEntity)

    @Insert(entity = UserEntity::class, onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertPartialUser(user: UserPartialDataEntity): Long

    @Query("UPDATE USERS SET IS_IMAGE_CACHED=:cached WHERE ID =:id")
    abstract fun updateUserImageCache(id: Int, cached: Boolean): Int

}