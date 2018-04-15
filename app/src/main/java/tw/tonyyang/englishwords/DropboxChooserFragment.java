package tw.tonyyang.englishwords;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.chooser.android.DbxChooser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

import tw.tonyyang.englishwords.db.WordsDao;
import tw.tonyyang.englishwords.util.LoadTask;
import tw.tonyyang.englishwords.util.PermissionManager;
import tw.tonyyang.englishwords.util.Tool;

@EFragment(R.layout.fragment_dropboxchooser)
public class DropboxChooserFragment extends Fragment {
    private static final Logger logger = LoggerFactory.getLogger(DropboxChooserFragment.class);

    @Bean
    Tool tool;

    @Bean
    WordsDao wordsDao;

    @Bean
    PermissionManager permissionManager;

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

    private Fragment fragment;

    private DbxChooser mChooser;

    private String[] items = {"從檔案目錄", "從Dropbox"};

    @AfterViews
    protected void initViews() {
        fragment = DropboxChooserFragment.this;
        try {
            mChooser = new DbxChooser(APIContract.DROPBOX_API_KEY);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
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
                        if (items[which].equals("從檔案目錄")) {
                            if (Build.VERSION.SDK_INT >= 23) {
                                permissionManager.verifyStoragePermissions(getActivity(), fragment, new PermissionManager.PermissionCallback() {
                                    @Override
                                    public void onPermissionGranted() {
                                        chooseFileFromLocal();
                                    }

                                    @Override
                                    public void onShowGuide() {
                                        Toast.makeText(getActivity(), "需要取得您的讀寫權限才能正常存取手機檔案，請至\"設定\"開啟讀寫權限。", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                                        intent.setData(uri);
                                        getActivity().startActivityForResult(intent, PermissionManager.REQUEST_EXTERNAL_STORAGE);
                                    }
                                });
                            } else {
                                chooseFileFromLocal();
                            }
                        } else if (items[which].equals("從Dropbox")) {
                            //如果有安裝DropBox
                            if (isInstalled("com.dropbox.android")) {
                                if (getActivity() == null || getActivity().isFinishing() || getActivity().isDestroyed()) {
                                    return;
                                }
                                try {
                                    DbxChooser.ResultType resultType;
                                    resultType = DbxChooser.ResultType.DIRECT_LINK;
                                    mChooser.forResultType(resultType).launch(DropboxChooserFragment.this, DBX_CHOOSER_REQUEST);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else { //如果沒安裝DropBox
                                if (getActivity() == null || getActivity().isFinishing() || getActivity().isDestroyed()) {
                                    return;
                                }
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.dropbox.android"));
                                startActivity(intent);
                            }
                        }
                    }

                }).create();
        alertdialog.show();
    }

    private void chooseFileFromLocal() {
        Intent intent = new Intent(
                Intent.ACTION_GET_CONTENT);
        intent.setType("text/*");
        Intent destIntent = Intent.createChooser(intent, "檔案目錄選擇");
        if (getActivity() == null || getActivity().isFinishing() || getActivity().isDestroyed()) {
            return;
        }
        startActivityForResult(destIntent, FILE_CHOOSER_REQUEST);
    }

    @TargetApi(Build.VERSION_CODES.M)
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionManager.REQUEST_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseFileFromLocal();
                } else {
                    Toast.makeText(getActivity(), "請取得內部檔案讀取權限，否則無法讀取本地端檔案。", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
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
