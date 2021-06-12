package tw.tonyyang.englishwords.ui.category

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tw.tonyyang.englishwords.R

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    interface OnRecyclerViewListener {
        fun onItemClick(v: View?, position: Int)
        fun onItemLongClick(v: View?, position: Int)
    }

    var onRecyclerViewListener: OnRecyclerViewListener? = null

    var categoryList: List<String> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.word_list_m1_item, viewGroup, false)
        val vh = ViewHolder(view)
        view.setOnClickListener(vh)
        return vh
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val category = categoryList[i]
        viewHolder.image.setImageResource(R.drawable.dot)
        viewHolder.title.text = category
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    fun getItem(position: Int): String? {
        return if (position < categoryList.size) categoryList[position] else null
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, OnLongClickListener {
        val image: ImageView = itemView.findViewById(R.id.iv_image)
        val title: TextView = itemView.findViewById(R.id.tv_title)
        override fun onClick(v: View) {
            onRecyclerViewListener?.onItemClick(v, adapterPosition)
        }

        override fun onLongClick(v: View): Boolean {
            onRecyclerViewListener?.onItemLongClick(v, adapterPosition)
            return false
        }
    }
}