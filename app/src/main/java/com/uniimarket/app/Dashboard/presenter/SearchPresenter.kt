package com.uniimarket.app.Dashboard.presenter

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.uniimarket.app.categories.model.CategoriesTypeData
import com.uniimarket.app.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchPresenter(
    var searchListener: SearchListener,
    var context: Context?
) {

    val sharedPref: SharedPreferences = context?.getSharedPreferences("uniimarket", Context.MODE_PRIVATE)!!

    fun getSearchProductList(productSearch: String) {

        var uid: String = sharedPref?.getString("id", null)
        val apiService = ApiClient.create()

//        Call<EstimatedProjectsData> call = apiService.getEstimatedProjectList(sharedPreferences.getString("uid", null));
        val call = apiService.searchProduct(productSearch, uid)

        call?.enqueue(object : Callback<CategoriesTypeData> {
            override fun onResponse(call: Call<CategoriesTypeData>, response: Response<CategoriesTypeData>) {
                Log.e("fav status", Gson().toJson(response.body()))
                try {
                    Log.e("fav status", response.body()!!.getStatus().toString())

                    if (response.body()!!.getStatus().toString().contains("true")) {
                        Log.e("fav mess", response.body()!!.getMessage())

                        val categoriesList = ArrayList(response.body()!!.getData())
                        Log.e("cate mess", response.body()!!.getMessage())
                        if (categoriesList.size > 0) {
                            Log.e("cat name", categoriesList[0].name)
                            searchListener.searchResponseSuccess(
                                "true",
                                response.body()!!.getMessage(),
                                categoriesList
                            )

                        } else {
                            searchListener.searchResponseFailure("false", response.body()!!.getMessage())
                        }


                    } else {
                        searchListener.searchResponseFailure("false", response.body()!!.getMessage())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    searchListener.searchResponseFailure(
                        "false",
                        "failure"
                    )
                }

            }

            override fun onFailure(call: Call<CategoriesTypeData>, t: Throwable) {
                Log.e("TAG", t.toString())
                //                    Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        })

    }

    interface SearchListener {

        fun searchResponseSuccess(
            status: String,
            message: String?,
            signInList: ArrayList<CategoriesTypeData.Datum>
        )

        fun searchResponseFailure(
            status: String,
            message: String?
        )
    }
}