package com.uniimarket.app.Dashboard.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class ComingSoonDatum {

    @SerializedName("id")
    @Expose
    private var id: String? = null
    @SerializedName("description")
    @Expose
    private var description: String? = null

    fun getId(): String? {
        return id
    }

    fun setId(id: String) {
        this.id = id
    }

    fun getDescription(): String? {
        return description
    }

    fun setDescription(description: String) {
        this.description = description
    }
}