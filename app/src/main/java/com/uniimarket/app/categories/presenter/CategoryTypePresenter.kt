package com.uniimarket.app.categories.presenter

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.uniimarket.app.categories.model.CategoriesTypeData
import com.uniimarket.app.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryTypePresenter(var categoryTypeListener: CategoryTypeListener, var context: Context?) {

    val sharedPref: SharedPreferences = context?.getSharedPreferences("uniimarket", Context.MODE_PRIVATE)!!

    fun getSortingCategories(
        categoryId: String?,
        subCategoryId: String?,
        latitude: Double,
        longitude: Double,
        sortingValue: String?,
        distance: String?
    ) {
        var uid: String = sharedPref?.getString("id", null)

        val apiService = ApiClient.create()

        val call =
            apiService.getSortingCategories(sortingValue, categoryId, subCategoryId, latitude, longitude, distance, uid)

        call?.enqueue(object : Callback<CategoriesTypeData> {
            override fun onResponse(call: Call<CategoriesTypeData>, response: Response<CategoriesTypeData>) {
                try {
                    Log.e("filter project ", response.body()!!.getStatus().toString())

                    if (response.body()!!.getStatus().toString().contains("true")) {
                        val categoriesList = ArrayList(response.body()!!.getData())
                        Log.e("sub cate mess", response.body()!!.getMessage())
                        if (categoriesList.size > 0) {
                            Log.e("sub cate name", categoriesList[0].name)
                            categoryTypeListener.categoryTypeResponseSuccess(
                                "true",
                                response.body()!!.getMessage(),
                                categoriesList
                            )
                        } else {
                            categoryTypeListener.categoryTypeResponseFailure("true", response.body()!!.getMessage())
                        }
                    } else {
                        categoryTypeListener.categoryTypeResponseFailure("true", response.body()!!.getMessage())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    categoryTypeListener.categoryTypeResponseFailure(
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

    fun getFilterCategories(id: String?, subCategoryId: String?, filterId: String?) {

        Log.e("cat id " + id, "scid " + subCategoryId + "--- fid " + filterId)
        var uid: String = sharedPref?.getString("id", null)
        val apiService = ApiClient.create()

        val call =
            apiService.getFilterProducts(id.toString(), subCategoryId.toString(), filterId.toString(), uid)

        call?.enqueue(object : Callback<CategoriesTypeData> {
            override fun onResponse(call: Call<CategoriesTypeData>, response: Response<CategoriesTypeData>) {
                try {
                    Log.e("filter project ", Gson().toJson(response.body()))

                    if (response.body()!!.getStatus().toString().contains("true")) {
                        val categoriesList = ArrayList(response.body()!!.getData())
                        Log.e("sub cate mess", response.body()!!.getMessage())
                        if (categoriesList.size > 0) {
                            Log.e("sub cate name", categoriesList[0].name)
                            categoryTypeListener.filterTypeResponseSuccess(
                                "true",
                                response.body()!!.getMessage(),
                                categoriesList
                            )
                        } else {
                            categoryTypeListener.filterTypeResponseFailure("true", response.body()!!.getMessage())
                        }
                    } else {
                        categoryTypeListener.filterTypeResponseFailure("true", response.body()!!.getMessage())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    categoryTypeListener.filterTypeResponseFailure(
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

    fun getNewPostFilter(filterId: String) {

//        Log.e("cat id " + id, "scid " + subCategoryId + "--- fid " + filterId)
        var uid: String = sharedPref?.getString("id", null)
        val apiService = ApiClient.create()

        val call =
            apiService.getNewPostFilterProducts( filterId.toString())

        call?.enqueue(object : Callback<CategoriesTypeData> {
            override fun onResponse(call: Call<CategoriesTypeData>, response: Response<CategoriesTypeData>) {
                try {
                    Log.e("filter project ", Gson().toJson(response.body()))

                    if (response.body()!!.getStatus().toString().contains("true")) {
                        val categoriesList = ArrayList(response.body()!!.getData())
                        Log.e("sub cate mess", response.body()!!.getMessage())
                        if (categoriesList.size > 0) {
                            Log.e("sub cate name", categoriesList[0]?.name)
                            categoryTypeListener.newPostFilterTypeResponseSuccess(
                                "true",
                                response.body()!!.getMessage(),
                                categoriesList
                            )
                        } else {
                            categoryTypeListener.filterTypeResponseFailure("true", response.body()!!.getMessage())
                        }
                    } else {
                        categoryTypeListener.filterTypeResponseFailure("true", response.body()!!.getMessage())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    categoryTypeListener.filterTypeResponseFailure(
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


    interface CategoryTypeListener {
        fun categoryTypeResponseSuccess(
            status: String,
            message: String?,
            categoryTypeDataList: java.util.ArrayList<CategoriesTypeData.Datum>
        )

        fun categoryTypeResponseFailure(status: String, message: String?)

        fun filterTypeResponseSuccess(
            status: String,
            message: String?,
            categoryTypeDataList: java.util.ArrayList<CategoriesTypeData.Datum>
        )

        fun filterTypeResponseFailure(status: String, message: String?)
        fun newPostFilterTypeResponseSuccess(
            s: String,
            message: String?,
            categoriesList: java.util.ArrayList<CategoriesTypeData.Datum>
        )
    }
}