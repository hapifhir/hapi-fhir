package org.hl7.fhir.r4.hapi.rest.server.helper;

import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;

import javax.annotation.Nonnull;

public class BatchHelperR4 {

  @Nonnull
  public static Long jobIdFromParameters(Parameters response) {
    DecimalType jobIdDecimal = (DecimalType) response.getParameter(ProviderConstants.OPERATION_BATCH_RESPONSE_JOB_ID);
    return jobIdDecimal.getValue().longValue();
  }

  @Nonnull
  public static String jobIdFromBatch2Parameters(Parameters response) {
    StringType jobIdString = (StringType) response.getParameter(ProviderConstants.OPERATION_BATCH_RESPONSE_JOB_ID);
    return jobIdString.getValue();
  }
}
