package org.devlang.gravatar;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GravatarExecutor {
    private static ExecutorService smGravatarThread = Executors.newFixedThreadPool(5);

    private GravatarExecutor(){

    }

    public static void execute(GravatarTask task) {
        smGravatarThread.execute(task);
    }
}
