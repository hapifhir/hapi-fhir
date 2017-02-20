package ca.uhn.fhir.jpa.service;

import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.util.MethodRequest;
import ca.uhn.fhir.jpa.util.RestUtilities;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.BaseResource;
import ca.uhn.fhir.model.dstu2.resource.Device;
import ca.uhn.fhir.model.dstu2.resource.Location;
import ca.uhn.fhir.model.dstu2.resource.MedicationAdministration;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Subscription;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.converter.dstu2.formats.IParser;
import org.hl7.fhir.converter.dstu2.model.Resource;
import org.hl7.fhir.convertor.dstu3.model.Reference;
import org.hl7.fhir.convertors.IGPackConverter102;
import org.hl7.fhir.convertors.VersionConvertor_10_20;
import org.hl7.fhir.dstu3.model.*;
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

/**
 * Created by Jeff on 2/15/2017.
 */
public class Dstu2ToDstu3Service {

    @Autowired
    @Qualifier("mySubscriptionDaoDstu2")
    protected IFhirResourceDao<Subscription> mySubscriptionDao;

    @Autowired
    @Qualifier("myLocationDaoDstu2")
    protected IFhirResourceDao<Location> myLocationDao;

    @Autowired
    @Qualifier("myDeviceDaoDstu2")
    protected IFhirResourceDao<Device> myDeviceDao;

    private static final Logger logger = LoggerFactory.getLogger(Dstu2ToDstu3Service.class);
    public static final String HOME = "home";

    private IGPackConverter102 packConverter102 = new IGPackConverter102();
    private VersionConvertor_10_20 fhirConverter = new VersionConvertor_10_20(packConverter102);

    public static final String HMS_CODESYSTEM = "http://cognitivemedicine.com/hms";
    public static final String HMS_CODE = "HMS-FHIR";

    //temporaily hardcoded
    //public static final String BASE_URL = "http://cognitive.cds.hspconsortium.org/baseDstu3";
//    public static final String BASE_URL = "http://192.168.1.186/baseDstu3";
    public static final String BASE_URL = "http://64.87.15.70:9091/baseDstu3";

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

        //send the observation to the DSTU3 server
        String response = RestUtilities.getResponse(BASE_URL + "/Observation", observationDstu3, MethodRequest.POST);
    }

    public void onCreateMedicationAdministration(MedicationAdministration medicationAdministration) throws FHIRException, IOException{
        String json = getAsFhirString(medicationAdministration);
        logger.info(json);
        org.hl7.fhir.convertor.dstu3.model.MedicationAdministration medAdmDstu3 = ConvertorService.convertMedicationAdministration(json);
        //temporarily set the device to null until we can add devices to DSTU3
        medAdmDstu3.setDevice(null);
        IBaseMetaType meta = medAdmDstu3.getMeta();
        IBaseCoding tag = meta.addTag();
        tag.setCode(HMS_CODE);
        tag.setSystem(HMS_CODESYSTEM);

        System.out.println(medAdmDstu3.getSubject().getReference());

        Reference reference = new Reference();
        //todo change to query
        if (medAdmDstu3.getSubject().getReference().equals("Patient/PatientId-1234")) {
            reference.setReference("MedicationRequest/medicationrequest-labet");
        } else if (medAdmDstu3.getSubject().getReference().equals("Patient/PatientId-1235")) {
            reference.setReference("MedicationRequest/medicationrequest-labet2");
        }
        medAdmDstu3.setPrescription(reference);

        //can send the MedicationAdministration to the DSTU3 server, but the patient id does not get included even though its in the object
        //String response = RestUtilities.getResponse(BASE_URL + "/MedicationAdministration", medAdmDstu3, MethodRequest.POST);

        String stringObject = RestUtilities.getAsDstu3JsonString(medAdmDstu3);
        //todo must fix code that converts the patient into a subject field, but must be patient
        stringObject = stringObject.replace("\"subject\": {", "\"patient\": {");
        logger.info(stringObject);
        StringEntity stringEntity = new StringEntity(stringObject);
        String response = RestUtilities.getResponse(BASE_URL + "/MedicationAdministration", stringEntity, MethodRequest.POST);


        System.out.println("...");
    }

    private String getAsFhirString(BaseResource resource) {
        return mySubscriptionDao.getContext().newJsonParser().setPrettyPrint(false).encodeResourceToString(resource);
    }

    private <T> T getAsConverterDstu2Resource(String json, Class<T> clazz) {
        InputStream stream = new ByteArrayInputStream(json.getBytes());
        IParser parser = new org.hl7.fhir.converter.dstu2.formats.JsonParser();
        Resource resource = null;
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
