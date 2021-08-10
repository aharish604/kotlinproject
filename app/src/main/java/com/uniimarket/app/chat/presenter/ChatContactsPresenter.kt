package com.uniimarket.app.chat.presenter

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.uniimarket.app.chat.model.ChatContactsData
import com.uniimarket.app.more.posts.model.AllUsersFriendData
import com.uniimarket.app.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatContactsPresenter(
    var chatContactsListener: ChatContactsListener,
    var context: Context?
) {

    val sharedPref: SharedPreferences = context?.getSharedPreferences("uniimarket", Context.MODE_PRIVATE)!!

    fun getChatContactsList() {

        var id: String = sharedPref?.getString("id", null)

        val apiService = ApiClient.create()

//        Call<EstimatedProjectsData> call = apiService.getEstimatedProjectList(sharedPreferences.getString("uid", null));
        val call = apiService.getChatContactsList(id)

        call?.enqueue(object : Callback<ChatContactsData> {
            override fun onResponse(call: Call<ChatContactsData>, response: Response<ChatContactsData>) {
                try {
                    Log.e("chat cont list", Gson().toJson(response.body()))

                    if (response.body()!!.getStatus().toString().contains("true")) {
                     val chatContactsList = ArrayList(response.body()!!.getData())
//                        Log.e("sub cate mess", response.body()!!.getMessage())
                        if (chatContactsList.size > 0) {
                            Log.e("sub cate name", chatContactsList[0].firstname)
                            chatContactsListener.chatContactsSuccessResponse(
                                "true",
                                "Message",
                                chatContactsList
                            )
                        } else {
                            chatContactsListener.chatContactsFailureResponse("true", "Message failure")
                        }

                    } else {
                        chatContactsListener.chatContactsFailureResponse("false", "Message failure")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    chatContactsListener.chatContactsFailureResponse(
                        "false",
                        "failure"
                    )
                }

            }

            override fun onFailure(call: Call<ChatContactsData>, t: Throwable) {
                Log.e("TAG", t.toString())
                //                    Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        })

    }

    fun getAllContactsList(search :String) {

//        AllUsersFriendData

        var id: String = sharedPref?.getString("id", null)

        val apiService = ApiClient.create()

//        Call<EstimatedProjectsData> call = apiService.getEstimatedProjectList(sharedPreferences.getString("uid", null));
        val call = apiService.getAllContactsList(search,id)

        call?.enqueue(object : Callback<AllUsersFriendData> {
            override fun onResponse(call: Call<AllUsersFriendData>, response: Response<AllUsersFriendData>) {
                try {
                    Log.e("chat cont list", Gson().toJson(response.body()))

                    if (response.body()!!.getStatus().toString().contains("true")) {
                        val chatContactsList = ArrayList(response.body()!!.getData())
//                        Log.e("sub cate mess", response.body()!!.getMessage())
                        if (chatContactsList.size > 0) {
                            Log.e("sub cate name", chatContactsList[0]?.firstname)
                            chatContactsListener.allContactsSuccessResponse(
                                "true",
                                "Message",
                                chatContactsList
                            )
                        } else {
                            chatContactsListener.allContactsFailureResponse("true", "Message failure")
                        }

                    } else {
                        chatContactsListener.allContactsFailureResponse("false", "Message failure")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    chatContactsListener.allContactsFailureResponse(
                        "false",
                        "failure"
                    )
                }

            }

            override fun onFailure(call: Call<AllUsersFriendData>, t: Throwable) {
                Log.e("TAG", t.toString())
                //                    Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        })

    }


    interface ChatContactsListener {
        fun chatContactsSuccessResponse(
            status: String,
            message: String?,
            chatContactsList: ArrayList<ChatContactsData.Datum>
        )
        fun chatContactsFailureResponse(status: String, message: String?)

        fun allContactsSuccessResponse(
            status: String,
            message: String?,
            chatContactsList: ArrayList<AllUsersFriendData.Datum?>
        )
        fun allContactsFailureResponse(status: String, message: String?)

    }

}