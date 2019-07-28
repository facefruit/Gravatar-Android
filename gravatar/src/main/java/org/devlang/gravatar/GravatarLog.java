package org.devlang.gravatar;

import android.util.Log;

public class GravatarLog {
    public static final String DEFAULT_TAG = "DEVLANG.ORG";

    public static String smTag = DEFAULT_TAG;

    public static String getSmTag() {
        return smTag;
    }

    public static void setSmTag(String smTag) {
        GravatarLog.smTag = smTag;
    }

    public static void d(String msg) {
        d(smTag, msg);
    }

    public static void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    public static void i(String msg) {
        i(smTag, msg);
    }

    public static void i(String tag, String msg) {
        Log.i(tag, msg);
    }
}
