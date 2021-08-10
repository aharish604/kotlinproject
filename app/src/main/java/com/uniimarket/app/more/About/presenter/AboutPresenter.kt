package com.uniimarket.app.more.About.presenter

import android.util.Log
import com.uniimarket.app.more.About.model.AboutData
import com.uniimarket.app.more.About.model.AboutDatum
import com.uniimarket.app.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AboutPresenter  (var aboutListener: AboutListener){


    fun getAboutData() {

        val apiService = ApiClient.create()

//        Call<EstimatedProjectsData> call = apiService.getEstimatedProjectList(sharedPreferences.getString("uid", null));
        val call = apiService.getAbout()

        call.enqueue(object : Callback<AboutData> {
            override fun onResponse(call: Call<AboutData>, response: Response<AboutData>) {
                try {

                    if (response.body()!!.getStatus().toString().contains("true")) {
                        val categoriesList = ArrayList(response.body()!!.getData())

                        aboutListener.AboutResponseSuccess(
                            "true",
                            response.body()!!.getMessage(),
                            categoriesList)

                    } else {
                        aboutListener.AboutResponseFailure("true", response.body()!!.getMessage())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    aboutListener.AboutResponseFailure(
                        "false",
                        "failure"
                    )
                }

            }

            override fun onFailure(call: Call<AboutData>, t: Throwable) {
                Log.e("TAG", t.toString())
                //                    Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        })

    }

    interface AboutListener {
        fun AboutResponseSuccess(
            status: String,
            message: String?,
            signInList: java.util.ArrayList<AboutDatum>
        )

        fun AboutResponseFailure(status: String, message: String?)

    }
}