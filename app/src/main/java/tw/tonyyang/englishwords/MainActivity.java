package tw.tonyyang.englishwords;

import android.app.Fragment;
import android.app.FragmentManager;
import com.google.android.material.tabs.TabLayout;
import androidx.legacy.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity {
    private static final Logger logger = LoggerFactory.getLogger(MainActivity.class);

    @Override
    protected void initToolbar() {
        super.initToolbar();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void onViewCreated() {
        final TabLayout tabs = findViewById(R.id.tabs);
        final ViewPager viewPager = findViewById(R.id.viewpager);
        tabs.removeAllTabs();
        tabs.addTab(tabs.newTab());
        tabs.addTab(tabs.newTab());
        tabs.addTab(tabs.newTab());
        viewPager.setAdapter(new SamplePagerAdapter(getFragmentManager(), tabs.getTabCount()));
        tabs.setupWithViewPager(viewPager);
    }

    private static class SamplePagerAdapter extends FragmentStatePagerAdapter {
        private int numOfTabs;
        private List<String> tabsTitle = Arrays.asList("讀取 / 更新", "單字列表", "單字考試");

        SamplePagerAdapter(FragmentManager fm, int numOfTabs) {
            super(fm);
            this.numOfTabs = numOfTabs;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position < tabsTitle.size() ? tabsTitle.get(position) : null;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FileChooserFragment();
                case 1:
                    return new WordListM1Fragment();
                case 2:
                    return new ExamFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return numOfTabs;
        }
    }
}