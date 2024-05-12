package ca.uhn.fhir.cr.r4.measure;

import ca.uhn.fhir.cr.r4.ICollectDataServiceFactory;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Measure;
import org.hl7.fhir.r4.model.Parameters;
import org.springframework.beans.factory.annotation.Autowired;

public class CollectDataOperationProvider {
	@Autowired
	ICollectDataServiceFactory myR4CollectDataServiceFactory;
	/**
	 * Implements the <a href=
	 * "http://hl7.org/fhir/R4/measure-operation-collect-data.html">$collect-data</a>
	 * operation found in the
	 * <a href="http://hl7.org/fhir/R4/clinicalreasoning-module.html">FHIR Clinical
	 * Reasoning Module</a>.
	 *
	 * <p>
	 * Returns a set of parameters with the generated MeasureReport and the
	 * resources that were used during the Measure evaluation
	 *
	 * @param theRequestDetails generally auto-populated by the HAPI server
	 *                          framework.
	 * @param theId             the Id of the Measure to sub data for
	 * @param thePeriodStart       The start of the reporting period
	 * @param thePeriodEnd         The end of the reporting period
	 * @param theSubject           the subject to use for the evaluation
	 * @param thePractitioner      the practitioner to use for the evaluation
	 *
	 * @return Parameters the parameters containing the MeasureReport and the
	 *         evaluated Resources
	 */
	@Description(
			shortDefinition = "$collect-data",
			value =
					"Implements the <a href=\"http://hl7.org/fhir/R4/measure-operation-collect-data.html\">$collect-data</a> operation found in the <a href=\"http://hl7.org/fhir/R4/clinicalreasoning-module.html\">FHIR Clinical Reasoning Module</a>.")
	@Operation(name = ProviderConstants.CR_OPERATION_COLLECTDATA, idempotent = true, type = Measure.class)
	public Parameters collectData(
			@IdParam IdType theId,
			@OperationParam(name = "periodStart") String thePeriodStart,
			@OperationParam(name = "periodEnd") String thePeriodEnd,
			@OperationParam(name = "subject") String theSubject,
			@OperationParam(name = "practitioner") String thePractitioner,
			RequestDetails theRequestDetails) {
		return myR4CollectDataServiceFactory
				.create(theRequestDetails)
				.collectData(theId, thePeriodStart, thePeriodEnd, theSubject, thePractitioner);
	}
}
