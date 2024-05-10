package com.heixss.findmypet.data.common

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val sessionManager: SessionManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Add authorization header to the original request
        sessionManager.getAccessToken()?.let { token ->
            val requestWithAuth = originalRequest.newBuilder().header("Authorization", "Bearer $token").build()
            return chain.proceed(requestWithAuth)
        }

        return chain.proceed(chain.request())
    }
}