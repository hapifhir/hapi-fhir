/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.util.ResourceCompartmentUtil;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isEmpty;
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

	@Autowired
	private PartitionSettings myPartitionSettings;

	/**
	 * Constructor
	 */
	public PatientIdPartitionInterceptor(
			FhirContext theFhirContext,
			ISearchParamExtractor theSearchParamExtractor,
			PartitionSettings thePartitionSettings) {
		myFhirContext = theFhirContext;
		mySearchParamExtractor = theSearchParamExtractor;
		myPartitionSettings = thePartitionSettings;
	}

	@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_CREATE)
	public RequestPartitionId identifyForCreate(IBaseResource theResource, RequestDetails theRequestDetails) {
		RuntimeResourceDefinition resourceDef = myFhirContext.getResourceDefinition(theResource);
		List<RuntimeSearchParam> compartmentSps =
				ResourceCompartmentUtil.getPatientCompartmentSearchParams(resourceDef);
		if (compartmentSps.isEmpty()) {
			return provideNonCompartmentMemberTypeResponse(theResource);
		}

		Optional<String> oCompartmentIdentity;
		if (resourceDef.getName().equals("Patient")) {
			oCompartmentIdentity =
					Optional.ofNullable(theResource.getIdElement().getIdPart());
			if (oCompartmentIdentity.isEmpty()) {
				throw new MethodNotAllowedException(
						Msg.code(1321) + "Patient resource IDs must be client-assigned in patient compartment mode");
			}
		} else {
			oCompartmentIdentity =
					ResourceCompartmentUtil.getResourceCompartment(theResource, compartmentSps, mySearchParamExtractor);
		}

		return oCompartmentIdentity
				.map(ci -> provideCompartmentMemberInstanceResponse(theRequestDetails, ci))
				.orElseGet(() -> provideNonCompartmentMemberInstanceResponse(theResource));
	}

	@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_READ)
	public RequestPartitionId identifyForRead(
			@Nonnull ReadPartitionIdRequestDetails theReadDetails, RequestDetails theRequestDetails) {
		List<RuntimeSearchParam> compartmentSps = Collections.emptyList();
		if (!isEmpty(theReadDetails.getResourceType())) {
			RuntimeResourceDefinition resourceDef =
					myFhirContext.getResourceDefinition(theReadDetails.getResourceType());
			compartmentSps = ResourceCompartmentUtil.getPatientCompartmentSearchParams(resourceDef);
			if (compartmentSps.isEmpty()) {
				return provideNonCompartmentMemberTypeResponse(null);
			}
		}

		//noinspection EnumSwitchStatementWhichMissesCases
		switch (theReadDetails.getRestOperationType()) {
			case READ:
			case VREAD:
				if ("Patient".equals(theReadDetails.getResourceType())) {
					return provideCompartmentMemberInstanceResponse(
							theRequestDetails,
							theReadDetails.getReadResourceId().getIdPart());
				}
				break;
			case SEARCH_TYPE:
				SearchParameterMap params = theReadDetails.getSearchParams();
				assert params != null;
				if ("Patient".equals(theReadDetails.getResourceType())) {
					List<String> idParts = getResourceIdList(params, "_id", "Patient", false);
					if (idParts.size() == 1) {
						return provideCompartmentMemberInstanceResponse(theRequestDetails, idParts.get(0));
					} else {
						return RequestPartitionId.allPartitions();
					}
				} else {
					for (RuntimeSearchParam nextCompartmentSp : compartmentSps) {
						List<String> idParts = getResourceIdList(params, nextCompartmentSp.getName(), "Patient", true);
						if (!idParts.isEmpty()) {
							return provideCompartmentMemberInstanceResponse(theRequestDetails, idParts.get(0));
						}
					}
				}

				break;
			case EXTENDED_OPERATION_SERVER:
				String extendedOp = theReadDetails.getExtendedOperationName();
				if (ProviderConstants.OPERATION_EXPORT.equals(extendedOp)
						|| ProviderConstants.OPERATION_EXPORT_POLL_STATUS.equals(extendedOp)) {
					return provideNonPatientSpecificQueryResponse(theReadDetails);
				}
				break;
			default:
				// nothing
		}

		if (isBlank(theReadDetails.getResourceType())) {
			return provideNonCompartmentMemberTypeResponse(null);
		}

		// If we couldn't identify a patient ID by the URL, let's try using the
		// conditional target if we have one
		if (theReadDetails.getConditionalTargetOrNull() != null) {
			return identifyForCreate(theReadDetails.getConditionalTargetOrNull(), theRequestDetails);
		}

		return provideNonPatientSpecificQueryResponse(theReadDetails);
	}

	private List<String> getResourceIdList(
			SearchParameterMap theParams, String theParamName, String theResourceType, boolean theExpectOnlyOneBool) {
		List<List<IQueryParameterType>> idParamAndList = theParams.get(theParamName);
		if (idParamAndList == null) {
			return Collections.emptyList();
		}

		List<String> idParts = new ArrayList<>();
		idParamAndList.stream().flatMap(Collection::stream).forEach(idParam -> {
			if (isNotBlank(idParam.getQueryParameterQualifier())) {
				throw new MethodNotAllowedException(Msg.code(1322) + "The parameter " + theParamName
						+ idParam.getQueryParameterQualifier() + " is not supported in patient compartment mode");
			}
			if (idParam instanceof ReferenceParam) {
				String chain = ((ReferenceParam) idParam).getChain();
				if (chain != null) {
					throw new MethodNotAllowedException(Msg.code(1323) + "The parameter " + theParamName + "." + chain
							+ " is not supported in patient compartment mode");
				}
			}
			IdType id = new IdType(idParam.getValueAsQueryToken(myFhirContext));
			if (!id.hasResourceType() || id.getResourceType().equals(theResourceType)) {
				idParts.add(id.getIdPart());
			}
		});

		if (theExpectOnlyOneBool && idParts.size() > 1) {
			throw new MethodNotAllowedException(Msg.code(1324) + "Multiple values for parameter " + theParamName
					+ " is not supported in patient compartment mode");
		}

		return idParts;
	}

	/**
	 * Return a partition or throw an error for FHIR operations that can not be used with this interceptor
	 */
	protected RequestPartitionId provideNonPatientSpecificQueryResponse(
			ReadPartitionIdRequestDetails theRequestDetails) {
		return RequestPartitionId.allPartitions();
	}

	/**
	 * Generate the partition for a given patient resource ID. This method may be overridden in subclasses, but it
	 * may be easier to override {@link #providePartitionIdForPatientId(RequestDetails, String)} instead.
	 */
	@Nonnull
	protected RequestPartitionId provideCompartmentMemberInstanceResponse(
			RequestDetails theRequestDetails, String theResourceIdPart) {
		int partitionId = providePartitionIdForPatientId(theRequestDetails, theResourceIdPart);
		return RequestPartitionId.fromPartitionIdAndName(partitionId, theResourceIdPart);
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
		throw new MethodNotAllowedException(Msg.code(1326) + "Resource of type "
				+ myFhirContext.getResourceType(theResource) + " has no values placing it in the Patient compartment");
	}

	/**
	 * Return a compartment ID (or throw an exception) when storing/reading resource types that
	 * are not in the patient compartment (e.g. ValueSet).
	 */
	@SuppressWarnings("unused")
	@Nonnull
	protected RequestPartitionId provideNonCompartmentMemberTypeResponse(IBaseResource theResource) {
		return RequestPartitionId.fromPartitionId(myPartitionSettings.getDefaultPartitionId());
	}
}
