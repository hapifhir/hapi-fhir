package ca.uhn.fhir.jpa.demo.subscription;

import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Subscription;

public class FhirDstu3Util {

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
}
