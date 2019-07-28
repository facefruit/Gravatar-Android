package org.devlang.gravatar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class GravatarResponse {
    public static final int FROM_NET = 0;
    public static final int FROM_FILE = 1;
    public static final int FROM_MEMORY = 2;

    private final Gravatar gravatar;
    private byte[] data;
    private Bitmap bitmap;
    private int fromType = FROM_NET;

    public GravatarResponse(Gravatar gravatar, byte[] data) {
        this.gravatar = gravatar;
        this.data = data;
    }

    public Gravatar getGravatar() {
        return gravatar;
    }

    public byte[] getData() {
        return data;
    }

    public Bitmap getBitmap() {
        return getBitmap(null);
    }

    public synchronized Bitmap getBitmap(BitmapFactory.Options opts) {
        if (bitmap == null) {
            if (opts == null) {
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            } else {
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
            }
        }
        return bitmap;
    }

    public Bitmap getBitmap(int width, int height) {
        Bitmap inBitmap = getBitmap();
        Bitmap outBitmap = null;
        if (inBitmap != null) {
            int inWidth = inBitmap.getWidth();
            int inHeight = inBitmap.getHeight();
            Matrix matrix = new Matrix();
            matrix.setScale(width * 1.0F / inWidth, height * 1.0F / inHeight);
            outBitmap = Bitmap.createBitmap(inBitmap, 0, 0, inWidth, inHeight, matrix, false);
        }
        return outBitmap;
    }

    public Bitmap getBitmap(int radius) {
        return getBitmap();
    }

    public int getWidth() {
        Bitmap bitmap = getBitmap();
        return bitmap == null ? 0 : bitmap.getWidth();
    }

    public int getHeight() {
        Bitmap bitmap = getBitmap();
        return bitmap == null ? 0 : bitmap.getHeight();
    }

    protected void setFromType(int fromType) {
        if (fromType != FROM_NET && fromType != FROM_FILE && fromType != FROM_MEMORY) {
            fromType = FROM_NET;
        }
        this.fromType = fromType;
    }

    public int getFromType() {
        return fromType;
    }
}
