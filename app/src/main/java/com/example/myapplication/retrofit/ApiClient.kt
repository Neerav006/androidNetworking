package com.example.myapplication.retrofit


import com.example.myapplication.BuildConfig
import com.example.myapplication.utils.Constant
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private val client: Retrofit
        get() {

            val builder = Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
            val client = OkHttpClient.Builder()


            client.connectTimeout(30, TimeUnit.SECONDS)
            client.readTimeout(30, TimeUnit.SECONDS)

            //Add token interceptor
//                client.addInterceptor(getTokenInterceptor())

            // add logging interceptor if DEBUG build
            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                loggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
                client.addInterceptor(loggingInterceptor)
            }

            client.interceptors().add(Interceptor { chain ->
                val newRequestBuilder = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("X-Requested-With", "XMLHttpRequest")

               /* if (AppPreference.IS_LOGGED_IN) {
                    newRequestBuilder.addHeader(
                        "Authorization",
                        "Bearer " + AppPreference.ACCESS_TOKEN
                    )
                }*/

                chain.proceed(newRequestBuilder.build())
            })

            builder.client(client.build())
            return builder.build()
        }

    val service: ApiInterface
        get() = client.create(ApiInterface::class.java)


//    /** Used for JWT laravel token in API*/
//    private fun getTokenInterceptor(): Interceptor {
//        return Interceptor {
//            val request = it.request()
//            if (AppPreference.USER_DATA.token.isNotEmpty()) {
//                val builder = request.url().newBuilder()
//                val httpUrl = builder.addQueryParameter("token", AppPreference.USER_DATA.token).build()
//                val newRequest: Request = request.newBuilder().url(httpUrl).build()
//                it.proceed(newRequest)
//            } else {
//                it.proceed(request)
//            }
//        }
//    }



}