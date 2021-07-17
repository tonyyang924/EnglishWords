package tw.tonyyang.englishwords.ui.word.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tw.tonyyang.englishwords.R
import tw.tonyyang.englishwords.database.entity.Word
import tw.tonyyang.englishwords.databinding.ItemWordBinding
import tw.tonyyang.englishwords.ui.base.BaseRecyclerViewHolder
import tw.tonyyang.englishwords.ui.base.OnRecyclerViewListener

class WordListAdapter(
    private val onRecyclerViewListener: OnRecyclerViewListener
) : RecyclerView.Adapter<WordListAdapter.ViewHolder>() {

    var wordList: List<Word> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun getItem(position: Int): Word = wordList[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemWordBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onRecyclerViewListener
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(wordList[position])
    }

    override fun getItemCount(): Int = wordList.size

    class ViewHolder(
        binding: ItemWordBinding,
        onRecyclerViewListener: OnRecyclerViewListener
    ) : BaseRecyclerViewHolder(binding.root, onRecyclerViewListener) {
        private val image: ImageView = itemView.findViewById(R.id.iv_image)
        private val title: TextView = itemView.findViewById(R.id.tv_title)
        private val info: TextView = itemView.findViewById(R.id.tv_info)
        private val category: TextView = itemView.findViewById(R.id.tv_category)

        fun bind(word: Word) {
            image.setImageResource(R.drawable.book)
            title.text = word.word
            info.text = word.wordMean
            category.text = word.category
        }
    }
}