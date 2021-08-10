package com.uniimarket.app.more.settings.view

import android.content.Context
import android.util.Log
import android.widget.RadioButton
import android.widget.TextView
import com.mindorks.placeholderview.annotations.Layout
import com.mindorks.placeholderview.annotations.Resolve
import com.mindorks.placeholderview.annotations.View
import com.mindorks.placeholderview.annotations.expand.Collapse
import com.mindorks.placeholderview.annotations.expand.Expand
import com.mindorks.placeholderview.annotations.expand.Parent
import com.mindorks.placeholderview.annotations.expand.SingleTop
import com.uniimarket.app.R

@Parent
@SingleTop
@Layout(R.layout.header_layout)
class HeaderView(var context: Context?, var mHeaderText: String) {

    private val TAG = "HeaderView"

    @View(R.id.header_text)
    internal var headerText: TextView? = null

    @View(R.id.rb_header)
    internal var radioButton: RadioButton? = null


//    private var mContext: Context? = null
//    private var mHeaderText: String? = null
//
//    fun HeaderView(context: Context, headerText: String) {
//        this.mContext = context
//        this.mHeaderText = headerText
//    }

    @Resolve
    private fun onResolve() {
        Log.d(TAG, "onResolve")
        headerText!!.text = mHeaderText

        radioButton?.setOnClickListener {
            if (!radioButton!!.isSelected) {
                radioButton!!.isChecked = true;
                radioButton!!.isSelected = true;
                onExpand()
            } else {
                radioButton!!.isChecked = false;
                radioButton!!.isSelected = false;
                onCollapse()
            }
//            Log.e("rb", ""+radioButton!!.isChecked)
//            if (radioButton?.isChecked == true) {
////                radioButton?.isChecked = false
//                Log.e(TAG, mHeaderText+"-------")
//                onExpand()
//            } else {
////                radioButton?.isChecked = true
//                onCollapse()
//            }
        }

//        radioButton?.text = mHeaderText
    }


//    @Click(com.uniimarket.app.R.id.header_text)
//    fun onItemClick() {
//        //        mExpandablePlaceHolderView.addChildView(mParentPosition, new ChildItem(mExpandablePlaceHolderView));
////        mExpandablePlaceHolderView.removeView(this)
////        Log.e("child ", "click " + movie.subCategorieName + "---")
//        radioButton?.isChecked = radioButton?.isChecked != true
////        updateNotificationStatus(movie.categoriesid, movie.subcategorieid)
//
//
//    }

    @Expand
    private fun onExpand() {
        Log.e(TAG, "onExpand")
//        radioButton?.isChecked = true
    }

    @Collapse
    private fun onCollapse() {
//        radioButton?.isChecked = false
        Log.e(TAG, "onCollapse")
    }

//    @Click(com.uniimarket.app.R.id.child_desc)
//    fun onItemClick() {
//        //        mExpandablePlaceHolderView.addChildView(mParentPosition, new ChildItem(mExpandablePlaceHolderView));
////        mExpandablePlaceHolderView.removeView(this)
//        Log.e("child ","click")
//    }
}
