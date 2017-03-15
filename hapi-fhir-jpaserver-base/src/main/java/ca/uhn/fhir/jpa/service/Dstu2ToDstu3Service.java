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
package ca.uhn.fhir.jpa.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.util.MethodRequest;
import ca.uhn.fhir.jpa.util.RestUtilities;
import ca.uhn.fhir.model.dstu2.resource.*;
import ca.uhn.fhir.rest.client.IGenericClient;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.converter.dstu2.formats.IParser;
import org.hl7.fhir.converter.dstu2.model.Resource;
import org.hl7.fhir.convertor.dstu3.model.Reference;
import org.hl7.fhir.convertors.IGPackConverter102;
import org.hl7.fhir.convertors.VersionConvertor_10_20;
import org.hl7.fhir.dstu3.model.MedicationRequest;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.FHIRFormatError;
import org.hl7.fhir.instance.model.IdType;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Dstu2ToDstu3Service {

    @Autowired
    @Qualifier("mySubscriptionDaoDstu2")
    private IFhirResourceDao<Subscription> mySubscriptionDao;

    @Autowired
    @Qualifier("myLocationDaoDstu2")
    private IFhirResourceDao<Location> myLocationDao;

    @Autowired
    @Qualifier("myDeviceDaoDstu2")
    private IFhirResourceDao<Device> myDeviceDao;

    private static final Logger logger = LoggerFactory.getLogger(Dstu2ToDstu3Service.class);
    public static final String HOME = "home";

    private final IGPackConverter102 packConverter102 = new IGPackConverter102();
    private final VersionConvertor_10_20 fhirConverter = new VersionConvertor_10_20(packConverter102);

    public static final String HMS_CODESYSTEM = "http://cognitivemedicine.com/hms";
    public static final String HMS_CODE = "HMS-FHIR";

    //temporaily hardcoded
    public static final String BASE_URL = "http://64.87.15.70:9091/baseDstu3";
    //public static final String BASE_URL = "http://localhost:9093/baseDstu3";

    /**
     * Convert to a DSTU3 resource, customize it and send to a DSTU3 server
     *
     * @param observation
     * @throws FHIRException
     * @throws IOException
     */
    public void onCreateObservation(Observation observation) throws FHIRException, IOException {
        String json = getAsFhirString(observation);
        logger.info(json);
        org.hl7.fhir.converter.dstu2.model.Observation observationDstu2 = getAsConverterDstu2Resource(json, org.hl7.fhir.converter.dstu2.model.Observation.class);
        org.hl7.fhir.convertor.dstu3.model.Observation observationDstu3 = fhirConverter.convertObservation(observationDstu2);

        observationDstu3.setComment(("hospital"));

        Reference deviceRef = observationDstu3.getDevice();
        if (deviceRef != null && deviceRef.getReference() != null) {
            IdType idType = new IdType(deviceRef.getReference());
            Device device = myDeviceDao.read(idType);

            if (device.getLocation().getReference().getValueAsString() != null) {
                Location location = myLocationDao.read(new IdType(device.getLocation().getReference().getValueAsString()));
                if (location != null && location.getName() != null) {
                    if (location.getName().toLowerCase().equals(HOME)) {
                        observationDstu3.setComment("home");
                        logger.debug("Found patient device");
                    }
                }
            }
        }

        LoincService.checkLoincCodes(observationDstu3);
        IBaseMetaType meta = observationDstu3.getMeta();
        IBaseCoding tag = meta.addTag();
        tag.setCode(HMS_CODE);
        tag.setSystem(HMS_CODESYSTEM);

        //temporarily set the device to null until we can add devices to DSTU3
        observationDstu3.setDevice(null);
        observationDstu3.setRelated(null);

        //send the observation to the DSTU3 server
        String response = RestUtilities.getResponse(BASE_URL + "/Observation", observationDstu3, MethodRequest.POST);
    }

    /**
     * Convert to a DSTU3 resource, customize it and send to a DSTU3 server
     *
     * @param medicationAdministration
     * @throws FHIRException
     * @throws IOException
     */
    public void onCreateMedicationAdministration(MedicationAdministration medicationAdministration) throws FHIRException, IOException {
        String json = getAsFhirString(medicationAdministration);
        logger.info(json);
        org.hl7.fhir.convertor.dstu3.model.MedicationAdministration medAdmDstu3 = ConvertorService.convertMedicationAdministration(json);
        //temporarily set the device to null until we can add devices to DSTU3
        medAdmDstu3.setDevice(null);
        IBaseMetaType meta = medAdmDstu3.getMeta();
        IBaseCoding tag = meta.addTag();
        tag.setCode(HMS_CODE);
        tag.setSystem(HMS_CODESYSTEM);

        //note: medication administrtions will create an empty prescription object if its empty
        if (medAdmDstu3.getPrescription().getId() == null) {
            List<org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent> myList = getDSTU3MedicationRequest(medAdmDstu3);
            if (myList.size() > 0) {
                Reference medicationReference = new Reference();
                String medicationRequestId = myList.get(0).getResource().getIdElement().getResourceType() + "/"
                        + myList.get(0).getResource().getIdElement().getIdPart();
                logger.info(medicationRequestId);
                medicationReference.setReference(medicationRequestId);
                medAdmDstu3.setPrescription(medicationReference);
            }
        }

        String stringObject = RestUtilities.getAsDstu3JsonString(medAdmDstu3);
        //fix converter's json serialization code that converts the patient into a "subject" field, but must be "patient" field
        stringObject = stringObject.replace("\"subject\": {", "\"patient\": {");
        logger.info(stringObject);
        StringEntity stringEntity = new StringEntity(stringObject);
        String response = RestUtilities.getResponse(BASE_URL + "/MedicationAdministration", stringEntity, MethodRequest.POST);

        System.out.println("...");
    }

    /**
     * Get a {@link MedicationRequest} resource from DSTU3 server
     *
     * @param medAdmDstu3
     * @return
     * @throws FHIRException
     */
    private List<org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent> getDSTU3MedicationRequest(org.hl7.fhir.convertor.dstu3.model.MedicationAdministration medAdmDstu3) throws FHIRException {
        String code = medAdmDstu3.getMedicationCodeableConcept().getCoding().get(0).getCode();
        String system = medAdmDstu3.getMedicationCodeableConcept().getCoding().get(0).getSystem();
        String patientId = medAdmDstu3.getSubject().getReference();

        FhirContext ctx = FhirContext.forDstu3();
        IGenericClient client = ctx.newRestfulGenericClient(BASE_URL);

        org.hl7.fhir.dstu3.model.Bundle results = client
                .search()
                .forResource(MedicationRequest.class)
                .where(MedicationRequest.PATIENT.hasId(patientId))
                .and(MedicationRequest.CODE.exactly().systemAndCode(system, code))
                .returnBundle(org.hl7.fhir.dstu3.model.Bundle.class)
                .execute();

        return results.getEntry();
    }

    /**
     * Convert DSTU2 resource to string
     *
     * @param resource
     * @return
     */
    private String getAsFhirString(BaseResource resource) {
        return mySubscriptionDao.getContext().newJsonParser().setPrettyPrint(false).encodeResourceToString(resource);
    }

    /**
     * Convert string to DSTU2 object from 'Converter' package
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    private <T> T getAsConverterDstu2Resource(String json, Class<T> clazz) {
        InputStream stream = new ByteArrayInputStream(json.getBytes());
        IParser parser = new org.hl7.fhir.converter.dstu2.formats.JsonParser();
        Resource resource;
        try {
            resource = parser.parse(stream);
        } catch (IOException e) {
            throw new RuntimeException("System error converting the JSON FHIR string to the converter dstu2 resource");
        } catch (FHIRFormatError e) {
            throw new RuntimeException("FHIR Format error in the conversion of the JSON FHIR string to the converter dstu2 resource");
        }
        return clazz.cast(resource);
    }
}
