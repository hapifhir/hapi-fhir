package ca.uhn.fhir.osgi;

/*
 * #%L
 * HAPI FHIR - OSGi Bundle
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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
