package tw.tonyyang.englishwords;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import tw.tonyyang.englishwords.database.Word;

public class ExamFragment extends Fragment {

    private TextView statusTV;

    private TextView chineseMeanTV;

    private TextView resultTV;

    private RadioGroup rGroup;

    private List<RadioButton> ansRadioBtnList = new ArrayList<>();

    private Button answerBtn;

    private String trueWord;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_exam, container, false);
        statusTV = view.findViewById(R.id.statusTV);
        chineseMeanTV = view.findViewById(R.id.chmeanTV);
        resultTV = view.findViewById(R.id.resultTV);
        rGroup = view.findViewById(R.id.rgroup);
        ansRadioBtnList.add((RadioButton) view.findViewById(R.id.ans1));
        ansRadioBtnList.add((RadioButton) view.findViewById(R.id.ans2));
        ansRadioBtnList.add((RadioButton) view.findViewById(R.id.ans3));
        ansRadioBtnList.add((RadioButton) view.findViewById(R.id.ans4));
        answerBtn = view.findViewById(R.id.answerBtn);
        answerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = "";
                for (int i = 0; i < ansRadioBtnList.size(); i++) {
                    if (ansRadioBtnList.get(i).isChecked()) {
                        result = ansRadioBtnList.get(i).getText().toString();
                    }
                }
                resultTV.setText(result.equals(trueWord)
                        ? "答對了！\n答案是：" + trueWord
                        : "答錯了唷！\n答案是：" + trueWord);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateUI(getRandomData());
    }

    private void updateUI(List<Word> list) {
        if (App.getDb().userDao().getCount() == 0 || list == null || list.size() <= 0) {
            statusTV.setVisibility(View.VISIBLE);
            chineseMeanTV.setText("");
            for (int i = 0; i < rGroup.getChildCount(); i++) {
                rGroup.getChildAt(i).setEnabled(false);
            }
            answerBtn.setEnabled(false);
        } else {
            statusTV.setVisibility(View.GONE);
            trueWord = list.get(0).getWord().replace("*", "");
            chineseMeanTV.setText(list.get(0).getWordMean());

            int rnd;
            int[] random = new int[4];
            HashSet rndSet = new HashSet<>(4);

            for (int i = 0; i < 4; i++) {
                rnd = (int) (4 * Math.random());
                while (!rndSet.add(rnd))
                    rnd = (int) (4 * Math.random());
                random[i] = rnd;
            }

            for (int i = 0; i < ansRadioBtnList.size(); i++) {
                ansRadioBtnList.get(i).setText(list.get(random[i]).getWord().replace("*", ""));
            }
        }
    }

    private List<Word> getRandomData() {
        return App.getDb().userDao().getRandomWords(4);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.exam, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId_ = item.getItemId();
        if (itemId_ == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        }
        if (itemId_ == R.id.action_random) {
            updateUI(getRandomData());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
