package tw.tonyyang.englishwords;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tw.tonyyang.englishwords.database.Word;


/**
 * Created by tonyyang on 2017/6/3.
 */

public class WordListM2Adapter extends RecyclerView.Adapter<WordListM2Adapter.ViewHolder> {

    interface OnRecyclerViewListener {
        void onItemClick(View v, int position);

        void onItemLongClick(View v, int position);
    }

    private List<Word> wordList;

    private OnRecyclerViewListener onRecyclerViewListener;

    void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    WordListM2Adapter(List<Word> wordList) {
        this.wordList = wordList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.word_list_m2_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(vh);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Word words = wordList.get(i);
        viewHolder.image.setImageResource(R.drawable.book);
        viewHolder.title.setText(words.getWord());
        viewHolder.info.setText(words.getWordMean());
        viewHolder.category.setText(words.getCategory());
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }

    Word getItem(int position) {
        return wordList.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private ImageView image;
        private TextView title;
        private TextView info;
        private TextView category;

        ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            info = itemView.findViewById(R.id.info);
            category = itemView.findViewById(R.id.category);
        }

        @Override
        public void onClick(View v) {
            if (onRecyclerViewListener != null) {
                onRecyclerViewListener.onItemClick(v, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (onRecyclerViewListener != null) {
                onRecyclerViewListener.onItemLongClick(v, getAdapterPosition());
            }
            return false;
        }
    }
}
