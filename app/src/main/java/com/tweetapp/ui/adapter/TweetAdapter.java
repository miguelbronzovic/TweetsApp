package com.tweetapp.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tweetapp.R;
import com.tweetapp.data.TweetEntity;
import com.tweetapp.helper.DateHelper;
import com.tweetapp.helper.HtmlHelper;
import com.tweetapp.helper.PicassoHelper;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Miguel Bronzovic.
 */
public final class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.TweetViewHolder> {
    private List<TweetEntity> values = new ArrayList<>();
    private String searchQuery;

    public void addNewItems(List<TweetEntity> items, String queryText) {
        searchQuery = queryText;

        values.clear();

        values = items;

        notifyDataSetChanged();
    }

    public void clearItems() {
        values.clear();

        notifyDataSetChanged();
    }

    @Override
    public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tweet_item, parent, false);

        return new TweetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TweetViewHolder holder, int position) {
        final TweetEntity item = values.get(position);

        holder.mediaImage.setVisibility(View.GONE);

        PicassoHelper.getInstance()
                .load(item.userProfileImageUrl)
                .fit()
                .centerInside()
                .into(holder.profileImage);

        holder.name.setText(item.userName);
        holder.screenName.setText(item.userScreenName);

        final String diff = DateHelper.calculateElapsedTimeString(item.created_at, DateTime.now());
        holder.created.setText(diff);

        final SpannableString parsedText = HtmlHelper.highlightHashTags(item.text, searchQuery);
        holder.text.setText(parsedText);

        if (!TextUtils.isEmpty(item.mediaUrl)) {
            holder.mediaImage.setVisibility(View.VISIBLE);

            PicassoHelper.getInstance()
                    .load(item.mediaUrl)
                    .fit()
                    .centerInside()
                    .into(holder.mediaImage);
        }
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class TweetViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.profile_image)
        ImageView profileImage;

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.screen_name)
        TextView screenName;

        @BindView(R.id.created)
        TextView created;

        @BindView(R.id.text)
        TextView text;

        @BindView(R.id.media_image)
        ImageView mediaImage;

        public TweetViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            text.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
}
