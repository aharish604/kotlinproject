package com.uniimarket.app.Expandable

import android.view.View
import android.widget.Checkable
import android.widget.CheckedTextView
import com.thoughtbot.expandablecheckrecyclerview.viewholders.CheckableChildViewHolder
import com.uniimarket.app.R

class MultiCheckArtistViewHolder(itemView: View) : CheckableChildViewHolder(itemView) {

    private val childCheckedTextView: CheckedTextView

    init {
        childCheckedTextView = itemView.findViewById(R.id.list_item_multicheck_artist_name) as CheckedTextView
    }

    override fun getCheckable(): Checkable {
        return childCheckedTextView
    }

    fun setArtistName(artistName: String) {
        childCheckedTextView.text = artistName
    }
}
