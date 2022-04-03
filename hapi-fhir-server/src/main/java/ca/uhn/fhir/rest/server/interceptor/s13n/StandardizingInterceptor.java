package ca.uhn.fhir.rest.server.interceptor.s13n;

/*-
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
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.fhirpath.FhirPathExecutionException;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.ConfigLoader;
import ca.uhn.fhir.rest.server.interceptor.s13n.standardizers.EmailStandardizer;
import ca.uhn.fhir.rest.server.interceptor.s13n.standardizers.FirstNameStandardizer;
import ca.uhn.fhir.rest.server.interceptor.s13n.standardizers.IStandardizer;
import ca.uhn.fhir.rest.server.interceptor.s13n.standardizers.LastNameStandardizer;
import ca.uhn.fhir.rest.server.interceptor.s13n.standardizers.PhoneStandardizer;
import ca.uhn.fhir.rest.server.interceptor.s13n.standardizers.TextStandardizer;
import ca.uhn.fhir.rest.server.interceptor.s13n.standardizers.TitleStandardizer;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Interceptor
public class StandardizingInterceptor {

	/**
	 * Pre-defined standardizers
	 */
	public enum StandardizationType {
		NAME_FAMILY, NAME_GIVEN, EMAIL, TITLE, PHONE, TEXT;
	}

	public static final String STANDARDIZATION_DISABLED_HEADER = "HAPI-Standardization-Disabled";

	private static final Logger ourLog = LoggerFactory.getLogger(StandardizingInterceptor.class);

	private Map<String, Map<String, String>> myConfig;
	private Map<String, IStandardizer> myStandardizers = new HashMap<>();

	public StandardizingInterceptor() {
		super();

		ourLog.info("Starting StandardizingInterceptor {}", this);

		myConfig = ConfigLoader.loadJson("classpath:field-s13n-rules.json", Map.class);
		initStandardizers();
	}

	public StandardizingInterceptor(Map<String, Map<String, String>> theConfig) {
		super();
		myConfig = theConfig;
		initStandardizers();
	}

	public void initStandardizers() {
		myStandardizers.put(StandardizationType.NAME_FAMILY.name(), new LastNameStandardizer());
		myStandardizers.put(StandardizationType.NAME_GIVEN.name(), new FirstNameStandardizer());
		myStandardizers.put(StandardizationType.EMAIL.name(), new EmailStandardizer());
		myStandardizers.put(StandardizationType.TITLE.name(), new TitleStandardizer());
		myStandardizers.put(StandardizationType.PHONE.name(), new PhoneStandardizer());
		myStandardizers.put(StandardizationType.TEXT.name(), new TextStandardizer());

		ourLog.info("Initialized standardizers {}", myStandardizers);
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED)
	public void resourcePreCreate(RequestDetails theRequest, IBaseResource theResource) {
		ourLog.debug("Standardizing on pre-create for - {}, {}", theRequest, theResource);
		standardize(theRequest, theResource);
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED)
	public void resourcePreUpdate(RequestDetails theRequest, IBaseResource theOldResource, IBaseResource theNewResource) {
		ourLog.debug("Standardizing on pre-update for - {}, {}, {}", theRequest, theOldResource, theNewResource);
		standardize(theRequest, theNewResource);
	}

	private void standardize(RequestDetails theRequest, IBaseResource theResource) {
		if (theRequest == null) {
			ourLog.debug("RequestDetails is null - unable to standardize {}", theResource);
			return;
		}

		if (!theRequest.getHeaders(STANDARDIZATION_DISABLED_HEADER).isEmpty()) {
			ourLog.debug("Standardization for {} is disabled via header {}", theResource, STANDARDIZATION_DISABLED_HEADER);
			return;
		}

		if (theResource == null) {
			ourLog.debug("Nothing to standardize for {}", theRequest);
			return;
		}

		FhirContext ctx = theRequest.getFhirContext();

		String resourceType = ctx.getResourceType(theResource);
		IFhirPath fhirPath = ctx.newFhirPath();

		for (Map.Entry<String, Map<String, String>> rule : myConfig.entrySet()) {
			String resourceFromConfig = rule.getKey();
			if (!appliesToResource(resourceFromConfig, resourceType)) {
				continue;
			}

			standardize(theResource, rule.getValue(), fhirPath);
		}
	}

	private void standardize(IBaseResource theResource, Map<String, String> theRules, IFhirPath theFhirPath) {
		for (Map.Entry<String, String> rule : theRules.entrySet()) {
			IStandardizer std = getStandardizer(rule);
			List<IBase> values;
			try {
				values = theFhirPath.evaluate(theResource, rule.getKey(), IBase.class);
			} catch (FhirPathExecutionException e) {
				ourLog.warn("Unable to evaluate path at {} for {}", rule.getKey(), theResource);
				return;
			}

			for (IBase v : values) {
				if (!(v instanceof IPrimitiveType)) {
					ourLog.warn("Value at path {} is of type {}, which is not of primitive type - skipping", rule.getKey(), v.fhirType());
					continue;
				}
				IPrimitiveType<?> value = (IPrimitiveType<?>) v;
				String valueString = value.getValueAsString();
				String standardizedValueString = std.standardize(valueString);
				value.setValueAsString(standardizedValueString);
				ourLog.debug("Standardized {} to {}", valueString, standardizedValueString);
			}
		}
	}

	private IStandardizer getStandardizer(Map.Entry<String, String> rule) {
		String standardizerName = rule.getValue();
		if (myStandardizers.containsKey(standardizerName)) {
			return myStandardizers.get(standardizerName);
		}

		IStandardizer standardizer;
		try {
			standardizer = (IStandardizer) Class.forName(standardizerName).getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new RuntimeException(Msg.code(349) + String.format("Unable to create standardizer %s", standardizerName), e);
		}

		myStandardizers.put(standardizerName, standardizer);
		return standardizer;
	}

	private boolean appliesToResource(String theResourceFromConfig, String theActualResourceType) {
		return theResourceFromConfig.equals(theActualResourceType);
	}

	public Map<String, Map<String, String>> getConfig() {
		return myConfig;
	}

	public void setConfig(Map<String, Map<String, String>> theConfig) {
		myConfig = theConfig;
	}

	public Map<String, IStandardizer> getStandardizers() {
		return myStandardizers;
	}

	public void setStandardizers(Map<String, IStandardizer> theStandardizers) {
		myStandardizers = theStandardizers;
	}
}
