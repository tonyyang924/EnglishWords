package tw.tonyyang.englishwords;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.TextView;

import com.dropbox.chooser.android.DbxChooser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

import tw.tonyyang.englishwords.util.Tool;
import tw.tonyyang.englishwords.db.WordsDao;
import tw.tonyyang.englishwords.util.LoadTask;

@EFragment(R.layout.fragment_dropboxchooser)
public class DropboxChooserFragment extends Fragment {
    private static final Logger logger = LoggerFactory.getLogger(DropboxChooserFragment.class);

    @Bean
    Tool tool;

    @Bean
    WordsDao wordsDao;

    /*
     * This is for you
     * to fill in!
     */
    private static final int DBX_CHOOSER_REQUEST = 0; // You can change this if needed
    private static final int FILE_CHOOSER_REQUEST = 1;

    @ViewById(R.id.chooser_button)
    Button mChooserButton;
    @ViewById(R.id.filenameTV)
    TextView filenameTV;
    @ViewById(R.id.filesizeTV)
    TextView filesizeTV;
    @ViewById(R.id.submitBtn)
    Button submitBtn;

    private DbxChooser mChooser;

    private String[] items = {"從檔案目錄", "從Dropbox"};

    @AfterViews
    protected void initViews() {
        mChooser = new DbxChooser(APIContract.DROPBOX_API_KEY);
        submitBtn.setEnabled(false);
    }

    @Click(R.id.chooser_button)
    public void choose() {
        AlertDialog alertdialog = new AlertDialog.Builder(getActivity())
                .setTitle("選擇檔案")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        // Toast.makeText(cxt, "你選擇的是:" + items[which],
                        // 3000).show();
                        if (items[which].equals("從檔案目錄")) {
                            // 建立"選擇檔案"的ACTION
                            Intent intent = new Intent(
                                    Intent.ACTION_GET_CONTENT);
                            // 設定檔案類型
                            intent.setType("text/*");
                            // 建立 "檔案選擇器" 的 Intent (第二個參數: 選擇器的標題)
                            Intent destIntent = Intent.createChooser(
                                    intent, "檔案目錄選擇");
                            // 切換到檔案選擇器 (它的處理結果, 會觸發 onActivityResult
                            // 事件)
                            startActivityForResult(destIntent,
                                    FILE_CHOOSER_REQUEST);
                        } else if (items[which].equals("從Dropbox")) {
                            if (isInstalled("com.dropbox.android")) { //如果有安裝dropbox
                                try {
                                    DbxChooser.ResultType resultType;
                                    resultType = DbxChooser.ResultType.DIRECT_LINK;
                                    mChooser.forResultType(resultType).launch(
                                            DropboxChooserFragment.this,
                                            DBX_CHOOSER_REQUEST);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else { //如果沒安裝dropbox
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.dropbox.android"));
                                startActivity(intent);
                            }
                        }
                    }

                }).create();
        alertdialog.show();
    }

    @Click(R.id.submitBtn)
    public void submit() {
        try {
            wordsDao.deleteAll();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.debug("words delete all failed.");
        }
        LoadTask task = new LoadTask(getContext());
        task.setShowProgressView(true);
        task.execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DBX_CHOOSER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                logger.debug("dbx chooser");
                DbxChooser.Result result = new DbxChooser.Result(data);
                tool.setFileUrl(result.getLink().toString());
                filenameTV.setText(tool.getFileUrl());
                filesizeTV.setText(String.valueOf(result.getSize()));
                submitBtn.setEnabled(true);
            }
        } else if (requestCode == FILE_CHOOSER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                logger.debug("file chooser");
                // 取得檔案的 Uri
                Uri uri = data.getData();
                if (uri != null) {
                    tool.setFileUrl(uri.toString());
                    filenameTV.setText(tool.getFileUrl());
                    filesizeTV.setText("");
                    submitBtn.setEnabled(true);
                }
            }
        }
    }

    private boolean isInstalled(String pack) {
        PackageManager pm = getContext().getPackageManager();
        boolean appInstalled;
        try {
            pm.getPackageInfo(pack, PackageManager.GET_ACTIVITIES);
            appInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            appInstalled = false;
        }
        return appInstalled;
    }

}
