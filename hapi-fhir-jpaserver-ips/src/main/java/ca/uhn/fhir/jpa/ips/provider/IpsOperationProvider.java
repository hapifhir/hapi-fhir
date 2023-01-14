package ca.uhn.fhir.jpa.ips.provider;

import ca.uhn.fhir.jpa.ips.api.IpsConstants;
import ca.uhn.fhir.jpa.ips.generator.IIpsGeneratorSvc;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IIdType;

public class IpsOperationProvider {

	private final IIpsGeneratorSvc myIpsGeneratorSvc;

	/**
	 * Constructor
	 */
	public IpsOperationProvider(IIpsGeneratorSvc theIpsGeneratorSvc) {
		myIpsGeneratorSvc = theIpsGeneratorSvc;
	}


	/**
	 * Patient/123/$summary
	 */
	@Operation(name = IpsConstants.OPERATION_SUMMARY, idempotent = true, bundleType = BundleTypeEnum.DOCUMENT, typeName = "Patient")
	public IBaseBundle patientInstanceSummary(
		@IdParam
		IIdType thePatientId,

		RequestDetails theRequestDetails
	) {
//		IBundleProvider patientResponse = patientInstanceSummaryInternal(theServletRequest, theId, theCount, theOffset, theLastUpdated, theSortSpec, toStringAndList(theContent), toStringAndList(theNarrative), toStringAndList(theFilter), toStringAndList(theTypes), theRequestDetails);
//		Bundle patientSummaryBundle = PatientSummary.buildFromSearch(patientResponse, this.fhirContext);
//		return patientSummaryBundle;
		return myIpsGeneratorSvc.generateIps(theRequestDetails, thePatientId);
	}

	/**
	 * /Patient/$summary?identifier=foo|bar
	 */
	@Operation(name = IpsConstants.OPERATION_SUMMARY, idempotent = true, bundleType = BundleTypeEnum.DOCUMENT, typeName = "Patient")
	public IBaseBundle patientTypeSummary(

		@Description(shortDefinition = "When the logical id of the patient is not used, servers MAY choose to support patient selection based on provided identifier")
		@OperationParam(name = "identifier", min = 0, max = 1)
		TokenParam thePatientIdentifier,

		RequestDetails theRequestDetails
	) {
//		IBundleProvider patientResponse = patientTypeSummaryInternal(theServletRequest, theCount, theOffset, theLastUpdated, theSortSpec, toStringAndList(theContent), toStringAndList(theNarrative), toStringAndList(theFilter), toStringAndList(theTypes), theRequestDetails, theIdentifier);
//		Bundle patientSummaryBundle = PatientSummary.buildFromSearch(patientResponse, this.fhirContext);
//		return patientSummaryBundle;
		return myIpsGeneratorSvc.generateIps(theRequestDetails, thePatientIdentifier);
	}

}
