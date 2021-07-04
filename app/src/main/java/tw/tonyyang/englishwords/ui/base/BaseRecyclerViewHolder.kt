package tw.tonyyang.englishwords.ui.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class BaseRecyclerViewHolder(itemView: View, onRecyclerViewListener: OnRecyclerViewListener): RecyclerView.ViewHolder(itemView) {
    init {
        itemView.setOnClickListener {
            onRecyclerViewListener.onItemClick(it, absoluteAdapterPosition)
        }
        itemView.setOnLongClickListener {
            return@setOnLongClickListener onRecyclerViewListener.onItemLongClick(
                it,
                absoluteAdapterPosition
            )
        }
    }
}