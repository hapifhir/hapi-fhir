package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.searchparam.extractor.BaseSearchParamExtractor;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Interceptor
public class PatientIdPartitionInterceptor {

	@Autowired
	private FhirContext myFhirContext;

	/**
	 * Constructor
	 */
	public PatientIdPartitionInterceptor() {
		super();
	}

	/**
	 * Constructor
	 */
	public PatientIdPartitionInterceptor(FhirContext theFhirContext) {
		this();
		myFhirContext = theFhirContext;
	}

	@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_CREATE)
	public RequestPartitionId identifyForCreate(IBaseResource theResource) {
		RuntimeResourceDefinition resourceDef = myFhirContext.getResourceDefinition(theResource);
		IFhirPath fhirPath = myFhirContext.newFhirPath();

		List<RuntimeSearchParam> compartmentSps = resourceDef
			.getSearchParams()
			.stream()
			.filter(param -> param.getParamType() == RestSearchParameterTypeEnum.REFERENCE)
			.filter(param -> param.getProvidesMembershipInCompartments() != null && param.getProvidesMembershipInCompartments().contains("Patient"))
			.collect(Collectors.toList());
		if (compartmentSps.isEmpty()) {
			return provideNonCompartmentMemberTypeResponse(theResource);
		}

		String compartmentIdentity;
		if (resourceDef.getName().equals("Patient")) {
			compartmentIdentity = theResource.getIdElement().getIdPart();
		} else {
			compartmentIdentity = compartmentSps
				.stream()
				.flatMap(param -> Arrays.stream(BaseSearchParamExtractor.splitPathsR4(param.getPath())))
				.filter(path -> isNotBlank(path))
				.map(path -> fhirPath.evaluateFirst(theResource, path, IBaseReference.class))
				.filter(refOpt -> refOpt.isPresent())
				.map(refOpt -> refOpt.get())
				.map(t -> t.getReferenceElement().getValue())
				.map(t -> new IdType(t).getIdPart())
				.filter(t -> isNotBlank(t))
				.findFirst()
				.orElse(null);
		}

		if (isBlank(compartmentIdentity)) {
			return provideNonCompartmentMemberInstanceResponse(theResource);
		}

		return RequestPartitionId.fromPartitionId(compartmentIdentity.hashCode());
	}

	protected RequestPartitionId provideNonCompartmentMemberInstanceResponse(IBaseResource theResource) {
		throw new PreconditionFailedException("Resource of type " + myFhirContext.getResourceType(theResource) + " has no values placing it in the Patient compartment");
	}

	@SuppressWarnings("unused")
	@Nonnull
	protected RequestPartitionId provideNonCompartmentMemberTypeResponse(IBaseResource theResource) {
		return RequestPartitionId.defaultPartition();
	}


}
