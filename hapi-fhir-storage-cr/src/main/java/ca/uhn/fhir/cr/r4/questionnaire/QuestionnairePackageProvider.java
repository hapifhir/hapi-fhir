package ca.uhn.fhir.cr.r4.questionnaire;

import ca.uhn.fhir.cr.r4.IQuestionnaireProcessorFactory;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Questionnaire;
import org.springframework.beans.factory.annotation.Autowired;

public class QuestionnairePackageProvider {
	@Autowired
	IQuestionnaireProcessorFactory myR4QuestionnaireProcessorFactory;

	/**
	 * Implements a $package operation following the <a href=
	 * "https://build.fhir.org/ig/HL7/crmi-ig/branches/master/packaging.html">CRMI IG</a>.
	 *
	 * @param theId             The id of the Questionnaire.
	 * @param theCanonical      The canonical identifier for the questionnaire (optionally version-specific).
	 * @Param theIsPut			 A boolean value to determine if the Bundle returned uses PUT or POST request methods.  Defaults to false.
	 * @param theRequestDetails The details (such as tenant) of this request. Usually
	 *                          autopopulated by HAPI.
	 * @return A Bundle containing the Questionnaire and all related Library, CodeSystem and ValueSet resources
	 */
	@Operation(name = ProviderConstants.CR_OPERATION_PACKAGE, idempotent = true, type = Questionnaire.class)
	public Bundle packageQuestionnaire(
		@IdParam IdType theId,
		@OperationParam(name = "canonical") String theCanonical,
		@OperationParam(name = "usePut") String theIsPut,
		RequestDetails theRequestDetails) {
		return (Bundle) myR4QuestionnaireProcessorFactory
			.create(theRequestDetails)
			.packageQuestionnaire(theId, new CanonicalType(theCanonical), null, Boolean.parseBoolean(theIsPut));
	}

	@Operation(name = ProviderConstants.CR_OPERATION_PACKAGE, idempotent = true, type = Questionnaire.class)
	public Bundle packageQuestionnaire(
		@OperationParam(name = "canonical") String theCanonical,
		@OperationParam(name = "usePut") String theIsPut,
		RequestDetails theRequestDetails) {
		return (Bundle) myR4QuestionnaireProcessorFactory
			.create(theRequestDetails)
			.packageQuestionnaire(null, new CanonicalType(theCanonical), null, Boolean.parseBoolean(theIsPut));
	}
}
