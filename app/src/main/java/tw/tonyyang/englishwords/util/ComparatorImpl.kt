package tw.tonyyang.englishwords.util

import java.util.*

/**
 * Created by tonyyang on 2017/7/8.
 * Reference: http://www.jianshu.com/p/fcd75a324c35
 */
class ComparatorImpl : Comparator<String> {
    override fun compare(lhs: String, rhs: String): Int {
        return lhs.compareTo(rhs)
    }
}