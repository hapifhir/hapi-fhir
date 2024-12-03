package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.PreferHeader;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

public class BulkDataExportUtil {
	/**
	 * Bulk data $export does not include the Binary type
	 */
	public static final String UNSUPPORTED_BINARY_TYPE = "Binary";

	private BulkDataExportUtil() {}

	private static final Set<FhirVersionEnum> PATIENT_COMPARTMENT_FHIR_VERSIONS_SUPPORT_DEVICE = Set.of(
			FhirVersionEnum.DSTU2,
			FhirVersionEnum.DSTU2_1,
			FhirVersionEnum.DSTU2_HL7ORG,
			FhirVersionEnum.DSTU3,
			FhirVersionEnum.R4,
			FhirVersionEnum.R4B);

	public static void validatePreferAsyncHeader(ServletRequestDetails theRequestDetails, String theOperationName) {
		String preferHeader = theRequestDetails.getHeader(Constants.HEADER_PREFER);
		PreferHeader prefer = RestfulServerUtils.parsePreferHeader(null, preferHeader);
		if (!prefer.getRespondAsync()) {
			throw new InvalidRequestException(Msg.code(513) + "Must request async processing for " + theOperationName);
		}
	}

	public static boolean isDeviceResourceSupportedForPatientCompartmentForFhirVersion(
			FhirVersionEnum theFhirVersionEnum) {
		return PATIENT_COMPARTMENT_FHIR_VERSIONS_SUPPORT_DEVICE.contains(theFhirVersionEnum);
	}

	public static String getServerBase(ServletRequestDetails theRequestDetails) {
		return StringUtils.removeEnd(theRequestDetails.getServerBaseForRequest(), "/");
	}
}
