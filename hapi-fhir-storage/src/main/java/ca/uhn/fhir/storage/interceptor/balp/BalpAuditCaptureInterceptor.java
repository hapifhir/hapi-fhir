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
package ca.uhn.fhir.storage.interceptor.balp;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IPreResourceShowDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.UrlUtil;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * The IHE Basic Audit Logging Pattern (BALP) interceptor can be used to autopmatically generate
 * AuditEvent resources that are conformant to the BALP profile in response to events in a
 * FHIR server. See <a href="https://hapifhir.io/hapi-fhir/security/balp_interceptor.html">BALP Interceptor</a>
 * in the HAPI FHIR documentation for more information.
 *
 * @since 6.6.0
 */
public class BalpAuditCaptureInterceptor {

	private final IBalpAuditEventSink myAuditEventSink;
	private final IBalpAuditContextServices myContextServices;
	private Set<String> myAdditionalPatientCompartmentParamNames;

	/**
	 * Constructor
	 *
	 * @param theAuditEventSink  This service is the target for generated AuditEvent resources. The {@link BalpAuditCaptureInterceptor}
	 *                           does not actually store AuditEvents, it simply generates them when appropriate and passes them to
	 *                           the sink service. The sink service might store them locally, transmit them to a remote
	 *                           repository, or even simply log them to a syslog.
	 * @param theContextServices This service supplies details to the BALP about the context of a given request. For example,
	 *                           in order to generate a conformant AuditEvent resource, this interceptor needs to determine the
	 *                           identity of the user and the client from the {@link ca.uhn.fhir.rest.api.server.RequestDetails}
	 *                           object.
	 */
	public BalpAuditCaptureInterceptor(
			@Nonnull IBalpAuditEventSink theAuditEventSink, @Nonnull IBalpAuditContextServices theContextServices) {
		Validate.notNull(theAuditEventSink);
		Validate.notNull(theContextServices);
		myAuditEventSink = theAuditEventSink;
		myContextServices = theContextServices;
	}

	private static void addEntityPatient(AuditEvent theAuditEvent, String thePatientId) {
		AuditEvent.AuditEventEntityComponent entityPatient = theAuditEvent.addEntity();
		entityPatient
				.getType()
				.setSystem(BalpConstants.CS_AUDIT_ENTITY_TYPE)
				.setCode(BalpConstants.CS_AUDIT_ENTITY_TYPE_1_PERSON)
				.setDisplay(BalpConstants.CS_AUDIT_ENTITY_TYPE_1_PERSON_DISPLAY);
		entityPatient
				.getRole()
				.setSystem(BalpConstants.CS_OBJECT_ROLE)
				.setCode(BalpConstants.CS_OBJECT_ROLE_1_PATIENT)
				.setDisplay(BalpConstants.CS_OBJECT_ROLE_1_PATIENT_DISPLAY);
		entityPatient.getWhat().setReference(thePatientId);
	}

	private static void addEntityData(AuditEvent theAuditEvent, String theDataResourceId) {
		AuditEvent.AuditEventEntityComponent entityData = theAuditEvent.addEntity();
		entityData
				.getType()
				.setSystem(BalpConstants.CS_AUDIT_ENTITY_TYPE)
				.setCode(BalpConstants.CS_AUDIT_ENTITY_TYPE_2_SYSTEM_OBJECT)
				.setDisplay(BalpConstants.CS_AUDIT_ENTITY_TYPE_2_SYSTEM_OBJECT_DISPLAY);
		entityData
				.getRole()
				.setSystem(BalpConstants.CS_OBJECT_ROLE)
				.setCode(BalpConstants.CS_OBJECT_ROLE_4_DOMAIN_RESOURCE)
				.setDisplay(BalpConstants.CS_OBJECT_ROLE_4_DOMAIN_RESOURCE_DISPLAY);
		entityData.getWhat().setReference(theDataResourceId);
	}

	public void setAdditionalPatientCompartmentParamNames(Set<String> theAdditionalPatientCompartmentParamNames) {
		myAdditionalPatientCompartmentParamNames = theAdditionalPatientCompartmentParamNames;
	}

