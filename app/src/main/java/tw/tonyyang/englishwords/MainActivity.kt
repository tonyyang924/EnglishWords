package tw.tonyyang.englishwords

import android.app.Fragment
import android.app.FragmentManager
import androidx.appcompat.widget.Toolbar
import androidx.legacy.app.FragmentStatePagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : BaseActivity() {
    override fun initToolbar() {
        super.initToolbar()
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override val layoutResource: Int
        get() = R.layout.activity_main

    override fun onViewCreated() {
        tabs.removeAllTabs()
        tabs.addTab(tabs.newTab())
        tabs.addTab(tabs.newTab())
        tabs.addTab(tabs.newTab())
        viewpager.adapter = SamplePagerAdapter(fragmentManager, tabs.tabCount)
        tabs.setupWithViewPager(viewpager)
    }

    private class SamplePagerAdapter internal constructor(fm: FragmentManager?, private val numOfTabs: Int) : FragmentStatePagerAdapter(fm) {
        private val tabsTitle = Arrays.asList("讀取 / 更新", "單字列表", "單字考試")
        override fun getPageTitle(position: Int): CharSequence? {
            return if (position < tabsTitle.size) tabsTitle[position] else null
        }

        override fun getItem(position: Int): Fragment? {
            when (position) {
                0 -> return FileChooserFragment()
                1 -> return WordListM1Fragment()
                2 -> return ExamFragment()
            }
            return null
        }

        override fun getCount(): Int {
            return numOfTabs
        }
    }
}