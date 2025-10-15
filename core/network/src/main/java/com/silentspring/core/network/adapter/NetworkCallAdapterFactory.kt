package com.silentspring.core.network.adapter

import com.google.gson.Gson
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

internal class NetworkCallAdapterFactory(
    private val gson: Gson
) : CallAdapter.Factory() {

    @Suppress("UNCHECKED_CAST")
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (Call::class.java != getRawType(returnType) && returnType !is ParameterizedType) {
            return null
        }

        check(returnType is ParameterizedType)

        val successBodyType = getParameterUpperBound(0, returnType)

        return object : CallAdapter<Any, Call<Result<*>>> {
            override fun adapt(call: Call<Any>): Call<Result<*>> =
                NetworkResponseCall(gson, call) as Call<Result<*>>

            override fun responseType(): Type = successBodyType
        }
    }
}