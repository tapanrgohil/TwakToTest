package com.tapan.twaktotest.data.user

import com.tapan.twaktotest.data.apimodels.UserListRS
import com.tapan.twaktotest.data.core.Resource
import com.tapan.twaktotest.data.core.getFlow
import com.tapan.twaktotest.data.core.getLocalFlow
import com.tapan.twaktotest.data.exception.StringProvider
import com.tapan.twaktotest.data.note.local.NoteLocalSource
import com.tapan.twaktotest.data.user.local.NoteEntity
import com.tapan.twaktotest.data.user.local.UserLocalSource
import com.tapan.twaktotest.data.user.remote.UserRemoteSource
import com.tapan.twaktotest.domain.model.User
import com.tapan.twaktotest.util.mapResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import retrofit2.Response
import java.lang.RuntimeException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userLocalSource: UserLocalSource,
    private val userRemoteSource: UserRemoteSource,
    private val noteLocalSource: NoteLocalSource,
    private val stringProvider: StringProvider
) : UserRepository {

    override fun getUsers(since: Int): Flow<Resource<List<User>>> {
        /* return flow<Resource<List<User>>> {
             emit(Resource.loading())
         }.map {
             userLocalSource.getAllUsersSingleTime().map { userEntity ->
                 userEntity.toDomainUser()
             }
         }.flatMapConcat {
             if (it.isNullOrEmpty()) {
                 return@flatMapConcat fetchData(0)
             } else {
                 if (since > 0 && it.last().id == since) {
                     fetchData(since)
                 } else if (since == 0 && it.isNotEmpty()) {
                     return@flatMapConcat flowOf(Resource.success(it))
                 } else {
                     throw RuntimeException("")
                 }
             }
         }*/

        return fetchData(since)
    }

    override fun searchUserLocal(keyword: String): Flow<Resource<List<User>>> {
        return getLocalFlow(localFlow = {
            userLocalSource.searchUser(keyword)
        }, stringProvider = stringProvider).mapResource {
            it.orEmpty().map { it.toDomainUser() }
        }
    }

    override suspend fun cacheImages(): Boolean {
        userLocalSource.getAllUsersSingleTime().filter {
            !it.userEntity.isImageCached
        }.forEach {
            if (userRemoteSource.startCatchingUserMedia(it.userEntity.avatarUrl)) {
                userLocalSource.updateUser(it.userEntity.copy(isImageCached = true))
            }
        }
        return true
    }

    override fun updateNote(note: String, id: Int): Flow<Resource<Int>> {
        return flow {
            emit(Resource.loading())
            noteLocalSource.insertNote(NoteEntity(id, note))
            emit(Resource.success(1))

        }
    }

    private fun fetchData(since: Int): Flow<Resource<List<User>>> {
        return getFlow(
            localFlow = {
                userLocalSource.getAllUsers().map { enties ->
                    enties.map { it.toDomainUser() }
                }
            },
            remote = {
                if (it.isNullOrEmpty()) {
                    Response.success(userRemoteSource.getUsers(since).body())
                } else {
                    var newSince = since
                    val latUserId = it.lastOrNull()?.id ?: -1
                    val apiRes = UserListRS()
                    var response: Response<UserListRS>? = null
                    while (response == null && (apiRes.isEmpty() || apiRes.maxOf { it.id } < latUserId)) {
                        val data = userRemoteSource.getUsers(newSince)
                        if (data.isSuccessful) {
                            data.body()?.let { it1 -> apiRes.addAll(it1) }
                            newSince = data.body()?.maxOf { it.id } ?: -1
                        } else {
                            response = data
                        }
                    }
                    if (response == null) {
                        response = Response.success(apiRes)
                    }
                    response!!
                }
            },
            saveToDb = { response ->
                userLocalSource.upsertPartialUsers(response.map { it.toUserEntity() })
            }, forceRefresh = {
                true
            }, stringProvider = stringProvider
        )


    }


    override fun getUserDetails(login: String): Flow<Resource<User>> {
        return getFlow(
            localFlow = {
                userLocalSource.getUserByLogin(login)
                    .map { entity ->
                        entity.toDomainUser()

                    }
            },
            remote = {
                userRemoteSource.getUserDetails(login)
            },
            saveToDb = { response ->
                val entity = userLocalSource.getUserByLoginSingle(login)
                val user = UserMapper.detailsRsToEntity(entity.userEntity, response)
                userLocalSource.updateUser(user)
            }, forceRefresh = {
                true //always try to fectch lates details
            }
        )
    }
}