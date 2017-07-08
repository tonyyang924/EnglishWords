package tw.tonyyang.englishwords.util;

import java.util.Comparator;

/**
 * Created by tonyyang on 2017/7/8.
 * Reference: http://www.jianshu.com/p/fcd75a324c35
 */

public class ComparatorImpl implements Comparator<String> {

    @Override
    public int compare(String lhs, String rhs) {
        return lhs.compareTo(rhs);
    }
}
