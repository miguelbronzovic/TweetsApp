package com.tweetapp.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tweetapp.R;
import com.tweetapp.api.ApiModule;
import com.tweetapp.api.TwitterAPIClient;
import com.tweetapp.data.TweetEntity;
import com.tweetapp.helper.ResourceHelper;
import com.tweetapp.service.TwitterService;
import com.tweetapp.ui.adapter.TweetAdapter;
import com.tweetapp.ui.view.DividerItemDecorator;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * Created by Miguel Bronzovic.
 */
public class SearchTweetsFragment extends Fragment {
    private final static String TWEETS_RESULTS_FROM_SEARCH = "tweets_results";
    private final static String SEARCH_QUERY = "search_query";
    private final static String PENDING_REQUEST = "pending_request";
    private final static String EMPTY_STATE_VISIBLE = "empty_state_visible";

    private final CompositeSubscription compSub1 = new CompositeSubscription();

    private final TwitterService twitterService = new TwitterService(new TwitterAPIClient(ApiModule.getOkHttpClient()));

    private TweetAdapter tweetAdapter;
    private List<TweetEntity> resultsFromSearch;
    private ProgressDialog progressDialog;
    private boolean pendingRequest;

    @BindView(R.id.tweets)
    RecyclerView tweetsListView;

    @BindView(R.id.search_text)
    TextInputEditText searchText;

    @BindView(R.id.search_input_layout)
    TextInputLayout searchInputLayout;

    @BindView(R.id.empty_state_layout)
    RelativeLayout emptySateLayout;

    public static SearchTweetsFragment newInstance() {
        return new SearchTweetsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        ButterKnife.bind(this, root);

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard();
                    root.requestFocus();
                    return true;
                }
                return false;
            }
        });

        tweetAdapter = new TweetAdapter();

        tweetsListView.setAdapter(tweetAdapter);
        tweetsListView.setLayoutManager(new LinearLayoutManager(getContext()));
        tweetsListView.setHasFixedSize(false);
        tweetsListView.addItemDecoration(new DividerItemDecorator(ResourceHelper.getDrawable(R.drawable.item_decorator_divider)));

        if (savedInstanceState != null) {
            final String searchQuery = savedInstanceState.getString(SEARCH_QUERY);
            resultsFromSearch = Parcels.unwrap(savedInstanceState.getParcelable(TWEETS_RESULTS_FROM_SEARCH));
            if (savedInstanceState.getInt(EMPTY_STATE_VISIBLE) == View.VISIBLE) {
                showNoResultsLayout(true);
            }
            if (resultsFromSearch != null) {
                tweetAdapter.addNewItems(resultsFromSearch, searchQuery);
            }
            if (savedInstanceState.getBoolean(PENDING_REQUEST)) {
                searchTweets(searchQuery);
            }

            hideKeyboard();
            root.requestFocus();
        }

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(TWEETS_RESULTS_FROM_SEARCH, Parcels.wrap(resultsFromSearch));
        outState.putString(SEARCH_QUERY, searchText.getText().toString().trim());
        outState.putBoolean(PENDING_REQUEST, pendingRequest);
        outState.putInt(EMPTY_STATE_VISIBLE, emptySateLayout.getVisibility());
    }

    @Override
    public void onDestroyView() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        progressDialog = null;
        compSub1.unsubscribe();
        super.onDestroyView();
    }

    @OnClick(R.id.search)
    public void searchOnTwitter_Click() {
        hideKeyboard();
        tweetAdapter.clearItems();
        showNoResultsLayout(false);
        if (validateSearchTextField()) {
            searchTweets(getQueryText());
        }
    }

    /**
     * Validates search input field </br>
     *
     * @return boolean is valid
     */
    private boolean validateSearchTextField() {
        if (searchText.getText().toString().trim().isEmpty()) {
            searchInputLayout.setError(ResourceHelper.getString(R.string.search_tweets_fragment_error_input_layout));
            return false;
        } else {
            searchInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    /**
     * Search for tweets based on the query. </br>
     *
     * @param searchText query
     */
    private void searchTweets(final String searchText) {
        pendingRequest = true;
        showProgressDialog();

        compSub1.add(twitterService.getTweets(searchText)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        tweetsListView.scrollToPosition(0);
                        pendingRequest = false;
                        hideProgressDialog();
                    }
                })
                .subscribe(new Action1<List<TweetEntity>>() {
                    @Override
                    public void call(List<TweetEntity> tweetEntities) {
                        resultsFromSearch = tweetEntities;
                        handleTweetEntitiesCall(tweetEntities, searchText);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Timber.e("*** ERROR - %s ***", throwable.getMessage());
                        pendingRequest = false;
                        hideProgressDialog();
                        Snackbar.make(getActivity().findViewById(R.id.root_layout), R.string.error_message_general,
                                Snackbar.LENGTH_SHORT).show();
                    }
                }));
    }

    /**
     * Handles the response from the service in the UI. </br>
     *
     * @param tweetEntities tweets
     * @param searchText query
     */
    private void handleTweetEntitiesCall(final List<TweetEntity> tweetEntities, final String searchText) {
        if (tweetEntities.isEmpty()) {
            showNoResultsLayout(true);
        } else {
            tweetAdapter.addNewItems(tweetEntities, searchText);
        }
    }

    /**
     * Shows empty state layout in the UI. </br>
     *
     * @param showLayout empty state
     */
    private void showNoResultsLayout(boolean showLayout) {
        if (showLayout) {
            tweetsListView.setVisibility(View.GONE);
            emptySateLayout.setVisibility(View.VISIBLE);
        } else {
            tweetsListView.setVisibility(View.VISIBLE);
            emptySateLayout.setVisibility(View.GONE);
        }
    }

    /**
     * Gets the search query. </br>
     *
     * @return input query
     */
    private String getQueryText() {
        return searchText.getText().toString().trim();
    }

    /**
     * Creates and shows a {@link ProgressDialog} dialog
     */
    private void showProgressDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(ResourceHelper.getString(R.string.search_tweets_fragment_progress_dialog_message));
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

    /**
     * Hides the software keyboard
     */
    private void hideKeyboard() {
        final InputMethodManager in = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
    }
}
