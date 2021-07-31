package com.tapan.twaktotest.data.core

import com.tapan.twaktotest.data.exception.AppException
import com.tapan.twaktotest.data.exception.StringProvider
import com.tapan.twaktotest.util.isListAndEmpty
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import retrofit2.Response
import kotlin.contracts.ExperimentalContracts

abstract class NetworkResource<RESULT, REQUEST> {

    fun asFlow(stringProvider: StringProvider? = null) = flow<Resource<RESULT>> {
        emit(Resource.loading())
        //if localFlow is not null, it means we are constantly observing database for changes
        val localFlow = fetchLocalFlow()

        try {
            val shouldFetch: Boolean
            var isInitialEmpty = false

            if (localFlow != null) {
                val result = localFlow.first()
                val isEmptyList = result.isListAndEmpty()
                shouldFetch = shouldFetchFromRemote(result)
                isInitialEmpty = isEmptyList
                if (!isEmptyList) emit(Resource.success(result))
            } else {
                val oneTimeData = fetchLocal()
                shouldFetch = shouldFetchFromRemote(oneTimeData)
                if (oneTimeData != null) {
                    emit(Resource.success(oneTimeData))
                }
            }

            if (!shouldFetch) {
                if (localFlow != null) {
                    emitAll(localFlow.map { Resource.success<RESULT>(it) })
                }
                return@flow
            }

            val apiResponse = fetchFromRemote(localFlow?.first())
            val data = apiResponse.body()
            if (apiResponse.isSuccessful && data != null) {
                saveRemoteData(data)
                if (localFlow == null) {
                    //enough for database to be updated
                    delay(100)
                    //emission is not continuous, so data won't be delivered automatically
                    //so we trigger the data manually after storing it locally
                    emit(Resource.success(fetchLocal()))
                }
            } else {
                val ex = AppException(apiResponse.code(), stringProvider)
                emit(Resource.error(ex.message, null, ex, apiResponse))
            }
            if (isInitialEmpty) {//to stop shimmer
                delay(100)
                emit(Resource.success(fetchLocalFlow()?.first()))
            }
        } catch (e: Exception) {
            val ex = AppException(e, stringProvider)
            emit(Resource.error(ex.message, null, ex))
        }

        if (localFlow != null) {

            emitAll(localFlow.map { Resource.success<RESULT>(it) })
        }
    }

    protected abstract suspend fun saveRemoteData(response: REQUEST)

    protected abstract fun fetchLocalFlow(): Flow<RESULT>?

    protected abstract fun fetchLocal(): RESULT?

    protected abstract suspend fun fetchFromRemote(result: RESULT?): Response<REQUEST>

    protected abstract fun shouldFetchFromRemote(result: RESULT?): Boolean

}

abstract class LocalResource<T> {

    fun asFlow(stringProvider: StringProvider? = null) = flow<Resource<T>> {
        emit(Resource.loading())
        //if localFlow is not null, it means we are constantly observing database for changes
        val localFlow = fetchLocalFlow()
        try {
            if (localFlow != null) {
                emit(Resource.success(localFlow.first()))
            } else {
                val oneTimeData = fetchLocal()
                if (oneTimeData != null) {
                    emit(Resource.success(oneTimeData))
                }
            }
        } catch (e: Exception) {
            val ex = AppException(e, stringProvider)
            emit(Resource.error(ex.message, null, ex))
        }

        if (localFlow != null) {
            emitAll(localFlow.map { Resource.success<T>(it) })
        }
    }

    protected abstract fun fetchLocalFlow(): Flow<T>?

    protected abstract fun fetchLocal(): T?

}

abstract class NetworkOnlyResource<RESULT, REQUEST> {

    fun asFlow(stringProvider: StringProvider? = null) = flow<Resource<RESULT>> {
        emit(Resource.loading())
        try {
            val apiResponse = fetchFromRemote()
            val data = apiResponse.body()
            if (apiResponse.isSuccessful && data != null) {
                val result = map(data)
                emit(Resource.success(result))
            } else {
                val ex = AppException(apiResponse.code(), stringProvider)
                emit(Resource.error(ex.message, null, ex, apiResponse))
            }
        } catch (e: Exception) {
            val ex = AppException(e, stringProvider)
            emit(Resource.error(ex.message, null, ex))
        }
    }

    protected abstract suspend fun fetchFromRemote(): Response<REQUEST>

    protected abstract suspend fun map(response: REQUEST): RESULT

}