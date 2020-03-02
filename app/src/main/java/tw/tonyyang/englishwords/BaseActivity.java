package tw.tonyyang.englishwords;

import android.os.Bundle;
import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Window;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tonyyang on 2017/6/25.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private static final Logger logger = LoggerFactory.getLogger(BaseActivity.class);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutResource());
        initToolbar();
        onViewCreated();
    }

    @CallSuper
    protected void initToolbar() {
        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_launcher);
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        setSupportActionBar(toolbar);
    }

    protected abstract int getLayoutResource();

    protected abstract void onViewCreated();
}
