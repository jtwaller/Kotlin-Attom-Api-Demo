package com.jtwaller.attomdemo.dagger

import android.content.Context
import com.jtwaller.attomdemo.R
import com.jtwaller.attomdemo.network.AttomApi
import com.jtwaller.attomdemo.network.RestApi
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {

    companion object {
        const val BASE_URL: String = "https://search.onboard-apis.com/"
    }

    @Provides
    @Named("header")
    fun provideOkHttpHeaderInterceptor(ctx: Context): Interceptor {
        return Interceptor { chain ->
                val request = chain
                        .request()
                        .newBuilder()
                        .addHeader("apikey", ctx.getString(R.string.attom_api_key))
                        .addHeader("Accept", "application/json")
                        .build()

                chain.proceed(request)
            }

    }

    @Provides
    @Named("logging")
    fun provideOkHttpLoggingInterceptor(ctx: Context): Interceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(@Named("header") headerInterceptor: Interceptor,
                            @Named("logging") loggingInterceptor: Interceptor): OkHttpClient {

        return OkHttpClient.Builder()
                .addInterceptor(headerInterceptor)
                .addInterceptor(loggingInterceptor)
                .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Provides
    @Singleton
    fun provideAttomApi(retrofit: Retrofit): AttomApi {
        return retrofit.create(AttomApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRestApi(attomApi: AttomApi): RestApi {
        return RestApi(attomApi)
    }

}