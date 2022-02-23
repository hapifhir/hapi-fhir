package ca.uhn.fhir.context;

/*
 * #%L
 * HAPI FHIR - Core Library
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

/**
 * Non-checked exception indicating that HAPI was unable to initialize due to 
 * a detected configuration problem.
 */
public class ConfigurationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ConfigurationException() {
		super();
	}

	public ConfigurationException(String theMessage, Throwable theCause) {
		super(theMessage, theCause);
	}

	public ConfigurationException(String theMessage) {
		super(theMessage);
	}

	public ConfigurationException(Throwable theCause) {
		super(theCause);
	}

}
