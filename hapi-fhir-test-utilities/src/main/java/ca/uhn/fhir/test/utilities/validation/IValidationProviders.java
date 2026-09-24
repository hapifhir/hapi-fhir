/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.util.ClasspathUtil;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IDomainResource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

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
		<P extends IBaseParameters> void addTerminologyResponse(String theOperation, String theUrl, String theCodeSystemVersion, String theCode, P theReturnParams);
		IBaseParameters addTerminologyResponse(String theOperation, String theUrl, String theCodeSystemVersion, String theCode, FhirContext theFhirContext, String theTerminologyResponseFile);
	}

	abstract class MyValidationProvider<T extends IDomainResource> implements IMyValidationProvider {
		private final Map<String, Exception> myExceptionMap = new HashMap<>();
		private boolean myShouldThrowExceptionForResourceNotFound = true;
		private final Map<String, IBaseParameters> myTerminologyResponseMap = new HashMap<>();
		private final Map<String, T> myTerminologyResourceMap = new HashMap<>();

		static String getInputKey(String theOperation, String theUrl, String theCode) {
			return getInputKey(theOperation, theUrl, null, theCode);
		}

		/**
		 * The code system version is part of the key, so a response registered for one version is not returned
		 * for a request naming another, or naming none. A test states the version it expects the code under
		 * test to send by registering the response under it.
		 */
		// Created by Claude Opus 5
		static String getInputKey(String theOperation, String theUrl, String theCodeSystemVersion, String theCode) {
			String url = theCodeSystemVersion == null ? theUrl : theUrl + "|" + theCodeSystemVersion;
			return theOperation + "-" + url + "#" + theCode;
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
			addTerminologyResponse(theOperation, theUrl, null, theCode, theReturnParams);
		}

		public IBaseParameters addTerminologyResponse(String theOperation, String theUrl, String theCode, FhirContext theFhirContext, String theTerminologyResponseFile) {
			return addTerminologyResponse(theOperation, theUrl, null, theCode, theFhirContext, theTerminologyResponseFile);
		}

		// Created by Claude Opus 5
		@Override
		public <P extends IBaseParameters> void addTerminologyResponse(String theOperation, String theUrl, String theCodeSystemVersion, String theCode, P theReturnParams) {
			myTerminologyResponseMap.put(getInputKey(theOperation, theUrl, theCodeSystemVersion, theCode), theReturnParams);
		}

		// Created by Claude Opus 5
		public IBaseParameters addTerminologyResponse(String theOperation, String theUrl, String theCodeSystemVersion, String theCode, FhirContext theFhirContext, String theTerminologyResponseFile) {
			IBaseParameters responseParams = ClasspathUtil.loadResource(theFhirContext, getParameterType(), theTerminologyResponseFile);
			addTerminologyResponse(theOperation, theUrl, theCodeSystemVersion, theCode, responseParams);
			return responseParams;
		}

		protected void addTerminologyResource(String theUrl, T theResource) {
			myTerminologyResourceMap.put(theUrl, theResource);
		}

		protected void addVersionedTerminologyResource(String theUrl, String theVersion, T theResource) {
			myTerminologyResourceMap.put(theUrl + "|" + theVersion, theResource);
		}
		public abstract T addTerminologyResource(String theUrl);

		public abstract T addTerminologyResource(String theUrl, String theVersion);
		protected IBaseParameters getTerminologyResponse(String theOperation, String theUrl, String theCode) throws Exception {
			return getTerminologyResponse(theOperation, theUrl, null, theCode);
		}

		// Created by Claude Opus 5
		protected IBaseParameters getTerminologyResponse(String theOperation, String theUrl, String theCodeSystemVersion, String theCode) throws Exception {
			// addException registers without a version, so an exception fires whichever version the request names
			String exceptionKey = getInputKey(theOperation, theUrl, theCode);
			if (myExceptionMap.containsKey(exceptionKey)) {
				throw myExceptionMap.get(exceptionKey);
			}
			String inputKey = getInputKey(theOperation, theUrl, theCodeSystemVersion, theCode);
			IBaseParameters params = myTerminologyResponseMap.get(inputKey);
			if (params == null) {
				throw new IllegalStateException("Test setup incomplete. Missing return params for " + inputKey);
			}
			return params;
		}

		protected T getTerminologyResource(UriParam theUrlParam) {
			return getTerminologyResource(theUrlParam, null);
		}

		/**
		 * The resource registered for the given url, preferring one registered for that version. Versioned
		 * resources are registered under the canonical key, so a version is looked up there first; a fixture
		 * which registered the resource by url alone still answers, as it did while the version could only
		 * reach this provider packed into the url.
		 */
		// Created by Claude Opus 5
		protected T getTerminologyResource(UriParam theUrlParam, @Nullable StringParam theVersionParam) {
			if (theUrlParam.isEmpty()) {
				throw new IllegalStateException("CodeSystem url should not be null.");
			}
			String urlValue = theUrlParam.getValue();
			String version = theVersionParam != null ? theVersionParam.getValue() : null;
			if (isNotBlank(version) && myTerminologyResourceMap.containsKey(urlValue + "|" + version)) {
				return myTerminologyResourceMap.get(urlValue + "|" + version);
			}
			if (!myTerminologyResourceMap.containsKey(urlValue) && myShouldThrowExceptionForResourceNotFound) {
				throw new IllegalStateException("Test setup incomplete. CodeSystem not found " + urlValue);
			}
			return myTerminologyResourceMap.get(urlValue);
		}

		/**
		 * A version-specific canonical arrives as a url plus a version search parameter, as it does on a real
		 * server: a conformance resource's url element never contains a pipe.
		 */
		// Created by Claude Opus 5
		@Search
		public List<T> find(
				@RequiredParam(name = "url") UriParam theUrlParam,
				@OptionalParam(name = "version") StringParam theVersionParam) {
			T resource = getTerminologyResource(theUrlParam, theVersionParam);
			return resource != null ? List.of(resource) : List.of();
		}
	}

	interface IMyLookupCodeProvider extends IResourceProvider {
		void setLookupCodeResult(IValidationSupport.LookupCodeResult theLookupCodeResult);
	}
}
