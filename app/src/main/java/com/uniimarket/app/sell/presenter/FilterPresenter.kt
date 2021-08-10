package com.uniimarket.app.sell.presenter

import android.content.Context
import android.util.Log
import com.uniimarket.app.network.ApiClient
import com.uniimarket.app.sell.model.FilterData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FilterPresenter(var filterListener: FilterListener, var context: Context?) {
    fun getFilter(subCategoryId: String?) {
        val apiService = ApiClient.create()

//        Call<EstimatedProjectsData> call = apiService.getEstimatedProjectList(sharedPreferences.getString("uid", null));
        val call = apiService.getFilters(subCategoryId.toString())

        call?.enqueue(object : Callback<FilterData> {
            override fun onResponse(call: Call<FilterData>, response: Response<FilterData>) {
                try {
                    Log.e("cancel project ", response.body()!!.getStatus().toString())

                    if (response.body()!!.getStatus().toString().contains("true")) {
                        val categoriesList = ArrayList(response.body()!!.getData())
                        Log.e("sub cate mess", response.body()!!.getMessage())
                        if (categoriesList.size > 0) {
                            Log.e("filter name", categoriesList[0].itemName)
                            filterListener.filterResponseSuccess(
                                "true",
                                response.body()!!.getMessage(),
                                categoriesList
                            )
                        } else {
                            filterListener.filterResponseFailure("true", response.body()!!.getMessage())
                        }
                    } else {
                        filterListener.filterResponseFailure("true", response.body()!!.getMessage())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    filterListener.filterResponseFailure(
                        "false",
                        "failure"
                    )
                }

            }

            override fun onFailure(call: Call<FilterData>, t: Throwable) {
                Log.e("TAG", t.toString())
                //                    Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        })

    }


    interface FilterListener {
        fun filterResponseSuccess(
            status: String,
            message: String?,
            filterDataList: java.util.ArrayList<FilterData.Datum>
        )

        fun filterResponseFailure(status: String, message: String?)
    }
}