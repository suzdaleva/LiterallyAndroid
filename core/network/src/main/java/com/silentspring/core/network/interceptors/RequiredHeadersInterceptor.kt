package com.silentspring.core.network.interceptors


import android.os.Build
import okhttp3.Interceptor
import okhttp3.Response
import java.util.Locale
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class RequiredHeadersInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val language = Locale.getDefault().language.takeIf { language -> language == "ru" }
            ?: Locale.ENGLISH.language

        // val appVersion = BuildConfig.VERSION
        val requestId = UUID.randomUUID().toString()
        val osVersion = Build.VERSION.RELEASE
        val deviceModel = "${Build.MANUFACTURER} ${Build.MODEL}".lowercase()
        val originalRequest = chain.request()
        val authenticatedRequest = originalRequest.newBuilder()
            .header(REQUEST_ID_HEADER, requestId)
            .header(OS_HEADER, OS)
            .header(OS_VERSION_HEADER, osVersion)
            .header(DEVICE_MODEL_HEADER, deviceModel)
            .header(ACCEPT_LANGUAGE_HEADER, language)
            .build()

        return chain.proceed(authenticatedRequest)
    }

    companion object {
        private const val OS = "Android"
        private const val REQUEST_ID_HEADER = "request-id"
        private const val APP_ID_HEADER = "app-id"
        private const val OS_HEADER = "os"
        private const val OS_VERSION_HEADER = "os-version"
        private const val APP_VERSION_HEADER = "app-version"
        private const val DEVICE_MODEL_HEADER = "device-model"
        private const val ACCEPT_LANGUAGE_HEADER = "accept-language"
    }
}