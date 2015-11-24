package ca.uhn.fhir.jaxrs.server.example;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ca.uhn.fhir.jaxrs.server.AbstractJaxRsResourceProvider;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.Condition;
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
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.AddProfileTagEnum;
import ca.uhn.fhir.rest.server.BundleInclusionRule;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.ETagSupportEnum;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IPagingProvider;
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

	static final String PATH = "/Patient";

	private static Long counter = 1L;
	private static final ConcurrentHashMap<String, List<Patient>> patients = new ConcurrentHashMap<String, List<Patient>>();

	public JaxRsPatientRestProvider() {
		super(JaxRsPatientRestProvider.class);
	}

	static {
		patients.put(String.valueOf(counter), createPatient("Van Houte"));
		patients.put(String.valueOf(counter), createPatient("Agnew"));
		for (int i = 0; i < 20; i++) {
			patients.put(String.valueOf(counter), createPatient("Random Patient " + counter));
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
		return list;
	}

	private static IdDt createId(final Long id, final Long theVersionId) {
		return new IdDt("Patient", "" + id, "" + theVersionId);
	}

	@Search
	public List<Patient> search(@RequiredParam(name = Patient.SP_NAME) final StringParam name) {
		final List<Patient> result = new LinkedList<Patient>();
		for (final List<Patient> patientIterator : patients.values()) {
			Patient single = null;
			for (Patient patient : patientIterator) {
				if (name == null || patient.getNameFirstRep().getFamilyFirstRep().getValueNotNull()
						.equals(name.getValueNotNull())) {
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
	public MethodOutcome update(@IdParam final IdDt theId, @ResourceParam final Patient patient) throws Exception {
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

	@Read
	public Patient find(@IdParam final IdDt theId) throws InvocationTargetException {
		if (patients.containsKey(theId.getIdPart())) {
			return getLast(patients.get(theId.getIdPart()));
		} else {
			throw new ResourceNotFoundException(theId);
		}
	}

	private Patient getLast(final List<Patient> list) {
		return list.get(list.size() - 1);
	}

	@Read(version = true)
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
	public MethodOutcome create(@ResourceParam final Patient patient, @ConditionalUrlParam String theConditional)
			throws Exception {
		patients.put("" + counter, createPatient(patient));
		final MethodOutcome result = new MethodOutcome().setCreated(true);
		result.setResource(patient);
		result.setId(patient.getId());
		return result;
	}

	@Delete
	public MethodOutcome delete(@IdParam final IdDt theId) throws InvocationTargetException {
		final Patient deletedPatient = find(theId);
		patients.remove(deletedPatient.getId().getIdPart());
		final MethodOutcome result = new MethodOutcome().setCreated(true);
		result.setResource(deletedPatient);
		return result;
	}

	@GET
	@Path("/{id}/$last")
	public Response operationLastGet(@PathParam("id") String id) throws Exception {
		return customOperation(null, RequestTypeEnum.GET, id, "$last",
				RestOperationTypeEnum.EXTENDED_OPERATION_TYPE);
	}

	@Search(compartmentName = "Condition")
	public List<IResource> searchCompartment(@IdParam IdDt thePatientId) {
		List<IResource> retVal = new ArrayList<IResource>();
		Condition condition = new Condition();
		condition.setId(new IdDt("665577"));
		retVal.add(condition);
		return retVal;
	}

	@POST
	@Path("/{id}/$last")
	public Response operationLast(final String resource) throws Exception {
		return customOperation(resource, RequestTypeEnum.POST, null, "$last",
				RestOperationTypeEnum.EXTENDED_OPERATION_TYPE);
	}

	@Operation(name = "last", idempotent = true, returnParameters = {
			@OperationParam(name = "return", type = StringDt.class) })
	public Parameters last(@OperationParam(name = "dummy") StringDt dummyInput) throws InvocationTargetException {
		Parameters parameters = new Parameters();
		Patient patient = find(new IdDt(counter.intValue() - 1));
		parameters.addParameter().setName("return").setResource(patient)
				.setValue(new StringDt((counter - 1) + "" + "inputVariable [ " + dummyInput.getValue() + "]"));
		return parameters;
	}

	@Override
	public Class<Patient> getResourceType() {
		return Patient.class;
	}

	/** THE DEFAULTS */

	@Override
	public List<IServerInterceptor> getInterceptors() {
		return Collections.emptyList();
	}

	@Override
	public ETagSupportEnum getETagSupport() {
		return ETagSupportEnum.DISABLED;
	}

	@Override
	public boolean isDefaultPrettyPrint() {
		return true;
	}

	@Override
	public AddProfileTagEnum getAddProfileTag() {
		return AddProfileTagEnum.NEVER;
	}

	@Override
	public boolean isUseBrowserFriendlyContentTypes() {
		return true;
	}

	@Override
	public IPagingProvider getPagingProvider() {
		return new FifoMemoryPagingProvider(10);
	}

	@Override
	public BundleInclusionRule getBundleInclusionRule() {
		return BundleInclusionRule.BASED_ON_INCLUDES;
	}

}
