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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class FhirServiceUtil {

    public static final String FHIR_DSTU3_URL = "http://localhost:9093/baseDstu3";
    public static final String FHIR_DSTU2_URL = "http://localhost:9092/baseDstu2";
    public static final String JSON_PAYLOAD = "application/json";
    public static final String XML_PAYLOAD = "application/xml";
    public static final String REST_HOOK_ENDPOINT = "http://localhost:10080/rest-hook";

    public static IGenericClient getFhirDstu3Client() {
        FhirContext ctx = FhirContext.forDstu3();
        return ctx.newRestfulGenericClient(FHIR_DSTU3_URL);
    }

    public static IGenericClient getFhirDstu2Client() {
        FhirContext ctx = FhirContext.forDstu2();
        return ctx.newRestfulGenericClient(FHIR_DSTU2_URL);
    }

    public static String createResource(IBaseResource resource, IGenericClient client) {
        MethodOutcome response = client.create().resource(resource).execute();
        resource.setId(response.getId());

        return response.getId().getIdPart();
    }

    public static String updateResource(IBaseResource resource, IGenericClient client) {
        MethodOutcome response = client.update().resource(resource).execute();
        return response.getId().getIdPart();
    }

    public static void deleteResource(String id, Class clazz, IGenericClient client) {
        client.delete().resourceById(clazz.getSimpleName(), id).execute();
    }


}
