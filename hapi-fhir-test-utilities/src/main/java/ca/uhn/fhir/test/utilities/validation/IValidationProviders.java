/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.test.utilities.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.util.ClasspathUtil;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IDomainResource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IValidationProviders {
	String CODE_SYSTEM = "http://code.system/url";
	String CODE_SYSTEM_VERSION = "1.0.0";
	String CODE_SYSTEM_NAME = "Test Code System";
	String CODE = "CODE";
	String VALUE_SET_URL = "http://value.set/url";
	String DISPLAY = "Explanation for code TestCode.";
	String LANGUAGE = "en";
	String ERROR_MESSAGE = "This is an error message";

	interface IMyValidationProvider extends IResourceProvider {
		void addException(String theOperation, String theUrl, String theCode, Exception theException);
		<P extends IBaseParameters> void addTerminologyResponse(String theOperation, String theUrl, String theCode, P theReturnParams);
		IBaseParameters addTerminologyResponse(String theOperation, String theUrl, String theCode, FhirContext theFhirContext, String theTerminologyResponseFile);
	}

	abstract class MyValidationProvider<T extends IDomainResource> implements IMyValidationProvider {
		private final Map<String, Exception> myExceptionMap = new HashMap<>();
		private boolean myShouldThrowExceptionForResourceNotFound = true;
		private final Map<String, IBaseParameters> myTerminologyResponseMap = new HashMap<>();
		private final Map<String, T> myTerminologyResourceMap = new HashMap<>();

		static String getInputKey(String theOperation, String theUrl, String theCode) {
			return theOperation + "-" + theUrl + "#" + theCode;
		}

		public void setShouldThrowExceptionForResourceNotFound(boolean theShouldThrowExceptionForResourceNotFound) {
			myShouldThrowExceptionForResourceNotFound = theShouldThrowExceptionForResourceNotFound;
		}

		public void addException(String theOperation, String theUrl, String theCode, Exception theException) {
			String inputKey = getInputKey(theOperation, theUrl, theCode);
			myExceptionMap.put(inputKey, theException);
		}

		abstract Class<? extends IBaseParameters> getParameterType();

		@Override
		public <P extends IBaseParameters> void addTerminologyResponse(String theOperation, String theUrl, String theCode, P theReturnParams) {
			myTerminologyResponseMap.put(getInputKey(theOperation, theUrl, theCode), theReturnParams);
		}

		public IBaseParameters addTerminologyResponse(String theOperation, String theUrl, String theCode, FhirContext theFhirContext, String theTerminologyResponseFile) {
			IBaseParameters responseParams = ClasspathUtil.loadResource(theFhirContext, getParameterType(), theTerminologyResponseFile);
			addTerminologyResponse(theOperation, theUrl, theCode, responseParams);
			return responseParams;
		}

		protected void addTerminologyResource(String theUrl, T theResource) {
			myTerminologyResourceMap.put(theUrl, theResource);
		}

		public abstract T addTerminologyResource(String theUrl);

		protected IBaseParameters getTerminologyResponse(String theOperation, String theUrl, String theCode) throws Exception {
			String inputKey = getInputKey(theOperation, theUrl, theCode);
			if (myExceptionMap.containsKey(inputKey)) {
				throw myExceptionMap.get(inputKey);
			}
			IBaseParameters params = myTerminologyResponseMap.get(inputKey);
			if (params == null) {
				throw new IllegalStateException("Test setup incomplete. Missing return params for " + inputKey);
			}
			return params;
		}

		protected T getTerminologyResource(UriParam theUrlParam) {
			if (theUrlParam.isEmpty()) {
				throw new IllegalStateException("CodeSystem url should not be null.");
			}
			String urlValue = theUrlParam.getValue();
			if (!myTerminologyResourceMap.containsKey(urlValue) && myShouldThrowExceptionForResourceNotFound) {
				throw new IllegalStateException("Test setup incomplete. CodeSystem not found " + urlValue);
			}
			return myTerminologyResourceMap.get(urlValue);
		}

		@Search
		public List<T> find(@RequiredParam(name = "url") UriParam theUrlParam) {
			T resource = getTerminologyResource(theUrlParam);
			return resource != null ? List.of(resource) : List.of();
		}
	}

	interface IMyLookupCodeProvider extends IResourceProvider {
		void setLookupCodeResult(IValidationSupport.LookupCodeResult theLookupCodeResult);
	}
}
