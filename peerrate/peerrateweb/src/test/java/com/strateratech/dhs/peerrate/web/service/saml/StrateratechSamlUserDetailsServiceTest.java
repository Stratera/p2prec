package com.strateratech.dhs.peerrate.web.service.saml;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.impl.AssertionBuilder;
import org.opensaml.saml2.core.impl.NameIDBuilder;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/applicationContext.xml",
        "classpath:META-INF/spring/applicationContext-test.xml" })
public class StrateratechSamlUserDetailsServiceTest {
	
	@Inject
	private StrateratechSamlUserDetailsService strateratechSamlUserDetailsService;

	@Test
	public void test() {
		NameID nameID =  new NameIDBuilder().buildObject();
	    nameID.setValue("sample nameid");
		Assertion authenticationAssertion = new AssertionBuilder().buildObject();
		
		SAMLCredential creds = new SAMLCredential(nameID, authenticationAssertion, "remoteEntityId", "localEntityID");
		Object o = strateratechSamlUserDetailsService.loadUserBySAML(creds);
	}

}
