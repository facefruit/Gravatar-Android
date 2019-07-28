package org.devlang.gravatar;

import android.os.Handler;
import android.os.Looper;

public class GravatarHandler implements GravatarCallBack {
    public static final int THREAD_CURRENT = 0;
    public static final int THREAD_MAIN = 1;
    public static final int THREAD_NEW = 2;

    private static final Handler mainHandler = new Handler(Looper.getMainLooper());
    private Handler myHandler;

    private GravatarCallBack callBack;
    private int operation;

    public GravatarHandler(GravatarCallBack callBack, int operation) {
        this.callBack = callBack;
        this.operation = operation;
        switch (operation) {
            case THREAD_NEW:
                myHandler = null;
                break;
            case THREAD_CURRENT:
                Looper mainLooper = Looper.getMainLooper();
                Looper myLooper = Looper.myLooper();
                if (mainLooper == myLooper) {
                    myHandler = mainHandler;
                } else {
                    myHandler = new Handler(myLooper);
                }
                break;
            case THREAD_MAIN:
            default:
                myHandler = mainHandler;
                break;
        }
    }

    @Override
    public void success(final GravatarRequest request, final GravatarResponse response) {
        switch (operation) {
            case THREAD_NEW:
                if (callBack != null) {
                    callBack.success(request, response);
                }
                break;
            case THREAD_CURRENT:
            case THREAD_MAIN:
            default:
                myHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callBack != null) {
                            callBack.success(request, response);
                        }
                    }
                });
                break;
        }
        //remove thread...
        storeGravatarResponse(response);
    }

    private void storeGravatarResponse(GravatarResponse response) {
        if (response != null) {
            if (response.getFromType() == GravatarResponse.FROM_NET) {
                GravatarMemoryCache.getInstance().put(response.getGravatar(), response);
                GravatarFileCache.getInstance().put(response.getGravatar(), response);
            } else if (response.getFromType() == GravatarResponse.FROM_FILE) {
                GravatarMemoryCache.getInstance().put(response.getGravatar(), response);
            }
        }
    }

    @Override
    public void error(final GravatarRequest request, final String code, final String erroMsg) {
        switch (operation) {
            case THREAD_NEW:
                if (callBack != null) {
                    callBack.error(request, code, erroMsg);
                }
                break;
            case THREAD_CURRENT:
            case THREAD_MAIN:
            default:
                myHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callBack != null) {
                            callBack.error(request, code, erroMsg);
                        }
                    }
                });
                break;
        }
    }
}
