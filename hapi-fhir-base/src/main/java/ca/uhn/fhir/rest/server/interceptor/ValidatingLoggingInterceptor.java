package ca.uhn.fhir.rest.server.interceptor;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 University Health Network
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

import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public class ValidatingLoggingInterceptor extends BaseValidatingInterceptor {

	private boolean myLogFailures=true;
	private Class<? extends BaseServerResponseException> myThrowType = InternalErrorException.class;
	
	/**
	 * If set to <code>true</code> (which is the default) the interceptor will
	 * log any validation failures.
	 */
	public boolean isLogFailures() {
		return myLogFailures;
	}

	/**
	 * If set to <code>true</code> (which is the default) the interceptor will
	 * log any validation failures.
	 */
	public void setLogFailures(boolean theLogFailures) {
		myLogFailures = theLogFailures;
	}

	/**
	 * If set to <code>true</code> (the default is <code>false</code>) the interceptor will
	 * throw any validation failures as an exception
	 */
	public boolean isThrowFailures() {
		return myThrowFailures;
	}

	public void setThrowFailures(boolean theThrowFailures) {
		myThrowFailures = theThrowFailures;
	}





	private boolean myThrowFailures;

}
