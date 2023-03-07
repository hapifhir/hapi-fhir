package ca.uhn.fhir.jpa.interceptor.balp;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.server.IPreResourceShowDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.AuditEvent;

import javax.annotation.Nonnull;
import java.util.Date;

/**
 * @since 6.6.0
 */
public class BalpAuditCaptureInterceptor {

	private final VersionCanonicalizer myCanonicalizer;
	private final IAuditEventSink myAuditEventSink;
	private final IAuditContextServices myContextServices;

	/**
	 * Constructor
	 */
	public BalpAuditCaptureInterceptor(@Nonnull FhirContext theFhirContext, @Nonnull IAuditEventSink theAuditEventSink, @Nonnull IAuditContextServices theContextServices) {
		Validate.notNull(theFhirContext);
		Validate.notNull(theAuditEventSink);
		Validate.notNull(theContextServices);
		myCanonicalizer = new VersionCanonicalizer(theFhirContext);
		myAuditEventSink = theAuditEventSink;
		myContextServices = theContextServices;
	}

	@Hook(Pointcut.STORAGE_PRESHOW_RESOURCES)
	public void accessResources(IPreResourceShowDetails theDetails, RequestDetails theRequestDetails) {
		ServletRequestDetails requestDetails = (ServletRequestDetails) theRequestDetails;

		String serverBaseUrl = requestDetails.getServerBaseForRequest();
		IBaseResource resource = theDetails.getResource(0);
		String resourceName = theRequestDetails.getFhirContext().getResourceType(resource);
		String patientId = resource.getIdElement().withServerBase(serverBaseUrl, resourceName).getValue();

		AuditEvent auditEvent = new AuditEvent();
		auditEvent.getMeta().addProfile(BalpProfileEnum.PATIENT_READ.getProfileUrl());
		auditEvent.getType()
			.setSystem("http://terminology.hl7.org/CodeSystem/audit-event-type")
			.setCode("rest")
			.setDisplay("Restful Operation");
		auditEvent.addSubtype()
			.setSystem("http://hl7.org/fhir/restful-interaction")
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
			.setAddress(requestDetails.getServletRequest().getRemoteAddr());
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
			.setSystem("http://terminology.hl7.org/CodeSystem/audit-entity-type")
			.setCode("2")
			.setDisplay("System Object");
		entityData
			.getRole()
			.setSystem("http://terminology.hl7.org/CodeSystem/object-role")
			.setCode("4")
			.setDisplay("Domain Resource");
		entityData
			.getWhat()
			.setReference(patientId);

		AuditEvent.AuditEventEntityComponent entityPatient = auditEvent.addEntity();
		entityPatient
			.getType()
			.setSystem("http://terminology.hl7.org/CodeSystem/audit-entity-type")
			.setCode("1")
			.setDisplay("Person");
		entityPatient
			.getRole()
			.setSystem("http://terminology.hl7.org/CodeSystem/object-role")
			.setCode("1")
			.setDisplay("Patient");
		entityPatient
			.getWhat()
			.setReference(patientId);

		IBaseResource storageAuditEvent = myCanonicalizer.auditEventFromCanonical(auditEvent);

		myAuditEventSink.recordAuditEvent(storageAuditEvent);

	}

}
