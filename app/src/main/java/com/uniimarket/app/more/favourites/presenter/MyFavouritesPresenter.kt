package com.uniimarket.app.more.favourites.presenter

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.uniimarket.app.more.favourites.model.MyFavouritesData
import com.uniimarket.app.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyFavouritesPresenter(var myFavouritesListener: MyFavouritesListener, var context: Context?) {

    val sharedPref: SharedPreferences = context?.getSharedPreferences("uniimarket", Context.MODE_PRIVATE)!!

    fun getFavouritesList() {

        var id: String = sharedPref?.getString("id", null)
        val apiService = ApiClient.create()

        val call = apiService.getFavouritesList(id)

        call?.enqueue(object : Callback<MyFavouritesData> {
            override fun onResponse(call: Call<MyFavouritesData>, response: Response<MyFavouritesData>) {
                try {
                    Log.e("fav status", Gson().toJson(response.body()))

                    if (response.body()!!.getStatus().toString().contains("true")) {
                        val notificationDataList = ArrayList(response.body()!!.getData())
                        Log.e("fav mess", response.body()!!.getMessage())
                        if (notificationDataList.size > 0) {
                            myFavouritesListener.myFavouritesResponseSuccess(
                                "true",
                                response.body()!!.getMessage(),
                                notificationDataList
                            )
                        } else {
                            myFavouritesListener.myFavouritesResponseFailure("true", response.body()!!.getMessage())
                        }
                    } else {
                        myFavouritesListener.myFavouritesResponseFailure("true", response.body()!!.getMessage())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    myFavouritesListener.myFavouritesResponseFailure(
                        "false",
                        "failure"
                    )
                }

            }

            override fun onFailure(call: Call<MyFavouritesData>, t: Throwable) {
                Log.e("TAG", t.toString())
            }
        })


    }


    interface MyFavouritesListener {
        fun myFavouritesResponseSuccess(
            status: String,
            message: String?,
            notificationDataList: java.util.ArrayList<MyFavouritesData.Datum>
        )

        fun myFavouritesResponseFailure(status: String, message: String?)
    }
}