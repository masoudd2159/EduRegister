package ir.masouddabbaghi.eduregister.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.masouddabbaghi.eduregister.R
import ir.masouddabbaghi.eduregister.data.model.HomeList
import ir.masouddabbaghi.eduregister.data.model.Slider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) {
        suspend fun homeList(
            rawFile: Int = R.raw.home_list,
            dispatcher: CoroutineDispatcher = Dispatchers.IO,
        ): HomeList =
            withContext(dispatcher) {
                val inputStream = context.resources.openRawResource(rawFile)
                val json = inputStream.bufferedReader().use { it.readText() }
                val gson = Gson()
                val homeListType = object : TypeToken<HomeList>() {}.type
                gson.fromJson(json, homeListType) as HomeList
            }

        suspend fun slider(
            rawFile: Int = R.raw.slider,
            dispatcher: CoroutineDispatcher = Dispatchers.IO,
        ): Slider =
            withContext(dispatcher) {
                val inputStream = context.resources.openRawResource(rawFile)
                val json = inputStream.bufferedReader().use { it.readText() }
                val gson = Gson()
                val sliderType = object : TypeToken<Slider>() {}.type
                gson.fromJson(json, sliderType) as Slider
            }
    }
