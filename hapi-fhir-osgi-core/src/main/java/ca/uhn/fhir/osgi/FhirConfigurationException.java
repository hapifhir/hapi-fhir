/**
 * This software has been produced by Akana, Inc. under a professional services
 * agreement with our customer. This work may contain material that is confidential 
 * and proprietary information of Akana, Inc. and is subject to copyright 
 * protection under laws of the United States of America and other countries. 
 * Akana, Inc. grants the customer non-exclusive rights to this material without
 * any warranty expressed or implied. 
 */

package ca.uhn.fhir.osgi;

/**
 * Exception thrown from the Spring-DM/OSGi wiring. These
 * exceptions are thrown when an error was encountered
 * that was caused by incorrect wiring.
 *
 * @author Akana, Inc. Professional Services
 *
 */
public class FhirConfigurationException extends Exception {

	public FhirConfigurationException() {
		super();
	}

	public FhirConfigurationException(String message) {
		super(message);
	}

	public FhirConfigurationException(Throwable cause) {
		super(cause);
	}

	public FhirConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

}
