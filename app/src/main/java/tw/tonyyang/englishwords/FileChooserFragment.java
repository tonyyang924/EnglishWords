package tw.tonyyang.englishwords;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tw.tonyyang.englishwords.util.LoadTask;
import tw.tonyyang.englishwords.util.PermissionManager;
import tw.tonyyang.englishwords.util.Tool;

import static tw.tonyyang.englishwords.RequestCodeStore.FILE_CHOOSER_REQUEST;
import static tw.tonyyang.englishwords.RequestCodeStore.REQUEST_EXTERNAL_STORAGE;

public class FileChooserFragment extends Fragment {
    private static final Logger logger = LoggerFactory.getLogger(FileChooserFragment.class);

    private PermissionManager permissionManager;
    private Tool tool;

    private TextView filenameTV;
    private TextView filesizeTV;
    private Button submitBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tool = Tool.getInstance();
        permissionManager = PermissionManager.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_dropboxchooser, container, false);
        filenameTV = view.findViewById(R.id.filenameTV);
        filesizeTV = view.findViewById(R.id.filesizeTV);
        submitBtn = view.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.getDb().userDao().deleteAll();
                LoadTask task = new LoadTask(getActivity());
                task.setShowProgressView(true);
                task.execute();
            }
        });
        submitBtn.setEnabled(false);
        View chooserButton = view.findViewById(R.id.chooser_button);
        chooserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    permissionManager.verifyStoragePermissions(getActivity(), FileChooserFragment.this, new PermissionManager.PermissionCallback() {
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
        });
        return view;
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
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                chooseFileFromLocal();
            } else {
                Toast.makeText(getActivity(), "請取得內部檔案讀取權限，否則無法讀取本地端檔案。", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
