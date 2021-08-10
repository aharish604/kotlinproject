package com.uniimarket.app.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


public class ApiClient {

    // production
    //    public static final String BASE_URL = BuildConfig.BASE_URL;
    //  // production
    //    public static final String BASE_URL = "http://204.48.22.81/Sociallink_Api/";
    // // testing

    private var retrofit: Retrofit? = null


    /**
     * This method returns retrofit client instance
     *
     * @return Retrofit object
     */
//    fun getClient(): Retrofit? {
//        if (retrofit == null) {
//
//            val gson = GsonBuilder()
//                .setLenient()
//                .create()
//            retrofit = Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build()
//        }
//        return this!!.retrofit
//    }

    companion object {
        fun create(): ApiInterface {

//            val BASE_URL = "http://13.234.112.106/uniimarket/"
            val BASE_URL = "http://13.235.71.131/"

            val gson = GsonBuilder()
                .setLenient()
                .create()

            val client = OkHttpClient.Builder()
                .connectTimeout(5000, TimeUnit.SECONDS)
                .readTimeout(5000, TimeUnit.SECONDS).build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

//            val retrofit = Retrofit.Builder()
//                .addCallAdapterFactory(
//                    RxJava2CallAdapterFactory.create())
//                .addConverterFactory(
//                    GsonConverterFactory.create())
//                .baseUrl(BASE_URL)
//                .build()

            return retrofit.create(ApiInterface::class.java)
        }
    }
}