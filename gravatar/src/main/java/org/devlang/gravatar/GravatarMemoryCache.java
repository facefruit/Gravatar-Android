package org.devlang.gravatar;

import android.util.LruCache;

public class GravatarMemoryCache implements GravatarCache {
    private static LruCache<String, byte[]> mLruCache;

    private GravatarMemoryCache(int maxSize) {
        mLruCache = new LruCache<>(maxSize);
    }

    @Override
    public void put(Gravatar gravatar, GravatarResponse gravatarResponse) {
        mLruCache.put(gravatar.getGravatarUrl(), gravatarResponse.getData());
    }

    @Override
    public void clear() {
        mLruCache.evictAll();
    }

    @Override
    public void remove(Gravatar gravatar) {
        if (gravatar != null) {
            mLruCache.remove(gravatar.getGravatarUrl());
        }
    }

    @Override
    public GravatarResponse get(Gravatar gravatar) {
        GravatarResponse response = null;
        byte[] data = mLruCache.get(gravatar.getGravatarUrl());
        if (data != null && data.length > 0) {
            response = new GravatarResponse(gravatar, data);
            response.setFromType(GravatarResponse.FROM_MEMORY);
        }
        GravatarLog.i(response == null ? "GravatarResponse break out memory" : "GravatarResponse hit at memory");
        return response;
    }

    static GravatarMemoryCache getInstance() {
        return Singleton.INSTANCE;
    }

    private static class Singleton {
        static final GravatarMemoryCache INSTANCE = new GravatarMemoryCache(GravatarManager.getMemoryMaxSize());
    }
}
