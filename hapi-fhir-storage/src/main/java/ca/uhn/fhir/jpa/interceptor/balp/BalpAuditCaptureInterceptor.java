package ca.uhn.fhir.jpa.interceptor.balp;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.UrlUtil;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.AuditEvent;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @since 6.6.0
 */
public class BalpAuditCaptureInterceptor {

	public static final String CS_AUDIT_EVENT_TYPE = "http://terminology.hl7.org/CodeSystem/audit-event-type";
	public static final String CS_AUDIT_ENTITY_TYPE = "http://terminology.hl7.org/CodeSystem/audit-entity-type";
	public static final String CS_AUDIT_ENTITY_TYPE_2_SYSTEM_OBJECT = "2";
	public static final String CS_AUDIT_ENTITY_TYPE_2_SYSTEM_OBJECT_DISPLAY = "System Object";
	public static final String CS_AUDIT_ENTITY_TYPE_1_PERSON = "1";
	public static final String CS_AUDIT_ENTITY_TYPE_1_PERSON_DISPLAY = "Person";
	public static final String CS_OBJECT_ROLE = "http://terminology.hl7.org/CodeSystem/object-role";
	public static final String CS_OBJECT_ROLE_1_PATIENT = "1";
	public static final String CS_OBJECT_ROLE_1_PATIENT_DISPLAY = "Patient";
	public static final String CS_OBJECT_ROLE_4_DOMAIN_RESOURCE = "4";
	public static final String CS_OBJECT_ROLE_4_DOMAIN_RESOURCE_DISPLAY = "Domain Resource";
	public static final String CS_RESTFUL_INTERACTION = "http://hl7.org/fhir/restful-interaction";
	public static final String CS_OBJECT_ROLE_24_QUERY = "24";
	private static final String CS_OBJECT_ROLE_24_QUERY_DISPLAY = "Query";
	private final IAuditEventSink myAuditEventSink;
	private final IAuditContextServices myContextServices;
	private Set<String> myAdditionalPatientCompartmentParamNames;

	/**
	 * Constructor
	 */
	public BalpAuditCaptureInterceptor(@Nonnull FhirContext theFhirContext, @Nonnull IAuditEventSink theAuditEventSink, @Nonnull IAuditContextServices theContextServices) {
		Validate.notNull(theFhirContext);
		Validate.notNull(theAuditEventSink);
		Validate.notNull(theContextServices);
		myAuditEventSink = theAuditEventSink;
		myContextServices = theContextServices;
	}

	public void setAdditionalPatientCompartmentParamNames(Set<String> theAdditionalPatientCompartmentParamNames) {
		myAdditionalPatientCompartmentParamNames = theAdditionalPatientCompartmentParamNames;
	}

	@Hook(Pointcut.SERVER_OUTGOING_RESPONSE)
	public void outgoingResponse(IBaseResource theResource, ServletRequestDetails theRequestDetails) {
		switch (theRequestDetails.getRestOperationType()) {
			case SEARCH_TYPE:
			case SEARCH_SYSTEM:
			case GET_PAGE:
				handleSearch((IBaseBundle) theResource, theRequestDetails);
				break;
			case READ:
			case VREAD:
				handleReadOrVRead(theResource, theRequestDetails);
				break;
		}


	}

	private void handleSearch(IBaseBundle theResponseBundle, ServletRequestDetails theRequestDetails) {

		FhirContext ctx = theRequestDetails.getFhirContext();
		List<IBaseResource> resources = BundleUtil.toListOfResources(ctx, theResponseBundle);
		Set<String> compartmentOwners = determinePatientCompartmentOwnersForResources(resources, theRequestDetails);

		if (!compartmentOwners.isEmpty()) {
			AuditEvent auditEvent = createAuditEventPatientQuery(theRequestDetails, compartmentOwners);
			myAuditEventSink.recordAuditEvent(auditEvent);
		}


	}

