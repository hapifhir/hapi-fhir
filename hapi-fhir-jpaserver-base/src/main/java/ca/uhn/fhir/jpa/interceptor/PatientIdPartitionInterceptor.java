package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.extractor.BaseSearchParamExtractor;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
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

/**
 * This interceptor allows JPA servers to be partitioned by Patient ID. It selects the compartment for read/create operations
 * based on the patient ID associated with the resource (and uses a default compartment ID for any resources
 * not in the patient compartment).
 */
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
	public RequestPartitionId identifyForCreate(IBaseResource theResource, RequestDetails theRequestDetails) {
		RuntimeResourceDefinition resourceDef = myFhirContext.getResourceDefinition(theResource);
		List<RuntimeSearchParam> compartmentSps = getCompartmentSearchParams(resourceDef);
		if (compartmentSps.isEmpty()) {
			return provideNonCompartmentMemberTypeResponse(theResource);
		}

		String compartmentIdentity;
		if (resourceDef.getName().equals("Patient")) {
			compartmentIdentity = theResource.getIdElement().getIdPart();
		} else {
			IFhirPath fhirPath = myFhirContext.newFhirPath();
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

		return provideCompartmentMemberInstanceResponse(theRequestDetails, compartmentIdentity);
	}

	@Nonnull
	private List<RuntimeSearchParam> getCompartmentSearchParams(RuntimeResourceDefinition resourceDef) {
		return resourceDef
			.getSearchParams()
			.stream()
			.filter(param -> param.getParamType() == RestSearchParameterTypeEnum.REFERENCE)
			.filter(param -> param.getProvidesMembershipInCompartments() != null && param.getProvidesMembershipInCompartments().contains("Patient"))
			.collect(Collectors.toList());
	}


	@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_READ)
	public RequestPartitionId identifyForRead(ReadPartitionIdRequestDetails theReadDetails, RequestDetails theRequestDetails) {
		RuntimeResourceDefinition resourceDef = myFhirContext.getResourceDefinition(theReadDetails.getResourceType());
		List<RuntimeSearchParam> compartmentSps = getCompartmentSearchParams(resourceDef);
		if (compartmentSps.isEmpty()) {
			return provideNonCompartmentMemberTypeResponse(null);
		}

		if (theReadDetails.getRestOperationType() == RestOperationTypeEnum.READ) {
			if ("Patient".equals(theReadDetails.getResourceType())) {
				return provideCompartmentMemberInstanceResponse(theRequestDetails, theReadDetails.getReadResourceId().getIdPart());
			}
		} else if (theReadDetails.getRestOperationType() == RestOperationTypeEnum.SEARCH_TYPE) {
			SearchParameterMap params = (SearchParameterMap) theReadDetails.getSearchParams();
			String idPart = null;
			if ("Patient".equals(theReadDetails.getResourceType())) {
				idPart = getSingleResourceIdValueOrNull(params, "_id", "Patient");
			} else {
				for (RuntimeSearchParam nextCompartmentSp : compartmentSps) {
					idPart = getSingleResourceIdValueOrNull(params, nextCompartmentSp.getName(), "Patient");
					if (idPart != null) {
						break;
					}
				}
			}

			if (isNotBlank(idPart)) {
				return provideCompartmentMemberInstanceResponse(theRequestDetails, idPart);
			}

		}

		return provideUnsupportedQueryResponse(theReadDetails);
	}

	private String getSingleResourceIdValueOrNull(SearchParameterMap theParams, String theParamName, String theResourceType) {
		String idPart = null;
		List<List<IQueryParameterType>> idParamAndList = theParams.get(theParamName);
		if (idParamAndList != null && idParamAndList.size() == 1) {
			List<IQueryParameterType> idParamOrList = idParamAndList.get(0);
			if (idParamOrList.size() == 1) {
				IQueryParameterType idParam = idParamOrList.get(0);
				if (isNotBlank(idParam.getQueryParameterQualifier())) {
					return null;
				}
				if (idParam instanceof ReferenceParam) {
					if (((ReferenceParam) idParam).getChain() != null) {
						return null;
					}
				}

				IdType id = new IdType(idParam.getValueAsQueryToken(myFhirContext));
				if (!id.hasResourceType() || id.getResourceType().equals(theResourceType)) {
					idPart = id.getIdPart();
				}
			}
		}
		return idPart;
	}


	/**
	 * Return a partition or throw an error for FHIR operations that can not be used with this interceptor
	 */
	protected RequestPartitionId provideUnsupportedQueryResponse(ReadPartitionIdRequestDetails theRequestDetails) {
		throw new InvalidRequestException("This server is not able to handle this request of type " + theRequestDetails.getRestOperationType());
	}


	/**
	 * Generate the partition for a given patient resource ID. This method may be overridden in subclasses, but it
	 * may be easier to override {@link #providePartitionIdForPatientId(RequestDetails, String)} instead.
	 */
	@Nonnull
	protected RequestPartitionId provideCompartmentMemberInstanceResponse(RequestDetails theRequestDetails, String theResourceIdPart) {
		int partitionId = providePartitionIdForPatientId(theRequestDetails, theResourceIdPart);
		return RequestPartitionId.fromPartitionId(partitionId);
	}

	/**
	 * Translates an ID (e.g. "ABC") into a compartment ID number.
	 *
	 * The default implementation of this method returns:
	 * <code>Math.abs(theResourceIdPart.hashCode()) % 15000</code>.
	 *
	 * This logic can be replaced with other logic of your choosing.
	 */
	@SuppressWarnings("unused")
	protected int providePartitionIdForPatientId(RequestDetails theRequestDetails, String theResourceIdPart) {
		return Math.abs(theResourceIdPart.hashCode()) % 15000;
	}

	/**
	 * Return a compartment ID (or throw an exception) when an attempt is made to search for a resource that is
	 * in the patient compartment, but without any search parameter identifying which compartment to search.
	 *
	 * E.g. this method will be called for the search <code>Observation?code=foo</code> since the patient
	 * is not identified in the URL.
	 */
	@Nonnull
	protected RequestPartitionId provideNonCompartmentMemberInstanceResponse(IBaseResource theResource) {
		throw new PreconditionFailedException("Resource of type " + myFhirContext.getResourceType(theResource) + " has no values placing it in the Patient compartment");
	}

	/**
	 * Return a compartment ID (or throw an exception) when storing/reading resource types that
	 * are not in the patient compartment (e.g. ValueSet).
	 */
	@SuppressWarnings("unused")
	@Nonnull
	protected RequestPartitionId provideNonCompartmentMemberTypeResponse(IBaseResource theResource) {
		return RequestPartitionId.defaultPartition();
	}


}
