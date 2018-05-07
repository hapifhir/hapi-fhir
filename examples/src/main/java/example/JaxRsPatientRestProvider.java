package example;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ca.uhn.fhir.jaxrs.server.AbstractJaxRsResourceProvider;
import ca.uhn.fhir.model.dstu2.resource.Parameters;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.*;

/**
 * A demo JaxRs Patient Rest Provider
 */
@Local
@Stateless
// START SNIPPET: jax-rs-provider-construction
@Path("/Patient")
@Produces({ MediaType.APPLICATION_JSON, Constants.CT_FHIR_JSON, Constants.CT_FHIR_XML })
public class JaxRsPatientRestProvider extends AbstractJaxRsResourceProvider<Patient> {

  public JaxRsPatientRestProvider() {
    super(JaxRsPatientRestProvider.class);
  }
// END SNIPPET: jax-rs-provider-construction

  @Override
  public Class<Patient> getResourceType() {
    return Patient.class;
  }
  
  
  @Create
  public MethodOutcome create(@ResourceParam final Patient patient, @ConditionalUrlParam String theConditional) {
    // create the patient ...
    return new MethodOutcome(new IdDt(1L)).setCreated(true);
  }
  
// START SNIPPET: jax-rs-provider-operation
  @GET
  @Path("/{id}/$someCustomOperation")
  public Response someCustomOperationUsingGet(@PathParam("id") String id, String resource) throws Exception {
    return customOperation(resource, RequestTypeEnum.GET, id, "$someCustomOperation", 
            RestOperationTypeEnum.EXTENDED_OPERATION_INSTANCE);
  }
  
  @Operation(name = "someCustomOperation", idempotent = true, returnParameters = {
      @OperationParam(name = "return", type = StringDt.class) })
  public Parameters someCustomOperation(@IdParam IdDt myId, @OperationParam(name = "dummy") StringDt dummyInput) {
    Parameters parameters = new Parameters();
    parameters.addParameter().setName("return").setValue(new StringDt("My Dummy Result"));
    return parameters;
  }  
 // END SNIPPET: jax-rs-provider-operation
  
  @POST
  @Path("/{id}/$someCustomOperation")
  public Response someCustomOperationUsingPost(@PathParam("id") String id, String resource) throws Exception {
    return customOperation(resource, RequestTypeEnum.POST, id, "$someCustomOperation", 
            RestOperationTypeEnum.EXTENDED_OPERATION_INSTANCE);
  }   
 
}
