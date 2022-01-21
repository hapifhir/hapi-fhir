package ca.uhn.hapi.fhir.docs;

/*-
 * #%L
 * HAPI FHIR - Docs
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.jaxrs.server.AbstractJaxRsResourceProvider;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
    return new MethodOutcome(new IdType(1L)).setCreated(true);
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
  public Parameters someCustomOperation(@IdParam IdType myId, @OperationParam(name = "dummy") StringDt dummyInput) {
    Parameters parameters = new Parameters();
    parameters.addParameter().setName("return").setValue(new StringType("My Dummy Result"));
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
