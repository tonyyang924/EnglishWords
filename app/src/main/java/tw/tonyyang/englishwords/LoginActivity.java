package tw.tonyyang.englishwords;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tonyyang on 2017/6/15.
 */

@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {
    private static final Logger logger = LoggerFactory.getLogger(LoginActivity.class);

    @ViewById(R.id.login_button)
    LoginButton loginBtn;

    private CallbackManager callbackManager;

    @AfterViews
    protected void initViews() {
        super.initViews();
        callbackManager = CallbackManager.Factory.create();
        loginBtn.setReadPermissions("email");
        loginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                logger.debug("" + loginResult.toString());
            }

            @Override
            public void onCancel() {
                logger.debug("onCancel");
            }

            @Override
            public void onError(FacebookException e) {
                logger.debug("" + e.getLocalizedMessage());
            }
        });
    }
}
