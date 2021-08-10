package com.uniimarket.app.Dashboard.presenter

import android.util.Log
import com.uniimarket.app.Dashboard.model.ComingSoonData
import com.uniimarket.app.Dashboard.model.ComingSoonDatum
import com.uniimarket.app.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ComingSoonPresenter  (var screenListener: ComingSoonListener){

    fun getComingSoonData() {

        val apiService = ApiClient.create()

//        Call<EstimatedProjectsData> call = apiService.getEstimatedProjectList(sharedPreferences.getString("uid", null));
        val call = apiService.getComingSoon()

        call.enqueue(object : Callback<ComingSoonData> {
            override fun onResponse(call: Call<ComingSoonData>, response: Response<ComingSoonData>) {
                try {

                    if (response.body()!!.getStatus().toString().contains("true")) {
                        val categoriesList = ArrayList(response.body()!!.getData())

                        screenListener.ComingSoonResponseSuccess(
                            "true",
                            response.body()!!.getMessage(),
                            categoriesList)

                    } else {
                        screenListener.ComingSoonResponseFailure("true", response.body()!!.getMessage())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    screenListener.ComingSoonResponseFailure(
                        "false",
                        "failure"
                    )
                }

            }

            override fun onFailure(call: Call<ComingSoonData>, t: Throwable) {
                Log.e("TAG", t.toString())
                //                    Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        })

    }

    interface ComingSoonListener {
        fun ComingSoonResponseSuccess(
            status: String,
            message: String?,
            signInList: java.util.ArrayList<ComingSoonDatum>
        )

        fun ComingSoonResponseFailure(status: String, message: String?)

    }
}