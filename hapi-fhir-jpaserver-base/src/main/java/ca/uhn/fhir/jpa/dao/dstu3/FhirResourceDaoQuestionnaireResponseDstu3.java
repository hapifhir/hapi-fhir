package ca.uhn.fhir.jpa.dao.dstu3;

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

import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.Questionnaire;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.IResourceLoader;
import ca.uhn.fhir.validation.IValidatorModule;
import ca.uhn.fhir.validation.ValidationResult;

public class FhirResourceDaoQuestionnaireResponseDstu3 extends FhirResourceDaoDstu3<QuestionnaireResponse> {

	private Boolean myValidateResponses;
	
	@Autowired
	@Qualifier("myQuestionnaireResponseValidatorDstu3")
	private IValidatorModule myQuestionnaireResponseValidatorDstu3;


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
		
		if (theResource == null || theResource.getQuestionnaire() == null || theResource.getQuestionnaire().getReference() == null || theResource.getQuestionnaire().getReference().isEmpty()) {
			return;
		}

		FhirValidator val = getContext().newValidator();
		val.setValidateAgainstStandardSchema(false);
		val.setValidateAgainstStandardSchematron(false);

		val.registerValidatorModule(myQuestionnaireResponseValidatorDstu3);

		ValidationResult result = val.validateWithResult(getContext().newJsonParser().parseResource(getContext().newJsonParser().encodeResourceToString(theResource)));
		if (!result.isSuccessful()) {
			IBaseOperationOutcome oo = getContext().newJsonParser().parseResource(OperationOutcome.class, getContext().newJsonParser().encodeResourceToString(result.toOperationOutcome()));
			throw new UnprocessableEntityException(getContext(), oo);
		}
	}

	public class JpaResourceLoader implements IResourceLoader {

		private RequestDetails myRequestDetails;

		public JpaResourceLoader(RequestDetails theRequestDetails) {
			super();
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
				return (T) in;
			} else if ("Questionnaire".equals(theType.getSimpleName())) {
				IFhirResourceDao<Questionnaire> dao = getDao(Questionnaire.class);
				Questionnaire vs = dao.read(theId, myRequestDetails);
				return (T) vs;
			} else {
				// Should not happen, validator will only ask for these two
				throw new IllegalStateException("Unexpected request to load resource of type " + theType);
			}

		}

	}

}
