package ca.uhn.fhir.rest.server.interceptor;

/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.uhn.fhir.rest.api.Constants;
import org.apache.commons.lang3.Validate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsProcessor;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.DefaultCorsProcessor;

import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public class CorsInterceptor extends InterceptorAdapter {

	private CorsProcessor myCorsProcessor;
	private CorsConfiguration myConfig;

	/**
	 * Constructor which creates an interceptor with default CORS configuration for use in
	 * a FHIR server. This includes:
	 * <ul>
	 * <li>Allowed Origin: *</li>
	 * <li>Allowed Header: Accept</li>
	 * <li>Allowed Header: Access-Control-Request-Headers</li>
	 * <li>Allowed Header: Access-Control-Request-Method</li>
	 * <li>Allowed Header: Cache-Control</li>
	 * <li>Exposed Header: Content-Location</li>
	 * <li>Allowed Header: Content-Type</li>
	 * <li>Exposed Header: Location</li>
	 * <li>Allowed Header: Origin</li>
	 * <li>Allowed Header: Prefer</li>
	 * <li>Allowed Header: X-Requested-With</li>
	 * </ul>
	 * Note that this configuration is useful for quickly getting CORS working, but
	 * in a real production system you probably want to consider whether it is
	 * appropriate for your situation. In particular, using "Allowed Origin: *"
	 * isn't always the right thing to do.
	 */
	public CorsInterceptor() {
		this(createDefaultCorsConfig());
	}

	/**
	 * Constructor which accepts the given configuration
	 *
	 * @param theConfiguration
	 *           The CORS configuration
	 */
	public CorsInterceptor(CorsConfiguration theConfiguration) {
		Validate.notNull(theConfiguration, "theConfiguration must not be null");
		myCorsProcessor = new DefaultCorsProcessor();
		setConfig(theConfiguration);
	}

	/**
	 * Gets the CORS configuration
	 */
	public CorsConfiguration getConfig() {
		return myConfig;
	}

	/**
	 * Sets the CORS configuration
	 */
	public void setConfig(CorsConfiguration theConfiguration) {
		myConfig = theConfiguration;
	}

	@Override
	public boolean incomingRequestPreProcessed(HttpServletRequest theRequest, HttpServletResponse theResponse) {
		if (CorsUtils.isCorsRequest(theRequest)) {
			boolean isValid;
			try {
				isValid = myCorsProcessor.processRequest(myConfig, theRequest, theResponse);
			} catch (IOException e) {
				throw new InternalErrorException(Msg.code(326) + e);
			}
			if (!isValid || CorsUtils.isPreFlightRequest(theRequest)) {
				return false;
			}
		}

		return super.incomingRequestPreProcessed(theRequest, theResponse);
	}

	private static CorsConfiguration createDefaultCorsConfig() {
		CorsConfiguration retVal = new CorsConfiguration();

		retVal.setAllowedHeaders(new ArrayList<>(Constants.CORS_ALLOWED_HEADERS));
		retVal.setAllowedMethods(new ArrayList<>(Constants.CORS_ALLWED_METHODS));

		retVal.addExposedHeader("Content-Location");
		retVal.addExposedHeader("Location");

		retVal.addAllowedOrigin("*");


		return retVal;
	}

}
