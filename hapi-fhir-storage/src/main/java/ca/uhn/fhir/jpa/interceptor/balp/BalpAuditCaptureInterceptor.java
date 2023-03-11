package ca.uhn.fhir.jpa.interceptor.balp;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.server.IPreResourceShowDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.FhirTerser;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.AuditEvent;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

	@Hook(Pointcut.STORAGE_PRESHOW_RESOURCES)
	public void accessResources(IPreResourceShowDetails theDetails, ServletRequestDetails theRequestDetails) {
		IBaseResource resource = theDetails.getResource(0);
		FhirContext fhirContext = theRequestDetails.getFhirContext();
		RuntimeResourceDefinition resourceDef = fhirContext.getResourceDefinition(resource);
		IIdType targetId = resource.getIdElement();
		String serverBaseUrl = theRequestDetails.getServerBaseForRequest();

		String dataResourceId = myContextServices.massageResourceIdForStorage(theRequestDetails, resource, targetId);
		List<String> patientIds = Collections.emptyList();
		if (resourceDef.getName().equals("Patient")) {
			patientIds = List.of(myContextServices.massageResourceIdForStorage(theRequestDetails, resource, targetId));
		} else {
			List<RuntimeSearchParam> compartmentSearchParameters = resourceDef.getSearchParamsForCompartmentName("Patient");
			if (compartmentSearchParameters.size() > 0) {
				FhirTerser terser = fhirContext.newTerser();
				patientIds = terser
					.getCompartmentOwnersForResource("Patient", resource, myAdditionalPatientCompartmentParamNames)
					.stream()
					.map(t -> myContextServices.massageResourceIdForStorage(theRequestDetails, resource, t))
					.collect(Collectors.toList());
			}
		}

		// If the resource is in the Patient compartment, create one audit
		// event for each compartment owner
		for (String patientId : patientIds) {
			AuditEvent auditEvent = createAuditEventPatientRead(theRequestDetails, serverBaseUrl, dataResourceId, patientId);
			myAuditEventSink.recordAuditEvent(auditEvent);
		}

		// Otherwise, this is a basic read so create a basic read audit event
		if (patientIds.isEmpty()) {
			AuditEvent auditEvent = createAuditEventBasicRead(theRequestDetails, serverBaseUrl, dataResourceId);
			myAuditEventSink.recordAuditEvent(auditEvent);
		}

	}

	@Nonnull
	private AuditEvent createAuditEventBasicRead(ServletRequestDetails theRequestDetails, String serverBaseUrl, String dataResourceId) {
		BalpProfileEnum profile = BalpProfileEnum.BASIC_READ;

		return createAuditEventRead(theRequestDetails, serverBaseUrl, dataResourceId, profile);
	}

	@Nonnull
	private AuditEvent createAuditEventPatientRead(ServletRequestDetails theRequestDetails, String serverBaseUrl, String dataResourceId, String patientId) {
		BalpProfileEnum profile = BalpProfileEnum.PATIENT_READ;

		AuditEvent auditEvent = createAuditEventRead(theRequestDetails, serverBaseUrl, dataResourceId, profile);

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
		return auditEvent;
	}

	@Nonnull
	private AuditEvent createAuditEventRead(ServletRequestDetails theRequestDetails, String serverBaseUrl, String dataResourceId, BalpProfileEnum profile) {
		AuditEvent auditEvent = new AuditEvent();
		auditEvent.getMeta().addProfile(profile.getProfileUrl());
		auditEvent.getType()
			.setSystem(CS_AUDIT_EVENT_TYPE)
			.setCode("rest")
			.setDisplay("Restful Operation");
		auditEvent.addSubtype()
			.setSystem(CS_RESTFUL_INTERACTION)
			.setCode(theRequestDetails.getRestOperationType().getCode())
			.setDisplay(theRequestDetails.getRestOperationType().getCode());
		auditEvent.setAction(AuditEvent.AuditEventAction.R);
		auditEvent.setOutcome(AuditEvent.AuditEventOutcome._0);
		auditEvent.setRecorded(new Date());

		auditEvent
			.getSource()
			.getObserver()
			.setDisplay(serverBaseUrl);

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
		serverAgent.getWho().setDisplay(serverBaseUrl);
		serverAgent
			.getNetwork()
			.setAddress(serverBaseUrl);
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
			.setReference(dataResourceId);
		return auditEvent;
	}

}
