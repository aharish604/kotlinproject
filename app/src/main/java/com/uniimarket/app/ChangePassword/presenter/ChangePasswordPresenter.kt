package com.uniimarket.app.ChangePassword.presenter

import android.content.Context
import android.util.Log
import com.uniimarket.app.ChangePassword.model.ChangePasswordData
import com.uniimarket.app.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordPresenter(
    var changePasswordListener: ChangePasswordListener,
    var context: Context?
) {
    fun changePassword(email: String?, pswd: String, newPswd: String, cNewPaswd: String) {


//        var id: String = sharedPref?.getString("id", null)

        val apiService = ApiClient.create()

//        Call<EstimatedProjectsData> call = apiService.getEstimatedProjectList(sharedPreferences.getString("uid", null));
        val call = apiService.changePassword(email,pswd,newPswd,cNewPaswd)

        call?.enqueue(object : Callback<ChangePasswordData> {
            override fun onResponse(call: Call<ChangePasswordData>, response: Response<ChangePasswordData>) {
                try {
                    Log.e("fav status", response.body()!!.getStatus().toString())

                    if (response.body()!!.getStatus().toString().contains("true")) {
                        Log.e("fav mess", response.body()!!.getMessage())
                        changePasswordListener.changePswdResponse("true", response.body()!!.getMessage())

                    } else {
                        changePasswordListener.changePswdResponse("false", response.body()!!.getMessage())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    changePasswordListener.changePswdResponse(
                        "false",
                        "failure"
                    )
                }

            }

            override fun onFailure(call: Call<ChangePasswordData>, t: Throwable) {
                Log.e("TAG", t.toString())
                //                    Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        })


    }


    interface ChangePasswordListener{
        fun changePswdResponse(status: String, message: String?)

    }

}