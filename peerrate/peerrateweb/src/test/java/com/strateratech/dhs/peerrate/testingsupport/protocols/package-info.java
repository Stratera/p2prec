/**
 * This package is required to contain subpackages for individual URL 
 * protocol handlers.  
 * 
 * There is a child package of this one called classpath.  Under 
 * classpath is a Handler.java that extends the URLStreamHandler.  
 * For all protocols you need to support ,add a subpackage with the name 
 * matching the protocol and then define a Handler class in it. 
 */
package com.strateratech.dhs.peerrate.testingsupport.protocols;
