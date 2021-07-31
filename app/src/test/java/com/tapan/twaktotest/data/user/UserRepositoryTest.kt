package com.tapan.twaktotest.data.user

import android.content.Context
import androidx.lifecycle.asLiveData
import com.tapan.twaktotest.data.apimodels.UserListRS
import com.tapan.twaktotest.data.core.Resource
import com.tapan.twaktotest.data.core.Status
import com.tapan.twaktotest.data.exception.AppException
import com.tapan.twaktotest.data.exception.StringProvider
import com.tapan.twaktotest.data.note.local.NoteLocalSource
import com.tapan.twaktotest.data.user.local.UserEntity
import com.tapan.twaktotest.data.user.local.UserLocalSource
import com.tapan.twaktotest.data.user.local.UserNoteJoined
import com.tapan.twaktotest.data.user.remote.UserRemoteSource
import com.tapan.twaktotest.domain.model.User
import dagger.hilt.android.EntryPointAccessors
import io.mockk.*
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response


@ExperimentalCoroutinesApi
class UserRepositoryTest : TestCase() {


    @MockK
    lateinit var stringProvider: StringProvider

    @MockK
    lateinit var userRemoteSource: UserRemoteSource

    @MockK
    lateinit var userLocalSource: UserLocalSource

    @MockK
    lateinit var noteLocalSource: NoteLocalSource

    lateinit var userRepository: UserRepository

    val coroutineDispatcher = TestCoroutineDispatcher()


    @Before
    public override fun setUp() {
        MockKAnnotations.init(this)
        super.setUp()
        userRepository = UserRepositoryImpl(userLocalSource, userRemoteSource, noteLocalSource,stringProvider)


    }

    @ExperimentalCoroutinesApi
    @Test
    fun testGetUsers() = runBlocking {
        mockkConstructor(AppException::class)

        mockkStatic(EntryPointAccessors::class)

        every { stringProvider.getErrorCodeMessageRes(any()) }.returns(1)
        every { stringProvider.messageForStringResource(any()) }.returns("Someting went worng")

        every { anyConstructed<AppException>().message }.returns("")
        every { anyConstructed<AppException>().errorCode }.returns(400)
        every { userLocalSource.insertUser(any()) }.returns(1)
        every { userLocalSource.upsertPartialUsers(any()) }.returns(Unit)
        coEvery { userRemoteSource.getUsers(any()) }.returns(Response.success(getList()))
        every { userLocalSource.getAllUsers() }.returns(flowOf(getEntities().map {
            UserNoteJoined(it,null)
        }))

        userRepository.getUsers(0)
            .collectIndexed { index, value ->
                if (index == 0) {
                    assert(value.status == Status.LOADING)
                } else {
                    assert(value.status == Status.SUCCESS)
                }
            }


    }

    @ExperimentalCoroutinesApi
    @Test
    fun testError() = runBlocking {
        mockkConstructor(AppException::class)

        mockkStatic(EntryPointAccessors::class)

        every { stringProvider.getErrorCodeMessageRes(any()) }.returns(1)
        every { stringProvider.messageForStringResource(any()) }.returns("Someting went worng")

        every { anyConstructed<AppException>().message }.returns("")
        every { anyConstructed<AppException>().errorCode }.returns(400)
        every { userLocalSource.insertUser(any()) }.returns(1)
        coEvery { userRemoteSource.getUsers(any()) }.throws(AppException(404))
        every { userLocalSource.getAllUsers() }.returns(flowOf(emptyList()))

        userRepository.getUsers(0)
            .collectIndexed { index, value ->
                if (index == 0) {
                    assert(value.status == Status.LOADING)
                }else if(index==1){
                    assert(value.status == Status.ERROR)
                }
            }


    }


}


fun testSearchUserLocal() {

}



fun testUpdateNote() {
    TODO()
}

fun testGetUserDetails() {
    TODO()
}
