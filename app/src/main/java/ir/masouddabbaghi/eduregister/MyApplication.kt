package ir.masouddabbaghi.eduregister

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : MultiDexApplication() {
    private var toast: Toast? = null
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    fun showDebouncedToast(message: String) {
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed({ showToast(message) }, DEBOUNCE_DELAY)
    }

    private fun showToast(message: String) {
        toast?.cancel()
        toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
        toast?.show()
    }

    companion object {
        const val BASE_URL = "https://serv.steelchat.ir/sma/"
        private const val DEBOUNCE_DELAY = 2_000L
        private lateinit var appInstance: MyApplication

        fun applicationContext(): Context = appInstance.applicationContext
    }
}
