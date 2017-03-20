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

import ca.uhn.fhir.rest.client.IGenericClient;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Subscription;
import org.junit.*;

/**
 * Must have a fhir server and web service endpoint to run these
 * tests which subscribe to the fhir and receive notifications
 */
@Ignore
public class ResthookSubscriptionDstu3TestsIT {

    private static IGenericClient client;

    @BeforeClass
    public static void init() {
        client = FhirServiceUtil.getFhirDstu3Client();
    }

    @Before
    public void clean() {
        RemoveDstu3TestIT.deleteResources(Subscription.class, null, client);
        RemoveDstu3TestIT.deleteResources(Observation.class, null, client);
    }

    @Test
    public void createSnomedObservation() {
        String id = FhirServiceUtil.createResource(FhirDstu3Util.getSnomedObservation(), client);
        FhirServiceUtil.deleteResource(id, Observation.class, client);
    }

    @Test
    public void testSubscriptionsWithoutPayload() {
        String code = "1000000050";
        String criteria = "Observation?code=SNOMED-CT|" + code;

        Observation loincObservation = FhirDstu3Util.getLoincObservation();
        Observation snomedObservation = FhirDstu3Util.getSnomedObservation();

        FhirDstu3Util.createSubscription(criteria, null, FhirServiceUtil.REST_HOOK_ENDPOINT, client);
        FhirServiceUtil.createResource(loincObservation, client); //should not trigger a notification
        FhirServiceUtil.createResource(snomedObservation, client); //should trigger one notification

        snomedObservation.setComment("mock change");

        FhirServiceUtil.updateResource(snomedObservation, client); //should trigger one notification
        FhirServiceUtil.deleteResource(snomedObservation.getIdElement().getIdPart(), Observation.class, client); //should trigger one notification
    }

    @Test
    public void testSubscriptionsWithXmlPayload() {
        String code = "1000000050";
        String criteria = "Observation?code=SNOMED-CT|" + code;

        Observation snomedObservation = FhirDstu3Util.getSnomedObservation();

        FhirDstu3Util.createSubscription(criteria, FhirServiceUtil.XML_PAYLOAD, FhirServiceUtil.REST_HOOK_ENDPOINT, client);
        FhirServiceUtil.createResource(snomedObservation, client); //should trigger one notification with xml resource in the body

        snomedObservation.setComment("mock change");

        FhirServiceUtil.updateResource(snomedObservation, client); //should trigger one notification with xml resource in the body
        FhirServiceUtil.deleteResource(snomedObservation.getIdElement().getIdPart(), Observation.class, client); //should trigger one notification with xml resource in the body
    }

    @Test
    public void testSubscriptionsWithJsonPayload() {
        String code = "1000000050";
        String criteria = "Observation?code=SNOMED-CT|" + code;

        Observation snomedObservation = FhirDstu3Util.getSnomedObservation();
        FhirServiceUtil.createResource(snomedObservation, client);
        FhirServiceUtil.createResource(snomedObservation, client);

        FhirDstu3Util.createSubscription(criteria, FhirServiceUtil.JSON_PAYLOAD, FhirServiceUtil.REST_HOOK_ENDPOINT, client);
        FhirServiceUtil.createResource(snomedObservation, client); //should trigger one notification with json resource in the body
    }

    @Test
    public void testSubscriptionsWithCustomXmlPayload() {
        String code = "1000000050";
        String criteria = "Observation?code=SNOMED-CT|" + code;
        String payloadCriteria = "application/fhir+query/" + criteria + "&_format=xml";
        Observation snomedObservation = FhirDstu3Util.getSnomedObservation();
        FhirServiceUtil.createResource(snomedObservation, client);
        FhirServiceUtil.createResource(snomedObservation, client);

        FhirDstu3Util.createSubscription(criteria, payloadCriteria, FhirServiceUtil.REST_HOOK_ENDPOINT, client);
        FhirServiceUtil.createResource(snomedObservation, client); //should trigger one notification with xml bundle resource in the body containing three observations
    }

    @Test
    public void testSubscriptionsWithCustomJsonPayload() {
        String code = "1000000050";
        String criteria = "Observation?code=SNOMED-CT|" + code;
        String payloadCriteria = "application/fhir+query/" + criteria + "&_format=json";
        Observation snomedObservation = FhirDstu3Util.getSnomedObservation();
        FhirServiceUtil.createResource(snomedObservation, client);

        FhirDstu3Util.createSubscription(criteria, payloadCriteria, FhirServiceUtil.REST_HOOK_ENDPOINT, client);
        FhirServiceUtil.createResource(snomedObservation, client); //should trigger one notification with JSON bundle resource in the body containing two observations
    }

