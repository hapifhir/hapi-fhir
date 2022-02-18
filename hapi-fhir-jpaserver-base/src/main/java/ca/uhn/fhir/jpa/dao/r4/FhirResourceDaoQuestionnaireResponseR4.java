package ca.uhn.fhir.jpa.dao.r4;

/*
 * #%L
 * HAPI FHIR JPA Server
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
import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import org.hl7.fhir.r4.model.QuestionnaireResponse;

public class FhirResourceDaoQuestionnaireResponseR4 extends BaseHapiFhirResourceDao<QuestionnaireResponse> {

	
//	@Override
//	protected void validateResourceForStorage(QuestionnaireResponse theResource, ResourceTable theEntityToSave, RequestDetails theRequestDetails) {
//		super.validateResourceForStorage(theResource, theEntityToSave, theRequestDetails);
//		if (!myValidateResponses) {
//			return;
//		}
//		
//		if (theResource == null || theResource.getQuestionnaire() == null || theResource.getQuestionnaire().getReference() == null || theResource.getQuestionnaire().getReference().isEmpty()) {
//			return;
//		}
//
//		FhirValidator val = getContext().newValidator();
//		val.setValidateAgainstStandardSchema(false);
//		val.setValidateAgainstStandardSchematron(false);
//
//		val.registerValidatorModule(myQuestionnaireResponseValidatorR4);
//
//		ValidationResult result = val.validateWithResult(getContext().newJsonParser().parseResource(getContext().newJsonParser().encodeResourceToString(theResource)));
//		if (!result.isSuccessful()) {
//			IBaseOperationOutcome oo = getContext().newJsonParser().parseResource(OperationOutcome.class, getContext().newJsonParser().encodeResourceToString(result.toOperationOutcome()));
//			throw new UnprocessableEntityException(Msg.code(1104) + getContext(), oo);
//		}
//	}
//
//	public class JpaResourceLoader implements IResourceLoader {
//
//		private RequestDetails myRequestDetails;
//
//		public JpaResourceLoader(RequestDetails theRequestDetails) {
//			super();
//			myRequestDetails = theRequestDetails;
//		}
//
//		@Override
//		public <T extends IBaseResource> T load(Class<T> theType, IIdType theId) throws ResourceNotFoundException {
//
//			/*
//			 * The QuestionnaireResponse validator uses RI structures, so for now we need to convert between that and HAPI
//			 * structures. This is a bit hackish, but hopefully it will go away at some point.
//			 */
//			if ("ValueSet".equals(theType.getSimpleName())) {
//				IFhirResourceDao<ValueSet> dao = getDao(ValueSet.class);
//				ValueSet in = dao.read(theId, myRequestDetails);
//				return (T) in;
//			} else if ("Questionnaire".equals(theType.getSimpleName())) {
//				IFhirResourceDao<Questionnaire> dao = getDao(Questionnaire.class);
//				Questionnaire vs = dao.read(theId, myRequestDetails);
//				return (T) vs;
//			} else {
//				// Should not happen, validator will only ask for these two
//				throw new IllegalStateException(Msg.code(1105) + "Unexpected request to load resource of type " + theType);
//			}
//
//		}
//
//	}

}
