package tw.tonyyang.englishwords.ui

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
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
        val tabLayoutLabels = listOf(
                getString(R.string.tab_label_importer),
                getString(R.string.tab_label_vocabularies),
                getString(R.string.tab_label_exam)
        )
        val tabLayout: TabLayout = findViewById(R.id.tabs)
        val viewpager: ViewPager2 = findViewById(R.id.viewpager)
        viewpager.adapter = LabelPagerAdapter(this, tabLayoutLabels.size)
        TabLayoutMediator(tabLayout, viewpager) { tab, position ->
            tab.text = tabLayoutLabels.getOrNull(position)
        }.attach()
    }

    private class LabelPagerAdapter(
            fragmentActivity: FragmentActivity,
            private val numOfTabs: Int
    ) : FragmentStateAdapter(fragmentActivity) {

        override fun getItemCount(): Int = numOfTabs

        override fun createFragment(position: Int): Fragment = when (position) {
            TAB_IMPORTER -> ImporterFragment.newInstance()
            TAB_WORD_LIST_M1 -> WordListM1Fragment()
            TAB_EXAM -> ExamFragment()
            else -> throw RuntimeException("Could not get fragment.")
        }
    }

    companion object {
        private const val TAB_IMPORTER = 0
        private const val TAB_WORD_LIST_M1 = 1
        private const val TAB_EXAM = 2
    }
}