    @Test
    public void testSubscriptionsWithCustomDefaultPayload() {
        String code = "1000000050";
        String criteria = "Observation?code=SNOMED-CT|" + code;
        String payloadCriteria = "application/fhir+query/" + criteria;

        Observation snomedObservation = FhirDstu3Util.getSnomedObservation();

        FhirDstu3Util.createSubscription(criteria, payloadCriteria, FhirServiceUtil.REST_HOOK_ENDPOINT, client);
        FhirServiceUtil.createResource(snomedObservation, client); //should trigger one notification with JSON bundle resource in the body containing one observations
    }

    @Test
    public void testSubscriptionsWithCustomDefaultPayloadThatIsEmpty() {
        String code = "1000000050";
        String criteria = "Observation?code=SNOMED-CT|" + code;
        String payloadCriteria = "application/fhir+query/Observation?code=SNOMED-CT|" + code + "1111";

        Observation snomedObservation = FhirDstu3Util.getSnomedObservation();

        FhirDstu3Util.createSubscription(criteria, payloadCriteria, FhirServiceUtil.REST_HOOK_ENDPOINT, client);
        FhirServiceUtil.createResource(snomedObservation, client); //should trigger one notification with JSON bundle resource in the body containing no observations
    }

    /**
     * Add a 5 second delay to the HttpRequestDstu3Job to test if threading is improving creation speed
     */
    @Test
    public void testSubscriptionsThreading() {
        String code = "1000000050";
        String criteria = "Observation?code=SNOMED-CT|" + code;
        Observation snomedObservation = FhirDstu3Util.getSnomedObservation();

        FhirDstu3Util.createSubscription(criteria, null, FhirServiceUtil.REST_HOOK_ENDPOINT, client);
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
        Observation snomedObservation = FhirDstu3Util.getSnomedObservation();

        FhirDstu3Util.createSubscription(criteria, null, FhirServiceUtil.REST_HOOK_ENDPOINT, client);
        FhirDstu3Util.createSubscription(criteria, null, FhirServiceUtil.REST_HOOK_ENDPOINT, client);
        FhirDstu3Util.createSubscription(criteria, null, FhirServiceUtil.REST_HOOK_ENDPOINT, client);
        FhirDstu3Util.createSubscription(criteria, null, FhirServiceUtil.REST_HOOK_ENDPOINT, client);
        FhirDstu3Util.createSubscription(criteria, null, FhirServiceUtil.REST_HOOK_ENDPOINT, client);

        System.out.println("start");
        FhirServiceUtil.createResource(snomedObservation, client); //should trigger one notification
        System.out.println("done");
    }

    @Test
    public void testSubscriptionsWithTMinusPayload() {
        String code = "1000000050";
        String criteria = "Observation?code=SNOMED-CT|" + code;
        String payloadCriteria = "application/fhir+query/" + criteria + "&date=Tminus100s" + "&_format=xml";
        Observation snomedObservation = FhirDstu3Util.getSnomedObservation();
        DateTimeType dateTimeType = DateTimeType.now();
        dateTimeType.setYear(2017);
        dateTimeType.setMonth(2);
        dateTimeType.setDay(1);
        System.out.println(dateTimeType.getValueAsString());
        snomedObservation.setEffective(dateTimeType);
        FhirServiceUtil.createResource(snomedObservation, client);
        snomedObservation.setEffective(DateTimeType.now());
        FhirServiceUtil.createResource(snomedObservation, client);
        FhirDstu3Util.createSubscription(criteria, payloadCriteria, FhirServiceUtil.REST_HOOK_ENDPOINT, client);
        snomedObservation.setEffective(DateTimeType.now());
        FhirServiceUtil.createResource(snomedObservation, client); //should trigger one notification with xml bundle resource in the body containing two observations
    }

    @Test
    public void testSubscriptionsWith2TMinusPayload() {
        String code = "1000000050";
        String criteria = "Observation?code=SNOMED-CT|" + code;
        String payloadCriteria = "application/fhir+query/" + criteria + "&date=Tminus2m" + "&_lastUpdated=Tminus20s" + "&_format=xml";
        Observation snomedObservation = FhirDstu3Util.getSnomedObservation();
        DateTimeType dateTimeType = DateTimeType.now();
        dateTimeType.setYear(2017);
        dateTimeType.setMonth(2);
        dateTimeType.setDay(1);
        System.out.println(dateTimeType.getValueAsString());
        snomedObservation.setEffective(dateTimeType);
        FhirServiceUtil.createResource(snomedObservation, client);
        snomedObservation.setEffective(DateTimeType.now());
        FhirServiceUtil.createResource(snomedObservation, client);
        FhirDstu3Util.createSubscription(criteria, payloadCriteria, FhirServiceUtil.REST_HOOK_ENDPOINT, client);
        snomedObservation.setEffective(DateTimeType.now());
        FhirServiceUtil.createResource(snomedObservation, client); //should trigger one notification with xml bundle resource in the body containing two observations
    }
}