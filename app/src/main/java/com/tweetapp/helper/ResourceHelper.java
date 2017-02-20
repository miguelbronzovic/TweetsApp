package com.tweetapp.helper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

/**
 * Created by Miguel Bronzovic.
 */
public final class ResourceHelper {
    private static Resources resources;

    private ResourceHelper() {}

    public static void init(Context context) {
     resources = context.getResources();
    }

    /**
     * Retrieve the string for the given id. </br>
     *
     * @param {@link int} resourceId
     * @return the corresponding string
     */
    public static String getString(int resourceId) {
        return resources.getString(resourceId);
    }

    /**
     * Retrieves the resource id´s drawable. </br>
     *
     * @param {@link int} resourceId
     * @return {@link Drawable} drawable
     */
    public static Drawable getDrawable(int resourceId) {
        return resources.getDrawable(resourceId);
    }

    /**
     * Retrieves the resource id´s color value. </br>
     *
     * @param {@link int} resId
     * @return int color value
     */
    public static int getColor(int resId) {
        return resources.getColor(resId);
    }
}
