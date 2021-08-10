package com.uniimarket.app

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class ScreenDatum {

    @SerializedName("id")
    @Expose
    private var id: String? = null
    @SerializedName("page1")
    @Expose
    private var page1: String? = null
    @SerializedName("page2")
    @Expose
    private var page2: String? = null
    @SerializedName("page3")
    @Expose
    private var page3: String? = null

    fun getId(): String? {
        return id
    }

    fun setId(id: String) {
        this.id = id
    }

    fun getPage1(): String? {
        return page1
    }

    fun setPage1(page1: String) {
        this.page1 = page1
    }

    fun getPage2(): String? {
        return page2
    }

    fun setPage2(page2: String) {
        this.page2 = page2
    }

    fun getPage3(): String? {
        return page3
    }

    fun setPage3(page3: String) {
        this.page3 = page3
    }

}