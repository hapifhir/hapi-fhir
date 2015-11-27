package ca.uhn.fhir.jaxrs.server.test;

import java.util.List;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.mockito.Mockito;

import ca.uhn.fhir.jaxrs.server.AbstractJaxRsResourceProvider;
import ca.uhn.fhir.model.api.IResource;
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
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IPagingProvider;

/**
 * A test server delegating each call to a mock
 */
@Path(TestJaxRsMockPatientRestProvider.PATH)
@Stateless
@Produces({ MediaType.APPLICATION_JSON, Constants.CT_FHIR_JSON, Constants.CT_FHIR_XML })
public class TestJaxRsMockPatientRestProvider extends AbstractJaxRsResourceProvider<Patient> {

	static final String PATH = "/Patient";

	public static final TestJaxRsMockPatientRestProvider mock = Mockito.mock(TestJaxRsMockPatientRestProvider.class);

	public static final FifoMemoryPagingProvider PAGING_PROVIDER;

	static
	{
		PAGING_PROVIDER = new FifoMemoryPagingProvider(10);
		PAGING_PROVIDER.setDefaultPageSize(10);
		PAGING_PROVIDER.setMaximumPageSize(100);
	}
	
	/**
	 * Constructor
	 */
	public TestJaxRsMockPatientRestProvider() {
		super();
	}

	@Search
	public List<Patient> search(@RequiredParam(name = Patient.SP_NAME) final StringParam name, @RequiredParam(name=Patient.SP_ADDRESS) StringAndListParam theAddressParts) {
		return mock.search(name, theAddressParts);
	}

	@Update
	public MethodOutcome update(@IdParam final IdDt theId, @ResourceParam final Patient patient) throws Exception {
		return mock.update(theId, patient);
	}

	@Read
	public Patient find(@IdParam final IdDt theId) {
		return mock.find(theId);
	}

	@Read(version = true)
	public Patient findHistory(@IdParam final IdDt theId) {
		return mock.findHistory(theId);
	}

	@Create
	public MethodOutcome create(@ResourceParam final Patient patient, @ConditionalUrlParam String theConditional)
			throws Exception {
		return mock.create(patient, theConditional);
	}

	@Delete
	public MethodOutcome delete(@IdParam final IdDt theId) {
		return mock.delete(theId);
	}

    @Search(compartmentName = "Condition")
    public List<IResource> searchCompartment(@IdParam IdDt thePatientId) {
        return mock.searchCompartment(thePatientId);
    }   
	
	@GET
	@Path("/{id}/$someCustomOperation")
	public Response someCustomOperationUsingGet(@PathParam("id") String id, String resource) throws Exception {
		return customOperation(resource, RequestTypeEnum.GET, id, "$someCustomOperation",
				RestOperationTypeEnum.EXTENDED_OPERATION_INSTANCE);
	}

	@POST
	@Path("/{id}/$someCustomOperation")
	public Response someCustomOperationUsingPost(@PathParam("id") String id, String resource) throws Exception {
		return customOperation(resource, RequestTypeEnum.POST, id, "$someCustomOperation",
				RestOperationTypeEnum.EXTENDED_OPERATION_INSTANCE);
	}

	@Operation(name = "someCustomOperation", idempotent = true, returnParameters = {
			@OperationParam(name = "return", type = StringDt.class) })
	public Parameters someCustomOperation(@IdParam IdDt myId, @OperationParam(name = "dummy") StringDt dummyInput) {
		return mock.someCustomOperation(myId, dummyInput);
	}

	@Override
	public Class<Patient> getResourceType() {
		return Patient.class;
	}

	@Override
	public IPagingProvider getPagingProvider() {
		return PAGING_PROVIDER;
	}

}
