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
package ca.uhn.fhir.jpa.demo.subscription;

import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Subscription;
import ca.uhn.fhir.rest.client.IGenericClient;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Must have a fhir server and web service endpoint to run these tests which subscribe to the fhir and receive notifications
 */
@Ignore
public class ResthookSubscriptionDstu2TestsIT {

    private static IGenericClient client;

    @BeforeClass
    public static void init() {
        client = FhirServiceUtil.getFhirDstu2Client();
    }

    @Before
    public void clean() {
        RemoveDstu2TestIT.deleteResources(Subscription.class, null, client);
        RemoveDstu2TestIT.deleteResources(Observation.class, null, client);
    }

    @Test
    public void testSubscriptionsWithoutPayload() {
        String code = "1000000050";
        String criteria = "Observation?code=SNOMED-CT|" + code;

        Observation loincObservation = FhirDstu2Util.getLoincObservation();
        Observation snomedObservation = FhirDstu2Util.getSnomedObservation();

        FhirDstu2Util.createSubscription(criteria, null, FhirServiceUtil.REST_HOOK_ENDPOINT, client);
        FhirServiceUtil.createResource(loincObservation, client); //should not trigger a notification
        FhirServiceUtil.createResource(snomedObservation, client); //should trigger one notification

        snomedObservation.setComments("mock change");

        FhirServiceUtil.updateResource(snomedObservation, client); //should trigger one notification
        FhirServiceUtil.deleteResource(snomedObservation.getIdElement().getIdPart(), Observation.class, client); //should trigger one notification
    }

    @Test
    public void testSubscriptionsWithXmlPayload() {
        String code = "1000000050";
        String criteria = "Observation?code=SNOMED-CT|" + code;

        Observation snomedObservation = FhirDstu2Util.getSnomedObservation();

        FhirDstu2Util.createSubscription(criteria, FhirServiceUtil.XML_PAYLOAD, FhirServiceUtil.REST_HOOK_ENDPOINT, client);
        FhirServiceUtil.createResource(snomedObservation, client); //should trigger one notification with xml resource in the body

        snomedObservation.setComments("mock change");

        FhirServiceUtil.updateResource(snomedObservation, client); //should trigger one notification with xml resource in the body
        FhirServiceUtil.deleteResource(snomedObservation.getIdElement().getIdPart(), Observation.class, client); //should trigger one notification with xml resource in the body
    }

    @Test
    public void testSubscriptionsWithJsonPayload() {
        String code = "1000000050";
        String criteria = "Observation?code=SNOMED-CT|" + code;

        Observation snomedObservation = FhirDstu2Util.getSnomedObservation();
        FhirServiceUtil.createResource(snomedObservation, client);
        FhirServiceUtil.createResource(snomedObservation, client);

        FhirDstu2Util.createSubscription(criteria, FhirServiceUtil.JSON_PAYLOAD, FhirServiceUtil.REST_HOOK_ENDPOINT, client);
        FhirServiceUtil.createResource(snomedObservation, client); //should trigger one notification with json resource in the body
    }

    @Test
    public void testSubscriptionsWithCustomXmlPayload() {
        String code = "1000000050";
        String criteria = "Observation?code=SNOMED-CT|" + code;
        String payloadCriteria = "application/fhir+query/" + criteria + "&_format=xml";
        Observation snomedObservation = FhirDstu2Util.getSnomedObservation();
        FhirServiceUtil.createResource(snomedObservation, client);
        FhirServiceUtil.createResource(snomedObservation, client);

        FhirDstu2Util.createSubscription(criteria, payloadCriteria, FhirServiceUtil.REST_HOOK_ENDPOINT, client);
        FhirServiceUtil.createResource(snomedObservation, client); //should trigger one notification with xml bundle resource in the body containing three observations
    }

