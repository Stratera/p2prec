package com.strateratech.dhs.peerrate.web.utils;

import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for handling JNDI tasks.
 * 
 * @author 2020
 * @version 1.0
 * @date: 08/4/2015
 *
 */
public final class JndiUtils {
    private static final Logger log = LoggerFactory.getLogger(JndiUtils.class);

    private static final String[] PREFIXES = new String[] { "java:", // regular
                                                                     // java
            "java:comp/env/", // tomcat default
            "java:/jboss/", // Jboss convention 1
            "java:/" // jboss convention 2
    };

    private JndiUtils() {
        // private constructor. Use static methods.
    }

    /**
     * Provides compatibility with real JEE containers and Tomcat while keeping
     * JNDI resource injection separate.
     * 
     * @param jndiName
     * @return
     * @throws NamingException
     */
    public static Object getJndiProperty(String jndiName) throws NamingException {

        Context ctx = new javax.naming.InitialContext();

        Object value = null;

        try {
            value = ctx.lookup(jndiName);
        } catch (NamingException e) {
            log.warn("Could not find property AT " + jndiName);
        }
        if (value == null) {
            for (int i = 0; i < PREFIXES.length; i++) {
                String jndiPlusPrefix = PREFIXES[i] + jndiName;
                log.debug("looking up {}", jndiPlusPrefix);
                try {
                    value = ctx.lookup(jndiPlusPrefix);
                    break;
                } catch (NameNotFoundException nnfe) {
                    log.warn("Unable to locate JNDI Resource named: {}" + jndiPlusPrefix);
                }
            }
        }

        return value;

    }
}
