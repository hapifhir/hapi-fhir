package ca.uhn.fhir.jaxrs.server.example;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ca.uhn.fhir.context.api.AddProfileTagEnum;
import ca.uhn.fhir.context.api.BundleInclusionRule;
import ca.uhn.fhir.jaxrs.server.AbstractJaxRsResourceProvider;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.*;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.*;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.*;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;

/**
 * A demo JaxRs Patient Rest Provider
 * 
 * @author Peter Van Houte | peter.vanhoute@agfa.com | Agfa Healthcare
 */
@Local
@Path(JaxRsPatientRestProvider.PATH)
@Stateless
@Produces({ MediaType.APPLICATION_JSON, Constants.CT_FHIR_JSON, Constants.CT_FHIR_XML })
public class JaxRsPatientRestProvider extends AbstractJaxRsResourceProvider<Patient> {

	private static Long counter = 1L;
	
	/**
	 * The HAPI paging provider for this server
	 */
	public static final IPagingProvider PAGE_PROVIDER;
	
	static final String PATH = "/Patient";
	private static final ConcurrentHashMap<String, List<Patient>> patients = new ConcurrentHashMap<String, List<Patient>>();

	static {
		PAGE_PROVIDER = new FifoMemoryPagingProvider(10);
	}

	static {
		patients.put(String.valueOf(counter), createPatient("Van Houte"));
		patients.put(String.valueOf(counter), createPatient("Agnew"));
		for (int i = 0; i < 20; i++) {
			patients.put(String.valueOf(counter), createPatient("Random Patient " + counter));
		}
	}

	public JaxRsPatientRestProvider() {
		super(JaxRsPatientRestProvider.class);
	}

	@Create
	public MethodOutcome create(@ResourceParam final Patient patient, @ConditionalUrlParam String theConditional) throws Exception {
		patients.put("" + counter, createPatient(patient));
		final MethodOutcome result = new MethodOutcome().setCreated(true);
		result.setResource(patient);
		result.setId(patient.getId());
		return result;
	}

	@Delete
	public MethodOutcome delete(@IdParam final IdDt theId) {
		final Patient deletedPatient = find(theId);
		patients.remove(deletedPatient.getId().getIdPart());
		final MethodOutcome result = new MethodOutcome().setCreated(true);
		result.setResource(deletedPatient);
		return result;
	}

	@Read
	public Patient find(@IdParam final IdDt theId) {
		if (patients.containsKey(theId.getIdPart())) {
			return getLast(patients.get(theId.getIdPart()));
		} else {
			throw new ResourceNotFoundException(theId);
		}
	}

	@Read(version = true)
	public Patient findVersion(@IdParam final IdDt theId) {
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

	// from BaseJpaResourceProvider
	@History
	public IBundleProvider getHistoryForInstance(@IdParam IdDt theId, @Since Date theSince, @At DateRangeParam theAt, RequestDetails theRequestDetails) {
		return new SimpleBundleProvider(Collections.emptyList(), "myTestId");
	}

	@History
	public IBundleProvider getHistoryForType(@Since Date theSince, @At DateRangeParam theAt, RequestDetails theRequestDetails) {
		return new SimpleBundleProvider(Collections.emptyList(), "myTestId");
	}

	@Operation(name = "firstVersion", idempotent = true, returnParameters = { @OperationParam(name = "return", type = StringDt.class) })
	public Parameters firstVersion(@IdParam final IdDt theId, @OperationParam(name = "dummy") StringDt dummyInput) {
		Parameters parameters = new Parameters();
		Patient patient = find(new IdDt(theId.getResourceType(), theId.getIdPart(), "0"));
		parameters.addParameter().setName("return").setResource(patient).setValue(new StringDt((counter - 1) + "" + "inputVariable [ " + dummyInput.getValue() + "]"));
		return parameters;
	}

	@Override
	public AddProfileTagEnum getAddProfileTag() {
		return AddProfileTagEnum.NEVER;
	}

	@Override
	public BundleInclusionRule getBundleInclusionRule() {
		return BundleInclusionRule.BASED_ON_INCLUDES;
	}

	@Override
	public ETagSupportEnum getETagSupport() {
		return ETagSupportEnum.DISABLED;
	}

	/** THE DEFAULTS */

	@Override
	public List<IServerInterceptor> getInterceptors_() {
		return Collections.emptyList();
	}

	private Patient getLast(final List<Patient> list) {
		return list.get(list.size() - 1);
	}

	@Override
	public IPagingProvider getPagingProvider() {
		return PAGE_PROVIDER;
	}

	@Override
	public Class<Patient> getResourceType() {
		return Patient.class;
	}

	@Override
	public boolean isDefaultPrettyPrint() {
		return true;
	}

	@GET
	@Path("/{id}/$firstVersion")
	public Response operationFirstVersionUsingGet(@PathParam("id") String id) throws IOException {
		return customOperation(null, RequestTypeEnum.GET, id, "$firstVersion", RestOperationTypeEnum.EXTENDED_OPERATION_INSTANCE);
	}

	@POST
	@Path("/{id}/$firstVersion")
	public Response operationFirstVersionUsingGet(@PathParam("id") String id, final String resource) throws Exception {
		return customOperation(resource, RequestTypeEnum.POST, id, "$firstVersion", RestOperationTypeEnum.EXTENDED_OPERATION_INSTANCE);
	}

	@Search
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

	@Search(compartmentName = "Condition")
	public List<IResource> searchCompartment(@IdParam IdDt thePatientId) {
		List<IResource> retVal = new ArrayList<IResource>();
		Condition condition = new Condition();
		condition.setId(new IdDt("665577"));
		retVal.add(condition);
		return retVal;
	}

	@Update
	public MethodOutcome update(@IdParam final IdDt theId, @ResourceParam final Patient patient) {
		final String idPart = theId.getIdPart();
		if (patients.containsKey(idPart)) {
			final List<Patient> patientList = patients.get(idPart);
			final Patient lastPatient = getLast(patientList);
			patient.setId(createId(theId.getIdPartAsLong(), lastPatient.getId().getVersionIdPartAsLong() + 1));
			patientList.add(patient);
			final MethodOutcome result = new MethodOutcome().setCreated(false);
			result.setResource(patient);
			result.setId(patient.getId());
			return result;
		} else {
			throw new ResourceNotFoundException(theId);
		}
	}

	private static IdDt createId(final Long id, final Long theVersionId) {
		return new IdDt("Patient", "" + id, "" + theVersionId);
	}

	private static List<Patient> createPatient(final Patient patient) {
		patient.setId(createId(counter, 1L));
		final LinkedList<Patient> list = new LinkedList<Patient>();
		list.add(patient);
		counter++;
		return list;
	}

	private static List<Patient> createPatient(final String name) {
		final Patient patient = new Patient();
		patient.getNameFirstRep().addFamily(name);
		return createPatient(patient);
	}

}
