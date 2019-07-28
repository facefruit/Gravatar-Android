package org.devlang.gravatar;

public interface GravatarCache {
    void put(Gravatar gravatar, GravatarResponse gravatarResponse);
    void clear();
    void remove(Gravatar gravatar);
    GravatarResponse get(Gravatar gravatar);
}