	/**
	 * Interceptor hook method. Do not call directly.
	 */
	@SuppressWarnings("EnumSwitchStatementWhichMissesCases")
	@Hook(Pointcut.STORAGE_PRESHOW_RESOURCES)
	void hookStoragePreShowResources(IPreResourceShowDetails theDetails, ServletRequestDetails theRequestDetails) {
		switch (theRequestDetails.getRestOperationType()) {
			case SEARCH_TYPE:
			case SEARCH_SYSTEM:
			case GET_PAGE:
				handleSearch(theDetails, theRequestDetails);
				break;
			case READ:
			case VREAD:
				handleReadOrVRead(theDetails, theRequestDetails);
				break;
			default:
				// No actions for other operations
		}
	}

	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED)
	public void hookStoragePrecommitResourceCreated(
			IBaseResource theResource, ServletRequestDetails theRequestDetails) {
		handleCreateUpdateDelete(
				theResource, theRequestDetails, BalpProfileEnum.BASIC_CREATE, BalpProfileEnum.PATIENT_CREATE);
	}

	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_DELETED)
	public void hookStoragePrecommitResourceDeleted(
			IBaseResource theResource, ServletRequestDetails theRequestDetails) {
		handleCreateUpdateDelete(
				theResource, theRequestDetails, BalpProfileEnum.BASIC_DELETE, BalpProfileEnum.PATIENT_DELETE);
	}

	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED)
	public void hookStoragePrecommitResourceUpdated(
			IBaseResource theOldResource, IBaseResource theResource, ServletRequestDetails theRequestDetails) {
		handleCreateUpdateDelete(
				theResource, theRequestDetails, BalpProfileEnum.BASIC_UPDATE, BalpProfileEnum.PATIENT_UPDATE);
	}

	private void handleCreateUpdateDelete(
			IBaseResource theResource,
			ServletRequestDetails theRequestDetails,
			BalpProfileEnum theBasicProfile,
			BalpProfileEnum thePatientProfile) {
		Set<String> patientCompartmentOwners =
				determinePatientCompartmentOwnersForResources(List.of(theResource), theRequestDetails);
		if (patientCompartmentOwners.isEmpty()) {
			AuditEvent auditEvent =
					createAuditEventBasicCreateUpdateDelete(theRequestDetails, theResource, theBasicProfile);
			myAuditEventSink.recordAuditEvent(auditEvent);
		} else {
			AuditEvent auditEvent = createAuditEventPatientCreateUpdateDelete(
					theRequestDetails, theResource, patientCompartmentOwners, thePatientProfile);
			myAuditEventSink.recordAuditEvent(auditEvent);
		}
	}

	private void handleReadOrVRead(IPreResourceShowDetails theDetails, ServletRequestDetails theRequestDetails) {
		Validate.isTrue(theDetails.size() == 1, "Unexpected number of results for read: %d", theDetails.size());
		IBaseResource resource = theDetails.getResource(0);
		if (resource != null) {
			String dataResourceId =
					myContextServices.massageResourceIdForStorage(theRequestDetails, resource, resource.getIdElement());
			Set<String> patientIds =
					determinePatientCompartmentOwnersForResources(List.of(resource), theRequestDetails);

			// If the resource is in the Patient compartment, create one audit
			// event for each compartment owner
			for (String patientId : patientIds) {
				AuditEvent auditEvent = createAuditEventPatientRead(theRequestDetails, dataResourceId, patientId);
				myAuditEventSink.recordAuditEvent(auditEvent);
			}

			// Otherwise, this is a basic read so create a basic read audit event
			if (patientIds.isEmpty()) {
				AuditEvent auditEvent = createAuditEventBasicRead(theRequestDetails, dataResourceId);
				myAuditEventSink.recordAuditEvent(auditEvent);
			}
		}
	}

	private void handleSearch(IPreResourceShowDetails theDetails, ServletRequestDetails theRequestDetails) {

		List<IBaseResource> resources = theDetails.getAllResources();
		Set<String> compartmentOwners = determinePatientCompartmentOwnersForResources(resources, theRequestDetails);

		if (!compartmentOwners.isEmpty()) {
			AuditEvent auditEvent = createAuditEventPatientQuery(theRequestDetails, compartmentOwners);
			myAuditEventSink.recordAuditEvent(auditEvent);
		} else {
			AuditEvent auditEvent = createAuditEventBasicQuery(theRequestDetails);
			myAuditEventSink.recordAuditEvent(auditEvent);
		}
	}

	@Nonnull
	private Set<String> determinePatientCompartmentOwnersForResources(
			List<IBaseResource> theResources, ServletRequestDetails theRequestDetails) {
		Set<String> patientIds = new TreeSet<>();
		FhirContext fhirContext = theRequestDetails.getFhirContext();

		for (IBaseResource resource : theResources) {
			RuntimeResourceDefinition resourceDef = fhirContext.getResourceDefinition(resource);
			if (resourceDef.getName().equals("Patient")) {
				patientIds.add(myContextServices.massageResourceIdForStorage(
						theRequestDetails, resource, resource.getIdElement()));
			} else {
				List<RuntimeSearchParam> compartmentSearchParameters =
						resourceDef.getSearchParamsForCompartmentName("Patient");
				if (!compartmentSearchParameters.isEmpty()) {
					FhirTerser terser = fhirContext.newTerser();
					terser
							.getCompartmentOwnersForResource(
									"Patient", resource, myAdditionalPatientCompartmentParamNames)
							.stream()
							.map(t -> myContextServices.massageResourceIdForStorage(theRequestDetails, resource, t))
							.forEach(patientIds::add);
				}
			}
		}
		return patientIds;
	}

	@Nonnull
	private AuditEvent createAuditEventCommonCreate(
			ServletRequestDetails theRequestDetails, IBaseResource theResource, BalpProfileEnum profile) {
		AuditEvent auditEvent = createAuditEventCommon(theRequestDetails, profile);

		String resourceId = myContextServices.massageResourceIdForStorage(
				theRequestDetails, theResource, theResource.getIdElement());
		addEntityData(auditEvent, resourceId);
		return auditEvent;
	}

	@Nonnull
	private AuditEvent createAuditEventBasicCreateUpdateDelete(
			ServletRequestDetails theRequestDetails, IBaseResource theResource, BalpProfileEnum theProfile) {
		return createAuditEventCommonCreate(theRequestDetails, theResource, theProfile);
	}

	@Nonnull
	private AuditEvent createAuditEventBasicQuery(ServletRequestDetails theRequestDetails) {
		BalpProfileEnum profile = BalpProfileEnum.BASIC_QUERY;
		AuditEvent auditEvent = createAuditEventCommonQuery(theRequestDetails, profile);
		return auditEvent;
	}

	@Nonnull
	private AuditEvent createAuditEventBasicRead(ServletRequestDetails theRequestDetails, String dataResourceId) {
		return createAuditEventCommonRead(theRequestDetails, dataResourceId, BalpProfileEnum.BASIC_READ);
	}

	@Nonnull
	private AuditEvent createAuditEventPatientCreateUpdateDelete(
			ServletRequestDetails theRequestDetails,
			IBaseResource theResource,
			Set<String> thePatientCompartmentOwners,
			BalpProfileEnum theProfile) {
		AuditEvent retVal = createAuditEventCommonCreate(theRequestDetails, theResource, theProfile);
		for (String next : thePatientCompartmentOwners) {
			addEntityPatient(retVal, next);
		}
		return retVal;
	}

	@Nonnull
	private AuditEvent createAuditEventPatientQuery(
			ServletRequestDetails theRequestDetails, Set<String> compartmentOwners) {
		BalpProfileEnum profile = BalpProfileEnum.PATIENT_QUERY;
		AuditEvent auditEvent = createAuditEventCommonQuery(theRequestDetails, profile);
		for (String next : compartmentOwners) {
			addEntityPatient(auditEvent, next);
		}
		return auditEvent;
	}

	@Nonnull
	private AuditEvent createAuditEventPatientRead(
			ServletRequestDetails theRequestDetails, String dataResourceId, String patientId) {
		BalpProfileEnum profile = BalpProfileEnum.PATIENT_READ;
		AuditEvent auditEvent = createAuditEventCommonRead(theRequestDetails, dataResourceId, profile);
		addEntityPatient(auditEvent, patientId);
		return auditEvent;
	}

	@Nonnull
	private AuditEvent createAuditEventCommon(ServletRequestDetails theRequestDetails, BalpProfileEnum theProfile) {
		RestOperationTypeEnum restOperationType = theRequestDetails.getRestOperationType();
		if (restOperationType == RestOperationTypeEnum.GET_PAGE) {
			restOperationType = RestOperationTypeEnum.SEARCH_TYPE;
		}

		AuditEvent auditEvent = new AuditEvent();
		auditEvent.getMeta().addProfile(theProfile.getProfileUrl());
		auditEvent
				.getText()
				.setDiv(new XhtmlNode().setValue("<div>Audit Event</div>"))
				.setStatus(org.hl7.fhir.r4.model.Narrative.NarrativeStatus.GENERATED);
		auditEvent
				.getType()
				.setSystem(BalpConstants.CS_AUDIT_EVENT_TYPE)
				.setCode("rest")
				.setDisplay("Restful Operation");
		auditEvent
				.addSubtype()
				.setSystem(BalpConstants.CS_RESTFUL_INTERACTION)
				.setCode(restOperationType.getCode())
				.setDisplay(restOperationType.getCode());
		auditEvent.setAction(theProfile.getAction());
		auditEvent.setOutcome(AuditEvent.AuditEventOutcome._0);
		auditEvent.setRecorded(new Date());

		auditEvent.getSource().getObserver().setDisplay(theRequestDetails.getFhirServerBase());

		AuditEvent.AuditEventAgentComponent clientAgent = auditEvent.addAgent();
		clientAgent.setWho(myContextServices.getAgentClientWho(theRequestDetails));
		clientAgent.getType().addCoding(theProfile.getAgentClientTypeCoding());
		clientAgent.getWho().setDisplay(myContextServices.getNetworkAddress(theRequestDetails));
		clientAgent
				.getNetwork()
				.setAddress(myContextServices.getNetworkAddress(theRequestDetails))
				.setType(myContextServices.getNetworkAddressType(theRequestDetails));
		clientAgent.setRequestor(false);

		AuditEvent.AuditEventAgentComponent serverAgent = auditEvent.addAgent();
		serverAgent.getType().addCoding(theProfile.getAgentServerTypeCoding());
		serverAgent.getWho().setDisplay(theRequestDetails.getFhirServerBase());
		serverAgent.getNetwork().setAddress(theRequestDetails.getFhirServerBase());
		serverAgent.setRequestor(false);

		AuditEvent.AuditEventAgentComponent userAgent = auditEvent.addAgent();
		userAgent
				.getType()
				.addCoding()
				.setSystem("http://terminology.hl7.org/CodeSystem/v3-ParticipationType")
				.setCode("IRCP")
				.setDisplay("information recipient");
		userAgent.setWho(myContextServices.getAgentUserWho(theRequestDetails));
		userAgent.setRequestor(true);

		AuditEvent.AuditEventEntityComponent entityTransaction = auditEvent.addEntity();
		entityTransaction
				.getType()
				.setSystem("https://profiles.ihe.net/ITI/BALP/CodeSystem/BasicAuditEntityType")
				.setCode("XrequestId");
		entityTransaction.getWhat().getIdentifier().setValue(theRequestDetails.getRequestId());
		return auditEvent;
	}

	@Nonnull
	private AuditEvent createAuditEventCommonQuery(ServletRequestDetails theRequestDetails, BalpProfileEnum profile) {
		AuditEvent auditEvent = createAuditEventCommon(theRequestDetails, profile);

		AuditEvent.AuditEventEntityComponent queryEntity = auditEvent.addEntity();
		queryEntity
				.getType()
				.setSystem(BalpConstants.CS_AUDIT_ENTITY_TYPE)
				.setCode(BalpConstants.CS_AUDIT_ENTITY_TYPE_2_SYSTEM_OBJECT)
				.setDisplay(BalpConstants.CS_AUDIT_ENTITY_TYPE_2_SYSTEM_OBJECT_DISPLAY);
		queryEntity
				.getRole()
				.setSystem(BalpConstants.CS_OBJECT_ROLE)
				.setCode(BalpConstants.CS_OBJECT_ROLE_24_QUERY)
				.setDisplay(BalpConstants.CS_OBJECT_ROLE_24_QUERY_DISPLAY);

		// Description
		StringBuilder description = new StringBuilder();
		description.append(theRequestDetails.getRequestType().name());
		description.append(" ");
		description.append(theRequestDetails.getCompleteUrl());
		queryEntity.setDescription(description.toString());

		// Query String
		StringBuilder queryString = new StringBuilder();
		queryString.append(theRequestDetails.getFhirServerBase());
		queryString.append("/");
		queryString.append(theRequestDetails.getRequestPath());
		boolean first = true;
		for (Map.Entry<String, String[]> nextEntrySet :
				theRequestDetails.getParameters().entrySet()) {
			for (String nextValue : nextEntrySet.getValue()) {
				if (first) {
					queryString.append("?");
					first = false;
				} else {
					queryString.append("&");
				}
				queryString.append(UrlUtil.escapeUrlParam(nextEntrySet.getKey()));
				queryString.append("=");
				queryString.append(UrlUtil.escapeUrlParam(nextValue));
			}
		}

		queryEntity.getQueryElement().setValue(queryString.toString().getBytes(StandardCharsets.UTF_8));
		return auditEvent;
	}

	@Nonnull
	private AuditEvent createAuditEventCommonRead(
			ServletRequestDetails theRequestDetails, String theDataResourceId, BalpProfileEnum theProfile) {
		AuditEvent auditEvent = createAuditEventCommon(theRequestDetails, theProfile);
		addEntityData(auditEvent, theDataResourceId);
		return auditEvent;
	}
}
