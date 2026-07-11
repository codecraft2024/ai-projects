package com.ghosttalk.core.network

import okhttp3.Interceptor
import okhttp3.Response

/**
 * ngrok free tier shows an interstitial page unless this header is sent.
 */
class NgrokSkipBrowserWarningInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header("ngrok-skip-browser-warning", "true")
            .build()
        return chain.proceed(request)
    }
}
