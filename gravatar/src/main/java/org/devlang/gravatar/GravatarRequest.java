package org.devlang.gravatar;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GravatarRequest {
    private final Gravatar gravatar;
    private GravatarCallBack callBack;
    private boolean isCancel;
    private Object tag;

    public static GravatarRequest obtain(Gravatar gravatar) {
        return obtain(gravatar, null);
    }

    public static GravatarRequest obtain(Gravatar gravatar, GravatarCallBack callBack) {
        return obtain(gravatar, callBack, GravatarHandler.THREAD_MAIN);
    }

    public static GravatarRequest obtain(Gravatar gravatar, GravatarCallBack callBack, int operation) {
        return new GravatarRequest(gravatar, new GravatarHandler(callBack, operation));
    }

    private GravatarRequest(Gravatar gravatar, GravatarCallBack callBack) {
        this.gravatar = gravatar;
        this.callBack = callBack;
    }

    GravatarTask newTask(Object tag) {
        setTag(tag);
        return new GravatarTask(this);
    }

    GravatarResponse execute() {
        GravatarResponse response = null;
        if (!isCancel) {
            response = GravatarMemoryCache.getInstance().get(gravatar);
            if (response == null && !isCancel) {
                response = GravatarFileCache.getInstance().get(gravatar);
                if (response == null && !isCancel) {
                    response = request();
                }
            }
        }
        callBackSuccess(this, response);
        return response;
    }

    void cancel() {
        isCancel = true;
    }

    Object getTag() {
        if (tag == null) {
            return this;
        }
        return tag;
    }

    void setTag(Object tag) {
        this.tag = tag;
    }

    private GravatarResponse request() {
        GravatarResponse response = null;
        try {
            Log.d("cmf", "gravatarUrl = " + gravatar.getGravatarUrl());
            URL url = new URL(gravatar.getGravatarUrl());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == 200) {
                InputStream is = urlConnection.getInputStream();
                byte[] read = new byte[1024 * 1024];
                int length;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                while ((length = is.read(read)) != -1) {
                    bos.write(read, 0, length);
                }
                is.close();
                byte[] data = bos.toByteArray();
                Log.d("cmf", "data.length = " + data.length);
                bos.close();
                response = new GravatarResponse(gravatar, data);
                response.setFromType(GravatarResponse.FROM_NET);
            } else {
                callBackError(this, String.valueOf(responseCode), urlConnection.getResponseMessage());
            }
        } catch (MalformedURLException e) {
            callBackError(this, MalformedURLException.class.getSimpleName(), e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            callBackError(this, IOException.class.getSimpleName(), e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    private void callBackSuccess(GravatarRequest request, GravatarResponse response) {
        if (callBack != null && !isCancel) {
            callBack.success(request, response);
        }
        GravatarRequestManager.Singleton.INSTANCE.end(request);
    }

    private void callBackError(GravatarRequest request, String code, String errorMsg) {
        if (callBack != null && !isCancel) {
            callBack.error(request, code, errorMsg);
        }
        GravatarRequestManager.Singleton.INSTANCE.end(request);
    }
}
