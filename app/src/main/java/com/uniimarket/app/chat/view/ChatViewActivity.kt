package com.uniimarket.app.chat.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.uniimarket.app.SellerProfile.model.SellerProfileData
import com.uniimarket.app.SellerProfile.presenter.SellerProfilePresenter
import com.uniimarket.app.chat.model.ChatViewData
import com.uniimarket.app.chat.model.UserInfoData
import com.uniimarket.app.chat.presenter.ChatViewPresenter
import de.hdodenhof.circleimageview.CircleImageView


class ChatViewActivity : AppCompatActivity(), ChatViewPresenter.ChatViewListener,
    SellerProfilePresenter.SellerProfileListener {


    var et_text_chat: EditText? = null
    var rv_chat_view: RecyclerView? = null
    var imv_back: ImageView? = null
    var imv_send: ImageView? = null
    var imv_attachment: ImageView? = null
    var cim_chat_profile: CircleImageView? = null
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

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(com.uniimarket.app.R.layout.activity_chat_view)

        initialVariables()


        imv_back?.setOnClickListener {
            onBackPressed()
        }

//        chatContact = intent.getSerializableExtra("chatContact") as? ChatContactsData.Datum

        uid = intent.getStringExtra("uid")

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

            } else {
                sendText(et_text_chat?.text.toString(), uid)
            }

        }

        cim_chat_profile?.setOnClickListener {



        }


        getChatList(uid)

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

    private fun initialVariables() {
        imv_send = findViewById(com.uniimarket.app.R.id.imv_send)
        imv_attachment = findViewById(com.uniimarket.app.R.id.imv_attachment)
        rv_chat_view = findViewById(com.uniimarket.app.R.id.rv_chat_view)
        et_text_chat = findViewById(com.uniimarket.app.R.id.et_text_chat)
        imv_back = findViewById(com.uniimarket.app.R.id.imv_back)
        cim_chat_profile = findViewById(com.uniimarket.app.R.id.cim_chat_profile)
        tv_user_name = findViewById(com.uniimarket.app.R.id.tv_user_name)
        tv_user_status = findViewById(com.uniimarket.app.R.id.tv_user_status)
        chatViewPresenter = ChatViewPresenter(this, this)
        sellerProfilePresenter = SellerProfilePresenter(this, this)
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
                var adapter: ChatViewAdapter = ChatViewAdapter(chatViewDataList, this, profilePic)
                var layoutManager: LinearLayoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
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
        } else {
            Toast.makeText(this, "Please try again", Toast.LENGTH_LONG).show()
        }

    }

    override fun chatSendFailureResponse(status: String, message: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    override fun onBackPressed() {
        super.onBackPressed()
        stop = false
    }

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