	private void handleReadOrVRead(IBaseResource theResource, ServletRequestDetails theRequestDetails) {
		String dataResourceId = myContextServices.massageResourceIdForStorage(theRequestDetails, theResource, theResource.getIdElement());
		Set<String> patientIds = determinePatientCompartmentOwnersForResources(List.of(theResource), theRequestDetails);

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

	@Nonnull
	private Set<String> determinePatientCompartmentOwnersForResources(List<IBaseResource> theResources, ServletRequestDetails theRequestDetails) {
		Set<String> patientIds = new TreeSet<>();
		FhirContext fhirContext = theRequestDetails.getFhirContext();

		for (IBaseResource resource : theResources) {
			RuntimeResourceDefinition resourceDef = fhirContext.getResourceDefinition(resource);
			if (resourceDef.getName().equals("Patient")) {
				patientIds.add(myContextServices.massageResourceIdForStorage(theRequestDetails, resource, resource.getIdElement()));
			} else {
				List<RuntimeSearchParam> compartmentSearchParameters = resourceDef.getSearchParamsForCompartmentName("Patient");
				if (compartmentSearchParameters.size() > 0) {
					FhirTerser terser = fhirContext.newTerser();
					terser
						.getCompartmentOwnersForResource("Patient", resource, myAdditionalPatientCompartmentParamNames)
						.stream()
						.map(t -> myContextServices.massageResourceIdForStorage(theRequestDetails, resource, t))
						.forEach(patientIds::add);
				}
			}
		}
		return patientIds;
	}

	@Nonnull
	private AuditEvent createAuditEventBasicQuery(ServletRequestDetails theRequestDetails, BalpProfileEnum profile) {
		AuditEvent auditEvent = createAuditEventCommon(theRequestDetails, profile, AuditEvent.AuditEventAction.E);

		AuditEvent.AuditEventEntityComponent queryEntity = auditEvent.addEntity();
		queryEntity
			.getType()
			.setSystem(CS_AUDIT_ENTITY_TYPE)
			.setCode(CS_AUDIT_ENTITY_TYPE_2_SYSTEM_OBJECT)
			.setDisplay(CS_AUDIT_ENTITY_TYPE_2_SYSTEM_OBJECT_DISPLAY);
		queryEntity
			.getRole()
			.setSystem(CS_OBJECT_ROLE)
			.setCode(CS_OBJECT_ROLE_24_QUERY)
			.setDisplay(CS_OBJECT_ROLE_24_QUERY_DISPLAY);

		StringBuilder queryString = new StringBuilder(theRequestDetails.getRequestPath());
		queryString.append("?");
		for (Map.Entry<String, String[]> nextEntrySet : theRequestDetails.getParameters().entrySet()) {
			for (String nextValue : nextEntrySet.getValue()) {
				queryString.append(UrlUtil.escapeUrlParam(nextEntrySet.getKey()));
				queryString.append("=");
				queryString.append(UrlUtil.escapeUrlParam(nextValue));
			}
		}

		StringBuilder description = new StringBuilder();
		HttpServletRequest servletRequest = theRequestDetails.getServletRequest();
		description.append(servletRequest.getMethod());
		description.append(" ");
		description.append(servletRequest.getRequestURI());
		if (isNotBlank(servletRequest.getQueryString())) {
			description.append("?");
			description.append(servletRequest.getQueryString());
		}
		queryEntity.setDescription(description.toString());

		queryEntity
			.getQueryElement()
			.setValue(queryString.toString().getBytes(StandardCharsets.UTF_8));
		return auditEvent;
	}

	@Nonnull
	private AuditEvent createAuditEventBasicRead(ServletRequestDetails theRequestDetails, String dataResourceId) {
		return createAuditEventCommonRead(theRequestDetails, dataResourceId, BalpProfileEnum.BASIC_READ);
	}

	@Nonnull
	private AuditEvent createAuditEventPatientQuery(ServletRequestDetails theRequestDetails, Set<String> compartmentOwners) {
		BalpProfileEnum profile = BalpProfileEnum.PATIENT_QUERY;
		AuditEvent auditEvent = createAuditEventBasicQuery(theRequestDetails, profile);
		for (String next : compartmentOwners) {
			addEntityPatient(next, auditEvent);
		}
		return auditEvent;
	}

	@Nonnull
	private AuditEvent createAuditEventPatientRead(ServletRequestDetails theRequestDetails, String dataResourceId, String patientId) {
		BalpProfileEnum profile = BalpProfileEnum.PATIENT_READ;
		AuditEvent auditEvent = createAuditEventCommonRead(theRequestDetails, dataResourceId, profile);
		addEntityPatient(patientId, auditEvent);
		return auditEvent;
	}

	private static void addEntityPatient(String patientId, AuditEvent auditEvent) {
		AuditEvent.AuditEventEntityComponent entityPatient = auditEvent.addEntity();
		entityPatient
			.getType()
			.setSystem(CS_AUDIT_ENTITY_TYPE)
			.setCode(CS_AUDIT_ENTITY_TYPE_1_PERSON)
			.setDisplay(CS_AUDIT_ENTITY_TYPE_1_PERSON_DISPLAY);
		entityPatient
			.getRole()
			.setSystem(CS_OBJECT_ROLE)
			.setCode(CS_OBJECT_ROLE_1_PATIENT)
			.setDisplay(CS_OBJECT_ROLE_1_PATIENT_DISPLAY);
		entityPatient
			.getWhat()
			.setReference(patientId);
	}

	@Nonnull
	private AuditEvent createAuditEventCommon(ServletRequestDetails theRequestDetails, BalpProfileEnum theProfile, AuditEvent.AuditEventAction action) {
		RestOperationTypeEnum restOperationType = theRequestDetails.getRestOperationType();
		if (restOperationType == RestOperationTypeEnum.GET_PAGE) {
			restOperationType = RestOperationTypeEnum.SEARCH_TYPE;
		}

		AuditEvent auditEvent = new AuditEvent();
		auditEvent.getMeta().addProfile(theProfile.getProfileUrl());
		auditEvent.getType()
			.setSystem(CS_AUDIT_EVENT_TYPE)
			.setCode("rest")
			.setDisplay("Restful Operation");
		auditEvent.addSubtype()
			.setSystem(CS_RESTFUL_INTERACTION)
			.setCode(restOperationType.getCode())
			.setDisplay(restOperationType.getCode());
		auditEvent.setAction(action);
		auditEvent.setOutcome(AuditEvent.AuditEventOutcome._0);
		auditEvent.setRecorded(new Date());

		auditEvent
			.getSource()
			.getObserver()
			.setDisplay(theRequestDetails.getServerBaseForRequest());

		AuditEvent.AuditEventAgentComponent clientAgent = auditEvent.addAgent();
		clientAgent.setWho(myContextServices.getAgentClientWho(theRequestDetails));
		clientAgent
			.getType()
			.addCoding()
			.setSystem("http://dicom.nema.org/resources/ontology/DCM")
			.setCode("110152")
			.setDisplay("Destination Role ID");
		clientAgent
			.getNetwork()
			.setAddress(theRequestDetails.getServletRequest().getRemoteAddr());
		clientAgent.setRequestor(false);

		AuditEvent.AuditEventAgentComponent serverAgent = auditEvent.addAgent();
		serverAgent
			.getType()
			.addCoding()
			.setSystem("http://dicom.nema.org/resources/ontology/DCM")
			.setCode("110153")
			.setDisplay("Source Role ID");
		serverAgent.getWho().setDisplay(theRequestDetails.getServerBaseForRequest());
		serverAgent
			.getNetwork()
			.setAddress(theRequestDetails.getServerBaseForRequest());
		serverAgent.setRequestor(false);

		AuditEvent.AuditEventAgentComponent userAgent = auditEvent.addAgent();
		userAgent
			.getType()
			.addCoding()
			.setSystem("http://terminology.hl7.org/CodeSystem/v3-ParticipationType")
			.setCode("IRCP")
			.setDisplay("information recipient");
		userAgent.setWho(myContextServices.getAgentUserWho(theRequestDetails));
		userAgent
			.setRequestor(true);

		AuditEvent.AuditEventEntityComponent entityTransaction = auditEvent.addEntity();
		entityTransaction
			.getType()
			.setSystem("https://profiles.ihe.net/ITI/BALP/CodeSystem/BasicAuditEntityType")
			.setCode("XrequestId");
		entityTransaction
			.getWhat()
			.getIdentifier()
			.setValue(theRequestDetails.getRequestId());
		return auditEvent;
	}

	@Nonnull
	private AuditEvent createAuditEventCommonRead(ServletRequestDetails theRequestDetails, String theDataResourceId, BalpProfileEnum theProfile) {
		AuditEvent auditEvent = createAuditEventCommon(theRequestDetails, theProfile, AuditEvent.AuditEventAction.R);

		AuditEvent.AuditEventEntityComponent entityData = auditEvent.addEntity();
		entityData
			.getType()
			.setSystem(CS_AUDIT_ENTITY_TYPE)
			.setCode(CS_AUDIT_ENTITY_TYPE_2_SYSTEM_OBJECT)
			.setDisplay(CS_AUDIT_ENTITY_TYPE_2_SYSTEM_OBJECT_DISPLAY);
		entityData
			.getRole()
			.setSystem(CS_OBJECT_ROLE)
			.setCode(CS_OBJECT_ROLE_4_DOMAIN_RESOURCE)
			.setDisplay(CS_OBJECT_ROLE_4_DOMAIN_RESOURCE_DISPLAY);
		entityData
			.getWhat()
			.setReference(theDataResourceId);
		return auditEvent;
	}

}
