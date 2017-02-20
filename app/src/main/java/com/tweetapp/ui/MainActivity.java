package com.tweetapp.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.tweetapp.R;
import com.tweetapp.api.ApiModule;
import com.tweetapp.api.TwitterAPIClient;
import com.tweetapp.helper.PreferenceHelper;
import com.tweetapp.helper.ResourceHelper;
import com.tweetapp.service.TwitterService;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static com.tweetapp.Constants.BEARER_TOKEN;

/**
 * Created by Miguel Bronzovic.
 */
public class MainActivity extends AppCompatActivity {
    private final static String FRAGMENT_STATE = "fragment_state";

    private CompositeSubscription compSub = new CompositeSubscription();

    private final TwitterService twitterService = new TwitterService(new TwitterAPIClient(ApiModule.getOkHttpClient()));

    private Fragment currentFragment;
    private ProgressDialog progressDialog;

    @BindView(R.id.root_layout)
    View rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_STATE);
        } else {
            currentFragment = SearchTweetsFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.root_layout, currentFragment, "current")
                    .commit();
        }

        connect();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        getSupportFragmentManager().putFragment(outState, FRAGMENT_STATE, currentFragment);
    }

    @Override
    protected void onDestroy() {
        compSub.unsubscribe();
        super.onDestroy();
    }

    /**
     * Connects to Twitter API to obtain the bearer token
     */
    private void connect() {
        if (TextUtils.isEmpty(PreferenceHelper.getString(BEARER_TOKEN))) {
            Timber.d("*** Connecting to Twitter API ***");
            showProgressDialog();

            compSub.add(twitterService.getToken()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnCompleted(new Action0() {
                        @Override
                        public void call() {
                            Toast.makeText(MainActivity.this, ResourceHelper.getString(R.string.message_token_obtained),
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String oAuthAccessToken) {
                            PreferenceHelper.save(BEARER_TOKEN, oAuthAccessToken);
                            Timber.d("*** OAUTH RESPONSE - Access Token: %s***", oAuthAccessToken);
                            hideProgressDialog();
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Timber.e("*** ERROR - %s ***", throwable.getMessage());
                            hideProgressDialog();
                            Snackbar.make(rootLayout, ResourceHelper.getString(R.string.error_message_general),
                                    Snackbar.LENGTH_SHORT).show();
                        }
                    }));
        }
    }

    /**
     * Creates and shows a {@link ProgressDialog} dialog
     */
    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.main_activity_progress_dialog_message));
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    /**
     * Hides a {@link ProgressDialog} dialog
     */
    private void hideProgressDialog() {
        progressDialog.dismiss();
    }
}
