package com.uniimarket.app

import android.content.Context
import android.util.Log
import com.uniimarket.app.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScreenPresenter (var screenListener: ScreenListener) {

    fun getIntroData() {

        val apiService = ApiClient.create()

//        Call<EstimatedProjectsData> call = apiService.getEstimatedProjectList(sharedPreferences.getString("uid", null));
        val call = apiService.getIntro()

        call.enqueue(object : Callback<ScreenData> {
            override fun onResponse(call: Call<ScreenData>, response: Response<ScreenData>) {
                try {
                    Log.e("categories list ", response.body().toString())

                    if (response.body()!!.getStatus().toString().contains("true")) {
                        val categoriesList = ArrayList(response.body()!!.getData())
                        Log.e("cate mess", response.body()!!.getMessage())

                        screenListener.screenResponseSuccess(
                                "true",
                                response.body()!!.getMessage(),
                                categoriesList)

                    } else {
                        screenListener.screenResponseFailure("true", response.body()!!.getMessage())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    screenListener.screenResponseFailure(
                        "false",
                        "failure"
                    )
                }

            }

            override fun onFailure(call: Call<ScreenData>, t: Throwable) {
                Log.e("TAG", t.toString())
                //                    Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        })

    }

    interface ScreenListener {
        fun screenResponseSuccess(
            status: String,
            message: String?,
            signInList: java.util.ArrayList<ScreenDatum>
        )

        fun screenResponseFailure(status: String, message: String?)

    }

}