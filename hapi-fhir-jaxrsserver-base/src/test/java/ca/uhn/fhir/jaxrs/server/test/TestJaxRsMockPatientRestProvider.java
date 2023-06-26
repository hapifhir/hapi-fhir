package ca.uhn.fhir.jaxrs.server.test;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import org.hl7.fhir.instance.model.api.IIdType;
import org.mockito.Mockito;

import ca.uhn.fhir.jaxrs.server.AbstractJaxRsResourceProvider;
import ca.uhn.fhir.jaxrs.server.interceptor.JaxRsExceptionInterceptor;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.*;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.*;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IPagingProvider;

/**
 * A test server delegating each call to a mock
 */
@Path(TestJaxRsMockPatientRestProvider.PATH)
@Stateless
@Produces({ MediaType.APPLICATION_JSON, Constants.CT_FHIR_JSON, Constants.CT_FHIR_XML })
@Interceptors(JaxRsExceptionInterceptor.class)
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
	public MethodOutcome update(@IdParam final IdDt theId, @ResourceParam final Patient patient,@ConditionalUrlParam final String theConditional) throws Exception {
		return mock.update(theId, patient, theConditional);
	}

	@Read
	public Patient find(@IdParam final IdDt theId) {
		return mock.find(theId);
	}

	@Read(version = true)
	public Patient findVersion(@IdParam final IdDt theId) {
		return mock.findVersion(theId);
	}

	@History
	public IBundleProvider getHistoryForInstance(@IdParam IIdType theId) {
		return mock.getHistoryForInstance(theId);
	}

	@History
	public IBundleProvider getHistoryForType() {
		return mock.getHistoryForType();
	}

	@Create
	public MethodOutcome create(@ResourceParam final Patient patient, @ConditionalUrlParam String theConditional)
			throws Exception {
		return mock.create(patient, theConditional);
	}

	@Delete
	public MethodOutcome delete(@IdParam final IdDt theId, @ConditionalUrlParam final String theConditional) {
		return mock.delete(theId, theConditional);
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
  
	@Validate()
	public MethodOutcome validate(@ResourceParam final Patient resource) {
		final MethodOutcome mO = new MethodOutcome();
		mO.setOperationOutcome(new OperationOutcome());
		return mO;
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
