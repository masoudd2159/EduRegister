package ir.masouddabbaghi.eduregister.network

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class NetworkUtils
    @Inject
    constructor() {
        fun <T> performNetworkRequest(call: suspend () -> Response<T>): Flow<NetworkResult<T>> =
            flow {
                try {
                    emit(NetworkResult.Loading(isLoading = true))

                    val response = call.invoke()

                    if (response.isSuccessful) {
                        response.body()?.let { data ->
                            emit(NetworkResult.Success(data = data))
                        } ?: emit(NetworkResult.Failure(errorMessage = "Response body is null"))
                    } else {
                        emit(NetworkResult.Failure(errorMessage = "Response not successful: ${response.code()}"))
                    }
                } catch (e: IOException) {
                    emit(NetworkResult.Failure(errorMessage = "Network error: ${e.message}"))
                } catch (e: HttpException) {
                    emit(NetworkResult.Failure(errorMessage = "HTTP error: ${e.code()}"))
                } catch (e: Exception) {
                    emit(NetworkResult.Failure(errorMessage = e.message ?: "Unknown error"))
                    Log.e(NetworkUtils::class.simpleName, "Unknown error -> ${e.message}")
                } finally {
                    emit(NetworkResult.Loading(isLoading = false))
                }
            }
    }
