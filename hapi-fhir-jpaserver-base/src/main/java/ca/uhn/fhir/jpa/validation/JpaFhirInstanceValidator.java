package ca.uhn.fhir.jpa.validation;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.exceptions.DefinitionException;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.FHIRFormatError;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r5.elementmodel.Element;
import org.hl7.fhir.r5.elementmodel.JsonParser;
import org.hl7.fhir.r5.utils.IResourceValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Locale;

public class JpaFhirInstanceValidator extends FhirInstanceValidator {

	private static final Logger ourLog = LoggerFactory.getLogger(JpaFhirInstanceValidator.class);
	private final FhirContext myFhirContext;
	@Autowired
	private ValidationSettings myValidationSettings;
	@Autowired
	private DaoRegistry myDaoRegistry;

	/**
	 * Constructor
	 */
	public JpaFhirInstanceValidator(FhirContext theFhirContext) {
		super(theFhirContext);
		myFhirContext = theFhirContext;
		setValidatorResourceFetcher(new MyValidatorResourceFetcher());
	}

	private class MyValidatorResourceFetcher implements IResourceValidator.IValidatorResourceFetcher {


		@SuppressWarnings("ConstantConditions")
		@Override
		public Element fetch(Object appContext, String theUrl) throws FHIRException {

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
				return new JsonParser(provideWorkerContext()).parse(myFhirContext.newJsonParser().encodeResourceToString(target), resourceType);
			} catch (Exception e) {
				throw new FHIRException(e);
			}
		}

		@Override
		public IResourceValidator.ReferenceValidationPolicy validationPolicy(Object appContext, String path, String url) {
			int slashIdx = url.indexOf("/");
			if (slashIdx > 0 && myFhirContext.getResourceTypes().contains(url.substring(0, slashIdx))) {
				return myValidationSettings.getLocalReferenceValidationDefaultPolicy();
			}

			return IResourceValidator.ReferenceValidationPolicy.IGNORE;
		}

		@Override
		public boolean resolveURL(Object appContext, String path, String url) throws IOException, FHIRException {
			return true;
		}

		@Override
		public byte[] fetchRaw(String url) throws IOException {
			return new byte[0];
		}

		@Override
		public void setLocale(Locale locale) {
			// ignore
		}

	}
}
