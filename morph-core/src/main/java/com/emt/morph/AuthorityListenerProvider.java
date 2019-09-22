package com.emt.morph;

import java.net.URI;

public interface AuthorityListenerProvider {

   AbstractListener getListener(URI uri);

}
