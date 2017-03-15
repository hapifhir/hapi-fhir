/*
 *  Copyright 2016 Cognitive Medical Systems, Inc (http://www.cognitivemedicine.com).
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
 */

package ca.uhn.fhir.jpa.demo.subscription;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Subscription;
import ca.uhn.fhir.model.dstu2.valueset.ObservationStatusEnum;
import ca.uhn.fhir.model.dstu2.valueset.SubscriptionChannelTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.SubscriptionStatusEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;

/**
 * Test the rest-hook subscriptions
 */
public class RestHookTestDstu2IT {

    private static final Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirSubscriptionWithSubscriptionIdDstu3IT.class);

    public static final String FHIR_URL = "http://localhost:8080/baseDstu2";

    private static String code = "1000000012";

    @Test
    public void testRestHookSubscription(){
        IGenericClient client = FhirServiceUtil.getFhirDstu2Client();

        String payload = "application/json";
        String endpoint = "http://localhost:10080/rest-hook";

        String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
        String criteria2 = "Observation?code=SNOMED-CT|" + code + "111&_format=xml";

        Subscription subscription1 = createSubscription(criteria1, payload, endpoint, client);
        Subscription subscription2 = createSubscription(criteria2, payload, endpoint, client);

        Observation observationTemp1 = sendObservation(code, "SNOMED-CT", client);
        Observation observation1 = client.read(Observation.class, observationTemp1.getId());
        //Should see only one subscription notification

        Subscription subscriptionTemp = client.read(Subscription.class, subscription2.getId());
        Assert.assertNotNull(subscriptionTemp);

        subscriptionTemp.setCriteria(criteria1);
        client.update().resource(subscriptionTemp).withId(subscriptionTemp.getIdElement()).execute();

        Observation observationTemp2 = sendObservation(code, "SNOMED-CT", client);
        Observation observation2 = client.read(Observation.class, observationTemp2.getId());
        //Should see two subscription notifications

        client.delete().resourceById("Subscription", subscription2.getId().getIdPart()).execute();

        Observation observationTemp3 = sendObservation(code, "SNOMED-CT", client);
        //Should see only one subscription notification

        Observation observation3 = client.read(Observation.class, observationTemp1.getId());
        CodeableConceptDt codeableConcept = new CodeableConceptDt();
        observation3.setCode(codeableConcept);
        CodingDt coding = codeableConcept.addCoding();
        coding.setCode(code + "111");
        coding.setSystem("SNOMED-CT");
        client.update().resource(observation3).withId(observation3.getIdElement()).execute();
        //Should see no subscription notification

        Observation observation3a = client.read(Observation.class, observationTemp1.getId());
        CodeableConceptDt codeableConcept2 = new CodeableConceptDt();
        observation3a.setCode(codeableConcept);
        CodingDt coding2 = codeableConcept.addCoding();
        coding2.setCode(code);
        coding2.setSystem("SNOMED-CT");
        client.update().resource(observation3a).withId(observation3a.getIdElement()).execute();
        //Should see only one subscription notification

        System.out.println("subscription id 1: " + subscription1.getId());
        System.out.println("subscription id 2: " + subscription2.getId());
        System.out.println("subscription temp id 2: " + subscriptionTemp.getId());
        System.out.println("observation id 1: " + observation1.getId());
        System.out.println("observation id 2: " + observation2.getId());
        System.out.println("observation id 3: " + observation3.getId());

        Assert.assertFalse(subscription1.getId().equals(subscription2.getId()));
        Assert.assertFalse(observation1.getId().isEmpty());
        Assert.assertFalse(observation2.getId().isEmpty());
    }

    @Test
    public void sendObservation(){
        IGenericClient client = FhirServiceUtil.getFhirDstu2Client();

        Observation observation1 = sendObservation(code, "SNOMED-CT", client);
        //Should see only one subscription notification
    }

    public Subscription createSubscription(String criteria, String payload, String endpoint, IGenericClient client) {
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

    public Observation sendObservation(String code, String system, IGenericClient client){
        Observation observation = new Observation();
        CodeableConceptDt codeableConcept = new CodeableConceptDt();
        observation.setCode(codeableConcept);
        CodingDt coding = codeableConcept.addCoding();
        coding.setCode(code);
        coding.setSystem(system);

        observation.setStatus(ObservationStatusEnum.FINAL);

        MethodOutcome methodOutcome = client.create().resource(observation).execute();

        String observationId = methodOutcome.getId().getIdPart();
        observation.setId(observationId);

        return observation;
    }
}
