package org.devlang.gravatar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class GravatarFileCache implements GravatarCache {

    private String fileCachePath;

    private GravatarFileCache(String path) {
        this.fileCachePath = path;
    }

    @Override
    public void put(Gravatar gravatar, GravatarResponse gravatarResponse) {
        File cacheDirect = new File(fileCachePath);
        if (!cacheDirect.exists()) {
            cacheDirect.mkdirs();
        }
        String fileName = Md5Util.md5Hex(gravatar.getGravatarUrl());
        File file = new File(cacheDirect, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(gravatarResponse.getData());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clear() {
        File cacheDirect = new File(fileCachePath);
        if (cacheDirect.exists()) {
            cacheDirect.delete();
        }
    }

    @Override
    public void remove(Gravatar gravatar) {
        File cacheDirect = new File(fileCachePath);
        if (cacheDirect.exists()) {
            String fileName = Md5Util.md5Hex(gravatar.getGravatarUrl());
            File file = new File(cacheDirect, fileName);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    @Override
    public GravatarResponse get(Gravatar gravatar) {
        GravatarResponse response = null;
        File cacheDirect = new File(fileCachePath);
        if (cacheDirect.exists()) {
            String fileName = Md5Util.md5Hex(gravatar.getGravatarUrl());
            File file = new File(cacheDirect, fileName);
            if (file.exists()) {
                try {
                    FileInputStream fis = new FileInputStream(file);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] read = new byte[1024 * 8];
                    int length;
                    while ((length = fis.read(read)) != -1) {
                        bos.write(read, 0, length);
                    }
                    fis.close();
                    byte[] data = bos.toByteArray();
                    bos.close();
                    response = new GravatarResponse(gravatar, data);
                    response.setFromType(GravatarResponse.FROM_FILE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            GravatarLog.i(response == null ? "GravatarResponse break out file" : "GravatarResponse hit at file(" + file.getAbsolutePath() + ")");
        } else {
            GravatarLog.i("GravatarResponse break out file");
        }
        return response;
    }

    static GravatarFileCache getInstance() {
        return Singleton.INSTANCE;
    }

    private static class Singleton {
        static final GravatarFileCache INSTANCE = new GravatarFileCache(GravatarManager.getFileCachePath());
    }
}
