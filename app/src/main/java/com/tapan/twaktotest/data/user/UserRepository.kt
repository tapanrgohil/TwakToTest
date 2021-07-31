package com.tapan.twaktotest.data.user

import com.tapan.twaktotest.data.core.Resource
import com.tapan.twaktotest.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getUsers(since: Int): Flow<Resource<List<User>>>
    fun searchUserLocal(keyword: String): Flow<Resource<List<User>>>
    fun getUserDetails(login: String): Flow<Resource<User>>
    suspend fun cacheImages(): Boolean
    fun updateNote(note: String, id: Int): Flow<Resource<Int>>
}