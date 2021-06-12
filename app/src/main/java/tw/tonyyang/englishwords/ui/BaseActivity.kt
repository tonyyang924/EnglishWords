package tw.tonyyang.englishwords.ui

import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    override fun onBackPressed() {
        val fragmentList = supportFragmentManager.fragments
        var handled = false
        fragmentList.forEach {
            if (it is BaseFragment) {
                handled = it.onBackPressed()
            }
            if (handled) return@forEach
        }
        if (!handled) {
            super.onBackPressed()
        }
    }
}