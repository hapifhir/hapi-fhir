package ca.uhn.fhir.jpa.dao;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import javax.annotation.PostConstruct;

import org.hl7.fhir.instance.hapi.validation.FhirQuestionnaireResponseValidator;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Questionnaire;
import ca.uhn.fhir.model.dstu2.resource.QuestionnaireResponse;
import ca.uhn.fhir.model.dstu2.resource.ValueSet;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.IResourceLoader;
import ca.uhn.fhir.validation.ValidationResult;

public class FhirResourceDaoQuestionnaireResponseDstu2 extends FhirResourceDaoDstu2<QuestionnaireResponse> {

	@Autowired
	@Qualifier("myFhirContextDstu2Hl7Org")
	private FhirContext myRefImplCtx;
	
	private Boolean myValidateResponses;

	/**
	 * Initialize the bean
	 */
	@PostConstruct
	public void initialize() {
		try {
			Class.forName("org.hl7.fhir.instance.model.QuestionnaireResponse");
			myValidateResponses = true;
		} catch (ClassNotFoundException e) {
			myValidateResponses = Boolean.FALSE;
		}
	}
	
	@Override
	protected void validateResourceForStorage(QuestionnaireResponse theResource, ResourceTable theEntityToSave, RequestDetails theRequestDetails) {
		super.validateResourceForStorage(theResource, theEntityToSave, theRequestDetails);
		if (!myValidateResponses) {
			return;
		}
		
		QuestionnaireResponse qa = (QuestionnaireResponse) theResource;
		if (qa == null || qa.getQuestionnaire() == null || qa.getQuestionnaire().getReference() == null || qa.getQuestionnaire().getReference().isEmpty()) {
			return;
		}

		FhirValidator val = myRefImplCtx.newValidator();
		val.setValidateAgainstStandardSchema(false);
		val.setValidateAgainstStandardSchematron(false);

		FhirQuestionnaireResponseValidator module = new FhirQuestionnaireResponseValidator();
		module.setResourceLoader(new JpaResourceLoader(theRequestDetails));
		val.registerValidatorModule(module);

		ValidationResult result = val.validateWithResult(myRefImplCtx.newJsonParser().parseResource(getContext().newJsonParser().encodeResourceToString(qa)));
		if (!result.isSuccessful()) {
			IBaseOperationOutcome oo = getContext().newJsonParser().parseResource(OperationOutcome.class, myRefImplCtx.newJsonParser().encodeResourceToString(result.toOperationOutcome()));
			throw new UnprocessableEntityException(getContext(), oo);
		}
	}

	public class JpaResourceLoader implements IResourceLoader {

		private RequestDetails myRequestDetails;

		public JpaResourceLoader(RequestDetails theRequestDetails) {
			myRequestDetails = theRequestDetails;
		}

		@Override
		public <T extends IBaseResource> T load(Class<T> theType, IIdType theId) throws ResourceNotFoundException {

			/*
			 * The QuestionnaireResponse validator uses RI structures, so for now we need to convert between that and HAPI
			 * structures. This is a bit hackish, but hopefully it will go away at some point.
			 */
			if ("ValueSet".equals(theType.getSimpleName())) {
				IFhirResourceDao<ValueSet> dao = getDao(ValueSet.class);
				ValueSet in = dao.read(theId, myRequestDetails);
				String encoded = getContext().newJsonParser().encodeResourceToString(in);

				// TODO: this is temporary until structures-dstu2 catches up to structures-hl7org.dstu2
				encoded = encoded.replace("\"define\"", "\"codeSystem\"");

				return myRefImplCtx.newJsonParser().parseResource(theType, encoded);
			} else if ("Questionnaire".equals(theType.getSimpleName())) {
				IFhirResourceDao<Questionnaire> dao = getDao(Questionnaire.class);
				Questionnaire vs = dao.read(theId, myRequestDetails);
				return myRefImplCtx.newJsonParser().parseResource(theType, getContext().newJsonParser().encodeResourceToString(vs));
			} else {
				// Should not happen, validator will only ask for these two
				throw new IllegalStateException("Unexpected request to load resource of type " + theType);
			}

		}

	}

}
