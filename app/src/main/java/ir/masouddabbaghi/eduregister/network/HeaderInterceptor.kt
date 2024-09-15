package ir.masouddabbaghi.eduregister.network

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response =
        chain.run {
            proceed(
                request()
                    .newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Accept-Language", "fa")
                    .build(),
            )
        }
}
