package com.uniimarket.app.chat.presenter

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.uniimarket.app.chat.model.ChatSendData
import com.uniimarket.app.chat.model.ChatViewData
import com.uniimarket.app.chat.model.UserInfoData
import com.uniimarket.app.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatViewPresenter(
    var chatViewListener: ChatViewListener,
    var context: Context?
) {

    val sharedPref: SharedPreferences =
        context?.getSharedPreferences("uniimarket", Context.MODE_PRIVATE)!!

    fun getChatHistory(friendId: String?) {
        try {

            var id: String = sharedPref?.getString("id", null)

            val apiService = ApiClient.create()

//        Call<EstimatedProjectsData> call = apiService.getEstimatedProjectList(sharedPreferences.getString("uid", null));
            val call = apiService.getChatHistoryList(id, friendId)

            Log.e("frd id $friendId", "user id $id")

            call?.enqueue(object : Callback<ChatViewData> {
                override fun onResponse(
                    call: Call<ChatViewData>,
                    response: Response<ChatViewData>
                ) {
                    try {
                        Log.e("chat cont list", Gson().toJson(response.body()))

                        if (response.body()!!.getStatus().toString().contains("true")) {
                            val chatContactsList = ArrayList(response.body()!!.getData())
//                        Log.e("sub cate mess", response.body()!!.getMessage())
                            if (chatContactsList.size > 0) {
                                Log.e("chat test", chatContactsList[0].description)
                                chatViewListener.chatViewSuccessResponse(
                                    "true",
                                    response.body()!!.getMessage(),
                                    chatContactsList
                                )
                            } else {
                                chatViewListener.chatViewFailureResponse(
                                    "true",
                                    response.body()!!.getMessage()
                                )
                            }
                        } else {
                            chatViewListener.chatViewFailureResponse(
                                "false",
                                response.body()!!.getMessage()
                            )
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        chatViewListener.chatViewFailureResponse(
                            "false",
                            "failure"
                        )
                    }
                }

                override fun onFailure(call: Call<ChatViewData>, t: Throwable) {
                    Log.e("TAG", t.toString())
                    //                    Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun sendText(text: String, friendId: String?) {

        var id: String = sharedPref?.getString("id", null)

        val apiService = ApiClient.create()

//        Call<EstimatedProjectsData> call = apiService.getEstimatedProjectList(sharedPreferences.getString("uid", null));
        val call = apiService.sendText(id, friendId, text)

        Log.e("frd id $friendId", "user id $id")

        call?.enqueue(object : Callback<ChatSendData> {
            override fun onResponse(call: Call<ChatSendData>, response: Response<ChatSendData>) {
                try {
                    Log.e("chat send res", Gson().toJson(response.body()))

                    if (response.body()!!.getStatus().toString().contains("true")) {

                        chatViewListener.chatSendResponse(
                            "true",
                            response.body()!!.getMessage()
                        )
                    } else {
                        chatViewListener.chatSendFailureResponse(
                            "false",
                            response.body()!!.getMessage()
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    chatViewListener.chatSendFailureResponse(
                        "false",
                        "failure"
                    )
                }
            }

            override fun onFailure(call: Call<ChatSendData>, t: Throwable) {
                Log.e("TAG", t.toString())
                chatViewListener.chatViewFailureResponse(
                    "false",
                    "failure"
                )
                //                    Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        })

    }

    fun getUserInfo(uid: String?) {

        var id: String = sharedPref?.getString("id", null)

        val apiService = ApiClient.create()

//        Call<EstimatedProjectsData> call = apiService.getEstimatedProjectList(sharedPreferences.getString("uid", null));
        val call = apiService.getUserInfo(uid)

//        Log.e("frd id $friendId", "user id $id")

        call?.enqueue(object : Callback<UserInfoData> {
            override fun onResponse(call: Call<UserInfoData>, response: Response<UserInfoData>) {
                try {
                    Log.e("chat cont list", Gson().toJson(response.body()))

                    if (response.body()!!.getStatus().toString().contains("true")) {
                        val userInfoData = response.body()!!.getData()
//                        Log.e("sub cate mess", response.body()!!.getMessage())

                        chatViewListener.getUserInfoSuccess(
                            "true",
                            response.body()!!.getMessage(),
                            userInfoData
                        )

                    } else {
                        chatViewListener.getUserInfoFailure("false", response.body()!!.getMessage())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    chatViewListener.getUserInfoFailure(
                        "false",
                        "failure"
                    )
                }
            }

            override fun onFailure(call: Call<UserInfoData>, t: Throwable) {
                Log.e("TAG", t.toString())
                //                    Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        })

    }


    interface ChatViewListener {
        fun chatViewSuccessResponse(
            status: String,
            message: String?,
            chatContactsList: ArrayList<ChatViewData.Datum>
        )

        fun chatViewFailureResponse(status: String, message: String?)

        fun chatSendResponse(status: String, message: String?)
        fun chatSendFailureResponse(status: String, message: String?)
        fun getUserInfoSuccess(status: String, message: String?, userInfoData: UserInfoData.Data?)

        fun getUserInfoFailure(status: String, message: String?)
    }
}