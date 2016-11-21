package ca.uhn.fhir.rest.server.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	 * Constructor
	 */
	public CorsInterceptor() {
		this(new CorsConfiguration());
	}

	/**
	 * Constructor
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
	 * Sets the CORS configuration
	 */
	public void setConfig(CorsConfiguration theConfiguration) {
		myConfig = theConfiguration;
	}

	/**
	 * Gets the CORS configuration
	 */
	public CorsConfiguration getConfig() {
		return myConfig;
	}

	@Override
	public boolean incomingRequestPreProcessed(HttpServletRequest theRequest, HttpServletResponse theResponse) {
		if (CorsUtils.isCorsRequest(theRequest)) {
			boolean isValid;
			try {
				isValid = myCorsProcessor.processRequest(myConfig, theRequest, theResponse);
			} catch (IOException e) {
				throw new InternalErrorException(e);
			}
			if (!isValid || CorsUtils.isPreFlightRequest(theRequest)) {
				return false;
			}
		}

		return super.incomingRequestPreProcessed(theRequest, theResponse);
	}

}
