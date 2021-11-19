package com.tapan.twaktotest.util

import android.view.View
import android.widget.ImageView
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tapan.twaktotest.data.core.Resource
import com.tapan.twaktotest.data.core.Status
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.visible(boolean: Boolean) {
    if (boolean) {
        visible()
    } else {
        gone()
    }
}

fun View.visibleInVisible(boolean: Boolean) {
    if (boolean) {
        visible()
    } else {
        invisible()
    }
}

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit) =
    liveData.observe(this, androidx.lifecycle.Observer(body))

fun <T : Any, S : StateFlow<T>> LifecycleOwner.observe(state: S, body: (T?) -> Unit) =
    lifecycleScope.launch {
        whenStarted {
            state.collectLatest {
                whenResumed {
                    body.invoke(it)

                }
            }
        }
    }


