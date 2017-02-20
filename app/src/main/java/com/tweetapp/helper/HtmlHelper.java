package com.tweetapp.helper;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.tweetapp.R;

/**
 * Created by Miguel Bronzovic.
 */
public final class HtmlHelper {
    private final static char HASH_TAG_CHAR = '#';
    private final static char SPACE_CHAR = ' ';

    private HtmlHelper(){}

    /**
     * Highlights hashtags from given text. </br>
     *
     * http://stackoverflow.com/questions/22291644/how-to-change-color-of-words-with-hashtags
     *
     * @param inputText value
     * @param textTohighlight value
     * @return {@link SpannableString} highlighted
     */
    public static SpannableString highlightHashTags(final String inputText, final String textTohighlight) {
        final SpannableString string = new SpannableString(inputText);

        int start = -1;
        for (int i = 0; i < inputText.length(); i++) {
            if (inputText.charAt(i) == HASH_TAG_CHAR) {
                start = i;
            } else if (inputText.charAt(i) == SPACE_CHAR || (i == inputText.length() - 1 && start != -1)) {
                if (start != -1) {
                    if (i == inputText.length() - 1) {
                        i++; // case for if hash is last word and there is no
                        // space after word
                    }
                    final String tag = inputText.substring(start, i);
                    string.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            // add color in hash tag
                            ds.setColor(ResourceHelper.getColor(R.color.colorHashTag));
                            ds.setUnderlineText(false);
                            if (tag.equalsIgnoreCase(textTohighlight)) {
                                ds.setFlags(TextPaint.FAKE_BOLD_TEXT_FLAG);
                            }
                        }
                    }, start, i, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    start = -1;
                }
            }
        }

        return string;
    }
}