    @Test
    public void testSubscriptionsWithCustomJsonPayload() {
        String code = "1000000050";
        String criteria = "Observation?code=SNOMED-CT|" + code;
        String payloadCriteria = "application/fhir+query/" + criteria + "&_format=json";
        Observation snomedObservation = FhirDstu2Util.getSnomedObservation();
        FhirServiceUtil.createResource(snomedObservation, client);

        FhirDstu2Util.createSubscription(criteria, payloadCriteria, FhirServiceUtil.REST_HOOK_ENDPOINT, client);
        FhirServiceUtil.createResource(snomedObservation, client); //should trigger one notification with JSON bundle resource in the body containing two observations
    }

    @Test
    public void testSubscriptionsWithCustomDefaultPayload() {
        String code = "1000000050";
        String criteria = "Observation?code=SNOMED-CT|" + code;
        String payloadCriteria = "application/fhir+query/" + criteria;

        Observation snomedObservation = FhirDstu2Util.getSnomedObservation();

        FhirDstu2Util.createSubscription(criteria, payloadCriteria, FhirServiceUtil.REST_HOOK_ENDPOINT, client);
        FhirServiceUtil.createResource(snomedObservation, client); //should trigger one notification with JSON bundle resource in the body containing one observations
    }

    @Test
    public void testSubscriptionsWithCustomDefaultPayloadThatIsEmpty() {
        String code = "1000000050";
        String criteria = "Observation?code=SNOMED-CT|" + code;
        String payloadCriteria = "application/fhir+query/Observation?code=SNOMED-CT|" + code + "1111";

        Observation snomedObservation = FhirDstu2Util.getSnomedObservation();

        FhirDstu2Util.createSubscription(criteria, payloadCriteria, FhirServiceUtil.REST_HOOK_ENDPOINT, client);
        FhirServiceUtil.createResource(snomedObservation, client); //should trigger one notification with JSON bundle resource in the body containing no observations
    }

    /**
     * Add a 5 second delay to the HttpRequestDstu3Job to test if threading is improving creation speed
     */
    @Test
    public void testSubscriptionsThreading() {
        String code = "1000000050";
        String criteria = "Observation?code=SNOMED-CT|" + code;
        Observation snomedObservation = FhirDstu2Util.getSnomedObservation();

        FhirDstu2Util.createSubscription(criteria, null, FhirServiceUtil.REST_HOOK_ENDPOINT, client);
        System.out.println("start");
        FhirServiceUtil.createResource(snomedObservation, client); //should trigger one notification
        FhirServiceUtil.createResource(snomedObservation, client); //should trigger one notification
        FhirServiceUtil.createResource(snomedObservation, client); //should trigger one notification
        FhirServiceUtil.createResource(snomedObservation, client); //should trigger one notification
        FhirServiceUtil.createResource(snomedObservation, client); //should trigger one notification
        System.out.println("done");
    }

    /**
     * Add a 5 second delay to the HttpRequestDstu3Job to test if threading is improving creation speed
     */
    @Test
    public void testSubscriptionsThreading2() {
        String code = "1000000050";
        String criteria = "Observation?code=SNOMED-CT|" + code;
        Observation snomedObservation = FhirDstu2Util.getSnomedObservation();

        FhirDstu2Util.createSubscription(criteria, null, FhirServiceUtil.REST_HOOK_ENDPOINT, client);
        FhirDstu2Util.createSubscription(criteria, null, FhirServiceUtil.REST_HOOK_ENDPOINT, client);
        FhirDstu2Util.createSubscription(criteria, null, FhirServiceUtil.REST_HOOK_ENDPOINT, client);
        FhirDstu2Util.createSubscription(criteria, null, FhirServiceUtil.REST_HOOK_ENDPOINT, client);
        FhirDstu2Util.createSubscription(criteria, null, FhirServiceUtil.REST_HOOK_ENDPOINT, client);

        System.out.println("start");
        FhirServiceUtil.createResource(snomedObservation, client); //should trigger one notification
        System.out.println("done");
    }
}
