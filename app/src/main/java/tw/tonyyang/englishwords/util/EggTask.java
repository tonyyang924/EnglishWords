package tw.tonyyang.englishwords.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tw.tonyyang.englishwords.R;

/**
 * Created by tonyyang on 2017/5/13.
 */

public abstract class EggTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    private static final Logger logger = LoggerFactory.getLogger(EggTask.class);

    protected ProgressDialog progress;

    protected Context context;

    protected boolean isShowProgressView = true;

    protected EggTask(Context context) {
        this.context = context;
        progress = new ProgressDialog(context);
        progress.setTitle(null);
        progress.setMessage(getProgressMessage());
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
    }

    protected String getProgressMessage() {
        return context.getString(R.string.loading_message);
    }

    @Override
    protected void onPreExecute() {
        if (isShowProgressView) {
            try {
                progress.show();
            } catch (Exception e) {
                logger.debug(String.valueOf(e));
            }
        }
    }

    @Override
    protected void onPostExecute(Result result) {
        try {
            progress.dismiss();
        } catch (IllegalArgumentException e) {
            logger.debug(String.valueOf(e));
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        try {
            progress.dismiss();
        } catch (IllegalArgumentException e) {
            logger.debug(String.valueOf(e));
        }
    }

    public void setShowProgressView(boolean isShowProgressView) {
        this.isShowProgressView = isShowProgressView;
    }
}
