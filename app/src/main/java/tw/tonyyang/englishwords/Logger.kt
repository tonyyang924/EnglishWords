package tw.tonyyang.englishwords

import android.util.Log

class Logger private constructor() {

    companion object {

        private val msgFormat: (msg: String) -> String = { msg ->
            "[${Thread.currentThread().name}] $msg"
        }

        fun v(tag: String, msg: String) {
            Log.v(tag, msgFormat(msg))
        }
        fun v(tag: String, msg: String, tr: Throwable) {
            Log.v(tag, msgFormat(msg), tr)
        }
        fun d(tag: String, msg: String) {
            Log.d(tag, msgFormat(msg))
        }
        fun d(tag: String, msg: String, tr: Throwable) {
            Log.d(tag, msgFormat(msg), tr)
        }
        fun i(tag: String, msg: String) {
            Log.i(tag, msgFormat(msg))
        }
        fun i(tag: String, msg: String, tr: Throwable) {
            Log.i(tag, msgFormat(msg), tr)
        }
        fun w(tag: String, msg: String) {
            Log.w(tag, msgFormat(msg))
        }
        fun w(tag: String, msg: String, tr: Throwable) {
            Log.w(tag, msgFormat(msg), tr)
        }
        fun w(tag: String, tr: Throwable) {
            Log.w(tag, tr)
        }
        fun e(tag: String, msg: String) {
            Log.e(tag, msgFormat(msg))
        }
        fun e(tag: String, msg: String, tr: Throwable) {
            Log.e(tag, msgFormat(msg), tr)
        }
    }
}