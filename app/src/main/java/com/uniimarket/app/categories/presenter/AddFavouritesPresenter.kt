package com.uniimarket.app.categories.presenter

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.uniimarket.app.categories.model.AddFavouritesData
import com.uniimarket.app.more.favourites.model.CheckFavourites
import com.uniimarket.app.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddFavouritesPresenter(
    var addFavouritesListener: AddFavouritesListener,
    var context: Context?
) {

    val sharedPref: SharedPreferences = context?.getSharedPreferences("uniimarket", Context.MODE_PRIVATE)!!

    fun addFavourites(pid: String?) {

        var id: String = sharedPref?.getString("id", null)

        val apiService = ApiClient.create()

//        Call<EstimatedProjectsData> call = apiService.getEstimatedProjectList(sharedPreferences.getString("uid", null));


        val call = apiService.addFavourites(pid, id.toString())

        call?.enqueue(object : Callback<AddFavouritesData> {
            override fun onResponse(call: Call<AddFavouritesData>, response: Response<AddFavouritesData>) {
                try {
                    Log.e("fav status", response.body()!!.getStatus().toString())

                    if (response.body()!!.getStatus().toString().contains("true")) {
                        Log.e("fav mess", response.body()!!.getMessage())
                        addFavouritesListener.addFavouritesResponse("true", response.body()!!.getData())

                    } else {
                        addFavouritesListener.addFavouritesResponse("false", response.body()!!.getData())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    addFavouritesListener.addFavouritesResponse(
                        "false",
                        "failure"
                    )
                }
            }

            override fun onFailure(call: Call<AddFavouritesData>, t: Throwable) {
                Log.e("TAG", t.toString())
                //                    Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        })
    }

    fun checkFavourites(pid: String?) {

        var id: String = sharedPref?.getString("id", null)

        val apiService = ApiClient.create()

//        Call<EstimatedProjectsData> call = apiService.getEstimatedProjectList(sharedPreferences.getString("uid", null));


        val call = apiService.checkFavourites(pid, id.toString())

        Log.e("pid", pid)

        call?.enqueue(object : Callback<CheckFavourites> {
            override fun onResponse(call: Call<CheckFavourites>, response: Response<CheckFavourites>) {
                try {
                    Log.e("fav status", Gson().toJson(response.body()))

                    if (response.body()!!.getStatus().toString().contains("true")) {
                        Log.e("fav mess", response.body()!!.getMessage())
                        addFavouritesListener.checkFavouritesResponse("true", response.body()!!.getMessage(), response.body()!!.getData())

                    } else {
                        addFavouritesListener.checkFavouritesResponse("false", response.body()!!.getMessage(),response.body()!!.getData())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    addFavouritesListener.checkFavouritesResponse(
                        "false",
                        "failure",
                        response.body()!!.getData()
                    )
                }
            }

            override fun onFailure(call: Call<CheckFavourites>, t: Throwable) {
                Log.e("TAG", t.toString())
                //                    Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        })

    }


    interface AddFavouritesListener {
        fun addFavouritesResponse(
            status: String,
            message: String?
        )

        fun checkFavouritesResponse(
            status: String,
            message: String?,
            data: String?
        )

//        fun AddFavouritesResponseFailure(status: String, message: String?)

    }
}