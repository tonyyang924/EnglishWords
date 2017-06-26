package tw.tonyyang.englishwords.util;

import android.content.Context;
import android.net.Uri;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import tw.tonyyang.englishwords.R;
import tw.tonyyang.englishwords.RealTimeUpdateEvent;

public class LoadTask extends EggTask<Void, Void, Void> {

    private Tool tool;

    public LoadTask(Context context) {
        super(context);
        tool = Tool_.getInstance_(context);
    }

    @Override
    protected String getProgressMessage() {
        return context.getString(R.string.loading_and_import);
    }

    @Override
    protected Void doInBackground(Void... params) {
        if (tool.getFileUrl() != null && tool.getFileUrl().contains("content://")) {
            //   content://xxx.xxx
            //   讀取手機內檔案
            try {
                Uri uri = Uri.parse(tool.getFileUrl());
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[10 * 1024];
                while (true) {
                    int len = inputStream.read(buffer);
                    //publishProgress(len);
                    if (len == -1) {
                        break;
                    }
                    arrayOutputStream.write(buffer, 0, len);
                }
                arrayOutputStream.close();
                inputStream.close();

                byte[] data = arrayOutputStream.toByteArray();
                FileOutputStream fileOutputStream = context.openFileOutput("toeic.xls", Context.MODE_PRIVATE);
                fileOutputStream.write(data);
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //   https://xxx.xxx
            //   讀取網路上檔案
            try {
                URL url = new URL(tool.getFileUrl());
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setConnectTimeout(10 * 1000);
                connection.connect();
                if (connection.getResponseCode() == 200) {
                    InputStream inputStream = connection.getInputStream();
                    ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[10 * 1024];
                    while (true) {
                        int len = inputStream.read(buffer);
                        if (len == -1) {
                            break;
                        }
                        arrayOutputStream.write(buffer, 0, len);
                    }
                    arrayOutputStream.close();
                    inputStream.close();

                    byte[] data = arrayOutputStream.toByteArray();
                    FileOutputStream fileOutputStream = context.openFileOutput("internet_file.xls", Context.MODE_PRIVATE);
                    fileOutputStream.write(data);
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        tool.readExcel();
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        DialogFactory.createSimpleOkErrorDialog(
                context, null, context.getString(R.string.loading_complete)).show();

        RealTimeUpdateEvent realTimeUpdateEvent = new RealTimeUpdateEvent(RealTimeUpdateEvent.Type.UPDATE_WORD_LIST);
        realTimeUpdateEvent.setMessage("更新列表資料");
        EventBus.getDefault().post(realTimeUpdateEvent);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        // TODO: popup dialog ask user retry?

    }
}