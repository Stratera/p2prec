package com.strateratech.dhs.peerrate.web.jee.listener;

import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Loads all the properties in the build manifest into a map and puts it on the
 * app context.
 * 
 * @author 2020
 * @version 1.0
 * @date: 08/4/2015
 *
 */
public class InitAppContextPropertiesListener implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(InitAppContextPropertiesListener.class);

    /**
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.
     *      ServletContextEvent)
     * 
     * @param arg0
     */
    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        // do nothing
    }

    /**
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.
     *      ServletContextEvent)
     * 
     * @param servletContextEvent
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        log.info("Context initializing");

        InputStream is = null;

        try {
            ServletContext ctx = servletContextEvent.getServletContext();

            is = ctx.getResourceAsStream("/META-INF/MANIFEST.MF");

            Properties props = new Properties();

            if (is != null) {
                props.load(is);
            }

            HashMap<String, String> manifest = new HashMap<String, String>();
            if (props != null) {
                for (Entry<Object, Object> prop : props.entrySet()) {
                    try {
                        if (!prop.getKey().toString().equalsIgnoreCase("class-path")) {
                            manifest.put(prop.getKey().toString(), prop.getValue().toString());
                        }
                    } catch (Exception e) {
                        log.warn("Exception caught settings manifest properties.", e);
                    }
                }
            }
            ctx.setAttribute("manifest", manifest);
            ctx.setAttribute("jvmStartTime", new Date(ManagementFactory.getRuntimeMXBean().getStartTime()));
        } catch (IOException e) {
            log.error("IOException caught reading manifest.", e);
        }
    }

}
