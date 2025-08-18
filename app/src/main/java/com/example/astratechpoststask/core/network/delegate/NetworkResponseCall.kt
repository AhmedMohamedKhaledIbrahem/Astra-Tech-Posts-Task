package com.example.astratechpoststask.core.network.delegate

import com.example.astratechpoststask.core.error.DataError
import com.example.astratechpoststask.core.error.toRemoteDataError
import com.example.astratechpoststask.core.utils.Result
import com.example.astratechpoststask.core.utils.RootError
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response

class NetworkResponseCall<T : Any, E : RootError>(
    private val delegate: Call<T>,
    private val errorConverter: Converter<ResponseBody, E>
) : Call<Result<T, E>> {

    override fun enqueue(callback: Callback<Result<T, E>>) {
        delegate.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T?>, response: Response<T?>) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    callback.onResponse(
                        this@NetworkResponseCall,
                        Response.success(Result.Success(body))
                    )
                } else {
                    val errorBody = response.errorBody()
                    val error = try {
                        errorBody?.let {
                            errorConverter.convert(it)
                        } ?: DataError.Network.UNKNOWN_ERROR
                    } catch (_: Throwable){
                        DataError.Network.UNKNOWN_ERROR
                    }
                    @Suppress("UNCHECKED_CAST")
                    callback.onResponse(
                        this@NetworkResponseCall,
                        Response.success(Result.Error(error as E))
                    )
                }
            }

            override fun onFailure(call: Call<T?>, t: Throwable) {
                val error : RootError = (t as? Exception)?.toRemoteDataError()
                    ?: DataError.Network.UNKNOWN_ERROR
                @Suppress("UNCHECKED_CAST")
                callback.onResponse(
                    this@NetworkResponseCall,
                    Response.success(Result.Error(error as E))
                )
            }

        })
    }


    override fun clone(): Call<Result<T, E>> =
        NetworkResponseCall(delegate.clone(), errorConverter)

    override fun execute(): Response<Result<T, E>> {
        throw UnsupportedOperationException("NetworkResponseCall doesn’t support execute()")
    }

    override fun cancel() = delegate.cancel()

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}