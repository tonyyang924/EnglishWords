package tw.tonyyang.englishwords

import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat

fun Toolbar.init() {
    setLogo(R.drawable.ic_launcher)
    title = App.appContext.getString(R.string.app_name)
    setTitleTextColor(ContextCompat.getColor(App.appContext, R.color.white))
}