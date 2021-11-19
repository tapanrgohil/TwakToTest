package com.tapan.twaktotest.util

import android.content.Context
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.widget.ImageView
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tapan.twaktotest.data.core.Resource
import com.tapan.twaktotest.data.core.Status
import com.tapan.twaktotest.data.exception.ErrorResolver
import com.tapan.twaktotest.ui.custom.LoadingView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlin.contracts.ExperimentalContracts

fun Any?.isListAndEmpty(): Boolean {
//    contract {
//        returns(true) implies (this@isListAndEmpty is List<*>)
//    }
    return this is List<*> && this.isEmpty()
}

fun <T> Resource<T>.doIfSuccess(data: (T) -> Unit): Resource<T> {
    if (status == Status.SUCCESS) {
        this.data?.let { data.invoke(it) } ?: kotlin.run {
            return Resource.error("Null data")
        }
    }
    return this
}

fun <T, R> Flow<Resource<T>>.mapResource(
    data: (T?) -> R
): Flow<Resource<R>> {
    return map {
        when (it.status) {
            Status.SUCCESS -> {
                Resource.success<R>(data.invoke(it.data))
            }
            Status.ERROR -> {
                Resource.error<R>(
                    it.message.orEmpty(),
                    null,
                    it.throwable,
                    it.retrofitResponse
                )
            }
            else -> {
                Resource.loading<R>()
            }
        }
    }
}

fun <T> Flow<T>.launchInBackGround(
    viewModel: ViewModel,
    mutableLiveData: MutableLiveData<T>? = null
): Job {
    return if (mutableLiveData != null) {
        flowOn(Dispatchers.IO)
            .onEach {
                mutableLiveData.postValue(it)
            }
            .launchIn(viewModel.viewModelScope)
    } else
        flowOn(Dispatchers.IO)
            .launchIn(viewModel.viewModelScope)
}

fun <T> Flow<T>.launchInBackGround(
    viewModel: ViewModel,
    mutableLiveData: MutableStateFlow<T>? = null
): Job {
    return if (mutableLiveData != null) {
        flowOn(Dispatchers.IO)
            .onEach {
                mutableLiveData.value = it
            }
            .launchIn(viewModel.viewModelScope)
    } else
        flowOn(Dispatchers.IO)
            .launchIn(viewModel.viewModelScope)
}


fun <R> LifecycleOwner.handleLoadingResponse(
    liveData: LiveData<Resource<R>>,
    loadingView: LoadingView? = null,
    @UiThread
    error: (() -> Unit)? = null,
    @UiThread
    process: ((R) -> Unit)? = null,

) {
    observe(liveData) {
        it?.apply {
            when (this.status) {
                Status.SUCCESS -> {
                    data?.let { it1 -> process?.invoke(it1) }
                    loadingView?.onStopLoading(true)
                }
                Status.ERROR -> {
                    it.throwable?.printStackTrace()
                    loadingView?.onStopLoading(
                        false,
                        it.throwable?.message ?: it.message.orEmpty(),
                        it
                    )
                error?.invoke()
                }
                Status.LOADING -> {
                    loadingView?.onInit()
                    loadingView?.onStartLoading()
                }
            }
        }
    }
}

fun <R> LifecycleOwner.handleLoadingResponse(
    liveData: StateFlow<Resource<R>>,
    loadingView: LoadingView? = null,
    @UiThread
    error: (() -> Unit)? = null,
    @UiThread
    process: ((R) -> Unit)? = null,

) {
    observe(liveData) {
        it?.apply {
            when (this.status) {
                Status.SUCCESS -> {
                    data?.let { it1 -> process?.invoke(it1) }
                    loadingView?.onStopLoading(true)
                }
                Status.ERROR -> {
                    it.throwable?.printStackTrace()
                    loadingView?.onStopLoading(
                        false,
                        it.throwable?.message ?: it.message.orEmpty(),
                        it
                    )
                error?.invoke()
                }
                Status.LOADING -> {
                    loadingView?.onInit()
                    loadingView?.onStartLoading()
                }
            }
        }
    }
}

fun Context.dp2Pixel(dp: Int): Float {
    return dp * (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)

}


fun ImageView.toInvertColor() {
    val negative = floatArrayOf(
        -1.0f, .0f, .0f, .0f, 255.0f,
        .0f, -1.0f, .0f, .0f, 255.0f,
        .0f, .0f, -1.0f, .0f, 255.0f,
        .0f, .0f, .0f, 1.0f, .0f
    )
    this.colorFilter = ColorMatrixColorFilter(negative)
}


inline fun <reified T> Gson.fromJson(json: String) =
    fromJson<T>(json, object : TypeToken<T>() {}.type)



