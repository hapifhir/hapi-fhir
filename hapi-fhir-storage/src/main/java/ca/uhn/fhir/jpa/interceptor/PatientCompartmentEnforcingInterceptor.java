package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.util.ResourceCompartmentUtil;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.util.StopWatch;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * This interceptor can be used to block resource updates which would make resource patient compartment change.
 * <p/>
 * This could be used when the JPA server has partitioning enabled, and Tenant Identification Strategy is PATIENT_ID.
 */
@Interceptor
public class PatientCompartmentEnforcingInterceptor {
	private static final Logger ourLog = LoggerFactory.getLogger(PatientCompartmentEnforcingInterceptor.class);

	private final FhirContext myFhirContext;
	private final ISearchParamExtractor mySearchParamExtractor;

	public PatientCompartmentEnforcingInterceptor(
			FhirContext theFhirContext, ISearchParamExtractor theSearchParamExtractor) {
		if (theFhirContext.getVersion().getVersion().isOlderThan(FhirVersionEnum.R4)) {
			throw new InternalErrorException(Msg.code(2475) + "Interceptor requires Fhir version R4 or later.");
		}

		myFhirContext = theFhirContext;
		mySearchParamExtractor = theSearchParamExtractor;
	}

	/**
	 * Blocks resource updates which make resource change Patient Compartment.
	 * @param theOldResource the original resource state
	 * @param theResource the updated resource state
	 */
	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED)
	public void storagePreStorageResourceUpdated(IBaseResource theOldResource, IBaseResource theResource) {

		ourLog.info("Interceptor STORAGE_PRESTORAGE_RESOURCE_UPDATED - started");
		StopWatch stopWatch = new StopWatch();
		try {
			Optional<String> oPatientCompartmentOld = getPatientCompartmentIdentity(theOldResource);
			Optional<String> oPatientCompartmentCurrent = getPatientCompartmentIdentity(theResource);
			if (oPatientCompartmentOld.isEmpty() || oPatientCompartmentCurrent.isEmpty()) {
				// resource is not part of Patient compartment
				return;
			}

			if (!oPatientCompartmentOld.get().equals(oPatientCompartmentCurrent.get())) {
				// Avoid disclosing compartments in message, which could have security implications
				throw new InternalErrorException(
						Msg.code(2476) + "Resource compartment changed. Was a referenced Patient changed?");
			}

		} finally {
			ourLog.info("Interceptor STORAGE_PRESTORAGE_RESOURCE_UPDATED - ended, execution took {}", stopWatch);
		}
	}

	private Optional<String> getPatientCompartmentIdentity(IBaseResource theResource) {
		RuntimeResourceDefinition resourceDef = myFhirContext.getResourceDefinition(theResource);
		List<RuntimeSearchParam> patientCompartmentSps =
				ResourceCompartmentUtil.getPatientCompartmentSearchParams(resourceDef);
		if (patientCompartmentSps.isEmpty()) {
			return Optional.empty();
		}

		if (resourceDef.getName().equals("Patient")) {
			String compartmentIdentity = theResource.getIdElement().getIdPart();
			if (isBlank(compartmentIdentity)) {
				throw new MethodNotAllowedException(
						Msg.code(2477) + "Patient resource IDs must be client-assigned in patient compartment mode");
			}
			return Optional.of(compartmentIdentity);
		}

		return ResourceCompartmentUtil.getResourceCompartment(
				theResource, patientCompartmentSps, mySearchParamExtractor);
	}
}
