package org.devlang.gravatar;

public class GravatarTask implements Runnable {
    private final GravatarRequest gravatarRequest;

    public GravatarTask(GravatarRequest request) {
        this.gravatarRequest = request;
    }

    @Override
    public void run() {
        gravatarRequest.execute();
    }
}
