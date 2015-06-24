package ca.uhn.fhir.rest.server;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * Works like the normal {@link ca.uhn.fhir.rest.server.IncomingRequestAddressStrategy} unless there's an x-forwarded-host present, in which case that's used in place of the server's address.
 *
 * If the Apache Http Server <i>mod_proxy</i> isn't configured to supply <i>x-forwarded-proto</i>, the factory method that you use to create the address strategy will determine the default. Note that
 * <i>mod_proxy</i> doesn't set this by default, but it can be configured via <i>RequestHeader set X-Forwarded-Proto http</i> (or https)
 *
 * If you want to set the protocol based on something other than the constructor argument, you should be able to do so by overriding <i>protocol</i>.
 *
 * Note that while this strategy was designed to work with Apache Http Server, and has been tested against it, it should work with any proxy server that sets <i>x-forwarded-host</i>
 *
 * Created by Bill de Beaubien on 3/30/2015.
 */
public class ApacheProxyAddressStrategy extends IncomingRequestAddressStrategy {
	private boolean myUseHttps = false;

	protected ApacheProxyAddressStrategy(boolean theUseHttps) {
		myUseHttps = theUseHttps;
	}

	public static ApacheProxyAddressStrategy forHttp() {
		return new ApacheProxyAddressStrategy(false);
	}

	public static ApacheProxyAddressStrategy forHttps() {
		return new ApacheProxyAddressStrategy(true);
	}

	@Override
	public String determineServerBase(ServletContext theServletContext, HttpServletRequest theRequest) {
		String forwardedHost = getForwardedHost(theRequest);
		if (forwardedHost != null) {
			return forwardedServerBase(theServletContext, theRequest, forwardedHost);
		}
		return super.determineServerBase(theServletContext, theRequest);
	}

	private String getForwardedHost(HttpServletRequest theRequest) {
		String forwardedHost = theRequest.getHeader("x-forwarded-host");
		if (forwardedHost != null) {
			int commaPos = forwardedHost.indexOf(',');
			if (commaPos >= 0) {
				forwardedHost = forwardedHost.substring(0, commaPos - 1);
			}
		}
		return forwardedHost;
	}

	public String forwardedServerBase(ServletContext theServletContext, HttpServletRequest theRequest, String theForwardedHost) {
		String serverBase = super.determineServerBase(theServletContext, theRequest);
		String host = theRequest.getHeader("host");
		if (host != null) {
			serverBase = serverBase.replace(host, theForwardedHost);
			serverBase = serverBase.substring(serverBase.indexOf("://"));
			return protocol(theRequest) + serverBase;
		}
		return serverBase;
	}

	protected String protocol(HttpServletRequest theRequest) {
		String protocol = theRequest.getHeader("x-forwarded-proto");
		if (protocol != null) {
			return protocol;
		}
		return myUseHttps ? "https" : "http";
	}
}
