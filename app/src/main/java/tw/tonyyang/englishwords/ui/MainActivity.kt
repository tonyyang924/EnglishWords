package tw.tonyyang.englishwords.ui

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import tw.tonyyang.englishwords.BaseActivity
import tw.tonyyang.englishwords.ui.exam.ExamFragment
import tw.tonyyang.englishwords.R
import tw.tonyyang.englishwords.WordListM1Fragment
import tw.tonyyang.englishwords.ui.importer.ImporterFragment

class MainActivity : BaseActivity() {

    override fun initToolbar() {
        super.initToolbar()
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override val layoutResource: Int
        get() = R.layout.activity_main

    override fun onViewCreated() {
        val tabs = findViewById<TabLayout>(R.id.tabs).apply {
            removeAllTabs()
            addTab(newTab())
            addTab(newTab())
            addTab(newTab())
        }
        val viewpager = findViewById<ViewPager>(R.id.viewpager).apply {
            adapter = SamplePagerAdapter(supportFragmentManager, tabs.tabCount)
        }
        tabs.setupWithViewPager(viewpager)
    }

    private class SamplePagerAdapter(
            fm: FragmentManager,
            private val numOfTabs: Int
    ) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        private val tabsTitle = listOf("讀取 / 更新", "單字列表", "單字考試")

        override fun getPageTitle(position: Int): CharSequence? {
            return if (position < tabsTitle.size) tabsTitle[position] else null
        }

        override fun getItem(position: Int): Fragment = when (position) {
            TAB_IMPORTER -> ImporterFragment.newInstance()
            TAB_WORD_LIST_M1 -> WordListM1Fragment()
            TAB_EXAM -> ExamFragment()
            else -> throw RuntimeException("Could not get fragment.")

        }

        override fun getCount(): Int = numOfTabs
    }

    companion object {
        private const val TAB_IMPORTER = 0
        private const val TAB_WORD_LIST_M1 = 1
        private const val TAB_EXAM = 2
    }
}