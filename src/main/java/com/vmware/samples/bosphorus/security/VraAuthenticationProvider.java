package com.vmware.samples.bosphorus.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class VraAuthenticationProvider implements AuthenticationProvider {
	
	private String vraUrl;
	
	private String vraTenant;
	
	private static Log log = LogFactory.getLog(VraAuthenticationProvider.class);
	
	
	public VraAuthenticationProvider(String vraUrl, String vraTenant) {
		this.vraUrl = vraUrl;
		this.vraTenant = vraTenant;
	}

	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		if(auth instanceof UsernamePasswordAuthenticationToken) {
			try {
				return new VraToken(vraUrl, vraTenant, auth.getPrincipal().toString(), auth.getCredentials().toString());
			} catch (Exception e) {
				log.error("Trouble logging in", e);
				throw new AuthenticationServiceException("Internal error", e);
			} 
		}
		return new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials());
	}

	@Override
	public boolean supports(Class<?> clazz) {
		System.out.println(clazz);
		return clazz.equals(UsernamePasswordAuthenticationToken.class) || clazz.equals(VraToken.class);
	}

}
