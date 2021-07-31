package com.tapan.twaktotest.data.exception

import android.content.Context
import androidx.annotation.StringRes
import androidx.collection.ArrayMap
import com.tapan.twaktotest.R
import javax.inject.Inject

interface StringProvider {
    fun messageForStringResource(@StringRes int: Int): String
    fun getErrorCodeMessageRes(@StringRes int: Int): Int
}

class StringProviderImpl @Inject constructor(
    private val
    context: Context
) : StringProvider {
    companion object {
        private val errorCodeStringMap = ArrayMap<Int, Int>()
    }

    init {
        synchronized(this) {
            errorCodeStringMap[HTTP_400_BAD_REQUEST] = R.string.bad_request
            errorCodeStringMap[HTTP_401_UNAUTHORIZED] = R.string.unauthorized_error
            errorCodeStringMap[UNKNOWN_ERROR] = R.string.unknown_error
            errorCodeStringMap[NO_ACTIVE_CONNECTION] = R.string.no_internet_connectivity
            errorCodeStringMap[TIMEOUT] = R.string.error_timeout
            errorCodeStringMap[CONNECTION_SHUT_DOWN] = R.string.no_internet_connectivity
            errorCodeStringMap[HTTP_403_FORBIDDEN] = R.string.internal_error
            errorCodeStringMap[HTTP_409_CONFLICT_RESPONSE] = R.string.conflict_response
            errorCodeStringMap[HTTP_413_PAYLOAD_TOO_LARGE] = R.string.payload_too_large
            errorCodeStringMap[HTTP_500_INTERNAL_ERROR] = R.string.internal_error
            errorCodeStringMap[HTTP_502_BAD_GATEWAY] = R.string.internal_error
            errorCodeStringMap[UNKNOWN_HTTP_EXCEPTION] = R.string.internal_error
            errorCodeStringMap[USER_LOGIN_FAILED] = R.string.internal_error
            errorCodeStringMap[CANT_CONNECT_TO_SERVER] = R.string.internal_error
            errorCodeStringMap[NULL_POINTER] = R.string.internal_error
            errorCodeStringMap[SSL_HANDSHAKE] = R.string.no_internet_connectivity
            errorCodeStringMap[FIREBASE_DATABASE_ERROR] = R.string.internal_error
            errorCodeStringMap[MISSING_DATA] = R.string.internal_error
            errorCodeStringMap[MISSING_JSON_DATA] = R.string.internal_error
            errorCodeStringMap[MALFORMED_JSON] = R.string.internal_error
            errorCodeStringMap[GOOGLE_GENERIC_ERROR] = R.string.internal_error
            errorCodeStringMap[INVALID_CREDENTIALS] = R.string.invalid_credentials
            errorCodeStringMap[TOO_MANY_REQUESTS] = R.string.internal_error
            errorCodeStringMap[DEVELOPER_SETUP_ERROR] = R.string.internal_error
            errorCodeStringMap[CANCELLATION_EXCEPTION] = R.string.internal_error
            errorCodeStringMap[ROCKET_CHAT_EXCEPTION] = R.string.internal_error
        }
    }

    override fun messageForStringResource(int: Int): String {
        return errorCodeStringMap[int]?.let { context.getString(it) }.orEmpty()
    }

    override fun getErrorCodeMessageRes(int: Int): Int {
        return errorCodeStringMap[int]!!
    }
}