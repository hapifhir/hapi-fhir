package ca.uhn.fhir.jpa.provider.r4;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.PatientEverythingParameters;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Account;
import org.hl7.fhir.r4.model.AdverseEvent;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.Appointment;
import org.hl7.fhir.r4.model.AppointmentResponse;
import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.r4.model.Basic;
import org.hl7.fhir.r4.model.BodyStructure;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.CareTeam;
import org.hl7.fhir.r4.model.ChargeItem;
import org.hl7.fhir.r4.model.Claim;
import org.hl7.fhir.r4.model.ClaimResponse;
import org.hl7.fhir.r4.model.ClinicalImpression;
import org.hl7.fhir.r4.model.Communication;
import org.hl7.fhir.r4.model.CommunicationRequest;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Consent;
import org.hl7.fhir.r4.model.Coverage;
import org.hl7.fhir.r4.model.CoverageEligibilityRequest;
import org.hl7.fhir.r4.model.CoverageEligibilityResponse;
import org.hl7.fhir.r4.model.DetectedIssue;
import org.hl7.fhir.r4.model.Device;
import org.hl7.fhir.r4.model.DeviceRequest;
import org.hl7.fhir.r4.model.DeviceUseStatement;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.DocumentManifest;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.EnrollmentRequest;
import org.hl7.fhir.r4.model.EpisodeOfCare;
import org.hl7.fhir.r4.model.ExplanationOfBenefit;
import org.hl7.fhir.r4.model.FamilyMemberHistory;
import org.hl7.fhir.r4.model.Flag;
import org.hl7.fhir.r4.model.Goal;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.ImagingStudy;
import org.hl7.fhir.r4.model.Immunization;
import org.hl7.fhir.r4.model.ImmunizationEvaluation;
import org.hl7.fhir.r4.model.ImmunizationRecommendation;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Invoice;
import org.hl7.fhir.r4.model.ListResource;
import org.hl7.fhir.r4.model.MeasureReport;
import org.hl7.fhir.r4.model.Media;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.MedicationAdministration;
import org.hl7.fhir.r4.model.MedicationDispense;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.MedicationStatement;
import org.hl7.fhir.r4.model.MolecularSequence;
import org.hl7.fhir.r4.model.NutritionOrder;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Procedure;
import org.hl7.fhir.r4.model.Provenance;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.RelatedPerson;
import org.hl7.fhir.r4.model.RequestGroup;
import org.hl7.fhir.r4.model.ResearchSubject;
import org.hl7.fhir.r4.model.RiskAssessment;
import org.hl7.fhir.r4.model.Schedule;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.hl7.fhir.r4.model.Specimen;
import org.hl7.fhir.r4.model.SupplyDelivery;
import org.hl7.fhir.r4.model.SupplyRequest;
import org.hl7.fhir.r4.model.VisionPrescription;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JpaPatientEverythingTest extends BaseResourceProviderR4Test {

    @AfterEach
    public void afterEach() {
        JpaStorageSettings defaults = new JpaStorageSettings();
        myStorageSettings.setResourceClientIdStrategy(defaults.getResourceClientIdStrategy());
    }

    private Reference createPatient() {
        Patient patient = new Patient();
        String patientId = myClient.create().resource(patient).execute().getId().toUnqualifiedVersionless().getValue();
        Reference referenceToPatient = new Reference();
        referenceToPatient.setReference(patientId);
        return referenceToPatient;
    }

    @Test
    public void testLargeEverythingFetchReturnsAllPossibleResources() throws IOException {
        myStorageSettings.setResourceClientIdStrategy(JpaStorageSettings.ClientIdStrategyEnum.ANY);

        // This bundle has a bunch of resources all in the compartment of the
        // patient below
        Bundle input = myFhirContext.newJsonParser().parseResource(Bundle.class, loadCompressedResource("large-bundle-for-everything.json.gz"));
        String patientId = "Patient/9656908";

        mySystemDao.transaction(mySrd, input);

        int expectedEverythingSize = 652;
        runInTransaction(() -> {
            assertEquals(expectedEverythingSize, myResourceTableDao.count());
        });

		// Try with a direct API call
        {
			Set<String> actualResourceIds = new HashSet<>();
			PatientEverythingParameters params = new PatientEverythingParameters();
			int pageSize = 10000;
			params.setCount(new IntegerType(pageSize));
			ServletRequestDetails request = new ServletRequestDetails();
			request.setServer(myServer.getRestfulServer());
			request.setServletRequest(new MockHttpServletRequest());
			request.setServletResponse(new MockHttpServletResponse());
			IBundleProvider outcome = myPatientDao.patientInstanceEverything(null, request, params, new IdType(patientId));
			List<IBaseResource> resources = outcome.getResources(0, pageSize);
			actualResourceIds.addAll(resources.stream().map(t -> t.getIdElement().toUnqualifiedVersionless().getValue()).toList());
					assertThat(actualResourceIds).hasSize(expectedEverythingSize);
		}

        // Try with an HTTP call
        {
            Set<String> actualResourceIds = new HashSet<>();
            Bundle outcome = myClient
                    .operation()
                    .onInstance(new IdType(patientId))
                    .named("$everything")
                    .withNoParameters(Parameters.class)
                    .useHttpGet()
                    .returnResourceType(Bundle.class)
                    .execute();
            while (true) {
                outcome
                        .getEntry()
                        .stream()
                        .map(t -> t.getResource().getIdElement().toUnqualifiedVersionless().getValue())
                        .forEach(t -> actualResourceIds.add(t));
                if (outcome.getLink("next") != null) {
                    outcome = myClient.loadPage().next(outcome).execute();
                } else {
                    break;
                }
            }

					assertThat(actualResourceIds).hasSize(expectedEverythingSize);
        }
    }

    @Test
    public void patientEverything_shouldReturnAccount_whenAccountRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        Account account = new Account();
        account.setSubject(List.of(referenceToPatient));
        String accountId = myClient.create().resource(account).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(accountId);
    }

    @Test
    public void patientEverything_shouldReturnAdverseEvent_whenAdverseEventRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        AdverseEvent adverseEvent = new AdverseEvent();
        adverseEvent.setSubject(referenceToPatient);
        String adverseEventId = myClient.create().resource(adverseEvent).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(adverseEventId);
    }


    @Test
    public void patientEverything_shouldReturnAllergyIntolerance_whenAllergyIntoleranceRefersToPatientAsPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        AllergyIntolerance allergyIntolerance = new AllergyIntolerance();
        allergyIntolerance.setPatient(referenceToPatient);
        String allergyIntoleranceId = myClient.create().resource(allergyIntolerance).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(allergyIntoleranceId);
    }

    @Test
    public void patientEverything_shouldReturnAllergyIntolerance_whenAllergyIntoleranceRefersToPatientAsRecorder() throws Exception {

        Reference referenceToPatient = createPatient();

        AllergyIntolerance allergyIntolerance = new AllergyIntolerance();
        allergyIntolerance.setRecorder(referenceToPatient);
        String allergyIntoleranceId = myClient.create().resource(allergyIntolerance).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(allergyIntoleranceId);
    }

    @Test
    public void patientEverything_shouldReturnAllergyIntolerance_whenAllergyIntoleranceRefersToPatientAsAsserter() throws Exception {

        Reference referenceToPatient = createPatient();

        AllergyIntolerance allergyIntolerance = new AllergyIntolerance();
        allergyIntolerance.setAsserter(referenceToPatient);
        String allergyIntoleranceId = myClient.create().resource(allergyIntolerance).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(allergyIntoleranceId);
    }

    @Test
    public void patientEverything_shouldReturnAppointment_whenAppointmentRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        Appointment appointment = new Appointment();
        Appointment.AppointmentParticipantComponent participantComponent = new Appointment.AppointmentParticipantComponent();
        participantComponent.setActor(referenceToPatient);
        appointment.setParticipant(List.of(participantComponent));
        String appointmentId = myClient.create().resource(appointment).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(appointmentId);
    }

    @Test
    public void patientEverything_shouldReturnAppointmentResponse_whenAppointmentResponseRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        AppointmentResponse appointmentResponse = new AppointmentResponse();
        appointmentResponse.setActor(referenceToPatient);
        String appointmentResponseId = myClient.create().resource(appointmentResponse).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(appointmentResponseId);
    }

    @Test
    public void patientEverything_shouldReturnAuditEvent_whenAuditEventRefersToPatientAsAgent() throws Exception {

        Reference referenceToPatient = createPatient();

        AuditEvent auditEvent = new AuditEvent();
        AuditEvent.AuditEventAgentComponent agentComponent = new AuditEvent.AuditEventAgentComponent();
        agentComponent.setWho(referenceToPatient);
        auditEvent.setAgent(List.of(agentComponent));
        String auditEventId = myClient.create().resource(auditEvent).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(auditEventId);
    }

    @Test
    public void patientEverything_shouldReturnAuditEvent_whenAuditEventRefersToPatientAsObserver() throws Exception {

        Reference referenceToPatient = createPatient();

        AuditEvent auditEvent = new AuditEvent();
        AuditEvent.AuditEventSourceComponent sourceComponent = new AuditEvent.AuditEventSourceComponent();
        sourceComponent.setObserver(referenceToPatient);
        auditEvent.setSource(sourceComponent);
        String auditEventId = myClient.create().resource(auditEvent).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(auditEventId);
    }

    @Test
    public void patientEverything_shouldReturnBasic_whenBasicRefersToPatientAsSubject() throws Exception {

        Reference referenceToPatient = createPatient();

        Basic basic = new Basic();
        basic.setSubject(referenceToPatient);
        String basicId = myClient.create().resource(basic).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(basicId);
    }

    @Test
    public void patientEverything_shouldReturnBasic_whenBasicRefersToPatientAsAuthor() throws Exception {

        Reference referenceToPatient = createPatient();

        Basic basic = new Basic();
        basic.setAuthor(referenceToPatient);
        String basicId = myClient.create().resource(basic).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(basicId);
    }

    @Test
    public void patientEverything_shouldReturnBodyStructure_whenBodyStructureRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        BodyStructure bodyStructure = new BodyStructure();
        bodyStructure.setPatient(referenceToPatient);
        String bodyStructureId = myClient.create().resource(bodyStructure).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(bodyStructureId);
    }

    @Test
    public void patientEverything_shouldReturnCarePlan_whenCarePlanRefersToPatientAsSubject() throws Exception {

        Reference referenceToPatient = createPatient();

        CarePlan carePlan = new CarePlan();
        carePlan.setSubject(referenceToPatient);
        String carePlanId = myClient.create().resource(carePlan).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(carePlanId);
    }

    @Test
    public void patientEverything_shouldReturnCareTeam_whenCareTeamRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        CareTeam careTeam = new CareTeam();
        careTeam.setSubject(referenceToPatient);
        String careTeamId = myClient.create().resource(careTeam).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(careTeamId);
    }

    @Test
    public void patientEverything_shouldReturnCareTeam_whenCareTeamRefersToPatientAsParticipantMember() throws Exception {

        Reference referenceToPatient = createPatient();

        CareTeam careTeam = new CareTeam();
        CareTeam.CareTeamParticipantComponent participantComponent = new CareTeam.CareTeamParticipantComponent();
        participantComponent.setMember(referenceToPatient);
        careTeam.setParticipant(List.of(participantComponent));
        String careTeamId = myClient.create().resource(careTeam).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(careTeamId);
    }

    @Test
    public void patientEverything_shouldReturnChargeItem_whenChargeItemRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        ChargeItem chargeItem = new ChargeItem();
        chargeItem.setSubject(referenceToPatient);
        String chargeItemId = myClient.create().resource(chargeItem).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(chargeItemId);
    }

    @Test
    public void patientEverything_shouldReturnChargeItem_whenChargeItemRefersToPatientAsPerformer() throws Exception {

        Reference referenceToPatient = createPatient();

        ChargeItem chargeItem = new ChargeItem();
        ChargeItem.ChargeItemPerformerComponent performerComponent = new ChargeItem.ChargeItemPerformerComponent();
        performerComponent.setActor(referenceToPatient);
        chargeItem.setPerformer(List.of(performerComponent));
        String chargeItemId = myClient.create().resource(chargeItem).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(chargeItemId);
    }

    @Test
    public void patientEverything_shouldReturnChargeItem_whenChargeItemRefersToPatientAsEnterer() throws Exception {

        Reference referenceToPatient = createPatient();

        ChargeItem chargeItem = new ChargeItem();
        chargeItem.setEnterer(referenceToPatient);
        String chargeItemId = myClient.create().resource(chargeItem).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(chargeItemId);
    }

    @Test
    public void patientEverything_shouldReturnClaim_whenClaimRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        Claim claim = new Claim();
        claim.setPatient(referenceToPatient);
        String claimId = myClient.create().resource(claim).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(claimId);
    }


    @Test
    public void patientEverything_shouldReturnClaim_whenClaimRefersToPatientAsPayee() throws Exception {

        Reference referenceToPatient = createPatient();

        Claim claim = new Claim();
        Claim.PayeeComponent payeeComponent = new Claim.PayeeComponent();
        payeeComponent.setParty(referenceToPatient);
        claim.setPayee(payeeComponent);
        String claimId = myClient.create().resource(claim).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(claimId);
    }


    @Test
    public void patientEverything_shouldReturnClaimResponse_whenClaimResponseRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        ClaimResponse claimResponse = new ClaimResponse();
        claimResponse.setPatient(referenceToPatient);
        String claimResponseId = myClient.create().resource(claimResponse).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(claimResponseId);
    }


    @Test
    public void patientEverything_shouldReturnClinicalImpression_whenClinicalImpressionRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        ClinicalImpression clinicalImpression = new ClinicalImpression();
        clinicalImpression.setSubject(referenceToPatient);
        String clinicalImpressionId = myClient.create().resource(clinicalImpression).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(clinicalImpressionId);
    }

    @Test
    public void patientEverything_shouldReturnCommunication_whenCommunicationRefersToPatientAsSubject() throws Exception {

        Reference referenceToPatient = createPatient();

        Communication communication = new Communication();
        communication.setSubject(referenceToPatient);
        String communicationId = myClient.create().resource(communication).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(communicationId);
    }

    @Test
    public void patientEverything_shouldReturnCommunication_whenCommunicationRefersToPatientAsSender() throws Exception {

        Reference referenceToPatient = createPatient();

        Communication communication = new Communication();
        communication.setSender(referenceToPatient);
        String communicationId = myClient.create().resource(communication).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(communicationId);
    }

    @Test
    public void patientEverything_shouldReturnCommunication_whenCommunicationRefersToPatientAsRecipient() throws Exception {

        Reference referenceToPatient = createPatient();

        Communication communication = new Communication();
        communication.setRecipient(List.of(referenceToPatient));
        String communicationId = myClient.create().resource(communication).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(communicationId);
    }

    @Test
    public void patientEverything_shouldReturnCommunicationRequest_whenCommunicationRequestRefersToPatientAsSubject() throws Exception {

        Reference referenceToPatient = createPatient();

        CommunicationRequest CommunicationRequest = new CommunicationRequest();
        CommunicationRequest.setSubject(referenceToPatient);
        String CommunicationRequestId = myClient.create().resource(CommunicationRequest).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(CommunicationRequestId);
    }

    @Test
    public void patientEverything_shouldReturnCommunicationRequest_whenCommunicationRequestRefersToPatientAsSender() throws Exception {

        Reference referenceToPatient = createPatient();

        CommunicationRequest CommunicationRequest = new CommunicationRequest();
        CommunicationRequest.setSender(referenceToPatient);
        String CommunicationRequestId = myClient.create().resource(CommunicationRequest).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(CommunicationRequestId);
    }

    @Test
    public void patientEverything_shouldReturnCommunicationRequest_whenCommunicationRequestRefersToPatientAsRecipient() throws Exception {

        Reference referenceToPatient = createPatient();

        CommunicationRequest CommunicationRequest = new CommunicationRequest();
        CommunicationRequest.setRecipient(List.of(referenceToPatient));
        String CommunicationRequestId = myClient.create().resource(CommunicationRequest).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(CommunicationRequestId);
    }

    @Test
    public void patientEverything_shouldReturnCommunicationRequest_whenCommunicationRequestRefersToPatientAsRequester() throws Exception {

        Reference referenceToPatient = createPatient();

        CommunicationRequest CommunicationRequest = new CommunicationRequest();
        CommunicationRequest.setRequester(referenceToPatient);
        String CommunicationRequestId = myClient.create().resource(CommunicationRequest).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(CommunicationRequestId);
    }


    @Test
    public void patientEverything_shouldReturnComposition_whenCompositionRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        Composition composition = new Composition();
        composition.setSubject(referenceToPatient);
        String compositionId = myClient.create().resource(composition).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(compositionId);
    }

    @Test
    public void patientEverything_shouldReturnComposition_whenCompositionRefersToPatientAsAuthor() throws Exception {

        Reference referenceToPatient = createPatient();

        Composition composition = new Composition();
        composition.setAuthor(List.of(referenceToPatient));
        String compositionId = myClient.create().resource(composition).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(compositionId);
    }

    @Test
    public void patientEverything_shouldReturnComposition_whenCompositionRefersToPatientAsAttester() throws Exception {

        Reference referenceToPatient = createPatient();

        Composition composition = new Composition();
        Composition.CompositionAttesterComponent attesterComponent = new Composition.CompositionAttesterComponent();
        attesterComponent.setParty(referenceToPatient);
        composition.setAttester(List.of(attesterComponent));
        String compositionId = myClient.create().resource(composition).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(compositionId);
    }

    @Test
    public void patientEverything_shouldReturnCondition_whenConditionRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        Condition condition = new Condition();
        condition.setSubject(referenceToPatient);
        String conditionId = myClient.create().resource(condition).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(conditionId);
    }

    @Test
    public void patientEverything_shouldReturnCondition_whenConditionRefersToPatientAsAsserter() throws Exception {

        Reference referenceToPatient = createPatient();

        Condition condition = new Condition();
        condition.setAsserter(referenceToPatient);
        String conditionId = myClient.create().resource(condition).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(conditionId);
    }

    @Test
    public void patientEverything_shouldReturnConsent_whenConsentRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        Consent consent = new Consent();
        consent.setPatient(referenceToPatient);
        String consentId = myClient.create().resource(consent).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(consentId);
    }

    @Test
    public void patientEverything_shouldReturnConsent_whenConsentRefersToPatientAsActor() throws Exception {

        Reference referenceToPatient = createPatient();

        Consent consent = new Consent();
        Consent.provisionComponent provisionComponent = new Consent.provisionComponent();
        Consent.provisionActorComponent actorComponent = new Consent.provisionActorComponent();
        actorComponent.setReference(referenceToPatient);
        provisionComponent.setActor(List.of(actorComponent));
        consent.setProvision(provisionComponent);
        String consentId = myClient.create().resource(consent).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(consentId);
    }

    @Test
    public void patientEverything_shouldReturnCoverage_whenCoverageRefersToPatientAsPolicyHolder() throws Exception {

        Reference referenceToPatient = createPatient();

        Coverage coverage = new Coverage();
        coverage.setPolicyHolder(referenceToPatient);
        String coverageId = myClient.create().resource(coverage).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(coverageId);
    }


    @Test
    public void patientEverything_shouldReturnCoverage_whenCoverageRefersToPatientAsSubscriber() throws Exception {

        Reference referenceToPatient = createPatient();

        Coverage coverage = new Coverage();
        coverage.setSubscriber(referenceToPatient);
        String coverageId = myClient.create().resource(coverage).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(coverageId);
    }


    @Test
    public void patientEverything_shouldReturnCoverage_whenCoverageRefersToPatientAsBeneficiary() throws Exception {

        Reference referenceToPatient = createPatient();

        Coverage coverage = new Coverage();
        coverage.setBeneficiary(referenceToPatient);
        String coverageId = myClient.create().resource(coverage).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(coverageId);
    }

    @Test
    public void patientEverything_shouldReturnCoverage_whenCoverageRefersToPatientAsPayor() throws Exception {

        Reference referenceToPatient = createPatient();

        Coverage coverage = new Coverage();
        coverage.setPayor(List.of(referenceToPatient));
        String coverageId = myClient.create().resource(coverage).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(coverageId);
    }

    @Test
    public void patientEverything_shouldReturnCoverageEligibilityRequest_whenCoverageEligibilityRequestRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        CoverageEligibilityRequest coverageEligibilityRequest = new CoverageEligibilityRequest();
        coverageEligibilityRequest.setPatient(referenceToPatient);
        String coverageEligibilityRequestId = myClient.create().resource(coverageEligibilityRequest).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(coverageEligibilityRequestId);
    }

    @Test
    public void patientEverything_shouldReturnCoverageEligibilityResponse_whenCoverageEligibilityResponseRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        CoverageEligibilityResponse coverageEligibilityResponse = new CoverageEligibilityResponse();
        coverageEligibilityResponse.setPatient(referenceToPatient);
        String coverageEligibilityResponseId = myClient.create().resource(coverageEligibilityResponse).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(coverageEligibilityResponseId);
    }

    @Test
    public void patientEverything_shouldReturnDetectedIssue_whenDetectedIssueRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        DetectedIssue detectedIssue = new DetectedIssue();
        detectedIssue.setPatient(referenceToPatient);
        String detectedIssueId = myClient.create().resource(detectedIssue).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(detectedIssueId);
    }

    @Test
    public void patientEverything_shouldReturnDevice_whenDeviceRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        Device device = new Device();
        device.setPatient(referenceToPatient);
        String deviceId = myClient.create().resource(device).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(deviceId);
    }

    @Test
    public void patientEverything_shouldReturnDeviceRequest_whenDeviceRequestRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        DeviceRequest deviceRequest = new DeviceRequest();
        deviceRequest.setSubject(referenceToPatient);
        String deviceRequestId = myClient.create().resource(deviceRequest).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(deviceRequestId);
    }

    @Test
    public void patientEverything_shouldReturnDeviceRequest_whenDeviceRequestRefersToPatientAsPerformer() throws Exception {

        Reference referenceToPatient = createPatient();

        DeviceRequest deviceRequest = new DeviceRequest();
        deviceRequest.setPerformer(referenceToPatient);
        String deviceRequestId = myClient.create().resource(deviceRequest).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(deviceRequestId);
    }

    @Test
    public void patientEverything_shouldReturnDeviceUseStatement_whenDeviceUseStatementRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        DeviceUseStatement deviceUseStatement = new DeviceUseStatement();
        deviceUseStatement.setSubject(referenceToPatient);
        String deviceUseStatementId = myClient.create().resource(deviceUseStatement).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(deviceUseStatementId);
    }

    @Test
    public void patientEverything_shouldReturnDiagnosticReport_whenDiagnosticReportRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        DiagnosticReport diagnosticReport = new DiagnosticReport();
        diagnosticReport.setSubject(referenceToPatient);
        String diagnosticReportId = myClient.create().resource(diagnosticReport).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(diagnosticReportId);
    }

    @Test
    public void patientEverything_shouldReturnDocumentManifest_whenDocumentManifestRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        DocumentManifest documentManifest = new DocumentManifest();
        documentManifest.setSubject(referenceToPatient);
        String documentManifestId = myClient.create().resource(documentManifest).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(documentManifestId);
    }

    @Test
    public void patientEverything_shouldReturnDocumentManifest_whenDocumentManifestRefersToPatientAsAuthor() throws Exception {

        Reference referenceToPatient = createPatient();

        DocumentManifest documentManifest = new DocumentManifest();
        documentManifest.setAuthor(List.of(referenceToPatient));
        String documentManifestId = myClient.create().resource(documentManifest).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(documentManifestId);
    }

    @Test
    public void patientEverything_shouldReturnDocumentManifest_whenDocumentManifestRefersToPatientAsRecipient() throws Exception {

        Reference referenceToPatient = createPatient();

        DocumentManifest documentManifest = new DocumentManifest();
        documentManifest.setRecipient(List.of(referenceToPatient));
        String documentManifestId = myClient.create().resource(documentManifest).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(documentManifestId);
    }

    @Test
    public void patientEverything_shouldReturnDocumentReference_whenDocumentReferenceRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        DocumentReference documentReference = new DocumentReference();
        documentReference.setSubject(referenceToPatient);
        String documentReferenceId = myClient.create().resource(documentReference).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(documentReferenceId);
    }

    @Test
    public void patientEverything_shouldReturnDocumentReference_whenDocumentReferenceRefersToPatientAsAuthor() throws Exception {

        Reference referenceToPatient = createPatient();

        DocumentReference documentReference = new DocumentReference();
        documentReference.setAuthor(List.of(referenceToPatient));
        String documentReferenceId = myClient.create().resource(documentReference).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(documentReferenceId);
    }

    @Test
    public void patientEverything_shouldReturnEncounter_whenEncounterRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        Encounter encounter = new Encounter();
        encounter.setSubject(referenceToPatient);
        String encounterId = myClient.create().resource(encounter).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(encounterId);
    }

    @Test
    public void patientEverything_shouldReturnEnrollmentRequest_whenEnrollmentRequestRefersToPatientAsCandidate() throws Exception {

        Reference referenceToPatient = createPatient();

        EnrollmentRequest enrollmentRequest = new EnrollmentRequest();
        enrollmentRequest.setCandidate(referenceToPatient);
        String enrollmentRequestId = myClient.create().resource(enrollmentRequest).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(enrollmentRequestId);
    }

    @Test
    public void patientEverything_shouldReturnEpisodeOfCare_whenEpisodeOfCareRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        EpisodeOfCare episodeOfCare = new EpisodeOfCare();
        episodeOfCare.setPatient(referenceToPatient);
        String episodeOfCareId = myClient.create().resource(episodeOfCare).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(episodeOfCareId);
    }

    @Test
    public void patientEverything_shouldReturnExplanationOfBenefit_whenExplanationOfBenefitRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        ExplanationOfBenefit explanationOfBenefit = new ExplanationOfBenefit();
        explanationOfBenefit.setPatient(referenceToPatient);
        String explanationOfBenefitId = myClient.create().resource(explanationOfBenefit).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(explanationOfBenefitId);
    }

    @Test
    public void patientEverything_shouldReturnExplanationOfBenefit_whenExplanationOfBenefitRefersToPatientAsPayee() throws Exception {

        Reference referenceToPatient = createPatient();

        ExplanationOfBenefit explanationOfBenefit = new ExplanationOfBenefit();
        ExplanationOfBenefit.PayeeComponent payeeComponent = new ExplanationOfBenefit.PayeeComponent();
        payeeComponent.setParty(referenceToPatient);
        explanationOfBenefit.setPayee(payeeComponent);
        String explanationOfBenefitId = myClient.create().resource(explanationOfBenefit).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(explanationOfBenefitId);
    }

    @Test
    public void patientEverything_shouldReturnFamilyMemberHistory_whenFamilyMemberHistoryRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        FamilyMemberHistory familyMemberHistory = new FamilyMemberHistory();
        familyMemberHistory.setPatient(referenceToPatient);
        String familyMemberHistoryId = myClient.create().resource(familyMemberHistory).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(familyMemberHistoryId);
    }

    @Test
    public void patientEverything_shouldReturnFlag_whenFlagRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        Flag flag = new Flag();
        flag.setSubject(referenceToPatient);
        String flagId = myClient.create().resource(flag).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(flagId);
    }

    @Test
    public void patientEverything_shouldReturnGoal_whenGoalRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        Goal goal = new Goal();
        goal.setSubject(referenceToPatient);
        String goalId = myClient.create().resource(goal).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(goalId);
    }

    @Test
    public void patientEverything_shouldReturnGroup_whenGroupRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        Group group = new Group();
        Group.GroupMemberComponent memberComponent = new Group.GroupMemberComponent();
        memberComponent.setEntity(referenceToPatient);
        group.setMember(List.of(memberComponent));
        String groupId = myClient.create().resource(group).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(groupId);
    }

    @Test
    public void patientEverything_shouldReturnImagingStudy_whenImagingStudyRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        ImagingStudy imagingStudy = new ImagingStudy();
        imagingStudy.setSubject(referenceToPatient);
        String imagingStudyId = myClient.create().resource(imagingStudy).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(imagingStudyId);
    }

    @Test
    public void patientEverything_shouldReturnImmunization_whenImmunizationRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        Immunization immunization = new Immunization();
        immunization.setPatient(referenceToPatient);
        String immunizationId = myClient.create().resource(immunization).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(immunizationId);
    }

    @Test
    public void patientEverything_shouldReturnImmunizationEvaluation_whenImmunizationEvaluationRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        ImmunizationEvaluation immunizationEvaluation = new ImmunizationEvaluation();
        immunizationEvaluation.setPatient(referenceToPatient);
        String immunizationEvaluationId = myClient.create().resource(immunizationEvaluation).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(immunizationEvaluationId);
    }

    @Test
    public void patientEverything_shouldReturnImmunizationRecommendation_whenImmunizationRecommendationRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        ImmunizationRecommendation immunizationRecommendation = new ImmunizationRecommendation();
        immunizationRecommendation.setPatient(referenceToPatient);
        String immunizationRecommendationId = myClient.create().resource(immunizationRecommendation).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(immunizationRecommendationId);
    }

    @Test
    public void patientEverything_shouldReturnInvoice_whenInvoiceRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        Invoice invoice = new Invoice();
        invoice.setSubject(referenceToPatient);
        String invoiceId = myClient.create().resource(invoice).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(invoiceId);
    }

    @Test
    public void patientEverything_shouldReturnInvoice_whenInvoiceRefersToPatientAsRecipient() throws Exception {

        Reference referenceToPatient = createPatient();

        Invoice invoice = new Invoice();
        invoice.setRecipient(referenceToPatient);
        String invoiceId = myClient.create().resource(invoice).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(invoiceId);
    }


    @Test
    public void patientEverything_shouldReturnList_whenListRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        ListResource listResource = new ListResource();
        listResource.setSubject(referenceToPatient);
        String listResourceId = myClient.create().resource(listResource).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(listResourceId);
    }

    @Test
    public void patientEverything_shouldReturnList_whenListRefersToPatientAsSource() throws Exception {

        Reference referenceToPatient = createPatient();

        ListResource listResource = new ListResource();
        listResource.setSource(referenceToPatient);
        String listResourceId = myClient.create().resource(listResource).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(listResourceId);
    }

    @Test
    public void patientEverything_shouldReturnMeasureReport_whenMeasureReportRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        MeasureReport measureReport = new MeasureReport();
        measureReport.setSubject(referenceToPatient);
        String measureReportId = myClient.create().resource(measureReport).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(measureReportId);
    }

    @Test
    public void patientEverything_shouldReturnMedia_whenMediaRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        Media media = new Media();
        media.setSubject(referenceToPatient);
        String mediaId = myClient.create().resource(media).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(mediaId);
    }

    @Test
    public void patientEverything_shouldReturnMedicationAdministration_whenMedicationAdministrationExistsThatRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        MedicationAdministration medicationAdministration = new MedicationAdministration();
        medicationAdministration.setSubject(referenceToPatient);
        String medicationAdministrationId = myClient.create().resource(medicationAdministration).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(medicationAdministrationId);
    }

    @Test
    public void patientEverything_shouldReturnMedicationDispense_whenMedicationDispenseExistsThatRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        MedicationDispense medicationDispense = new MedicationDispense();
        medicationDispense.setSubject(referenceToPatient);
        String medicationDispenseId = myClient.create().resource(medicationDispense).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(medicationDispenseId);
    }

    @Test
    public void patientEverything_shouldReturnMedicationDispense_whenMedicationDispenseExistsThatRefersToPatientAsPerformer() throws Exception {

        Reference referenceToPatient = createPatient();

        MedicationDispense medicationDispense = new MedicationDispense();
        MedicationDispense.MedicationDispensePerformerComponent performerComponent = new MedicationDispense.MedicationDispensePerformerComponent();
        performerComponent.setActor(referenceToPatient);
        medicationDispense.setPerformer(List.of(performerComponent));
        String medicationDispenseId = myClient.create().resource(medicationDispense).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(medicationDispenseId);
    }

    @Test
    public void patientEverything_shouldReturnMedicationRequest_whenMedicationRequestExistsThatRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        MedicationRequest medicationDispense = new MedicationRequest();
        medicationDispense.setSubject(referenceToPatient);
        String medicationDispenseId = myClient.create().resource(medicationDispense).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(medicationDispenseId);
    }

    @Test
    public void patientEverything_shouldReturnMedicationRequest_whenMedicationRequestExistsThatRefersToPatientAsRequester() throws Exception {

        Reference referenceToPatient = createPatient();

        MedicationRequest medicationDispense = new MedicationRequest();
        medicationDispense.setRequester(referenceToPatient);
        String medicationDispenseId = myClient.create().resource(medicationDispense).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(medicationDispenseId);
    }

    @Test
    public void patientEverything_shouldReturnMedicationRequest_whenMedicationRequestExistsThatRefersToPatientAsPerformer() throws Exception {

        Reference referenceToPatient = createPatient();

        MedicationRequest medicationDispense = new MedicationRequest();
        medicationDispense.setPerformer(referenceToPatient);
        String medicationDispenseId = myClient.create().resource(medicationDispense).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(medicationDispenseId);
    }

    @Test
    public void patientEverything_shouldReturnMedicationStatement_whenMedicationStatementExistsThatRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        MedicationStatement medicationStatement = new MedicationStatement();
        medicationStatement.setSubject(referenceToPatient);
        String medicationStatementId = myClient.create().resource(medicationStatement).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(medicationStatementId);
    }

    @Test
    public void patientEverything_shouldReturnMedicationStatement_whenMedicationStatementExistsThatRefersToPatientAsSource() throws Exception {

        Reference referenceToPatient = createPatient();

        MedicationStatement medicationStatement = new MedicationStatement();
        medicationStatement.setInformationSource(referenceToPatient);
        String medicationStatementId = myClient.create().resource(medicationStatement).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(medicationStatementId);
    }

    @Test
    public void patientEverything_shouldReturnMolecularSequence_whenMolecularSequenceExistsThatRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        MolecularSequence molecularSequence = new MolecularSequence();
        molecularSequence.setPatient(referenceToPatient);
        String molecularSequenceId = myClient.create().resource(molecularSequence).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(molecularSequenceId);
    }

    @Test
    public void patientEverything_shouldReturnNutritionOrder_whenNutritionOrderExistsThatRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        NutritionOrder nutritionOrder = new NutritionOrder();
        nutritionOrder.setPatient(referenceToPatient);
        String nutritionOrderId = myClient.create().resource(nutritionOrder).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(nutritionOrderId);
    }

    @Test
    public void patientEverything_shouldReturnObservation_whenObservationExistsThatRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        Observation observation = new Observation();
        observation.setSubject(referenceToPatient);
        String observationId = myClient.create().resource(observation).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(observationId);
    }

    @Test
    public void patientEverything_shouldReturnOrganization_whenPatientRefersToItAsManagingOrganization() throws Exception {

        Organization organization = new Organization();
        String organizationId = myClient.create().resource(organization).execute().getId().toUnqualifiedVersionless().getValue();
        Reference referenceToOrganization = new Reference();
        referenceToOrganization.setReference(organizationId);

        Patient patient = new Patient();
        patient.setManagingOrganization(referenceToOrganization);
        String patientId = myClient.create().resource(patient).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(patientId);
			assertThat(actual).contains(patientId);
			assertThat(actual).contains(organizationId);
    }

    @Test
    public void patientEverything_shouldReturnOrganization_whenPatientRefersToItAsGeneralPractitioner() throws Exception {

        Organization organization = new Organization();
        String organizationId = myClient.create().resource(organization).execute().getId().toUnqualifiedVersionless().getValue();
        Reference referenceToOrganization = new Reference();
        referenceToOrganization.setReference(organizationId);

        Patient patient = new Patient();
        patient.setGeneralPractitioner(List.of(referenceToOrganization));
        String patientId = myClient.create().resource(patient).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(patientId);
			assertThat(actual).contains(patientId);
			assertThat(actual).contains(organizationId);
    }

    @Test
    public void patientEverything_shouldReturnOtherPatient_whenOtherPatientExistsThatRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        Patient otherPatient = new Patient();
        Patient.PatientLinkComponent patientLinkComponent = new Patient.PatientLinkComponent();
        patientLinkComponent.setOther(referenceToPatient);
        otherPatient.setLink(List.of(patientLinkComponent));
        String otherPatientId = myClient.create().resource(otherPatient).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(otherPatientId);
    }

    @Test
    public void patientEverything_shouldReturnPerson_whenPersonExistsThatRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        Person person = new Person();
        Person.PersonLinkComponent personLinkComponent = new Person.PersonLinkComponent();
        personLinkComponent.setTarget(referenceToPatient);
        person.setLink(List.of(personLinkComponent));
        String personId = myClient.create().resource(person).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(personId);
    }

    @Test
    public void patientEverything_shouldReturnPractitioner_whenPatientRefersToItAsGeneralPractitioner() throws Exception {

        Practitioner practitioner = new Practitioner();
        String practitionerId = myClient.create().resource(practitioner).execute().getId().toUnqualifiedVersionless().getValue();
        Reference referenceToPractitioner = new Reference();
        referenceToPractitioner.setReference(practitionerId);

        Patient patient = new Patient();
        patient.setGeneralPractitioner(List.of(referenceToPractitioner));
        String patientId = myClient.create().resource(patient).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(patientId);
			assertThat(actual).contains(patientId);
			assertThat(actual).contains(practitionerId);
    }

    @Test
    public void patientEverything_shouldReturnProcedure_whenProcedureExistsThatRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        Procedure procedure = new Procedure();
        procedure.setSubject(referenceToPatient);
        String procedureId = myClient.create().resource(procedure).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(procedureId);
    }

    @Test
    public void patientEverything_shouldReturnProvenance_whenProvenanceExistsThatRefersToPatientAsAgentWho() throws Exception {

        Reference referenceToPatient = createPatient();

        Provenance provenance = new Provenance();
        Provenance.ProvenanceAgentComponent provenanceAgentComponent = new Provenance.ProvenanceAgentComponent();
        provenanceAgentComponent.setWho(referenceToPatient);
        provenance.setAgent(List.of(provenanceAgentComponent));
        String procedureId = myClient.create().resource(provenance).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(procedureId);
    }

    @Test
    public void patientEverything_shouldReturnQuestionnaireResponse_whenProvenanceExistsThatRefersToPatientAsAgentWho() throws Exception {

        Reference referenceToPatient = createPatient();

        Provenance provenance = new Provenance();
        Provenance.ProvenanceAgentComponent provenanceAgentComponent = new Provenance.ProvenanceAgentComponent();
        provenanceAgentComponent.setWho(referenceToPatient);
        provenance.setAgent(List.of(provenanceAgentComponent));
        String procedureId = myClient.create().resource(provenance).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(procedureId);
    }

    @Test
    public void patientEverything_shouldReturnRelatedPerson_whenRelatedPersonExistsThatRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        RelatedPerson relatedPerson = new RelatedPerson();
        relatedPerson.setPatient(referenceToPatient);
        String relatedPersonId = myClient.create().resource(relatedPerson).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(relatedPersonId);
    }

    @Test
    public void patientEverything_shouldReturnRequestGroup_whenRequestGroupExistsThatRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        RequestGroup requestGroup = new RequestGroup();
        requestGroup.setSubject(referenceToPatient);
        String requestGroupId = myClient.create().resource(requestGroup).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(requestGroupId);
    }

    @Test
    public void patientEverything_shouldReturnResearchSubject_whenResearchSubjectExistsThatRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        ResearchSubject researchSubject = new ResearchSubject();
        researchSubject.setIndividual(referenceToPatient);
        String researchSubjectId = myClient.create().resource(researchSubject).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(researchSubjectId);
    }

    @Test
    public void patientEverything_shouldReturnRiskAssessment_whenRiskAssessmentExistsThatRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        RiskAssessment riskAssessment = new RiskAssessment();
        riskAssessment.setSubject(referenceToPatient);
        String riskAssessmentId = myClient.create().resource(riskAssessment).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(riskAssessmentId);
    }

    @Test
    public void patientEverything_shouldReturnSchedule_whenScheduleExistsThatRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        Schedule schedule = new Schedule();
        schedule.setActor(List.of(referenceToPatient));
        String scheduleId = myClient.create().resource(schedule).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(scheduleId);
    }

    @Test
    public void patientEverything_shouldReturnServiceRequest_whenServiceRequestExistsThatRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setSubject(referenceToPatient);
        String serviceRequestId = myClient.create().resource(serviceRequest).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(serviceRequestId);
    }

    @Test
    public void patientEverything_shouldReturnServiceRequest_whenServiceRequestExistsThatRefersToPatientAsPerformer() throws Exception {

        Reference referenceToPatient = createPatient();

        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setPerformer(List.of(referenceToPatient));
        String serviceRequestId = myClient.create().resource(serviceRequest).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(serviceRequestId);
    }

    @Test
    public void patientEverything_shouldReturnServiceRequest_whenServiceRequestExistsThatRefersToPatientAsRequester() throws Exception {

        Reference referenceToPatient = createPatient();

        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setRequester(referenceToPatient);
        String serviceRequestId = myClient.create().resource(serviceRequest).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(serviceRequestId);
    }

    @Test
    public void patientEverything_shouldReturnSpecimen_whenSpecimenExistsThatRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        Specimen specimen = new Specimen();
        specimen.setSubject(referenceToPatient);
        String specimenId = myClient.create().resource(specimen).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(specimenId);
    }

    @Test
    public void patientEverything_shouldReturnSupplyDelivery_whenSupplyDeliveryExistsThatRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        SupplyDelivery supplyDelivery = new SupplyDelivery();
        supplyDelivery.setPatient(referenceToPatient);
        String supplyDeliveryId = myClient.create().resource(supplyDelivery).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(supplyDeliveryId);
    }

    @Test
    public void patientEverything_shouldReturnSupplyRequest_whenSupplyRequestExistsThatRefersToPatientAsRequester() throws Exception {

        Reference referenceToPatient = createPatient();

        SupplyRequest supplyRequest = new SupplyRequest();
        supplyRequest.setRequester(referenceToPatient);
        String supplyRequestId = myClient.create().resource(supplyRequest).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(supplyRequestId);
    }

    @Test
    public void patientEverything_shouldReturnSupplyRequest_whenSupplyRequestExistsThatRefersToPatientAsDeliverTo() throws Exception {

        Reference referenceToPatient = createPatient();

        SupplyRequest supplyRequest = new SupplyRequest();
        supplyRequest.setDeliverTo(referenceToPatient);
        String supplyRequestId = myClient.create().resource(supplyRequest).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(supplyRequestId);
    }

    @Test
    public void patientEverything_shouldReturnVisionPrescription_whenVisionPrescriptionExistsThatRefersToPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        VisionPrescription visionPrescription = new VisionPrescription();
        visionPrescription.setPatient(referenceToPatient);
        String visionPrescriptionID = myClient.create().resource(visionPrescription).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(visionPrescriptionID);
    }

    @Test
    public void patientEverything_shouldReturnMedication_whenMedicationAdministrationExistsThatRefersToMedicationAndPatient() throws Exception {

        Reference referenceToPatient = createPatient();

        Medication medication = new Medication();
        String medicationId = myClient.create().resource(medication).execute().getId().toUnqualifiedVersionless().getValue();
        Reference referenceToMedication = new Reference();
        referenceToMedication.setReference(medicationId);

        MedicationAdministration medicationAdministration = new MedicationAdministration();
        medicationAdministration.setSubject(referenceToPatient);
        medicationAdministration.setMedication(referenceToMedication);
        String medicationAdministrationId = myClient.create().resource(medicationAdministration).execute().getId().toUnqualifiedVersionless().getValue();

        Set<String> actual = getActualEverythingResultIds(referenceToPatient.getReference());
			assertThat(actual).contains(referenceToPatient.getReference());
			assertThat(actual).contains(medicationId);
			assertThat(actual).contains(medicationAdministrationId);
    }

    @Test
    public void everything_typeFilterWithRecursivelyRelatedResources_shouldReturnSameAsNonTypeFilteredEverything() {
        String testBundle;
        {
            testBundle = """
                    			{
                           "resourceType": "Bundle",
                           "type": "transaction",
                           "entry": [
                               {
                                   "fullUrl": "https://interop.providence.org:8000/Patient/385235",
                                   "resource": {
                                       "resourceType": "Patient",
                                       "id": "385235",
                                       "active": true,
                                       "name": [
                                           {
                                               "family": "TESTING",
                                               "given": [
                                                   "TESTER",
                                                   "T"
                                               ]
                                           }
                                       ],
                                       "gender": "female"
                                   },
                                   "request": {
                                       "method": "POST"
                                   }
                               },
                               {
                                   "fullUrl": "https://interop.providence.org:8000/Encounter/385236",
                                   "resource": {
                                       "resourceType": "Encounter",
                                       "id": "385236",
                                       "subject": {
                                           "reference": "Patient/385235"
                                       }
                                   },
                                   "request": {
                                       "method": "POST"
                                   }
                               },
                               {
                                   "fullUrl": "https://interop.providence.org:8000/Observation/385237",
                                   "resource": {
                                       "resourceType": "Observation",
                                       "id": "385237",
                                       "subject": {
                                           "reference": "Patient/385235"
                                       },
                                       "encounter": {
                                           "reference": "Encounter/385236"
                                       },
                                       "performer": [
                                           {
                                               "reference": "Practitioner/79070"
                                           },
                                           {
                                               "reference": "Practitioner/8454"
                                           }
                                       ],
                                       "valueQuantity": {
                                           "value": 100.9,
                                           "unit": "%",
                                           "system": "http://unitsofmeasure.org",
                                           "code": "%"
                                       }
                                   },
                                   "request": {
                                       "method": "POST"
                                   }
                               },
                               {
                                   "fullUrl": "https://interop.providence.org:8000/Practitioner/8454",
                                   "resource": {
                                       "resourceType": "Practitioner",
                                       "id": "8454"
                                   },
                                   "request": {
                                       "method": "POST"
                                   }
                               },
                               {
                                   "fullUrl": "https://interop.providence.org:8000/Practitioner/79070",
                                   "resource": {
                                       "resourceType": "Practitioner",
                                       "id": "79070",
                                       "active": true
                                   },
                                   "request": {
                                       "method": "POST"
                                   }
                               }
                           ]
                       }
                    """;
        }

        IParser parser = myFhirContext.newJsonParser();
        Bundle inputBundle = parser.parseResource(Bundle.class, testBundle);

        int resourceCount = inputBundle.getEntry().size();
        HashSet<String> resourceTypes = new HashSet<>();
        for (Bundle.BundleEntryComponent entry : inputBundle.getEntry()) {
            resourceTypes.add(entry.getResource().getResourceType().name());
        }
			// there are 2 practitioners in the bundle
			assertThat(resourceTypes).hasSize(4);

        // pre-seed the resources
        Bundle responseBundle = myClient.transaction()
                .withBundle(inputBundle)
                .execute();
		assertNotNull(responseBundle);
			assertThat(responseBundle.getEntry()).hasSize(resourceCount);

        IIdType patientId = null;
        for (Bundle.BundleEntryComponent entry : responseBundle.getEntry()) {
			assertEquals("201 Created", entry.getResponse().getStatus());
            if (entry.getResponse().getLocation().contains("Patient")) {
                patientId = new IdType(entry.getResponse().getLocation());
            }
        }
		assertNotNull(patientId);
		assertNotNull(patientId.getIdPart());

        ourLog.debug("------ EVERYTHING");
        // test without types filter
        {
            Bundle response = myClient.operation()
                    .onInstance(String.format("Patient/%s", patientId.getIdPart()))
                    .named(JpaConstants.OPERATION_EVERYTHING)
                    .withNoParameters(Parameters.class)
                    .returnResourceType(Bundle.class)
                    .execute();
			assertNotNull(response);
					assertThat(response.getEntry()).hasSize(resourceCount);
            for (Bundle.BundleEntryComponent entry : response.getEntry()) {
							assertThat(resourceTypes).contains(entry.getResource().getResourceType().name());
            }
        }

        ourLog.debug("------- EVERYTHING WITH TYPES");
        // test with types filter
        {
            Parameters parameters = new Parameters();
            parameters.addParameter(Constants.PARAM_TYPE, String.join(",", resourceTypes));
            Bundle response = myClient.operation()
                    .onInstance(String.format("Patient/%s", patientId.getIdPart()))
                    .named(JpaConstants.OPERATION_EVERYTHING)
                    .withParameters(parameters)
                    .returnResourceType(Bundle.class)
                    .execute();
			assertNotNull(response);
					assertThat(response.getEntry()).hasSize(resourceCount);
            for (Bundle.BundleEntryComponent entry : response.getEntry()) {
							assertThat(resourceTypes).contains(entry.getResource().getResourceType().name());
            }
        }
    }

    private Set<String> getActualEverythingResultIds(String patientId) throws IOException {
        Bundle bundle;
        HttpGet get = new HttpGet(myClient.getServerBase() + "/" + patientId + "/$everything?_format=json");
        CloseableHttpResponse resp = ourHttpClient.execute(get);
        try {
			assertEquals(EncodingEnum.JSON.getResourceContentTypeNonLegacy(), resp.getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue().replaceAll(";.*", ""));
            bundle = EncodingEnum.JSON.newParser(myFhirContext).parseResource(Bundle.class, IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8));
        } finally {
            IOUtils.closeQuietly(resp);
        }

		assertNull(bundle.getLink("next"));

        Set<String> actual = new TreeSet<>();
        for (Bundle.BundleEntryComponent nextEntry : bundle.getEntry()) {
            actual.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
        }

        return actual;
    }

}

