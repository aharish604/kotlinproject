package com.uniimarket.app.sell.presenter

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.google.gson.Gson
import com.uniimarket.app.network.ApiClient
import com.uniimarket.app.sell.model.ProductSellUpdate
import com.uniimarket.app.sell.model.SellData
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class SellPresenter(var sellListener: SellListener, var context: Context?) {

    val sharedPref: SharedPreferences = context?.getSharedPreferences("uniimarket", Context.MODE_PRIVATE)!!
    val apiService = ApiClient.create()

    fun updateSellItem(
        sellerName: String,
        uniiMail: String,
        date: String,
        location: String,
        productName: String,
        price: String,
        description: String,
        catId: String?,
        subCategoryId: String?,
        productType: String?,
        setPrice: String?,
        file: File?,
        filePathUri: Uri?,
        lat: Double?,
        lon: Double?
    ) {


        var fileToUpload: MultipartBody.Part? = null
        try {
//            val file = File(filepath)
            val mFile = RequestBody.create(MediaType.parse("image/*"), file)
            fileToUpload = MultipartBody.Part.createFormData("product_pics", file?.name, mFile)
        } catch (e: Exception) {
            e.printStackTrace()
        }


//        val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file)
        val requestFile = RequestBody.create(MediaType.parse(context?.contentResolver?.getType(filePathUri)), file)
        val sellerName = RequestBody.create(MediaType.parse("text/plain"), sellerName)
        val uniiMail = RequestBody.create(MediaType.parse("text/plain"), uniiMail)
        val date = RequestBody.create(MediaType.parse("text/plain"), date)
        val latit = RequestBody.create(MediaType.parse("text/plain"), "" + lat)
        val longi = RequestBody.create(MediaType.parse("text/plain"), "" + lon)
        val productName = RequestBody.create(MediaType.parse("text/plain"), productName)
        val price = RequestBody.create(MediaType.parse("text/plain"), price)
        val description = RequestBody.create(MediaType.parse("text/plain"), description)
        val catId = RequestBody.create(MediaType.parse("text/plain"), catId)
        val subCategoryId = RequestBody.create(MediaType.parse("text/plain"), subCategoryId)
        val productTypeRB = RequestBody.create(MediaType.parse("text/plain"), productType)
        val setPrice = RequestBody.create(MediaType.parse("text/plain"), setPrice)
        val location = RequestBody.create(MediaType.parse("text/plain"), location)
//        val productType = RequestBody.create(MediaType.parse("text/plain"), "")

        val body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile)
        var id: String = sharedPref?.getString("id", null)
        Log.e("uid", id)
        val uid = RequestBody.create(MediaType.parse("text/plain"), id)
        Log.e("product type", productType)

        val call = apiService.updateSell(
            sellerName, uniiMail, date, latit, longi, productName, price,
            description, catId, subCategoryId, productTypeRB, setPrice, uid, location, fileToUpload
        )

        call.enqueue(object : Callback<SellData> {

            override fun onResponse(call: Call<SellData>, response: Response<SellData>) {
                try {
                    Log.e("sell response ", Gson().toJson(response.body()) + "")

                    if (response.body()!!.getStatus().toString().contains("true")) {
                        Log.e("sell mess ", response.body()!!.getMessage())
                        response.body()!!.getMessage()?.let { sellListener.sellResponse("true", it) }

                    } else {
                        response.body()!!.getMessage()?.let { sellListener.sellResponse("false", it) }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    sellListener.sellResponse(
                        "false", "failure"
                    )
                }

            }

            override fun onFailure(call: Call<SellData>, t: Throwable) {
                Log.e("TAG", t.toString())
                sellListener.sellResponse(
                    "false", "failure"
                )
            }
        })

    }

    fun updateProductWithoutImage(
        productStatus: String,
        productName: String,
        productType: String?,
        categoryId: String?,
        subCategoryId: String?,
        description: String,
        price: String,
        setPrice: String?,
        productId: String?,
        date: String,
        pointLatitude: Double,
        pointLongitude: Double,
        location: String
    ) {
        var uid: String = sharedPref?.getString("id", null)

        val call = apiService.updateProductSell(
            productStatus, productName, productType, categoryId, subCategoryId, description, price, setPrice,
            productId, date, pointLatitude, pointLongitude, uid, location
        )

        call.enqueue(object : Callback<ProductSellUpdate> {

            override fun onResponse(call: Call<ProductSellUpdate>, response: Response<ProductSellUpdate>) {
                try {
                    Log.e("sell response ", Gson().toJson(response.body()) + "")

                    if (response.body()!!.getStatus().toString().contains("true")) {
                        Log.e("sell mess ", response.body()!!.getMessage())
                        response.body()!!.getMessage()?.let { sellListener.sellResponse("true", it) }

                    } else {
                        response.body()!!.getMessage()?.let { sellListener.sellResponse("false", it) }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    sellListener.sellResponse(
                        "false", "failure"
                    )
                }

            }

            override fun onFailure(call: Call<ProductSellUpdate>, t: Throwable) {
                Log.e("TAG", t.toString())
                sellListener.sellResponse(
                    "false", "failure"
                )
            }
        })

    }

    fun updateProductWithImage(
        productStatus: String,
        productName: String,
        productType: String?,
        categoryId: String?,
        subCategoryId: String?,
        description: String,
        price: String,
        setPrice: String?,
        productId: String?,
        date: String,
        pointLatitude: Double,
        pointLongitude: Double,
        file: File?,
        filePathUri: Uri?,
        location: String
    ) {


//        val apiService = ApiClient.create()
//
        var fileToUpload: MultipartBody.Part? = null
        try {
//            val file = File(filepath)
            val mFile = RequestBody.create(MediaType.parse("image/*"), file)
            fileToUpload = MultipartBody.Part.createFormData("product_pics", file?.name, mFile)
        } catch (e: Exception) {
            e.printStackTrace()
        }


//        val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file)
        val requestFile = RequestBody.create(MediaType.parse(context?.contentResolver?.getType(filePathUri)), file)
        val productStatus = RequestBody.create(MediaType.parse("text/plain"), productStatus)
        val date = RequestBody.create(MediaType.parse("text/plain"), date)
        val latit = RequestBody.create(MediaType.parse("text/plain"), "" + pointLatitude)
        val longi = RequestBody.create(MediaType.parse("text/plain"), "" + pointLongitude)
        val productName = RequestBody.create(MediaType.parse("text/plain"), productName)
        val price = RequestBody.create(MediaType.parse("text/plain"), price)
        val description = RequestBody.create(MediaType.parse("text/plain"), description)
        val catId = RequestBody.create(MediaType.parse("text/plain"), categoryId)
        val subCategoryId = RequestBody.create(MediaType.parse("text/plain"), subCategoryId)
        val productTypeRB = RequestBody.create(MediaType.parse("text/plain"), productType)
        val setPrice = RequestBody.create(MediaType.parse("text/plain"), setPrice)
        val productId = RequestBody.create(MediaType.parse("text/plain"), productId)
        val location = RequestBody.create(MediaType.parse("text/plain"), location)
//        val productType = RequestBody.create(MediaType.parse("text/plain"), "")

        var id: String = sharedPref?.getString("id", null)
        Log.e("uid", id)
        val uid = RequestBody.create(MediaType.parse("text/plain"), id)
        Log.e("product type", productType)

        val call = apiService.updateProductSellWithImage(
            productStatus, productName, productTypeRB, catId, subCategoryId, description, price, setPrice,
            productId, date, latit, longi, uid,fileToUpload,location
        )

        call.enqueue(object : Callback<ProductSellUpdate> {

            override fun onResponse(call: Call<ProductSellUpdate>, response: Response<ProductSellUpdate>) {
                try {
                    Log.e("sell response ", Gson().toJson(response.body()) + "")

                    if (response.body()!!.getStatus().toString().contains("true")) {
                        Log.e("sell mess ", response.body()!!.getMessage())
                        response.body()!!.getMessage()?.let { sellListener.sellResponse("true", it) }

                    } else {
                        response.body()!!.getMessage()?.let { sellListener.sellResponse("false", it) }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    sellListener.sellResponse(
                        "false", "failure"
                    )
                }

            }

            override fun onFailure(call: Call<ProductSellUpdate>, t: Throwable) {
                Log.e("TAG", t.toString())
                sellListener.sellResponse(
                    "false", "failure"
                )
            }
        })
    }

    interface SellListener {
        fun sellResponse(status: String, message: String)
    }
}