package ca.uhn.fhir.jpa.dao.dstu3;

import org.hl7.fhir.dstu3.model.QuestionnaireResponse;

public class FhirResourceDaoQuestionnaireResponseDstu3 extends FhirResourceDaoDstu3<QuestionnaireResponse> {

	
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
//		val.registerValidatorModule(myQuestionnaireResponseValidatorDstu3);
//
//		ValidationResult result = val.validateWithResult(getContext().newJsonParser().parseResource(getContext().newJsonParser().encodeResourceToString(theResource)));
//		if (!result.isSuccessful()) {
//			IBaseOperationOutcome oo = getContext().newJsonParser().parseResource(OperationOutcome.class, getContext().newJsonParser().encodeResourceToString(result.toOperationOutcome()));
//			throw new UnprocessableEntityException(getContext(), oo);
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
//				throw new IllegalStateException("Unexpected request to load resource of type " + theType);
//			}
//
//		}
//
//	}

}
