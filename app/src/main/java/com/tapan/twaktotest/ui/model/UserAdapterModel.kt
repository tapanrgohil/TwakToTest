package com.tapan.twaktotest.ui.model

data class UserAdapterModel(
    val userUiModel: UserUiModel?,
    var isLoading:Boolean? = false,
    val type: UserAdapterModelType = UserAdapterModelType.NORMAL
){
    var isFilterApplied:Boolean=false
}

sealed class UserAdapterModelType {
    object NORMAL : UserAdapterModelType()
    class PROGRESS(val message: String?) : UserAdapterModelType()
}