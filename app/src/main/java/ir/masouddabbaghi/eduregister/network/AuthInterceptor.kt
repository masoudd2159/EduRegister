package ir.masouddabbaghi.eduregister.network

import android.util.Log
import ir.masouddabbaghi.eduregister.data.storage.SharedPreferencesHelper
import ir.masouddabbaghi.eduregister.data.storage.SharedPreferencesKeys.KEY_ACCESS_TOKEN
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor
    @Inject
    constructor(
        private val sharedPreferencesHelper: SharedPreferencesHelper,
    ) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val accessToken = getAccessTokenFromSharedPreferences(chain)
            val request =
                chain
                    .request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer $accessToken")
                    .build()

            return chain.proceed(request)
        }

        private fun getAccessTokenFromSharedPreferences(chain: Interceptor.Chain): String {
            val accessTokenPreferences = sharedPreferencesHelper.getString(KEY_ACCESS_TOKEN, "")
            return if (accessTokenPreferences.isEmpty()) {
                Log.w(AuthInterceptor::class.java.simpleName, "Access Token is null in this activity")
                ""
            } else {
                Log.i(
                    AuthInterceptor::class.java.simpleName,
                    "Access Token for this activity is : $accessTokenPreferences for this URL ${getServiceURL(chain.request())}",
                )
                accessTokenPreferences
            }
        }

        private fun getServiceURL(request: Request): String = request.url.toUrl().toString()
    }
