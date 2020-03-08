package tw.tonyyang.englishwords

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tw.tonyyang.englishwords.database.Word

class WordListM2Adapter internal constructor(private val wordList: List<Word>) : RecyclerView.Adapter<WordListM2Adapter.ViewHolder>() {
    interface OnRecyclerViewListener {
        fun onItemClick(v: View?, position: Int)
        fun onItemLongClick(v: View?, position: Int)
    }

    private var onRecyclerViewListener: OnRecyclerViewListener? = null
    fun setOnRecyclerViewListener(onRecyclerViewListener: OnRecyclerViewListener?) {
        this.onRecyclerViewListener = onRecyclerViewListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.word_list_m2_item, viewGroup, false)
        val vh = ViewHolder(view)
        view.setOnClickListener(vh)
        return vh
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val words = wordList[i]
        viewHolder.image.setImageResource(R.drawable.book)
        viewHolder.title.text = words.word
        viewHolder.info.text = words.wordMean
        viewHolder.category.text = words.category
    }

    override fun getItemCount(): Int {
        return wordList.size
    }

    fun getItem(position: Int): Word {
        return wordList[position]
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, OnLongClickListener {
        val image: ImageView = itemView.findViewById(R.id.iv_image)
        val title: TextView = itemView.findViewById(R.id.tv_title)
        val info: TextView = itemView.findViewById(R.id.tv_info)
        val category: TextView = itemView.findViewById(R.id.tv_category)
        override fun onClick(v: View) {
            onRecyclerViewListener?.onItemClick(v, adapterPosition)
        }

        override fun onLongClick(v: View): Boolean {
            onRecyclerViewListener?.onItemLongClick(v, adapterPosition)
            return false
        }
    }
}