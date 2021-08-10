package com.uniimarket.app.categories.view

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.uniimarket.app.R

class CategoriesActivity : AppCompatActivity() {

    var rv_category_types: RecyclerView? = null
    var rv_category_part: RecyclerView? = null
    var tv_category_part: TextView? = null
    var tv_category_type: TextView? = null
    var imv_filter: ImageView? = null
    var imv_sorting: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_categories)


    }
}
