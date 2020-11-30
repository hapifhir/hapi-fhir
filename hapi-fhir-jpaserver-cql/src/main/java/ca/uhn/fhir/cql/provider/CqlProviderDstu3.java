package ca.uhn.fhir.cql.provider;

import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.MeasureReport;
import org.hl7.fhir.dstu3.model.StringType;
import org.opencds.cqf.dstu3.providers.MeasureOperationsProvider;

public class CqlProviderDstu3 {

	private MeasureOperationsProvider ourMeasureOperationsProvider;

	public CqlProviderDstu3(MeasureOperationsProvider theMeasureOperationsProvider) {
		ourMeasureOperationsProvider = theMeasureOperationsProvider;
	}

	@Operation(name = ProviderConstants.CQL_EVALUATE_MEASURE, type = MeasureReport.class)
	public MeasureReport updateLink(@OperationParam(name=ProviderConstants.CQL_EVALUATE_MEASURE_PATIENT_ID, min = 0, max = 1) StringType thePatientId,
									 @OperationParam(name=ProviderConstants.CQL_EVALUATE_MEASURE_PERIOD_START, min = 0, max = 1) StringType thePeriodStart,
									 @OperationParam(name=ProviderConstants.CQL_EVALUATE_MEASURE_PERIOD_END, min = 0, max = 1) StringType thePeriodEnd,
									 ServletRequestDetails theRequestDetails) {

		IdType patientId = new IdType("Patient", thePatientId.getValue());

		return (MeasureReport) ourMeasureOperationsProvider.evaluateMeasure(patientId, thePeriodStart.getValue(),
			thePeriodEnd.getValue(), null, null, null, null, null,
			null, null, null, null);
	}

	// FIXME KBD Figure out if we can eliminate this method or not...
	public MeasureOperationsProvider getMeasureOperationsProvider() {
		return ourMeasureOperationsProvider;
	}
}
