package ir.masouddabbaghi.eduregister.network

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import ir.masouddabbaghi.eduregister.MyApplication.Companion.applicationContext
import ir.masouddabbaghi.eduregister.R
import ir.masouddabbaghi.eduregister.data.model.ErrorModel
import ir.masouddabbaghi.eduregister.data.storage.SharedPreferencesHelper
import ir.masouddabbaghi.eduregister.ui.activity.SplashScreenActivity
import okhttp3.Interceptor
import okhttp3.Response
import okio.GzipSource
import java.nio.charset.Charset
import javax.inject.Inject

class ErrorResponseInterceptor
    @Inject
    constructor(
        private val sharedPreferencesHelper: SharedPreferencesHelper,
    ) : Interceptor {
        private val mainHandler = Handler(Looper.getMainLooper())

        override fun intercept(chain: Interceptor.Chain): Response {
            val response = chain.proceed(chain.request())
            val code = response.code
            if (code in 400..499) {
                responseBody(response)?.also { errorString ->
                    val responseError: ErrorModel = Gson().fromJson(errorString, ErrorModel::class.java)

                    responseError.message.forEach { errorDetail ->
                        val logMessage = "${errorDetail.field}: ${errorDetail.error}"
                        Log.e(ErrorResponseInterceptor::class.java.simpleName, logMessage)
                        showToast(logMessage.trim())
                    }

                    Log.e(ErrorResponseInterceptor::class.java.simpleName, "Error: ${responseError.error}")
                    Log.e(ErrorResponseInterceptor::class.java.simpleName, "Status Code: ${responseError.statusCode}")

                    if (code == 401) {
                        sharedPreferencesHelper.clearSharedPreferences()
                        val intent = Intent(applicationContext(), SplashScreenActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        applicationContext().startActivity(intent)
                    }
                }
            }

            if (code in 500..599) {
                responseBody(response)?.also { errorString ->
                    Log.e(ErrorResponseInterceptor::class.java.simpleName, errorString)
                    showToast(applicationContext().getString(R.string.server_error_hint))
                }
            }

            return response
        }

        private fun responseBody(response: Response): String? {
            val responseBody = response.body
            val contentLength = responseBody.contentLength()

            if (contentLength == 0L) {
                return null
            }

            val source = responseBody.source().apply { request(Long.MAX_VALUE) } // Buffer the entire body.
            var buffer = source.buffer
            val headers = response.headers

            if ("gzip".equals(headers["Content-Encoding"], ignoreCase = true)) {
                GzipSource(buffer.clone()).use { gzippedResponseBody ->
                    buffer = okio.Buffer().apply { writeAll(gzippedResponseBody) }
                }
            }

            val charset: Charset = responseBody.contentType()?.charset(Charsets.UTF_8) ?: Charsets.UTF_8
            return buffer.clone().readString(charset)
        }

        private fun showToast(message: String) {
            mainHandler.post {
                Toast.makeText(applicationContext(), message, Toast.LENGTH_LONG).show()
            }
        }
    }
