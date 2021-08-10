package com.uniimarket.app.Expandable

import android.view.View
import android.view.animation.Animation.RELATIVE_TO_SELF
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import com.uniimarket.app.R

class GenreViewHolder(itemView: View) : GroupViewHolder(itemView) {

    var genreName: TextView
    var arrow: ImageView
    var icon: ImageView

    init {
        genreName = itemView.findViewById(R.id.list_item_genre_name) as TextView
        arrow = itemView.findViewById(R.id.list_item_genre_arrow) as ImageView
        icon = itemView.findViewById(R.id.list_item_genre_icon) as ImageView
    }

    fun setGenreTitle(genre: ExpandableGroup<*>) {
        if (genre is Genre) {
            genreName.text = genre.title
            icon.setBackgroundResource((genre as Genre).iconResId)
        }
        if (genre is MultiCheckGenre) {
            genreName.text = genre.getTitle()
//            icon.setBackgroundResource((genre).iconResId)
        }
//        if (genre is SingleCheckGenre) {
//            genreName.text = genre.title
//            icon.setBackgroundResource((genre as SingleCheckGenre).getIconResId())
//        }
    }

    override fun expand() {
        animateExpand()
    }

    override fun collapse() {
        animateCollapse()
    }

    private fun animateExpand() {
        val rotate = RotateAnimation(360f, 180f, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f)
        rotate.duration = 300
        rotate.fillAfter = true
        arrow.animation = rotate
    }

    private fun animateCollapse() {
        val rotate = RotateAnimation(180f, 360f, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f)
        rotate.duration = 300
        rotate.fillAfter = true
        arrow.animation = rotate
    }
}
