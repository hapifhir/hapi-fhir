package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.*;

public class FhirR4Util {

    public static final String LPI_CODESYSTEM = "http://cognitivemedicine.com/lpi";
    public static final String LPI_CODE = "LPI-FHIR";

    public static Subscription createSubscription(String criteria, String payload, String endpoint, IGenericClient client) {
        Subscription subscription = new Subscription();
        subscription.setReason("Monitor new neonatal function (note, age will be determined by the monitor)");
        subscription.setStatus(Subscription.SubscriptionStatus.REQUESTED);
        subscription.setCriteria(criteria);

        Subscription.SubscriptionChannelComponent channel = new Subscription.SubscriptionChannelComponent();
        channel.setType(Subscription.SubscriptionChannelType.RESTHOOK);
        channel.setPayload(payload);
        channel.setEndpoint(endpoint);
        subscription.setChannel(channel);

        MethodOutcome methodOutcome = client.create().resource(subscription).execute();
        subscription.setId(methodOutcome.getId().getIdPart());

        return subscription;
    }

    public static Observation getSnomedObservation() {
        Coding snomedCoding = new Coding();
        snomedCoding.setSystem("SNOMED-CT");
        snomedCoding.setCode("1000000050");

        Observation observation = new Observation();

        observation.setStatus(Observation.ObservationStatus.FINAL);
        observation.getCode().addCoding(snomedCoding);

        return observation;
    }

    public static Observation getLoincObservation() {
        Coding snomedCoding = new Coding();
        snomedCoding.setSystem("http://loinc.org");
        snomedCoding.setCode("55284-4");
        snomedCoding.setDisplay("Blood Pressure");

        Observation observation = new Observation();

        observation.setStatus(Observation.ObservationStatus.FINAL);
        observation.getCode().addCoding(snomedCoding);

        return observation;
    }

    /**
     * Create a patient object for the test
     *
     * @return
     */
    public static Patient getPatient() {
        String patientId = "1";

        Patient patient = new Patient();
        patient.setGender(Enumerations.AdministrativeGender.MALE);

        Identifier identifier = patient.addIdentifier();
        identifier.setValue(patientId);
        identifier.setSystem(LPI_CODESYSTEM);

        IBaseMetaType meta = patient.getMeta();
        IBaseCoding tag = meta.addTag();
        tag.setCode(LPI_CODE);
        tag.setSystem(LPI_CODESYSTEM);
        setTag(patient);
        return patient;
    }

    /**
     * Set the tag for a resource
     *
     * @param resource
     */
    public static void setTag(IBaseResource resource) {
        IBaseMetaType meta = resource.getMeta();
        IBaseCoding tag = meta.addTag();
        tag.setCode(LPI_CODE);
        tag.setSystem(LPI_CODESYSTEM);
    }

}
