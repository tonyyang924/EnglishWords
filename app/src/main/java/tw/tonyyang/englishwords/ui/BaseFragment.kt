package tw.tonyyang.englishwords.ui

import androidx.fragment.app.Fragment

open class BaseFragment : Fragment(), OnBackPressed {

    /**
     * could handle back press.
     * @return true if back press was handled
     */
    override fun onBackPressed(): Boolean {
        return false
    }
}