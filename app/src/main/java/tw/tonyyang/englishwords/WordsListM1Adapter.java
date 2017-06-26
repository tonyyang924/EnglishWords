package tw.tonyyang.englishwords;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tw.tonyyang.englishwords.db.Words;

/**
 * Created by tonyyang on 2017/6/3.
 */

public class WordsListM1Adapter extends RecyclerView.Adapter<WordsListM1Adapter.ViewHolder> {

    interface OnRecyclerViewListener {
        void onItemClick(View v, int position);
        void onItemLongClick(View v, int position);
    }

    private List<Words> wordsList = new ArrayList<>();

    private OnRecyclerViewListener onRecyclerViewListener;

    void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    WordsListM1Adapter(List<Words> wordsList) {
        this.wordsList = wordsList;
    }

    public void setWordsList(List<Words> wordsList) {
        this.wordsList = wordsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wordslist_m1_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(vh);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Words words = wordsList.get(i);
        viewHolder.image.setImageResource(R.drawable.dot);
        viewHolder.title.setText(words.getCategory());
    }

    @Override
    public int getItemCount() {
        return wordsList.size();
    }

    Words getItem(int position) {
        return position < wordsList.size() ? wordsList.get(position) : null;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private ImageView image;
        private TextView title;

        ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
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
