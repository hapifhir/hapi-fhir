package ca.uhn.fhir.jaxrs.server.example;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.api.AddProfileTagEnum;
import ca.uhn.fhir.context.api.BundleInclusionRule;
import ca.uhn.fhir.jaxrs.server.AbstractJaxRsResourceProvider;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.ETagSupportEnum;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.*;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A demo JaxRs Patient Rest Provider
 */
@Local
@Path(JaxRsPatientRestProviderR4.PATH)
@Stateless
@Produces({ MediaType.APPLICATION_JSON, Constants.CT_FHIR_JSON, Constants.CT_FHIR_XML })
public class JaxRsPatientRestProviderR4 extends AbstractJaxRsResourceProvider<Patient> {

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

	public JaxRsPatientRestProviderR4() {
		super(FhirContext.forDstu3(), JaxRsPatientRestProviderR4.class);
	}

	@Create
	public MethodOutcome create(@ResourceParam final Patient patient, @ConditionalUrlParam String theConditional) throws Exception {
		patients.put("" + counter, createPatient(patient));
		final MethodOutcome result = new MethodOutcome().setCreated(true);
		result.setResource(patient);
		result.setId(new IdType(patient.getId()));
		return result;
	}

	@Delete
	public MethodOutcome delete(@IdParam final IdType theId) {
		final Patient deletedPatient = find(theId);
		patients.remove(deletedPatient.getIdElement().getIdPart());
		final MethodOutcome result = new MethodOutcome().setCreated(true);
		result.setResource(deletedPatient);
		return result;
	}

	@Read
	public Patient find(@IdParam final IdType theId) {
		if (patients.containsKey(theId.getIdPart())) {
			return getLast(patients.get(theId.getIdPart()));
		} else {
			throw new ResourceNotFoundException(theId);
		}
	}

	@Read(version = true)
	public Patient findVersion(@IdParam final IdType theId) {
		if (patients.containsKey(theId.getIdPart())) {
			final List<Patient> list = patients.get(theId.getIdPart());
			for (final Patient patient : list) {
				if (patient.getIdElement().getVersionIdPartAsLong().equals(theId.getVersionIdPartAsLong())) {
					return patient;
				}
			}
		}
		throw new ResourceNotFoundException(theId);
	}

	@History
	public IBundleProvider getHistoryForInstance(@IdParam IdType theId, @Since Date theSince, @At DateRangeParam theAt, RequestDetails theRequestDetails) {
		return new SimpleBundleProvider(Collections.emptyList(), "myTestId");
	}

	@History
	public IBundleProvider getHistoryForType(@Since Date theSince, @At DateRangeParam theAt, RequestDetails theRequestDetails) {
		return new SimpleBundleProvider(Collections.emptyList(), "myTestId");
	}

	@Operation(name = "firstVersion", idempotent = true, returnParameters = { @OperationParam(name = "return", type = StringType.class) })
	public Parameters firstVersion(@IdParam final IdType theId, @OperationParam(name = "dummy") StringType dummyInput) {
		Parameters parameters = new Parameters();
		Patient patient = find(new IdType(theId.getResourceType(), theId.getIdPart(), "0"));
		parameters.addParameter().setName("return").setResource(patient).setValue(new StringType((counter - 1) + "" + "inputVariable [ " + dummyInput.getValue() + "]"));
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
				if (name == null || patient.getName().get(0).getFamilyElement().getValueNotNull().equals(name.getValueNotNull())) {
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
	public List<IBaseResource> searchCompartment(@IdParam IdType thePatientId) {
		List<IBaseResource> retVal = new ArrayList<IBaseResource>();
		Condition condition = new Condition();
		condition.setId(new IdType("665577"));
		retVal.add(condition);
		return retVal;
	}

	@Update
	public MethodOutcome update(@IdParam final IdType theId, @ResourceParam final Patient patient) {
		final String idPart = theId.getIdPart();
		if (patients.containsKey(idPart)) {
			final List<Patient> patientList = patients.get(idPart);
			final Patient lastPatient = getLast(patientList);
			patient.setId(createId(theId.getIdPartAsLong(), lastPatient.getIdElement().getVersionIdPartAsLong() + 1));
			patientList.add(patient);
			final MethodOutcome result = new MethodOutcome().setCreated(false);
			result.setResource(patient);
			result.setId(new IdType(patient.getId()));
			return result;
		} else {
			throw new ResourceNotFoundException(theId);
		}
	}

	private static IdType createId(final Long id, final Long theVersionId) {
		return new IdType("Patient", "" + id, "" + theVersionId);
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
		patient.getName().add(new HumanName().setFamily(name));
		return createPatient(patient);
	}

}
