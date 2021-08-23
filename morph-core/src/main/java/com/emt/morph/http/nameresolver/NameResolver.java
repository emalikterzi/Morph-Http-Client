package com.emt.morph.http.nameresolver;

import java.net.URI;
import java.util.List;

public interface NameResolver {

    /**
     * SHOULD BE LOCK
     *
     * @param uri
     * @return
     */
    List<InetSocketAddress> resolveAvailable(URI uri);

}
