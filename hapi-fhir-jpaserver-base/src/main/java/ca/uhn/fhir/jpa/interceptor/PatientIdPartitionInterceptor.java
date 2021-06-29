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

		return provideCompartmentMemberInstanceResponse(compartmentIdentity);
	}

	@Nonnull
	private List<RuntimeSearchParam> getCompartmentSearchParams(RuntimeResourceDefinition resourceDef) {
		List<RuntimeSearchParam> compartmentSps = resourceDef
			.getSearchParams()
			.stream()
			.filter(param -> param.getParamType() == RestSearchParameterTypeEnum.REFERENCE)
			.filter(param -> param.getProvidesMembershipInCompartments() != null && param.getProvidesMembershipInCompartments().contains("Patient"))
			.collect(Collectors.toList());
		return compartmentSps;
	}


	@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_READ)
	public RequestPartitionId identifyForRead(ReadPartitionIdRequestDetails theRequestDetails) {
		RuntimeResourceDefinition resourceDef = myFhirContext.getResourceDefinition(theRequestDetails.getResourceType());
		List<RuntimeSearchParam> compartmentSps = getCompartmentSearchParams(resourceDef);
		if (compartmentSps.isEmpty()) {
			return provideNonCompartmentMemberTypeResponse(null);
		}

		if (theRequestDetails.getRestOperationType() == RestOperationTypeEnum.READ) {
			if ("Patient".equals(theRequestDetails.getResourceType())) {
				return provideCompartmentMemberInstanceResponse(theRequestDetails.getReadResourceId().getIdPart());
			}
		} else if (theRequestDetails.getRestOperationType() == RestOperationTypeEnum.SEARCH_TYPE) {
			SearchParameterMap params = (SearchParameterMap) theRequestDetails.getSearchParams();
			String idPart = null;
			if ("Patient".equals(theRequestDetails.getResourceType())) {
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
				return provideCompartmentMemberInstanceResponse(idPart);
			}

		}

		return provideUnsupportedQueryResponse(theRequestDetails);
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

	protected RequestPartitionId provideUnsupportedQueryResponse(ReadPartitionIdRequestDetails theRequestDetails) {
		throw new InvalidRequestException("This server is not able to handle this request of type " + theRequestDetails.getRestOperationType());
	}


	@Nonnull
	protected RequestPartitionId provideCompartmentMemberInstanceResponse(String theResourceIdPart) {
		return RequestPartitionId.fromPartitionId(theResourceIdPart.hashCode());
	}

	@Nonnull
	protected RequestPartitionId provideNonCompartmentMemberInstanceResponse(IBaseResource theResource) {
		throw new PreconditionFailedException("Resource of type " + myFhirContext.getResourceType(theResource) + " has no values placing it in the Patient compartment");
	}

	@SuppressWarnings("unused")
	@Nonnull
	protected RequestPartitionId provideNonCompartmentMemberTypeResponse(IBaseResource theResource) {
		return RequestPartitionId.defaultPartition();
	}


}
