package com.tapan.twaktotest.ui.custom

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.tapan.twaktotest.R
import com.tapan.twaktotest.data.core.Resource
import com.tapan.twaktotest.data.exception.ErrorCallback
import com.tapan.twaktotest.data.exception.ErrorResolver
import com.tapan.twaktotest.databinding.LoadingViewBinding
import com.tapan.twaktotest.util.gone
import com.tapan.twaktotest.util.visible

class CustomLoader : ConstraintLayout, LoadingView, ErrorCallback {

    private lateinit var binds: LoadingViewBinding
    private var errorResolver: ErrorResolver? = null

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        inflate(context, R.layout.loading_view, this)
        binds = LoadingViewBinding.bind(this)
    }

    override fun onStartLoading() {
        binds.progressBar.visible()
    }

    override fun onStopLoading(success: Boolean, message: String, resource: Resource<*>?) {
        if (errorResolver == null || resource == null) {
            if (message.isNotEmpty()) {
                binds.tvError.setText(message)
                binds.tvError.visible()
            } else {
                binds.tvError.gone()
            }
        } else {
            errorResolver?.handleErrorResponse(
                resource.retrofitResponse,
                resource.throwable,
                resource.message,
                errorCallback = this@CustomLoader
            )
        }
        binds.progressBar.gone()
    }

    override fun onInit() {
        binds.tvError.text = ""
        binds.tvError.gone()
    }

    fun setErrorResolver(errorResolver: ErrorResolver) {
        this.errorResolver = errorResolver
    }

    override fun onNoInternet(retryAction: () -> Unit) {
        binds.tvError.setText(R.string.no_internet_connectivity)
    }

    override fun onUnknownError() {
        binds.tvError.setText(R.string.unknown_error)
    }

    override fun showError(errorMessage: String) {
        binds.tvError.setText(errorMessage)

    }


}