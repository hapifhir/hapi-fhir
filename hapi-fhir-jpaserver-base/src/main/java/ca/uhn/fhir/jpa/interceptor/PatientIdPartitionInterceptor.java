package ca.uhn.fhir.jpa.interceptor;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.extractor.BaseSearchParamExtractor;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import org.apache.commons.lang3.StringUtils;
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
 * based on the patient ID associated with the resource (and uses a default partition ID for any resources
 * not in the patient compartment).
 */
@Interceptor
public class PatientIdPartitionInterceptor {

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private ISearchParamExtractor mySearchParamExtractor;

	/**
	 * Constructor
	 */
	public PatientIdPartitionInterceptor() {
		super();
	}

	/**
	 * Constructor
	 */
	public PatientIdPartitionInterceptor(FhirContext theFhirContext, ISearchParamExtractor theSearchParamExtractor) {
		this();
		myFhirContext = theFhirContext;
		mySearchParamExtractor = theSearchParamExtractor;
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
			if (isBlank(compartmentIdentity)) {
				throw new MethodNotAllowedException(Msg.code(1321) + "Patient resource IDs must be client-assigned in patient compartment mode");
			}
		} else {
			compartmentIdentity = compartmentSps
				.stream()
				.flatMap(param -> Arrays.stream(BaseSearchParamExtractor.splitPathsR4(param.getPath())))
				.filter(StringUtils::isNotBlank)
				.map(path -> mySearchParamExtractor.getPathValueExtractor(theResource, path).get())
				.filter(t -> !t.isEmpty())
				.map(t -> t.get(0))
				.filter(t -> t instanceof IBaseReference)
				.map(t -> (IBaseReference) t)
				.map(t -> t.getReferenceElement().getValue())
				.map(t -> new IdType(t).getIdPart())
				.filter(StringUtils::isNotBlank)
				.findFirst()
				.orElse(null);
			if (isBlank(compartmentIdentity)) {
				return provideNonCompartmentMemberInstanceResponse(theResource);
			}
		}


		return provideCompartmentMemberInstanceResponse(theRequestDetails, compartmentIdentity);
	}

	@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_READ)
	public RequestPartitionId identifyForRead(ReadPartitionIdRequestDetails theReadDetails, RequestDetails theRequestDetails) {
		RuntimeResourceDefinition resourceDef = myFhirContext.getResourceDefinition(theReadDetails.getResourceType());
		List<RuntimeSearchParam> compartmentSps = getCompartmentSearchParams(resourceDef);
		if (compartmentSps.isEmpty()) {
			return provideNonCompartmentMemberTypeResponse(null);
		}

		//noinspection EnumSwitchStatementWhichMissesCases
		switch (theReadDetails.getRestOperationType()) {
			case READ:
			case VREAD:
				if ("Patient".equals(theReadDetails.getResourceType())) {
					return provideCompartmentMemberInstanceResponse(theRequestDetails, theReadDetails.getReadResourceId().getIdPart());
				}
				break;
			case SEARCH_TYPE:
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

				break;

			default:
				// nothing
		}

		// If we couldn't identify a patient ID by the URL, let's try using the
		// conditional target if we have one
		if (theReadDetails.getConditionalTargetOrNull() != null) {
			return identifyForCreate(theReadDetails.getConditionalTargetOrNull(), theRequestDetails);
		}

		return provideNonPatientSpecificQueryResponse(theReadDetails);
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

	private String getSingleResourceIdValueOrNull(SearchParameterMap theParams, String theParamName, String theResourceType) {
		String idPart = null;
		List<List<IQueryParameterType>> idParamAndList = theParams.get(theParamName);
		if (idParamAndList != null && idParamAndList.size() == 1) {
			List<IQueryParameterType> idParamOrList = idParamAndList.get(0);
			if (idParamOrList.size() == 1) {
				IQueryParameterType idParam = idParamOrList.get(0);
				if (isNotBlank(idParam.getQueryParameterQualifier())) {
					throw new MethodNotAllowedException(Msg.code(1322) + "The parameter " + theParamName + idParam.getQueryParameterQualifier() + " is not supported in patient compartment mode");
				}
				if (idParam instanceof ReferenceParam) {
					String chain = ((ReferenceParam) idParam).getChain();
					if (chain != null) {
						throw new MethodNotAllowedException(Msg.code(1323) + "The parameter " + theParamName + "." + chain + " is not supported in patient compartment mode");
					}
				}

				IdType id = new IdType(idParam.getValueAsQueryToken(myFhirContext));
				if (!id.hasResourceType() || id.getResourceType().equals(theResourceType)) {
					idPart = id.getIdPart();
				}
			} else if (idParamOrList.size() > 1) {
				throw new MethodNotAllowedException(Msg.code(1324) + "Multiple values for parameter " + theParamName + " is not supported in patient compartment mode");
			}
		} else if (idParamAndList != null && idParamAndList.size() > 1) {
			throw new MethodNotAllowedException(Msg.code(1325) + "Multiple values for parameter " + theParamName + " is not supported in patient compartment mode");
		}
		return idPart;
	}


	/**
	 * Return a partition or throw an error for FHIR operations that can not be used with this interceptor
	 */
	protected RequestPartitionId provideNonPatientSpecificQueryResponse(ReadPartitionIdRequestDetails theRequestDetails) {
		return RequestPartitionId.allPartitions();
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
	 * <p>
	 * The default implementation of this method returns:
	 * <code>Math.abs(theResourceIdPart.hashCode()) % 15000</code>.
	 * <p>
	 * This logic can be replaced with other logic of your choosing.
	 */
	@SuppressWarnings("unused")
	protected int providePartitionIdForPatientId(RequestDetails theRequestDetails, String theResourceIdPart) {
		return Math.abs(theResourceIdPart.hashCode() % 15000);
	}

	/**
	 * Return a compartment ID (or throw an exception) when an attempt is made to search for a resource that is
	 * in the patient compartment, but without any search parameter identifying which compartment to search.
	 * <p>
	 * E.g. this method will be called for the search <code>Observation?code=foo</code> since the patient
	 * is not identified in the URL.
	 */
	@Nonnull
	protected RequestPartitionId provideNonCompartmentMemberInstanceResponse(IBaseResource theResource) {
		throw new MethodNotAllowedException(Msg.code(1326) + "Resource of type " + myFhirContext.getResourceType(theResource) + " has no values placing it in the Patient compartment");
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
