package com.example.astratechpoststask.core.network.adapter

import com.example.astratechpoststask.core.network.delegate.NetworkResponseCall
import com.example.astratechpoststask.core.utils.Result
import com.example.astratechpoststask.core.utils.RootError
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Converter
import java.lang.reflect.Type

class NetworkAdapter<T : Any, E : RootError>(
    private val successType: Type,
    private val errorConverter: Converter<ResponseBody, E>
) : CallAdapter<T, Call<Result<T, E>>> {
    override fun responseType(): Type = successType

    override fun adapt(call: Call<T>): Call<Result<T, E>> {
        return NetworkResponseCall(call, errorConverter)
    }
}