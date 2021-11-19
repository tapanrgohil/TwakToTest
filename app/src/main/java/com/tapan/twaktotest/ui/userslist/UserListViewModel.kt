package com.tapan.twaktotest.ui.userslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tapan.twaktotest.data.core.Resource
import com.tapan.twaktotest.data.user.UserRepository
import com.tapan.twaktotest.ui.model.UserAdapterModel
import com.tapan.twaktotest.ui.userslist.UserUiModelMapper.toUserAdapterModelList
import com.tapan.twaktotest.util.isListAndEmpty
import com.tapan.twaktotest.util.launchInBackGround
import com.tapan.twaktotest.util.mapResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.CancellationException
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private var flowUsers: Job? = null
    private var flowSeach: Job? = null

    private val _userListLiveData =
        MutableStateFlow<Resource<List<UserAdapterModel>>>(Resource.idle())
    val userListLiveData = _userListLiveData as StateFlow<Resource<List<UserAdapterModel>>>

    private val _searchUserLiveData = MutableStateFlow<Resource<List<UserAdapterModel>>>(Resource.idle())
    val searchUserLiveData = _searchUserLiveData as StateFlow<Resource<List<UserAdapterModel>>>


    fun getAllUsers(since: Int) {
        flowUsers?.cancel(CancellationException("New request"))
        flowUsers = userRepository.getUsers(since)
            .mapResource {
                it.orEmpty().toUserAdapterModelList()
            }
            .launchInBackGround(this, _userListLiveData)
    }

    fun filter(keyword: String) {
        flowUsers?.cancel(CancellationException("Start Search"))
        flowSeach?.cancel(CancellationException("New request"))
        flowUsers = null
        flowSeach = userRepository.searchUserLocal(keyword)
            .mapResource {
                it.orEmpty().toUserAdapterModelList()
            }
            .launchInBackGround(this, _searchUserLiveData)
    }

    fun clearSearch() {
        flowSeach?.cancel(CancellationException("New request"))
    }


}