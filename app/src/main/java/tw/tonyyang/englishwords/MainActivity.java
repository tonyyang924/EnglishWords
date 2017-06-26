package tw.tonyyang.englishwords;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    private static final Logger logger = LoggerFactory.getLogger(MainActivity.class);

    @ViewById(R.id.tabs)
    TabLayout mTabs;

    @ViewById(R.id.viewpager)
    ViewPager mViewPager;

    @AfterViews
    protected void initViews() {
        super.initViews();
        mTabs.removeAllTabs();
        mTabs.addTab(mTabs.newTab());
        mTabs.addTab(mTabs.newTab());
        mTabs.addTab(mTabs.newTab());
        mViewPager.setAdapter(new SamplePagerAdapter(getSupportFragmentManager(), mTabs.getTabCount()));
        mTabs.setupWithViewPager(mViewPager);
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.toolbar_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.action_search) {
                    Toast.makeText(MainActivity.this, R.string.menu_search, Toast.LENGTH_SHORT).show();
                } else if (menuItemId == R.id.action_notification) {
                    Toast.makeText(MainActivity.this, R.string.menu_notifications, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        setSupportActionBar(toolbar);
    }

    class SamplePagerAdapter extends FragmentStatePagerAdapter {
        private int numOfTabs;
        private List<String> tabsTitle = Arrays.asList("讀取 / 更新", "單字列表", "單字考試");

        public SamplePagerAdapter(FragmentManager fm, int numOfTabs) {
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
                    return new DropboxChooserFragment_();
                case 1:
                    return new WordsListM1Fragment_();
                case 2:
                    return new ExamFragment_();
            }
            return null;
        }

        @Override
        public int getCount() {
            return numOfTabs;
        }
    }
}