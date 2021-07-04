package tw.tonyyang.englishwords.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tw.tonyyang.englishwords.R
import tw.tonyyang.englishwords.databinding.WordListM1ItemBinding
import tw.tonyyang.englishwords.ui.base.BaseRecyclerViewHolder
import tw.tonyyang.englishwords.ui.base.OnRecyclerViewListener

class CategoryAdapter(
    private val onRecyclerViewListener: OnRecyclerViewListener
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    var categoryList: List<String> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            WordListM1ItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onRecyclerViewListener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categoryList[position])
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    fun getItem(position: Int): String? {
        return if (position < categoryList.size) categoryList[position] else null
    }

    class ViewHolder(
        binding: WordListM1ItemBinding,
        onRecyclerViewListener: OnRecyclerViewListener
    ) : BaseRecyclerViewHolder(binding.root, onRecyclerViewListener) {
        private val image: ImageView = itemView.findViewById(R.id.iv_image)
        private val title: TextView = itemView.findViewById(R.id.tv_title)

        fun bind(category: String) {
            image.setImageResource(R.drawable.dot)
            title.text = category
        }
    }
}