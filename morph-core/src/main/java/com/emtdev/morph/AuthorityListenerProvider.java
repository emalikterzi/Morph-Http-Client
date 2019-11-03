package com.emtdev.morph;

import java.net.URI;

public interface AuthorityListenerProvider {

   AbstractListener getListener(URI uri);

}
