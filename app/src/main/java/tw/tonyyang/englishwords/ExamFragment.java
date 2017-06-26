package tw.tonyyang.englishwords;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.ViewsById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

import tw.tonyyang.englishwords.db.Words;
import tw.tonyyang.englishwords.db.WordsDao;

import static tw.tonyyang.englishwords.R.id.ans1;
import static tw.tonyyang.englishwords.R.id.ans2;
import static tw.tonyyang.englishwords.R.id.ans3;
import static tw.tonyyang.englishwords.R.id.ans4;

@EFragment(R.layout.fragment_exam)
@OptionsMenu(R.menu.exam)
public class ExamFragment extends Fragment {
    private static final Logger logger = LoggerFactory.getLogger(ExamFragment.class);

    @Bean
    WordsDao wordsDao;

    @ViewById(R.id.statusTV)
    TextView statusTV;

    @ViewById(R.id.chmeanTV)
    TextView chineseMeanTV;

    @ViewById(R.id.resultTV)
    TextView resultTV;

    @ViewById(R.id.rgroup)
    RadioGroup rGroup;

    @ViewsById({ans1, ans2, ans3, ans4})
    List<RadioButton> ansRadioBtnList;

    @ViewById(R.id.answerBtn)
    Button answerBtn;

    private String trueWord;

    @Click(R.id.answerBtn)
    public void answer() {
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

    @AfterViews
    protected void initViews() {
        setHasOptionsMenu(true);
        updateUI(getRandomData());
    }

    @UiThread
    protected void updateUI(List<Words> list) {
        if (getAllDataCount() == 0 || list == null || list.size() <= 0) {
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

    @OptionsItem(android.R.id.home)
    protected void onBackClicked() {
        getActivity().onBackPressed();
    }

    @OptionsItem(R.id.action_random)
    protected void onRandom() {
        updateUI(getRandomData());
    }

    private int getAllDataCount() {
        int count = 0;
        try {
            long countOf = wordsDao.count();
            count = (int) countOf;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    private List<Words> getRandomData() {
        try {
            return wordsDao.getRawDao().queryBuilder().orderByRaw("RANDOM()").limit(4).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
