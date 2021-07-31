package com.tapan.twaktotest.data

import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
        newRequest.addHeader(
            "username",
            "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"
        )
        return chain.proceed(newRequest.build())

    }

}