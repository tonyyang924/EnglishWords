package tw.tonyyang.englishwords.util

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AlertDialog
import org.slf4j.LoggerFactory
import tw.tonyyang.englishwords.R

abstract class EggTask<Params, Progress, Result> internal constructor(private val context: Context) : AsyncTask<Params, Progress, Result>() {

    private val progress: AlertDialog by lazy {
        UiUtils.getProgressDialog(context, context.getString(R.string.loading_message))
    }

    private var isShowProgressView = true

    fun setShowProgressView(isShowProgressView: Boolean) {
        this.isShowProgressView = isShowProgressView
    }

    override fun onPreExecute() {
        if (isShowProgressView) {
            try {
                progress.show()
            } catch (e: Exception) {
                logger.debug(e.toString())
            }
        }
    }

    override fun onPostExecute(result: Result) {
        try {
            progress.dismiss()
        } catch (e: IllegalArgumentException) {
            logger.debug(e.toString())
        }
    }

    override fun onCancelled() {
        super.onCancelled()
        try {
            progress.dismiss()
        } catch (e: IllegalArgumentException) {
            logger.debug(e.toString())
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(EggTask::class.java)
    }
}