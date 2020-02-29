package tw.tonyyang.englishwords;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by tonyyang on 2017/6/3.
 */

public class WordListM1Adapter extends RecyclerView.Adapter<WordListM1Adapter.ViewHolder> {

    interface OnRecyclerViewListener {
        void onItemClick(View v, int position);

        void onItemLongClick(View v, int position);
    }

    private List<String> categoryList;

    private OnRecyclerViewListener onRecyclerViewListener;

    void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    WordListM1Adapter(List<String> categoryList) {
        this.categoryList = categoryList;
    }

    void setWordList(List<String> categoryList) {
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.word_list_m1_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(vh);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        String category = categoryList.get(i);
        viewHolder.image.setImageResource(R.drawable.dot);
        viewHolder.title.setText(category);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    String getItem(int position) {
        return position < categoryList.size() ? categoryList.get(position) : null;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private ImageView image;
        private TextView title;

        ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
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
