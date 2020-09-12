package tw.tonyyang.englishwords.preference

import android.content.Context
import android.content.Context.MODE_PRIVATE

@Suppress("unused")
class GlobalPreference {

    companion object {
        private const val FILE_NAME = "global_preference"

        const val FLAG_PERMISSIONS_STORAGE = "flag_permissions_storage"

        fun <T> put(context: Context, key: String, value: T) {
            val editor = context.getSharedPreferences(FILE_NAME, MODE_PRIVATE).edit()
            when (value) {
                is String -> {
                    editor.putString(key, value).apply()
                }
                is Int -> {
                    editor.putInt(key, value).apply()
                }
                is Long -> {
                    editor.putLong(key, value).apply()
                }
                is Float -> {
                    editor.putFloat(key, value).apply()
                }
                is Boolean -> {
                    editor.putBoolean(key, value).apply()
                }
                else -> {
                    // value type is illegal.
                }
            }
        }

        fun getString(context: Context, key: String): String? {
            return context.getSharedPreferences(FILE_NAME, MODE_PRIVATE)
                    .getString(key, "")
        }

        fun getInt(context: Context, key: String): Int {
            return context.getSharedPreferences(FILE_NAME, MODE_PRIVATE)
                    .getInt(key, 0)
        }

        fun getLong(context: Context, key: String): Long {
            return context.getSharedPreferences(FILE_NAME, MODE_PRIVATE)
                    .getLong(key, 0)
        }

        fun getFloat(context: Context, key: String): Float {
            return context.getSharedPreferences(FILE_NAME, MODE_PRIVATE)
                    .getFloat(key, 0F)
        }

        fun getBoolean(context: Context, key: String): Boolean {
            return context.getSharedPreferences(FILE_NAME, MODE_PRIVATE)
                    .getBoolean(key, false)
        }
    }
}