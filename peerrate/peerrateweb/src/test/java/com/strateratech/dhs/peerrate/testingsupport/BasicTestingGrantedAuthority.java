package com.strateratech.dhs.peerrate.testingsupport;

import org.springframework.security.core.GrantedAuthority;

/**
 * Basic Granted authority built for testing.  This object is basically 
 * a Granted authority/Permision implementation based on a string.  This
 * implementation does not have domain or facetted support (IE. >Dev>peerrateweb>admin 
 * where permissions would be orgainized into some 
 * Directory-based hierarchy)
 * 
 * @author 2020
 * @date Oct 19, 2015
 * @version 1.2
 * @since 1.1
 */
public class BasicTestingGrantedAuthority implements GrantedAuthority {
    private static final long serialVersionUID = 1L;
    private String authority;

    public BasicTestingGrantedAuthority(String authority) {
        super();
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

}
