/*
 *  Copyright 2017 Cognitive Medical Systems, Inc (http://www.cognitivemedicine.com).
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  @author Jeff Chung
 */

package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Subscription;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;

/**
 * Test the rest-hook subscriptions
 */
@Ignore
public class RestHookTestDstu3WithSubscriptionResponseCriteriaIT {

    private static final Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirSubscriptionWithSubscriptionIdDstu3Test.class);

    @Test
    public void testRestHookSubscription() {
        IGenericClient client = FhirServiceUtil.getFhirDstu3Client();

        String payload = "application/json";
        String endpoint = "http://localhost:10080/rest-hook";

        String code = "1000000050";
        String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
        String criteria2 = "Observation?code=SNOMED-CT|" + code + "111&_format=xml";

        Subscription subscription1 = createSubscription(criteria1, "Observation?_format=xml", endpoint, client);
        Subscription subscription2 = createSubscription(criteria2, payload, endpoint, client);

        Observation observation1 = sendObservation(code, "SNOMED-CT", client);
        //Should see a bundle

        Subscription subscriptionTemp = client.read(Subscription.class, subscription2.getId());
        Assert.assertNotNull(subscriptionTemp);

        subscriptionTemp.setCriteria(criteria1);
        client.update().resource(subscriptionTemp).withId(subscriptionTemp.getIdElement()).execute();

        Observation observation2 = sendObservation(code, "SNOMED-CT", client);
        //Should see two subscription notifications

        client.delete().resourceById(new IdDt("Subscription", subscription2.getId())).execute();

        Observation observationTemp3 = sendObservation(code, "SNOMED-CT", client);
        //Should see only one subscription notification

        Observation observation3 = client.read(Observation.class, observationTemp3.getId());
        CodeableConcept codeableConcept = new CodeableConcept();
        observation3.setCode(codeableConcept);
        Coding coding = codeableConcept.addCoding();
        coding.setCode(code + "111");
        coding.setSystem("SNOMED-CT");
        client.update().resource(observation3).withId(observation3.getIdElement()).execute();
        //Should see no subscription notification

        Observation observation3a = client.read(Observation.class, observationTemp3.getId());

        CodeableConcept codeableConcept1 = new CodeableConcept();
        observation3a.setCode(codeableConcept1);
        Coding coding1 = codeableConcept1.addCoding();
        coding1.setCode(code);
        coding1.setSystem("SNOMED-CT");
        client.update().resource(observation3a).withId(observation3a.getIdElement()).execute();
        //Should see only one subscription notification

        Assert.assertFalse(subscription1.getId().equals(subscription2.getId()));
        Assert.assertFalse(observation1.getId().isEmpty());
        Assert.assertFalse(observation2.getId().isEmpty());
    }

    public Subscription createSubscription(String criteria, String payload, String endpoint, IGenericClient client) {
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

    public Observation sendObservation(String code, String system, IGenericClient client) {
        Observation observation = new Observation();
        CodeableConcept codeableConcept = new CodeableConcept();
        observation.setCode(codeableConcept);
        Coding coding = codeableConcept.addCoding();
        coding.setCode(code);
        coding.setSystem(system);

        observation.setStatus(Observation.ObservationStatus.FINAL);

        MethodOutcome methodOutcome = client.create().resource(observation).execute();

        String observationId = methodOutcome.getId().getIdPart();
        observation.setId(observationId);

        return observation;
    }
}
