package tw.tonyyang.englishwords;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

/**
 * Created by tonyyang on 2017/5/13.
 */

@EActivity(R.layout.activity_splash)
public class SplashActivity extends BaseActivity {

    @AfterViews
    protected void initViews() {
        super.initViews();
    }
}
