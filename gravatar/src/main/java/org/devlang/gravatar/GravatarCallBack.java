package org.devlang.gravatar;

public interface GravatarCallBack {
    void success(GravatarRequest request, GravatarResponse response);
    void error(GravatarRequest request, String code, String erroMsg);
}
