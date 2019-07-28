package org.devlang.gravatar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class GravatarRequestManager {
    private static Map<Object, List<GravatarRequest>> smGravatarRequestMap = new HashMap<>();


    private GravatarRequestManager() {

    }

    void add(Object tag, GravatarRequest request) {
        List<GravatarRequest> list = smGravatarRequestMap.get(tag);
        if (list == null) {
            list = new ArrayList<>();
            smGravatarRequestMap.put(tag, list);
        }
        list.add(request);
    }

    void cancel(Object tag) {
        List<GravatarRequest> list = smGravatarRequestMap.remove(tag);
        if (list != null) {
            for (GravatarRequest request : list) {
                request.cancel();
            }
        }
    }

    void cancelAll() {
        Set<Object> keySet = smGravatarRequestMap.keySet();
        for (Object key : keySet) {
            cancel(key);
        }
    }

    void cancel(GravatarRequest request) {
        List<GravatarRequest> list = smGravatarRequestMap.get(request.getTag());
        if (list != null) {
            for (GravatarRequest gravatarRequest : list) {
                if (gravatarRequest == request) {
                    gravatarRequest.cancel();
                    return;
                }
            }
        }
    }

    void end(GravatarRequest request) {
        List<GravatarRequest> list = smGravatarRequestMap.get(request.getTag());
        if (list != null) {
            list.remove(request);
        }
    }

    static class Singleton {
        static final GravatarRequestManager INSTANCE = new GravatarRequestManager();
    }
}
