package org.hl7.fhir.r4.hapi.rest.server.helper;

import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;

public class BatchHelperR4 {

	@Nonnull
	public static Long jobIdFromParameters(Parameters response) {
		DecimalType jobIdDecimal =
				(DecimalType) response.getParameterValue(ProviderConstants.OPERATION_BATCH_RESPONSE_JOB_ID);
		return jobIdDecimal.getValue().longValue();
	}

	@Nonnull
	public static String jobIdFromBatch2Parameters(Parameters response) {
		StringType jobIdString =
				(StringType) response.getParameterValue(ProviderConstants.OPERATION_BATCH_RESPONSE_JOB_ID);
		return jobIdString.getValue();
	}
}
