package vinkas.app;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.client.AuthData;

import vinkas.library.R;
import vinkas.util.Helper;

/**
 * Created by Vinoth on 3-5-16.
 */
public abstract class SplashActivity extends Activity {

    private static final int REQUEST_CODE_CONNECT = 1001;

    void authenticate() {
        AuthData authData = getApp().getDatabase().getFirebase().getAuth();
        if (authData == null) {
            Intent intent = new Intent(this, ConnectActivity.class);
            startActivityForResult(intent, REQUEST_CODE_CONNECT);
        } else
            getApp().getDatabase().onAuthenticated(authData);
    }

    public boolean isReady() {
        if (getApp().isReady())
            return true;
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialize();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    protected void initialize() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (isReady() == false)
                        sleep(1000);
                    startMainActivity();
                } catch (InterruptedException e) {
                    Helper.onException(e);
                }
            }
        };
        thread.start();
        authenticate();
    }

    protected void startMainActivity() {
        Intent i = new Intent(this, getMainActivityClass());
        startActivity(i);
        finish();
    }

    protected abstract Class<?> getMainActivityClass();

}