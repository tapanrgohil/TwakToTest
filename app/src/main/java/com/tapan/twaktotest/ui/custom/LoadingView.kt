package com.tapan.twaktotest.ui.custom

import com.tapan.twaktotest.data.core.Resource

interface LoadingView {
    fun onStartLoading()
    fun onStopLoading(success: Boolean, message: String = "", resource: Resource<*>? = null)
    fun onInit()
}