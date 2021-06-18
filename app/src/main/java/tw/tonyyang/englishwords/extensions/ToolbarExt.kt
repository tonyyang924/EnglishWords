package tw.tonyyang.englishwords.extensions

import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import tw.tonyyang.englishwords.App
import tw.tonyyang.englishwords.R

fun Toolbar.init() {
    setLogo(R.drawable.ic_launcher)
    title = App.appContext.getString(R.string.app_name)
    setTitleTextColor(ContextCompat.getColor(App.appContext, R.color.white))
}