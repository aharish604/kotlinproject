package com.uniimarket.app.chat.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.uniimarket.app.Dashboard.view.DashboardActivity
import com.uniimarket.app.Helper
import com.uniimarket.app.R
import com.uniimarket.app.SellerProfile.model.SellerProfileData
import com.uniimarket.app.SellerProfile.presenter.SellerProfilePresenter
import com.uniimarket.app.chat.model.ChatViewData
import com.uniimarket.app.chat.model.UserInfoData
import com.uniimarket.app.chat.presenter.ChatViewPresenter

class ChatViewFragment(val profileId: String) : Fragment(), ChatViewPresenter.ChatViewListener,
    SellerProfilePresenter.SellerProfileListener {

    private var count: Int = 0
    var et_text_chat: EditText? = null
    var rv_chat_view: RecyclerView? = null
    var imv_back: ImageView? = null
    var imv_send: ImageView? = null
    var imv_attachment: ImageView? = null
    var cim_chat_profile: ImageView? = null
    var ll_profile: LinearLayout? = null
    var tv_user_name: TextView? = null
    var tv_user_status: TextView? = null
    var chatViewDataList: ArrayList<ChatViewData.Datum> = ArrayList()
    var chatViewPresenter: ChatViewPresenter? = null
    //    var chatContact: ChatContactsData.Datum? = null
    val handler = Handler()
    var scrolledPosition: Int = 0
    var stop: Boolean = true
    var uid: String? = null
    var profilePic: String? = null
    var sellerProfilePresenter: SellerProfilePresenter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater?.inflate(R.layout.activity_chat_view, container, false)

        initialVariables(view)

        imv_back?.setOnClickListener {
            stop = false
//            onBackPressed()
            (context as DashboardActivity).replaceFragment(
                R.id.frame_layout,
                ChatFragment(), false, false
            )
        }

        (context as DashboardActivity).ll_toolbar?.visibility = View.GONE

//        chatContact = intent.getSerializableExtra("chatContact") as? ChatContactsData.Datum

//        uid = getStringExtra("uid")
        uid = profileId + ""
        Log.e("uid", uid + "sdhfsjd")
        getUserInfo(uid)

//        tv_user_name?.text = chatContact?.firstname
//        tv_user_status?.text = chatContact?.email

//        val handler = Handler()
//        handler.postDelayed(Runnable {
//            getChatList(chatContact?.id)
//            handler
//        }, 2000)


        callChatHistory()

//        cim_chat_profile?.let {
//            Glide.with(this).load(chatContact?.profilepic)
//                .centerCrop()
//                //                .fitCenter()
//                .placeholder(com.uniimarket.app.R.drawable.profile)
//                //                .preload(150,150)
//                .into(it)
//        }


        et_text_chat?.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View, event: MotionEvent): Boolean {
                // TODO Auto-generated method stub
                if (view.id === com.uniimarket.app.R.id.et_text_chat) {
                    view.parent.requestDisallowInterceptTouchEvent(true)
                    when (event.action and MotionEvent.ACTION_MASK) {
                        MotionEvent.ACTION_UP -> view.parent.requestDisallowInterceptTouchEvent(
                            false
                        )
                    }
                }
                return false
            }
        })

        imv_send?.setOnClickListener {
            if (et_text_chat?.text?.toString()?.trim()?.isEmpty()!!) {
                Toast.makeText(context, "Please enter text", Toast.LENGTH_LONG).show()
            } else {
                if (count == 0) {
                    count = 1
                    sendText(et_text_chat?.text.toString(), uid)
                }
            }

        }

        ll_profile?.setOnClickListener {
            (context as DashboardActivity).replaceFragment(
                R.id.frame_layout,
                ChatViewProfile(uid + ""), false, false
            )
        }

        cim_chat_profile?.setOnClickListener {
            Helper.productSeller = "chat"
            (context as DashboardActivity).replaceFragment(
                R.id.frame_layout,
                ChatViewProfile(uid + ""), false, false
            )

        }


        getChatList(uid)

        return view

    }


    private fun getUserInfo(uid: String?) {

        chatViewPresenter?.getUserInfo(uid)

    }

    private fun callChatHistory() {

        if (stop) {

            handler.postDelayed(Runnable {
                getChatList(uid)
//            val recylerViewState = rv_chat_view?.layoutManager?.onSaveInstanceState()
                val myLayoutManager = rv_chat_view?.layoutManager
//            val scrollPosition = myLayoutManager.findFirstVisibleItemPosition()
                callChatHistory()


//            rv_chat_view?.layoutManager?.onRestoreInstanceState(recylerViewState)
            }, 2000)
        }
    }

    private fun sendText(text: String, friend_id: String?) {

        chatViewPresenter?.sendText(text, friend_id)
    }

    @SuppressLint("WrongConstant")
    private fun getChatList(friend_id: String?) {


        chatViewPresenter?.getChatHistory(friend_id)

    }

    private fun initialVariables(view: View) {
        imv_send = view?.findViewById(com.uniimarket.app.R.id.imv_send)
        imv_attachment = view?.findViewById(com.uniimarket.app.R.id.imv_attachment)
        rv_chat_view = view?.findViewById(com.uniimarket.app.R.id.rv_chat_view)
        et_text_chat = view?.findViewById(com.uniimarket.app.R.id.et_text_chat)
        imv_back = view?.findViewById(com.uniimarket.app.R.id.imv_back)
        cim_chat_profile = view?.findViewById(com.uniimarket.app.R.id.cim_chat_profile)
        ll_profile = view?.findViewById(R.id.ll_profile)
        tv_user_name = view?.findViewById(com.uniimarket.app.R.id.tv_user_name)
        tv_user_status = view?.findViewById(com.uniimarket.app.R.id.tv_user_status)
        chatViewPresenter = ChatViewPresenter(this, context)
        sellerProfilePresenter = SellerProfilePresenter(this, context)
    }

    @SuppressLint("WrongConstant")
    override fun chatViewSuccessResponse(
        status: String,
        message: String?,
        chatContactsList: ArrayList<ChatViewData.Datum>
    ) {

        if (chatContactsList.size > 0) {
            if (chatViewDataList.size == chatContactsList.size) {


            } else {
                chatViewDataList = chatContactsList
                chatViewDataList.reverse()
                var adapter: ChatViewAdapter =
                    ChatViewAdapter(chatViewDataList, context, profilePic)
                var layoutManager: LinearLayoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
                rv_chat_view?.layoutManager = layoutManager
                rv_chat_view?.adapter = adapter
                adapter.notifyDataSetChanged()

            }
        } else {

            sellerProfilePresenter?.notificationRequestList(uid)
        }
    }

    override fun chatViewFailureResponse(status: String, message: String?) {
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun chatSendResponse(status: String, message: String?) {
        if (status == "true") {
            et_text_chat?.setText("")
            chatViewPresenter?.getChatHistory(uid)
            count = 0
        } else {
            Toast.makeText(context, "Please try again", Toast.LENGTH_LONG).show()
        }

    }

    override fun chatSendFailureResponse(status: String, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        count = 0;

    }

    override fun getUserInfoSuccess(
        status: String,
        message: String?,
        userInfoData: UserInfoData.Data?
    ) {

        Log.e("uid", userInfoData?.id)
        Log.e("f name", userInfoData?.firstname)
        Log.e("l name", userInfoData?.lastname)
        Log.e("pro pic", userInfoData?.profilepic)

        tv_user_name?.text = "${userInfoData?.firstname} ${userInfoData?.lastname}"
        tv_user_status?.text = userInfoData?.email

        profilePic = userInfoData?.profilepic.toString()

        cim_chat_profile?.let {
            Glide.with(this).load(userInfoData?.profilepic)
                .centerCrop()
                .placeholder(com.uniimarket.app.R.drawable.profile)
                .into(it)
        }

    }

    override fun getUserInfoFailure(status: String, message: String?) {

    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//        stop = false
//    }

    override fun onDestroy() {
        super.onDestroy()
        stop = false
    }

    override fun sellProfileResponseSuccess(
        status: Boolean,
        message: String?,
        subCategoryDataList: SellerProfileData.Records?,
        productList: ArrayList<SellerProfileData.Datum>?
    ) {


    }

    override fun sellProfileResponseFailure(status: Boolean, message: String?) {

    }

    override fun sellNotificationResponseFailure(status: Boolean, message: String?) {

    }

    override fun sellNotificationResponseSuccess(status: Boolean, data: String?) {

    }

    override fun sellUpdateNotificationResponse(status: Boolean, message: String?, data: String?) {

    }

    override fun notificationRequestResponse(status: Boolean, message: String?, data: String?) {

    }

    override fun notificationRequestResponseFailure(status: Boolean, message: String?) {

    }

}