package tw.tonyyang.englishwords.util;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import tw.tonyyang.englishwords.R;
import tw.tonyyang.englishwords.RealTimeUpdateEvent;

public class LoadTask extends EggTask<Void, Void, Void> {

    public static final String TMP_FILE_NAME = "vocabulary.xls";

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
        byte[] data = new byte[0];
        String fileUrl = tool.getFileUrl();
        if (fileUrl != null && (fileUrl.contains("content://") || fileUrl.contains("file:///"))) {
            data = readFile(fileUrl);
        } else {
            try {
                URL url = new URL(tool.getFileUrl());
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setConnectTimeout(10 * 1000);
                connection.connect();
                if (connection.getResponseCode() == 200) {
                    InputStream inputStream = connection.getInputStream();
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
                    data = arrayOutputStream.toByteArray();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = context.openFileOutput(TMP_FILE_NAME, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (fileOutputStream != null) {
            try {
                fileOutputStream.write(data);
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        tool.readExcel();
        return null;
    }

    private byte[] readFile(String fileName) {
        byte[] data = new byte[4 * 1024];
        try {
            InputStream inputstream = context.getContentResolver().openInputStream(Uri.parse(fileName));
            int bytesRead = inputstream.read(data);
            while (bytesRead != -1) {
                bytesRead = inputstream.read(data);
            }
            inputstream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        Toast.makeText(context, context.getString(R.string.loading_complete), Toast.LENGTH_LONG).show();

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