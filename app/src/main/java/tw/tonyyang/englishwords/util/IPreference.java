package tw.tonyyang.englishwords.util;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by tonyyang on 2017/7/8.
 * Reference: http://www.jianshu.com/p/fcd75a324c35
 */

public interface IPreference {

    enum DataType {
        INTEGER, LONG, BOOLEAN, FLOAT, STRING, STRING_SET
    }

    <T> void put(String key, T value);

    <T> void putAll(Map<String, T> map);

    void putAll(String key, List<String> list);

    void putAll(String key, List<String> list, Comparator<String> comparator);

    <T> T get(String key, DataType dataType);

    Map<String, ?> getAll();

    List<String> getAll(String key);

    void remove(String key);

    void removeAll(List<String> keys);

    void removeAll(String... keys);

    boolean contains(String key);

    void clear();

    String getString(String key);

    float getFloat(String key);

    int getInteger(String key);

    long getLong(String key);

    Set<String> getSet(String key);

    boolean getBoolean(String key);


}
