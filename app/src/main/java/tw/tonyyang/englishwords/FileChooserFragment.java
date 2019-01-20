package tw.tonyyang.englishwords;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
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

import static tw.tonyyang.englishwords.RequestCodeStore.FILE_CHOOSER_REQUEST;
import static tw.tonyyang.englishwords.RequestCodeStore.REQUEST_EXTERNAL_STORAGE;

@EFragment(R.layout.fragment_dropboxchooser)
public class FileChooserFragment extends Fragment {

    private static final Logger logger = LoggerFactory.getLogger(FileChooserFragment.class);

    @Bean
    protected Tool tool;

    @Bean
    protected WordsDao wordsDao;

    @Bean
    protected PermissionManager permissionManager;

    @ViewById(R.id.filenameTV)
    protected TextView filenameTV;

    @ViewById(R.id.filesizeTV)
    protected TextView filesizeTV;

    @ViewById(R.id.submitBtn)
    protected Button submitBtn;

    @AfterViews
    protected void initViews() {
        submitBtn.setEnabled(false);
    }

    @Click(R.id.chooser_button)
    public void choose() {
        if (Build.VERSION.SDK_INT >= 23) {
            permissionManager.verifyStoragePermissions(getActivity(), this, new PermissionManager.PermissionCallback() {
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
                    getActivity().startActivityForResult(intent, REQUEST_EXTERNAL_STORAGE);
                }
            });
        } else {
            chooseFileFromLocal();
        }
    }

    private void chooseFileFromLocal() {
        Intent intent = new Intent(
                Intent.ACTION_GET_CONTENT);
        intent.setType("application/vnd.ms-excel");
        Intent destIntent = Intent.createChooser(intent, "檔案目錄選擇");
        if (getActivity() == null || getActivity().isFinishing() || getActivity().isDestroyed()) {
            return;
        }
        startActivityForResult(destIntent, FILE_CHOOSER_REQUEST);
    }

    @Click(R.id.submitBtn)
    public void submit() {
        try {
            wordsDao.deleteAll();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.debug("words delete all failed.");
        }
        LoadTask task = new LoadTask(getActivity());
        task.setShowProgressView(true);
        task.execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_REQUEST) {
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
            case REQUEST_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseFileFromLocal();
                } else {
                    Toast.makeText(getActivity(), "請取得內部檔案讀取權限，否則無法讀取本地端檔案。", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
