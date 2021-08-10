package com.uniimarket.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView


class DummyActivity : AppCompatActivity() {

    var rv_Dummy: RecyclerView? = null

    var image_list =
        arrayListOf<Int>(R.drawable.introshopping, R.drawable.intro_one, R.drawable.intro_two)
    var string_text = arrayListOf<String>("Dheeraj", "Azar", "Naresh")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dummy)

        rv_Dummy = findViewById(R.id.rvDummy)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(rv_Dummy)
        rv_Dummy?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_Dummy?.addItemDecoration(CirclePagerIndicatorDecoration())


    }
}
