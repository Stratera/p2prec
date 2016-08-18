/**
 * 
 */
package com.strateratech.dhs.peerrate.testingsupport.protocols.classpath;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * This Handler handles resolving a the classpath: urls for tests.  
 * We do not support this for production use.
 * @author 2020
 * @date Nov 25, 2015 3:08:20 PM
 * @version 
 */
public class Handler extends URLStreamHandler {

    @Override
    protected URLConnection openConnection(final URL u) throws IOException {
        final URL resourceUrl = Thread.currentThread().getContextClassLoader().getSystemClassLoader().getResource(u.getPath());
        return resourceUrl.openConnection();
    }
}