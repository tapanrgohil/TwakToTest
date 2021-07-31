package com.tapan.twaktotest.data.exception

import androidx.annotation.StringRes
import androidx.collection.ArrayMap
import com.tapan.twaktotest.R
import com.tapan.twaktotest.data.core.Resource
import okhttp3.internal.http2.ConnectionShutdownException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.CancellationException
import javax.net.ssl.SSLHandshakeException

/**
 * Google common error codes: https://developers.google.com/android/reference/com/google/android/gms/common/api/CommonStatusCodes
 * Google sign in error codes: https://developers.google.com/android/reference/com/google/android/gms/auth/api/signin/GoogleSignInStatusCodes.html
 */
//http errors
const val HTTP_400_BAD_REQUEST = 400
const val HTTP_401_UNAUTHORIZED = 401
const val HTTP_403_FORBIDDEN = 403
const val HTTP_404_NOT_FOUND = 404
const val HTTP_413_PAYLOAD_TOO_LARGE = 413
const val HTTP_422_UNPROCESSIBLE_ENTITY = 422
const val HTTP_409_CONFLICT_RESPONSE = 409
const val HTTP_500_INTERNAL_ERROR = 500
const val HTTP_502_BAD_GATEWAY = 502
const val USER_LOGIN_FAILED = 451

//general exceptions
const val UNKNOWN_ERROR = 1000
const val NO_ACTIVE_CONNECTION = 1001
const val TIMEOUT = 1002
const val CONNECTION_SHUT_DOWN = 1003
const val UNKNOWN_HTTP_EXCEPTION = 1006
const val MISSING_DATA = 1007
const val CANT_CONNECT_TO_SERVER = 1008
const val MISSING_JSON_DATA = 1009
const val MALFORMED_JSON = 1010
const val HTTP_INTERCEPTOR_EXCEPTION = 1011
const val DEVELOPER_SETUP_ERROR = 1012
const val CANCELLATION_EXCEPTION = 1013
const val ROCKET_CHAT_EXCEPTION = 1014

//google and firebase errors
const val NULL_POINTER = 1315
const val SSL_HANDSHAKE = 1316
const val FIREBASE_DATABASE_ERROR = 1321
const val GOOGLE_GENERIC_ERROR = 1329
const val INVALID_CREDENTIALS = 1330
const val TOO_MANY_REQUESTS = 1331
const val GENERIC_FIREBASE_ERROR = 1334

class AppException : RuntimeException {


    var errorCode: Int = 0
    private var throwable: Throwable? = null
    private var errorMessage: String? = null

    constructor(errorCode: Int, stringProvider: StringProvider? = null) : super() {
        this.errorCode = errorCode
        errorMessage = if (stringProvider?.getErrorCodeMessageRes(errorCode) == null) super.message
        else stringProvider.messageForStringResource(
            stringProvider.getErrorCodeMessageRes(
                errorCode
            )
        )
    }

    constructor(errorCode: Int, message: String, stringProvider: StringProvider? = null) : super() {
        this.errorCode = errorCode
        this.errorMessage = message
    }

    constructor(throwable: Throwable?, stringProvider: StringProvider? = null) : super(throwable) {
        this.throwable = throwable

        /**
         * Higher priority exceptions should be placed higher in the when statement. If an exception
         * is a subclass of multiple cases, the first case would be selected.
         */
        when (throwable) {
            is AppException -> {
                this.errorCode = throwable.errorCode
                this.errorMessage = throwable.errorMessage
                this.throwable = throwable.throwable
            }
            is HttpException -> errorCode = when (throwable.code()) {
                400 -> HTTP_400_BAD_REQUEST
                401 -> HTTP_401_UNAUTHORIZED
                403 -> HTTP_403_FORBIDDEN
                404 -> INVALID_CREDENTIALS //for sandbox login API
                409 -> HTTP_409_CONFLICT_RESPONSE
                413 -> HTTP_413_PAYLOAD_TOO_LARGE
                422 -> HTTP_422_UNPROCESSIBLE_ENTITY
                500 -> HTTP_500_INTERNAL_ERROR
                502 -> HTTP_502_BAD_GATEWAY
                else -> UNKNOWN_HTTP_EXCEPTION
            }
            is UnknownHostException -> errorCode = NO_ACTIVE_CONNECTION
            is NullPointerException -> //if a stacktrace shows that an API call leads to this,
                // we have received a null "data" object in the response, despite it being successful
                // (having "success" in the status).
                errorCode = NULL_POINTER
            is SSLHandshakeException -> errorCode = SSL_HANDSHAKE
            is SocketTimeoutException -> errorCode = TIMEOUT
            is ConnectException -> errorCode = CANT_CONNECT_TO_SERVER
            is ConnectionShutdownException -> errorCode = CONNECTION_SHUT_DOWN
            is CancellationException -> errorCode = CANCELLATION_EXCEPTION
            else -> errorCode = UNKNOWN_ERROR
        }

        val errorCodeMessageRes = stringProvider?.getErrorCodeMessageRes(errorCode)
        if (errorCodeMessageRes != null) {
            errorMessage = stringProvider.messageForStringResource(errorCodeMessageRes)
        } else if (errorMessage == null) {
            errorMessage = stringProvider?.messageForStringResource(R.string.unknown_error)
                ?: "Unknown Error"
        }
    }

    override val cause: Throwable?
        get() = throwable


    override val message: String
        get() = errorMessage?.let { "$it: $errorCode" } ?: ""

    fun isNetworkError() = networkErrorCodes.contains(errorCode)

    companion object {
        val networkErrorCodes =
            listOf(
                NO_ACTIVE_CONNECTION, TIMEOUT, CANT_CONNECT_TO_SERVER, SSL_HANDSHAKE,
                CONNECTION_SHUT_DOWN
            )
    }
}

fun Throwable?.isNetworkError(): Boolean {
    return when (this) {
        is AppException -> isNetworkError()
        is UnknownHostException,
        is SSLHandshakeException,
        is SocketTimeoutException,
        is ConnectException,
        is ConnectionShutdownException -> true
        else -> false
    }
}

fun <T> Resource<T>.isNetworkError() = throwable.isNetworkError()
