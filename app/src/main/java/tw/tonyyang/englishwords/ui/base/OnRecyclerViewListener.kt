package tw.tonyyang.englishwords.ui.base

import android.view.View

interface OnRecyclerViewListener {

    fun onItemClick(v: View, position: Int)

    fun onItemLongClick(v: View, position: Int): Boolean {
        return false
    }
}