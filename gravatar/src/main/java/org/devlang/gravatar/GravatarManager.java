package org.devlang.gravatar;

import android.os.Environment;

public class GravatarManager {
    private static long smDefaultSize = 80;
    private static String smDefaultImage = Gravatar.IMAGE_DEFAULT;
    private static boolean smForceDefault = false;
    private static String smRating = Gravatar.RATING_G;
    private static boolean smSecureRequest = true;
    private static int smMemoryMaxSize = 1024 * 1024 * 8 * 5;
    private static String smFileCachePath = Environment.getExternalStorageDirectory() + "/gravatar/";

    private GravatarManager() {
    }

    public static long getDefaultSize() {
        return smDefaultSize;
    }

    public static void setSize(long defaultSize) {
        GravatarManager.smDefaultSize = defaultSize;
    }

    public static String getDefaultImage() {
        return smDefaultImage;
    }

    public static void setDefaultImage(String defaultImage) {
        GravatarManager.smDefaultImage = defaultImage;
    }

    public static boolean isForceDefault() {
        return smForceDefault;
    }

    public static void setForceDefault(boolean forceDefault) {
        GravatarManager.smForceDefault = forceDefault;
    }

    public static String getRating() {
        return smRating;
    }

    public static void setRating(String rating) {
        GravatarManager.smRating = rating;
    }

    public static boolean isSecureRequest() {
        return smSecureRequest;
    }

    public static void setSecureRequest(boolean secureRequest) {
        GravatarManager.smSecureRequest = secureRequest;
    }

    public static void enqueue(GravatarRequest request) {
        enqueue(request, request);
    }

    public static void enqueue(Object tag, GravatarRequest request) {
        GravatarTask gravatarTask = request.newTask(tag);
        GravatarExecutor.execute(gravatarTask);
        GravatarRequestManager.Singleton.INSTANCE.add(request.getTag(), request);
    }

    public static GravatarResponse execute(GravatarRequest request) {
        GravatarRequestManager.Singleton.INSTANCE.add(request.getTag(), request);
        return request.execute();
    }

    public static void cancel(Object tag) {
        GravatarRequestManager.Singleton.INSTANCE.cancel(tag);
    }

    public static void cancel(GravatarRequest request) {
        GravatarRequestManager.Singleton.INSTANCE.cancel(request);
    }

    public static void cancelAll() {
        GravatarRequestManager.Singleton.INSTANCE.cancelAll();
    }

    public static void setMemoryCache(int size) {
        GravatarManager.smMemoryMaxSize = size;
    }

    public static int getMemoryMaxSize() {
        return GravatarManager.smMemoryMaxSize;
    }

    public static void setFileCachePath(String path) {
        GravatarManager.smFileCachePath = path;
    }

    public static String getFileCachePath() {
        return GravatarManager.smFileCachePath;
    }
}
