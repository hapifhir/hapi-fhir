package ca.uhn.fhir.cr.r4.plandefinition;

import ca.uhn.fhir.cr.r4.IPlanDefinitionProcessorFactory;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.PlanDefinition;
import org.springframework.beans.factory.annotation.Autowired;

public class PlanDefinitionPackageProvider {
	@Autowired
	IPlanDefinitionProcessorFactory myR4PlanDefinitionProcessorFactory;

	@Operation(name = ProviderConstants.CR_OPERATION_PACKAGE, idempotent = true, type = PlanDefinition.class)
	public IBaseBundle packagePlanDefinition(
		@IdParam IdType theId,
		@OperationParam(name = "canonical") String theCanonical,
		@OperationParam(name = "usePut") String theIsPut,
		RequestDetails theRequestDetails)
		throws InternalErrorException, FHIRException {
		return myR4PlanDefinitionProcessorFactory
			.create(theRequestDetails)
			.packagePlanDefinition(theId, new CanonicalType(theCanonical), null, Boolean.parseBoolean(theIsPut));
	}

	@Operation(name = ProviderConstants.CR_OPERATION_PACKAGE, idempotent = true, type = PlanDefinition.class)
	public IBaseBundle packagePlanDefinition(
		@OperationParam(name = "id") String theId,
		@OperationParam(name = "canonical") String theCanonical,
		@OperationParam(name = "usePut") String theIsPut,
		RequestDetails theRequestDetails)
		throws InternalErrorException, FHIRException {
		return myR4PlanDefinitionProcessorFactory
			.create(theRequestDetails)
			.packagePlanDefinition(
				new IdType("PlanDefinition", theId),
				new CanonicalType(theCanonical),
				null,
				Boolean.parseBoolean(theIsPut));
	}
}
