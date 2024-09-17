package ir.masouddabbaghi.eduregister.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import ir.masouddabbaghi.eduregister.MyApplication
import ir.masouddabbaghi.eduregister.MyApplication.Companion.applicationContext
import ir.masouddabbaghi.eduregister.R
import okhttp3.Interceptor
import okhttp3.Response

class NetworkCheckInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isNetworkAvailable()) {
            Log.w(
                NetworkCheckInterceptor::class.java.simpleName,
                "No Internet Connection in This Activity : ${applicationContext().javaClass.simpleName}",
            )
            val myApplication = applicationContext().applicationContext as MyApplication
            myApplication.showDebouncedToast(applicationContext().resources.getString(R.string.connection_fail))
        }
        return chain.proceed(chain.request())
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = applicationContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null &&
            (
                capabilities.hasTransport(
                    NetworkCapabilities.TRANSPORT_WIFI,
                ) ||
                    capabilities.hasTransport(
                        NetworkCapabilities.TRANSPORT_CELLULAR,
                    ) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            )
    }
}
