package com.uniimarket.app.chat.view

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.uniimarket.app.Dashboard.view.DashboardActivity
import com.uniimarket.app.Helper
import com.uniimarket.app.R
import com.uniimarket.app.chat.model.ChatContactsData
import com.uniimarket.app.chat.presenter.ChatContactsPresenter
import com.uniimarket.app.home.view.HomeFragment
import com.uniimarket.app.more.posts.model.AllUsersFriendData
import com.uniimarket.app.more.posts.view.ItemSoldAdapter

class ChatFragment : Fragment(), ChatContactsPresenter.ChatContactsListener,
    ItemSoldAdapter.AdapterOnClick {


    var rv_chat_contacts: RecyclerView? = null
    var tv_chat_contacts: TextView? = null
    var rv_search_contacts: RecyclerView? = null
    var imv_chat_fab: ImageView? = null
    var chatContactsDataList: ArrayList<ChatContactsData.Datum> = ArrayList()
    var chatSearchDataList: ArrayList<AllUsersFriendData.Datum> = ArrayList()
    var chatContactsPresenter: ChatContactsPresenter? = null
    var adapter: ChatContactsAdapter? = null
    var searchAdapter: ItemSoldAdapter? = null
    val handler = Handler()
    var scrolledPosition: Int = 0
    var progressDialog: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater?.inflate(R.layout.chat_fragment, container, false)

        initialVariables(view)

        (context as DashboardActivity).ll_toolbar?.visibility = View.VISIBLE

        (context as DashboardActivity).ll_back?.setOnClickListener {
            //            (context as DashboardActivity).ll_back?.visibility = View.GONE
            Helper.chatStop = false
            (context as DashboardActivity).replaceFragment(
                R.id.frame_layout,
                HomeFragment(), true, false
            )
        }

        (context as DashboardActivity).ll_back?.visibility = View.VISIBLE


        (context as DashboardActivity).imv_search?.visibility = View.GONE
        (context as DashboardActivity).imv_user_search?.visibility = View.VISIBLE

        (context as DashboardActivity).imv_user_search?.setOnClickListener {

            if (((context as DashboardActivity).et_search?.text.toString().trim()) == "") {
                rv_search_contacts?.visibility = View.GONE
                rv_chat_contacts?.visibility = View.VISIBLE
            } else {
//                filter(((context as DashboardActivity).et_search?.text.toString().trim()))
                rv_search_contacts?.visibility = View.VISIBLE
                rv_chat_contacts?.visibility = View.GONE
                progressDialog?.setMessage("Loading...")
                progressDialog?.setCancelable(false)
                progressDialog?.show()
                chatContactsPresenter?.getAllContactsList((context as DashboardActivity).et_search?.text.toString().trim())
            }

            hideKeyboard(view)
        }

        var sharedPref: SharedPreferences? = context?.getSharedPreferences("uniimarket", 0)
        (context as DashboardActivity).imv_profile?.let {
            Glide.with(this)
                .load(sharedPref?.getString("profilepic", null))
                .placeholder(R.drawable.profile).into(it)
        }

        imv_chat_fab?.setOnClickListener {
            //            startActivity(Intent(context, ))
        }

        (context as DashboardActivity).et_search?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // TODO Auto-generated method stub
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {


//                rv_search_contacts?.visibility = View.VISIBLE
//                rv_chat_contacts?.visibility = View.GONE
//                chatContactsPresenter?.getAllContactsList((context as DashboardActivity).et_search?.text.toString().trim())
//                filter(s.toString())
                if (s.toString() == "") {
                    rv_chat_contacts?.visibility = View.VISIBLE
                    rv_search_contacts?.visibility = View.GONE
                }
            }
        })

        Helper.chatStop = true

        getChatContacts()
        callChatHistory()


        return view
    }

    private fun hideKeyboard(view : View ) {

        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    internal fun filter(text: String) {

        val spinnerDataList1 = java.util.ArrayList<ChatContactsData.Datum>()

        for (d in chatContactsDataList) {
            if (d.firstname?.toLowerCase()?.trim()?.contains(text.toLowerCase().trim { it <= ' ' })!!) {
                spinnerDataList1.add(d)
            }
        }

        adapter?.updateList(spinnerDataList1)

    }

    @SuppressLint("WrongConstant")
    private fun getChatContacts() {
//        progressDialog?.setMessage("Loading...")
//        progressDialog?.setCancelable(false)
//        progressDialog?.show()
        chatContactsPresenter?.getChatContactsList()

//        for (i in 0 until 10) {
//            var chatContactsData = ChatContactsData(
//                "Naresh " + i, "I'm new to world",
//                "9:30 AM", "3", R.mipmap.ic_launcher
//            )
//
//            chatContactsDataList.add(chatContactsData)
//        }


    }


    private fun initialVariables(view: View?) {
        imv_chat_fab = view?.findViewById(R.id.imv_chat_fab)
        rv_chat_contacts = view?.findViewById(R.id.rv_chat_contacts)
        tv_chat_contacts = view?.findViewById(R.id.tv_chat_contacts)
        rv_search_contacts = view?.findViewById(R.id.rv_search_contacts)
        chatContactsPresenter = ChatContactsPresenter(this, context)

        progressDialog = ProgressDialog(context)
    }

    @SuppressLint("WrongConstant")
    override fun chatContactsSuccessResponse(
        status: String,
        message: String?,
        chatContactsList: ArrayList<ChatContactsData.Datum>
    ) {
        chatContactsDataList.clear()
        val sharedPref: SharedPreferences =
            context?.getSharedPreferences("uniimarket", Context.MODE_PRIVATE)!!
        var id: String = sharedPref?.getString("id", null)

        for (i in 0 until chatContactsList.size) {
            if ((chatContactsList[i].id)?.equals(id)!!) {

            } else {
                chatContactsDataList.add(chatContactsList[i])
            }
        }
//        chatContactsDataList = chatContactsList
        adapter = ChatContactsAdapter(chatContactsDataList, context)
        rv_chat_contacts?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_chat_contacts?.adapter = adapter
        tv_chat_contacts?.visibility = View.GONE
    }

    override fun chatContactsFailureResponse(status: String, message: String?) {
        rv_chat_contacts?.visibility = View.GONE
//        val dialog = Dialog(activity, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setCancelable(false)
//
//        dialog.setContentView(R.layout.notificaiton_popup)
//        val body = dialog.findViewById(R.id.imv_cancel) as ImageView
////        body.text = title
////        val yesBtn = dialog .findViewById(R.id.yesBtn) as Button
//        val tv_security_text = dialog.findViewById(R.id.tv_security_text) as TextView
//        tv_security_text.text = message
//        body.setOnClickListener {
//            //            notificationPresenter?.getNotificationList()
//            dialog.dismiss()
//        }
//
//        dialog.show()

        tv_chat_contacts?.visibility = View.VISIBLE
    }

    @SuppressLint("WrongConstant")
    override fun allContactsSuccessResponse(
        status: String,
        message: String?,
        chatContactsList: ArrayList<AllUsersFriendData.Datum?>
    ) {
        try {
            progressDialog?.dismiss()
        }catch (e: Exception){
            e.printStackTrace()
        }
        chatSearchDataList.clear()
        val sharedPref: SharedPreferences =
            context?.getSharedPreferences("uniimarket", Context.MODE_PRIVATE)!!
        var id: String = sharedPref?.getString("id", null)

        for (i in 0 until chatContactsList.size) {
//            Log.e("name", chatContactsList[i]?.firstname)
//            if ((chatContactsList[i]?.id)?.equals(id)!!) {
//
//            } else {
            chatContactsList[i]?.let { chatSearchDataList.add(it) }
//            }
        }

        rv_search_contacts?.visibility = View.VISIBLE
        rv_chat_contacts?.visibility = View.GONE
//        if (chatSearchDataList.size > 0) {
//            rv_search_contacts?.visibility = View.VISIBLE
//            rv_chat_contacts?.visibility = View.GONE
////            tv_security_text?.visibility = View.GONE
//        } else {
//            rv_search_contacts?.visibility = View.VISIBLE
//            rv_chat_contacts?.visibility = View.GONE
////            tv_security_text?.visibility = View.VISIBLE
//        }

        searchAdapter = ItemSoldAdapter(chatSearchDataList, context, this)
        rv_search_contacts?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_search_contacts?.adapter = searchAdapter
        tv_chat_contacts?.visibility = View.GONE

    }

    override fun allContactsFailureResponse(status: String, message: String?) {
        try {
            progressDialog?.dismiss()
        }catch (e: Exception){
            e.printStackTrace()
        }
        rv_search_contacts?.visibility = View.GONE
        tv_chat_contacts?.visibility = View.VISIBLE
//        val dialog = Dialog(activity, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setCancelable(false)
//
//        dialog.setContentView(R.layout.notificaiton_popup)
//        val body = dialog.findViewById(R.id.imv_cancel) as ImageView
////        body.text = title
////        val yesBtn = dialog .findViewById(R.id.yesBtn) as Button
//        val tv_security_text = dialog.findViewById(R.id.tv_security_text) as TextView
//        tv_security_text.text = message
//        body.setOnClickListener {
//            //            notificationPresenter?.getNotificationList()
//            dialog.dismiss()
//        }
//
//        dialog.show()
    }

    override fun onClick(item: Int, friendId: String) {
        (context as DashboardActivity).et_search?.setText("")
//        val intent = Intent(context, ChatViewActivity::class.java)
////            intent.putExtra("chatContact",chatContactsData)
//        intent.putExtra("uid", friendId)
//        context?.startActivity(intent)

//        var chatViewFragment: ChatViewFragment = ChatViewFragment(friendId + "")

        Helper.chatStop = false
        (context as DashboardActivity).replaceFragment(
            R.id.frame_layout,
            ChatViewFragment(friendId), false, false
        )


    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("chat destroy", "destroy")
        Helper.chatStop = false
    }

    private fun callChatHistory() {

        if (Helper.chatStop) {

            handler.postDelayed(Runnable {
                getChatContacts()
                callChatHistory()
            }, 2000)
        }
    }

}
