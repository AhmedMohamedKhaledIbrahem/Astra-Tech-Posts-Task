package com.example.astratechpoststask.core.network.factory

import com.example.astratechpoststask.core.network.adapter.NetworkAdapter
import com.example.astratechpoststask.core.utils.Result
import com.example.astratechpoststask.core.utils.RootError
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class NetworkAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation?>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) return null
        val callType = getParameterUpperBound(0, returnType as ParameterizedType)
        if (getRawType(callType) != Result::class.java) return null
        require(callType is ParameterizedType) {
            "Result must be parameterized as Result<SuccessBody, ErrorBody>"
        }
        val successBodyType = getParameterUpperBound(0, callType)
        val errorBodyType = getParameterUpperBound(1, callType)
        val errorConverter =
            retrofit.nextResponseBodyConverter<Any>(null, errorBodyType, annotations)
        @Suppress("UNCHECKED_CAST")
        return NetworkAdapter<Any, RootError>(
            successBodyType,
            errorConverter as Converter<ResponseBody, RootError>
        )
    }
}