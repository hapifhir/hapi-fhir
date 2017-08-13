package ca.uhn.fhir.jpa.subscription;

import org.hl7.fhir.instance.model.api.*;

import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.resource.*;
import ca.uhn.fhir.model.dstu2.valueset.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;

public class FhirDstu2Util {

    public static final String LPI_CODESYSTEM = "http://cognitivemedicine.com/lpi";
    public static final String LPI_CODE = "LPI-FHIR";

    public static Subscription createSubscription(String criteria, String payload, String endpoint, IGenericClient client) {
        Subscription subscription = new Subscription();
        subscription.setReason("Monitor new neonatal function (note, age will be determined by the monitor)");
        subscription.setStatus(SubscriptionStatusEnum.REQUESTED);
        subscription.setCriteria(criteria);

        Subscription.Channel channel = new Subscription.Channel();
        channel.setType(SubscriptionChannelTypeEnum.REST_HOOK);
        channel.setPayload(payload);
        channel.setEndpoint(endpoint);
        subscription.setChannel(channel);

        MethodOutcome methodOutcome = client.create().resource(subscription).execute();
        subscription.setId(methodOutcome.getId().getIdPart());

        return subscription;
    }

    public static Observation getSnomedObservation() {
        CodingDt snomedCoding = new CodingDt();
        snomedCoding.setSystem("SNOMED-CT");
        snomedCoding.setCode("1000000050");

        Observation observation = new Observation();

        observation.setStatus(ObservationStatusEnum.FINAL);
        observation.getCode().addCoding(snomedCoding);

        return observation;
    }

    public static Observation getLoincObservation() {
        CodingDt snomedCoding = new CodingDt();
        snomedCoding.setSystem("http://loinc.org");
        snomedCoding.setCode("55284-4");
        snomedCoding.setDisplay("Blood Pressure");

        Observation observation = new Observation();

        observation.setStatus(ObservationStatusEnum.FINAL);
        observation.getCode().addCoding(snomedCoding);

        return observation;
    }

    public static Patient getPatient() {
        String patientId = "1";

        Patient patient = new Patient();
        patient.setGender(AdministrativeGenderEnum.MALE);

        IdentifierDt identifier = patient.addIdentifier();
        identifier.setValue(patientId);
        identifier.setSystem(LPI_CODESYSTEM);

        IBaseMetaType meta = patient.getMeta();
        IBaseCoding tag = meta.addTag();
        tag.setCode(LPI_CODE);
        tag.setSystem(LPI_CODESYSTEM);

        setTag(patient);

        return patient;
    }

    public static void setTag(IBaseResource resource) {
        IBaseMetaType meta = resource.getMeta();
        IBaseCoding tag = meta.addTag();
        tag.setCode(LPI_CODE);
        tag.setSystem(LPI_CODESYSTEM);
    }
}
