/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.common.hapi.validation.validator.VersionSpecificWorkerContextWrapper;
import org.hl7.fhir.exceptions.DefinitionException;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.FHIRFormatError;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r5.elementmodel.Element;
import org.hl7.fhir.r5.elementmodel.JsonParser;
import org.hl7.fhir.r5.model.CanonicalResource;
import org.hl7.fhir.r5.utils.validation.IResourceValidator;
import org.hl7.fhir.r5.utils.validation.IValidatorResourceFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Locale;

public class ValidatorResourceFetcher implements IValidatorResourceFetcher {

	private static final Logger ourLog = LoggerFactory.getLogger(ValidatorResourceFetcher.class);

	private final FhirContext myFhirContext;
	private final IValidationSupport myValidationSupport;
	private final DaoRegistry myDaoRegistry;
	private final VersionSpecificWorkerContextWrapper myVersionSpecificContextWrapper;

	public ValidatorResourceFetcher(
			FhirContext theFhirContext, IValidationSupport theValidationSupport, DaoRegistry theDaoRegistry) {
		myFhirContext = theFhirContext;
		myValidationSupport = theValidationSupport;
		myDaoRegistry = theDaoRegistry;
		myVersionSpecificContextWrapper =
				VersionSpecificWorkerContextWrapper.newVersionSpecificWorkerContextWrapper(myValidationSupport);
	}

	@Override
	public Element fetch(IResourceValidator iResourceValidator, Object appContext, String theUrl)
			throws FHIRFormatError, DefinitionException, FHIRException, IOException {
		IdType id = new IdType(theUrl);
		String resourceType = id.getResourceType();
		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(resourceType);
		IBaseResource target;
		try {
			target = dao.read(id, (RequestDetails) appContext);
		} catch (ResourceNotFoundException e) {
			ourLog.info("Failed to resolve local reference: {}", theUrl);
			return null;
		}

		try {
			return new JsonParser(myVersionSpecificContextWrapper)
					.parse(
							new ArrayList<>(),
							org.hl7.fhir.utilities.json.parser.JsonParser.parseObject(
									myFhirContext.newJsonParser().encodeResourceToString(target)));
		} catch (Exception e) {
			throw new FHIRException(Msg.code(576) + e);
		}
	}

	@Override
	public boolean resolveURL(
			IResourceValidator iResourceValidator, Object o, String s, String s1, String s2, boolean isCanonical)
			throws IOException, FHIRException {
		return true;
	}

	@Override
	public byte[] fetchRaw(IResourceValidator iResourceValidator, String s) throws MalformedURLException, IOException {
		throw new UnsupportedOperationException(Msg.code(577));
	}

	@Override
	public IValidatorResourceFetcher setLocale(Locale locale) {
		// ignore
		return this;
	}

	@Override
	public CanonicalResource fetchCanonicalResource(IResourceValidator iResourceValidator, String s)
			throws URISyntaxException {
		return null;
	}

	@Override
	public boolean fetchesCanonicalResource(IResourceValidator iResourceValidator, String s) {
		return false;
	}
}
