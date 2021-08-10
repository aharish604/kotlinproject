package com.uniimarket.app.more.settings.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uniimarket.app.Dashboard.view.DashboardActivity
import com.uniimarket.app.Helper
import com.uniimarket.app.R
import com.uniimarket.app.SellerProfile.view.SellerUserFragment
import com.uniimarket.app.chat.model.ChatContactsData
import com.uniimarket.app.chat.presenter.ChatContactsPresenter
import com.uniimarket.app.more.posts.model.AllUsersFriendData
import com.uniimarket.app.more.posts.view.ItemSoldAdapter

class FriendsList : Fragment(), ChatContactsPresenter.ChatContactsListener,
    ItemSoldAdapter.AdapterOnClick {

    var chatContactsDataList: ArrayList<AllUsersFriendData.Datum> = ArrayList()
    var chatContactsPresenter: ChatContactsPresenter? = null
    var rv_chat_contacts: RecyclerView? = null
    var tv_security_text: TextView? = null
    var adapter: ItemSoldAdapter? = null
    var imv_cancel: ImageView? = null
    var progressDialog: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater?.inflate(com.uniimarket.app.R.layout.friends_layout, container, false)

        (context as DashboardActivity).ll_back?.visibility = View.VISIBLE

        Log.e("friend", " called")

        (context as DashboardActivity).ll_back?.setOnClickListener {
            (context as DashboardActivity).replaceFragment(
                com.uniimarket.app.R.id.frame_layout,
                SettingsFragment(), true, false
            )
        }

        initialVariables(view)
        imv_cancel?.visibility = View.GONE

        getAllFriendsList()

        return view
    }

    private fun getAllFriendsList() {
        progressDialog?.setMessage("Loading...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()

        if ((context as DashboardActivity).et_search?.text.toString().isNotEmpty()) {
            chatContactsPresenter?.getAllContactsList((context as DashboardActivity).et_search?.text.toString())
            Helper.searchProduct = ""
            Log.e("se","if");
            (context as DashboardActivity).et_search?.setText("")
        } else {
            Log.e("se","else");
            chatContactsPresenter?.getAllContactsList("")
        }
    }

    private fun initialVariables(view: View?) {
        progressDialog = ProgressDialog(context)

        tv_security_text = view?.findViewById(R.id.tv_security_text)
        rv_chat_contacts = view?.findViewById(R.id.rv_chat_contacts)
        imv_cancel = view?.findViewById(R.id.imv_cancel)
        chatContactsPresenter = ChatContactsPresenter(this, context)
    }

    override fun chatContactsSuccessResponse(
        status: String,
        message: String?,
        chatContactsList: ArrayList<ChatContactsData.Datum>
    ) {
        try {
            progressDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun chatContactsFailureResponse(status: String, message: String?) {
        try {
            progressDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("WrongConstant")
    override fun allContactsSuccessResponse(
        status: String,
        message: String?,
        chatContactsList: ArrayList<AllUsersFriendData.Datum?>
    ) {
        try {
            progressDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val sharedPref: SharedPreferences =
            context?.getSharedPreferences("uniimarket", Context.MODE_PRIVATE)!!
        var id: String = sharedPref?.getString("id", null)

        for (i in 0 until chatContactsList.size) {
            if ((chatContactsList[i]?.id)?.equals(id)!!) {

            } else {
                chatContactsList[i]?.let { chatContactsDataList.add(it) }
            }
        }


        if (chatContactsDataList.size > 0) {
            rv_chat_contacts?.visibility = View.VISIBLE
            tv_security_text?.visibility = View.GONE
        } else {
            rv_chat_contacts?.visibility = View.GONE
            tv_security_text?.visibility = View.VISIBLE
        }

        adapter = ItemSoldAdapter(chatContactsDataList, context, this)
        rv_chat_contacts?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_chat_contacts?.adapter = adapter
    }

    override fun allContactsFailureResponse(status: String, message: String?) {
        try {
            progressDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onClick(item: Int, friendId: String) {

        Helper.friendId = friendId

        if (friendId == "") {
            status("No User found")
        } else {

            (context as DashboardActivity).replaceFragment(
                com.uniimarket.app.R.id.frame_layout,
                SellerUserFragment(), true, false
            )
        }
    }

    fun status(message: String) {
        val dialog = Dialog(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)

        dialog.setContentView(com.uniimarket.app.R.layout.notificaiton_popup)
        val body = dialog.findViewById(com.uniimarket.app.R.id.imv_cancel) as ImageView
//        body.text = title
//        val yesBtn = dialog .findViewById(R.id.yesBtn) as Button
        val tv_security_text =
            dialog.findViewById(com.uniimarket.app.R.id.tv_security_text) as TextView
        tv_security_text.text = message
        body.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }


}
