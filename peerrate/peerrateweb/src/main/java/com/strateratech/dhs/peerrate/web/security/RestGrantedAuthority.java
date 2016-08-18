package com.strateratech.dhs.peerrate.web.security;

import org.springframework.security.core.GrantedAuthority;

import com.strateratech.dhs.peerrate.web.enumeration.ApplicationPermission;
import com.strateratech.dhs.peerrate.web.enumeration.ApplicationRole;


/**
 * @author 2020
 * @date Mar 9, 2016
 * @version 
 *
 */
public class RestGrantedAuthority implements GrantedAuthority {

	private static final long serialVersionUID = 1L;
	
	private String tenant;
	private ApplicationRole role;
	private ApplicationPermission permission;

	/**
	 * Overriding org.springframework.security.core.GrantedAuthority#getAuthority()
	 * 
	 */
	@Override
	public String getAuthority() {
		return getPermission().name();
	}
	

	/**
	 * Construct a role with the organization + role + permission
	 * 
	 * @param org
	 * @param role
	 * @param permission
	 */
	public RestGrantedAuthority(String tenant, ApplicationRole role, ApplicationPermission permission) {
		super();
		this.tenant = tenant;
		this.role = role;
		this.permission = permission;
	}



	/**
     * @return the tenant
     */
    public String getTenant() {
        return tenant;
    }


    /**
     * @param tenant the tenant to set
     */
    public void setTenant(String tenant) {
        this.tenant = tenant;
    }


    /**
	 * Method to get the role.
	 * 
	 * @return role
	 */
	public ApplicationRole getRole() {
		return role;
	}

	/**
	 * Method to get the Application Permission.
	 * 
	 * @return permission
	 */
	public ApplicationPermission getPermission() {
		return permission;
	}

    /**
	 * Overriding - java.lang.Object#toString()
	 * 
	 */
	@Override
	public String toString() {
		return "RestGrantedAuthority [tenant=" + tenant + ", role=" + role + ", permission=" + permission + "]";
	}	

}
