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

package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.service.Dstu2ToDstu3Service;
import ca.uhn.fhir.model.dstu2.resource.MedicationAdministration;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.rest.server.interceptor.InterceptorAdapter;
import org.hl7.fhir.exceptions.FHIRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class Dstu2ToDstu3Interceptor extends InterceptorAdapter implements IJpaServerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(Dstu2ToDstu3Interceptor.class);

    @Autowired
    private Dstu2ToDstu3Service dstu2ToDstu3Service;

    /**
     * Resource created handler
     *
     * @param theDetails       The request details
     * @param theResourceTable The actual created entity
     */
    @Override
    public void resourceCreated(ActionRequestDetails theDetails, ResourceTable theResourceTable) {
        String resourceType = theDetails.getResourceType();

        try {
            if ("Observation".equals(resourceType)) {
                logger.info("Found observation");
                dstu2ToDstu3Service.onCreateObservation((Observation) theDetails.getResource());
            } else if ("MedicationAdministration".equals(resourceType)) {
                logger.info("Found MedicationAdministration");
                dstu2ToDstu3Service.onCreateMedicationAdministration((MedicationAdministration) theDetails.getResource());
            } else {
                logger.info("Skipping conversion of resource: " + resourceType);
            }
        } catch (IOException e) {
            //throw new RuntimeException("System error sending the STU3 observation");
        } catch (FHIRException e) {
            throw new RuntimeException("FHIR error converting the DSTU2 model to STU3");
        }

        logger.info("resource created type: " + resourceType);
    }

    /**
     * Resource updated handler
     *
     * @param theDetails       The request details
     * @param theResourceTable The actual updated entity
     */
    @Override
    public void resourceUpdated(ActionRequestDetails theDetails, ResourceTable theResourceTable) {
        String resourceType = theDetails.getResourceType();

        logger.info("resource updated type: " + resourceType);
    }

    /**
     * Resource deleted handler
     *
     * @param theDetails       The request details
     * @param theResourceTable The actual updated entity
     */
    @Override
    public void resourceDeleted(ActionRequestDetails theDetails, ResourceTable theResourceTable) {
        logger.info("resource removed type: " + theDetails.getResourceType());
    }
}