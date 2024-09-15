package ir.masouddabbaghi.eduregister.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.masouddabbaghi.eduregister.MyApplication.Companion.BASE_URL
import ir.masouddabbaghi.eduregister.data.storage.SharedPreferencesHelper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideOkHttp(sharedPreferencesHelper: SharedPreferencesHelper): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(provideLoggingInterceptor())
            .addInterceptor(ErrorResponseInterceptor(sharedPreferencesHelper))
            .addInterceptor(NetworkCheckInterceptor())
            .addInterceptor(HeaderInterceptor())
            .addInterceptor(AuthInterceptor(sharedPreferencesHelper))
            .readTimeout(45, TimeUnit.SECONDS)
            .connectTimeout(45, TimeUnit.SECONDS)
            .build()

    @Singleton
    @Provides
    @Named("loggingInterceptor")
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides
    fun provideApiClient(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
}
