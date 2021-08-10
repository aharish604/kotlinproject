package com.uniimarket.app.Forgotpassword.presenter

import android.util.Log
import com.uniimarket.app.Forgotpassword.model.ForgotPasswordData
import com.uniimarket.app.Forgotpassword.view.ForgotPasswordActivity
import com.uniimarket.app.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPswdPresenter(var forgotPasswordListener: ForgotPasswordListener,
                          var forgotPasswordActivity: ForgotPasswordActivity
) {
    fun forgotPassword(
        email: String,
        pswd: String,
        cnfPswd: String,
        vCode: String
    ) {

        val apiService = ApiClient.create()

        val call = apiService.getForgotPswdResponse(email,pswd,cnfPswd,vCode)

        call.enqueue(object : Callback<ForgotPasswordData> {
            override fun onResponse(call: Call<ForgotPasswordData>, response: Response<ForgotPasswordData>) {
                try {
                    Log.e("cancel project ", response.body()!!.getStatus().toString())

                    if (response.body()!!.getStatus().toString().contains("true")) {
                        Log.e("cancel project ", response.body()!!.getMessage())

                        forgotPasswordListener.forgotPswdResponseSuccess(
                            "true",
                            response.body()!!.getMessage())
                    } else {
                        forgotPasswordListener.forgotPswdResponseSuccess("false", response.body()!!.getMessage())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    forgotPasswordListener.forgotPswdResponseSuccess(
                        "false",
                        "failure"
                    )
                }

            }

            override fun onFailure(call: Call<ForgotPasswordData>, t: Throwable) {
                Log.e("TAG", t.toString())
                //                    Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        })

    }


    interface ForgotPasswordListener {
         fun forgotPswdResponseSuccess(status: String, message: String?)
//         fun forgotPswdResponseFailure(status: String, message: String?)
    }
}