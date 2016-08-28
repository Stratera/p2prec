package com.strateratech.dhs.peerrate.web.enumeration;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;


public class ApplicationRoleTest {

    @Test
    public void testByRoleName() {
        ApplicationRole role = ApplicationRole.byRoleName("ROLE_EMPLOYEE");
        Assert.assertEquals(ApplicationRole.EMPLOYEE, role);
    }

    @Test
    public void testGetPermissions() {
        List<ApplicationPermission> permissions = ApplicationRole.EMPLOYEE.getPermissions();
        Assert.assertNotNull(permissions);
        Assert.assertFalse(permissions.isEmpty());
    }

}
