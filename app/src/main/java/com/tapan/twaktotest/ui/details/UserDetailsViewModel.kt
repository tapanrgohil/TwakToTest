package com.tapan.twaktotest.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tapan.twaktotest.data.core.Resource
import com.tapan.twaktotest.data.user.UserRepository
import com.tapan.twaktotest.domain.model.User
import com.tapan.twaktotest.ui.model.UserUiModel
import com.tapan.twaktotest.ui.userslist.UserUiModelMapper.mapUserToUserAdapterModel
import com.tapan.twaktotest.ui.userslist.UserUiModelMapper.mapUserToUserUi
import com.tapan.twaktotest.util.launchInBackGround
import com.tapan.twaktotest.util.mapResource

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel
@Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userDetailsLiveData = MutableStateFlow<Resource<UserUiModel>>(Resource.idle())
    val userDetailsLiveData = _userDetailsLiveData as StateFlow<Resource<UserUiModel>>

    fun getUserDetails(login: String) {
        userRepository.getUserDetails(login)
            .mapResource {
                it?.mapUserToUserUi()!!
            }
            .launchInBackGround(this, _userDetailsLiveData)
    }

    fun saveNote(note: String, id: Int) {
        userRepository.updateNote(note, id)
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

}