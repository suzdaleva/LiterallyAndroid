package com.silentspring.core.network.di

import com.google.gson.Gson
import com.silentspring.core.network.utils.AuthInterceptorOkHttpClient
import com.silentspring.core.network.utils.UnauthInterceptorOkHttpClient
import com.silentspring.core.network.adapter.NetworkCallAdapterFactory
import com.silentspring.core.network.interceptors.AuthorizationHeaderInterceptor
import com.silentspring.core.network.interceptors.RequiredHeadersInterceptor
import com.silentspring.core.network.utils.TokenAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

const val BASE_URL = "http://192.168.0.13:8082"
//"SHA256(certificate.crt)= e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"

@Module
@InstallIn(SingletonComponent::class)
object CoreNetworkModule {


    //ToDo add certificatePinner
//    val certificatePinner = CertificatePinner.Builder()
//        .add("192.168.0.12", "sha256/e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855")
//        .build()

    @Singleton
    @Provides
    @UnauthInterceptorOkHttpClient
    internal fun provideUnauthOkHttpClient(
        requiredHeadersInterceptor: RequiredHeadersInterceptor
    ): OkHttpClient {

        return OkHttpClient.Builder()
            .addNetworkInterceptor(requiredHeadersInterceptor)
            .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(10L, TimeUnit.SECONDS)
            .readTimeout(10L, TimeUnit.SECONDS)
            .writeTimeout(10L, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    @AuthInterceptorOkHttpClient
    internal fun provideAuthOkHttpClient(
        requiredHeadersInterceptor: RequiredHeadersInterceptor,
        authorizationHeaderInterceptor: AuthorizationHeaderInterceptor,
        @TokenAuthenticator tokenAuthenticator: Authenticator
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .authenticator(tokenAuthenticator)
            .addNetworkInterceptor(authorizationHeaderInterceptor)
            .addNetworkInterceptor(requiredHeadersInterceptor)
            .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(10L, TimeUnit.SECONDS)
            .readTimeout(10L, TimeUnit.SECONDS)
            .writeTimeout(10L, TimeUnit.SECONDS)
            .build()
    }


    @Singleton
    @Provides
    @UnauthInterceptorOkHttpClient
    internal fun provideUnauthRetrofit(
        @UnauthInterceptorOkHttpClient okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        networkCallAdapterFactory: NetworkCallAdapterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(networkCallAdapterFactory)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    @AuthInterceptorOkHttpClient
    internal fun provideAuthRetrofit(
        @AuthInterceptorOkHttpClient okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        networkCallAdapterFactory: NetworkCallAdapterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(networkCallAdapterFactory)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    internal fun provideNetworkAdapterFactory(
        gson: Gson
    ): NetworkCallAdapterFactory {
        return NetworkCallAdapterFactory(gson)
    }
}