package ir.masouddabbaghi.eduregister.network

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import ir.masouddabbaghi.eduregister.MyApplication.Companion.applicationContext
import ir.masouddabbaghi.eduregister.R
import ir.masouddabbaghi.eduregister.data.model.ErrorResponse
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
                handleClientErrors(response, code)
            } else if (code in 500..599) {
                handleServerErrors(response)
            }
            return response
        }

        private fun handleClientErrors(
            response: Response,
            code: Int,
        ) {
            responseBody(response)?.let { errorString ->
                Log.d(ErrorResponseInterceptor::class.java.simpleName, "Raw error response: $errorString")

                when {
                    errorString.contains("\"status\"") && errorString.contains("\"title\"") -> {
                        try {
                            val responseError = Gson().fromJson(errorString, ErrorResponse.Model1::class.java)
                            handleModel1Error(responseError, code)
                        } catch (e: JsonSyntaxException) {
                            Log.e(ErrorResponseInterceptor::class.java.simpleName, "Error parsing Model1: $e")
                        }
                    }

                    errorString.contains("\"errors\"") && !errorString.contains("\"Password\"") -> {
                        try {
                            val responseError = Gson().fromJson(errorString, ErrorResponse.Model2::class.java)
                            handleModel2Error(responseError, code)
                        } catch (e: JsonSyntaxException) {
                            Log.e(ErrorResponseInterceptor::class.java.simpleName, "Error parsing Model2: $e")
                        }
                    }

                    errorString.contains("\"errors\"") && errorString.contains("\"Password\"") -> {
                        try {
                            val responseError = Gson().fromJson(errorString, ErrorResponse.Model3::class.java)
                            handleModel3Error(responseError, code)
                        } catch (e: JsonSyntaxException) {
                            Log.e(ErrorResponseInterceptor::class.java.simpleName, "Error parsing Model3: $e")
                        }
                    }

                    else -> {
                        Log.e(ErrorResponseInterceptor::class.java.simpleName, "Unknown error format: $errorString")
                    }
                }
            }
        }

        private fun handleModel1Error(
            error: ErrorResponse.Model1,
            code: Int,
        ) {
            val logMessage = "Error: ${error.title}"
            Log.e(ErrorResponseInterceptor::class.java.simpleName, logMessage)
            showToast(logMessage.trim())

            if (code == 401) {
                sharedPreferencesHelper.clearSharedPreferences()
                Intent(applicationContext(), SplashScreenActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    applicationContext().startActivity(this)
                }
            }
        }

        private fun handleModel2Error(
            error: ErrorResponse.Model2,
            code: Int,
        ) {
            Log.e(ErrorResponseInterceptor::class.java.simpleName, "format Model 2: $error")
            error.errors.forEach { errorDetail ->
                Log.e(ErrorResponseInterceptor::class.java.simpleName, errorDetail)
                showToast(errorDetail.trim())
            }

            if (code == 401) {
                sharedPreferencesHelper.clearSharedPreferences()
                Intent(applicationContext(), SplashScreenActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    applicationContext().startActivity(this)
                }
            }
        }

        private fun handleModel3Error(
            error: ErrorResponse.Model3,
            code: Int,
        ) {
            val logMessage = "Error: ${error.title}"
            Log.e(ErrorResponseInterceptor::class.java.simpleName, logMessage)
            showToast(logMessage.trim())

            if (code == 401) {
                sharedPreferencesHelper.clearSharedPreferences()
                Intent(applicationContext(), SplashScreenActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    applicationContext().startActivity(this)
                }
            }
        }

        private fun handleServerErrors(response: Response) {
            responseBody(response)?.let { errorString ->
                Log.e(ErrorResponseInterceptor::class.java.simpleName, errorString)
                showToast(applicationContext().getString(R.string.server_error_hint))
            }
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
