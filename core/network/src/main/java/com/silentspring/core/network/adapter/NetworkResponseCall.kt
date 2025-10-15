package com.silentspring.core.network.adapter

import android.util.Log
import com.google.gson.Gson
import com.silentspring.core.common.exceptions.NetworkException
import com.silentspring.core.network.model.ErrorResponse
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class NetworkResponseCall<Success : Any>(
    private val gson: Gson,
    private val delegate: Call<Success>
) : Call<Success> {

    @Suppress("UNCHECKED_CAST")
    override fun enqueue(callback: Callback<Success>) {
        return delegate.enqueue(object : Callback<Success> {

            override fun onFailure(call: Call<Success>, throwable: Throwable) {
                throwable.printStackTrace()
                callback.onFailure(
                    this@NetworkResponseCall,
                    NetworkException(
                        status = 500,
                        errors = emptyList()
                    )
                )
            }

            override fun onResponse(call: Call<Success>, response: Response<Success>) {
                val body = response.body()
                val errorBody = response.errorBody()

                if (response.isSuccessful) {
                    callback.onResponse(
                        this@NetworkResponseCall,
                        Response.success(body ?: Any() as? Success)
                    )
                } else {
                    val errorResponse: ErrorResponse = try {
                        gson.fromJson(
                            errorBody?.string(),
                            ErrorResponse::class.java
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                        ErrorResponse(
                            timestamp = "${response.raw().receivedResponseAtMillis}",
                            errors = emptyList()
                        )
                    }
                    Log.i("Literally", "$errorResponse")
                    callback.onFailure(
                        this@NetworkResponseCall,
                        NetworkException(
                            status = response.code(),
                            errors = errorResponse.errors ?: emptyList()
                        )
                    )
                }
            }

        })
    }

    override fun execute(): Response<Success> {
        throw UnsupportedOperationException("NetworkResponseCall doesn't support execute")
    }

    override fun clone(): Call<Success> = NetworkResponseCall(gson, delegate.clone())
    override fun isExecuted(): Boolean = delegate.isExecuted
    override fun cancel() = delegate.cancel()
    override fun isCanceled(): Boolean = delegate.isCanceled
    override fun request(): Request = delegate.request()
    override fun timeout(): Timeout = delegate.timeout()

}