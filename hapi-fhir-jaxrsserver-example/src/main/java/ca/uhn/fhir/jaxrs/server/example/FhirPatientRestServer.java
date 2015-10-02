package ca.uhn.fhir.jaxrs.server.example;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ca.uhn.fhir.jaxrs.server.AbstractResourceRestServer;
import ca.uhn.fhir.jaxrs.server.interceptor.ExceptionInterceptor;
import ca.uhn.fhir.model.dstu2.resource.Parameters;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

/**
 * Fhir Physician Rest Service
 * @author axmpm
 *
 */
@Local(IFhirPatientRestServer.class)
@Path(FhirPatientRestServer.PATH)
@Stateless
@Produces(MediaType.APPLICATION_JSON)
public class FhirPatientRestServer extends AbstractResourceRestServer<Patient> implements IFhirPatientRestServer {
    
    static final String PATH = "/Patient";
    
    private static Long counter = 1L;
    private static final ConcurrentHashMap<String, List<Patient>> patients = new ConcurrentHashMap<String, List<Patient>>();
    
    protected FhirPatientRestServer() throws Exception {
        super(FhirPatientRestServer.class);
    }    
    
    static {
        patients.put(""+counter, createPatient("Agfa"));
        patients.put(""+(counter), createPatient("Healthcare"));
        for(int i = 0 ; i<20 ; i++) {
            patients.put(""+(counter), createPatient("Random Patient " + counter));
        }
    }

    private static List<Patient> createPatient(final String name) {
        final Patient patient = new Patient();
        patient.getNameFirstRep().addFamily(name);
        return createPatient(patient);
    }
    
    private static List<Patient> createPatient(final Patient patient) {
        patient.setId(createId(counter, 1L));
        final LinkedList<Patient> list = new LinkedList<Patient>();
        list.add(patient);
        counter++;
        return list ;
    }    

    private static IdDt createId(final Long id, final Long theVersionId) {
        return new IdDt("Patient", "" + id, "" + theVersionId);
    }
    
    @Search
    @Override
    public List<Patient> search(@RequiredParam(name = Patient.SP_NAME) final StringParam name) {
        final List<Patient> result = new LinkedList<Patient>();
        for (final List<Patient> patientIterator : patients.values()) {
            Patient single = null;
            for (Patient patient : patientIterator) {
                if (name == null || patient.getNameFirstRep().getFamilyFirstRep().getValueNotNull().equals(name.getValueNotNull())) {
                    single = patient;
                }
            }
            if (single != null) {
                result.add(single);
            }
        }
        return result;
    }
    
    @Update
    @Override
    public MethodOutcome update(@IdParam final IdDt theId, @ResourceParam final Patient patient)
            throws Exception {
        final String idPart = theId.getIdPart();
        if(patients.containsKey(idPart)) {
            final List<Patient> patientList = patients.get(idPart);
            final Patient lastPatient = getLast(patientList);
            patient.setId(createId(theId.getIdPartAsLong(), lastPatient.getId().getVersionIdPartAsLong()+1));
            patientList.add(patient);
            final MethodOutcome result = new MethodOutcome().setCreated(false);
            result.setResource(patient);
            result.setId(patient.getId());
            return result;
        } else { 
            throw new ResourceNotFoundException(theId);
        }
    }    

    @Override
    @Read
    public Patient find(@IdParam final IdDt theId) {
        if(patients.containsKey(theId.getIdPart())) {
            return getLast(patients.get(theId.getIdPart()));
        } else { 
            throw new ResourceNotFoundException(theId);
        }
    }
    
    
    private Patient getLast(final List<Patient> list) {
        return list.get(list.size()-1);
    }

    @Override
    @Read(version = false)
    public Patient findHistory(@IdParam final IdDt theId) {
        if (patients.containsKey(theId.getIdPart())) {
            final List<Patient> list = patients.get(theId.getIdPart());
            for (final Patient patient : list) {
                if (patient.getId().getVersionIdPartAsLong().equals(theId.getVersionIdPartAsLong())) {
                    return patient;
                }
            }
        }
        throw new ResourceNotFoundException(theId);
    }

    @Create
    @Override
    public MethodOutcome create(@ResourceParam final Patient patient, @ConditionalUrlParam String theConditional) 
            throws Exception {
        patients.put(""+counter, createPatient(patient));
        final MethodOutcome result = new MethodOutcome().setCreated(true);
        result.setResource(patient);
        result.setId(patient.getId());
        return result;
    }
    
    @Delete
    @Override
    public MethodOutcome delete(@IdParam final IdDt theId) {
        final Patient deletedPatient = find(theId);
        patients.remove(deletedPatient.getId().getIdPart());
        final MethodOutcome result = new MethodOutcome().setCreated(true);
        result.setResource(deletedPatient);
        return result;
    }
    
    
    @GET
    @Path("/{id}/$last")
    @Interceptors(ExceptionInterceptor.class)
    @Override
    public Response operationLastGet(final String resource)
            throws Exception {
        return customOperation(null, RequestTypeEnum.GET);
    }
    
    @POST
    @Path("/{id}/$last")
    @Interceptors(ExceptionInterceptor.class)
    @Override
    public Response operationLast(final String resource)
            throws Exception {
        return customOperation(getParser().parseResource(resource), RequestTypeEnum.POST);
    }
    
//    @ca.uhn.fhir.rest.annotation.Validate
//    public MethodOutcome validate(
//            @ResourceParam T theResource, 
//            @ResourceParam String theRawResource, 
//            @ResourceParam EncodingEnum theEncoding, 
//            @ca.uhn.fhir.rest.annotation.Validate.Mode ValidationModeEnum theMode,
//            @ca.uhn.fhir.rest.annotation.Validate.Profile String theProfile) {
//        return validate(theResource, null, theRawResource, theEncoding, theMode, theProfile);
//    }

  @Operation(name="last", idempotent=true, returnParameters= {
      @OperationParam(name="return", type=StringDt.class)
  })
  @Override
  public Parameters last(@OperationParam(name = "dummy") StringDt dummyInput) {
      System.out.println("inputparameter");
      Parameters parameters = new Parameters();
      Patient patient = find(new IdDt(counter.intValue()-1));
      parameters
          .addParameter()
          .setName("return")
          .setResource(patient)
          .setValue(new StringDt((counter-1)+"" + "inputVariable [ " + dummyInput.getValue()+ "]"));
      return parameters;
  }    
    
    @Override
    public Class<Patient> getResourceType() {
        return Patient.class;
    }

}
