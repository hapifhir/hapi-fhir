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

import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.gclient.IQuery;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Subscription;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Ignore
public class RemoveDstu3TestIT {

    private static Logger logger = LoggerFactory.getLogger(RemoveDstu3TestIT.class);
    public static final int NUM_TO_DELETE_PER_QUERY = 10000;

    @Test
    public void remove() {
        IGenericClient client = FhirServiceUtil.getFhirDstu3Client();
        deleteResources(Subscription.class, null, client);
        deleteResources(Observation.class, null, client);
/*		 try {
		 	//wait for cache to clear
			 Thread.sleep(3000);
		 } catch (InterruptedException e) {
			 e.printStackTrace();
		 }
*/
		 Bundle bundle = searchResources(Observation.class, null, NUM_TO_DELETE_PER_QUERY, client);
        Assert.assertNotNull(bundle);
        List<Bundle.BundleEntryComponent> entry = bundle.getEntry();
		 Assert.assertTrue(entry.isEmpty());
    }

    /**
     * Delete resources from specified class and tag
     *
     * @param clazz
     * @param tag
     * @param <T>
     */
    public static <T extends IBaseResource> void deleteResources(Class<T> clazz, IBaseCoding tag, IGenericClient client) {
        Bundle bundle = searchResources(clazz, tag, NUM_TO_DELETE_PER_QUERY, client);
        List<Bundle.BundleEntryComponent> bundleEntryComponents = bundle.getEntry();

//        while (bundleEntryComponents.size() > 0) {
            for (Bundle.BundleEntryComponent bundleEntryComponent : bundleEntryComponents) {
                IBaseResource resource = bundleEntryComponent.getResource();
                String id = resource.getIdElement().getIdPart();
                String className = clazz.getSimpleName();

                logger.info("deleting resource------------------------------------------>" + className + "/" + id);

                client.delete().resourceById(className, id).execute();
            }
//			currently loops forever due to the FHIR server using a cached query result
//			  bundle = searchResources(clazz, tag, NUM_TO_DELETE_PER_QUERY, client);
//            bundleEntryComponents = bundle.getEntry();
//        }
    }

    /**
     * Get resources from specified class and tag
     *
     * @param clazz
     * @param tag
     * @param limit
     * @param <T>
     * @return
     */
    public static <T extends IBaseResource> Bundle searchResources(Class<T> clazz, IBaseCoding tag, Integer limit, IGenericClient client) {
        IQuery iquery = client.search().forResource(clazz);

        if (tag != null) {
            iquery.withTag(tag.getSystem(), tag.getCode());
        }

        if (limit != null) {
            iquery.count(limit);
        }

        return (Bundle) iquery.returnBundle(Bundle.class).execute();
    }
}
