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

import ca.uhn.fhir.rest.server.IncomingRequestAddressStrategy;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * Works like the normal {@link ca.uhn.fhir.rest.server.IncomingRequestAddressStrategy} unless there's an
 * x-forwarded-host present, in which case that's used in place of the server's address.
 *
 * Because Apache Http Server's mod_proxy doesn't supply <i>x-forwarded-proto</i>, you have to tell it whether to use http or https
 * by using the appropriate factory method.
 *
 * If you want to make that determination based on something other than the constructor argument, you should be able to do so
 * by overriding <i>prefix</i>.
 *
 * Note that while this strategy was designed to work with Apache Http Server, and has been tested against it, it should work with
 * any proxy server that sets <i>x-forwarded-host</i>
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
        String forwardedHost = theRequest.getHeader("x-forwarded-host");
        if (forwardedHost != null) {
            int commaPos = forwardedHost.indexOf(',');
            if (commaPos >= 0) {
                forwardedHost = forwardedHost.substring(0, commaPos - 1);
            }
            String requestFullPath = StringUtils.defaultString(theRequest.getRequestURI());
            String serverBase = prefix(theRequest) + forwardedHost + requestFullPath;
            return serverBase;
        }
        return super.determineServerBase(theServletContext, theRequest);
    }

    protected String prefix(HttpServletRequest theRequest) {
        return myUseHttps ? "https://" : "http://";
    }
}
