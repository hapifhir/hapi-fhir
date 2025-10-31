package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.mdm.helper.MdmLinkHelper;
import ca.uhn.fhir.jpa.mdm.helper.testmodels.MDMLinkResults;
import ca.uhn.fhir.jpa.mdm.helper.testmodels.MDMState;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.mdm.svc.BulkExportMdmEidMatchOnlyResourceExpander;
import ca.uhn.fhir.mdm.svc.MdmEidMatchOnlyExpandSvc;
import ca.uhn.fhir.mdm.util.EIDHelper;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BulkExportMdmEidMatchOnlyResourceExpanderIT extends BaseMdmR4Test {

	@Autowired
	private BulkExportMdmEidMatchOnlyResourceExpander myResourceExpander;

	@Autowired
	private IHapiTransactionService myHapiTransactionService;

	@Autowired
	private EIDHelper myEIDHelper;

	@Autowired
	private MdmEidMatchOnlyExpandSvc myMdmEidMatchOnlyExpandSvc;

	@Autowired
	private MdmLinkHelper myLinkHelper;

	@BeforeEach
	public void before() throws Exception {
		super.before();

		myMdmEidMatchOnlyExpandSvc.setMyEidHelper(myEIDHelper);
	}

	@Test
	public void expandPatients_bulkExport_returnsLinkedIds() {
		// setup
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		String eidValue = "eid-value";
		String patientEidSystem = myMdmSettings.getMdmRules()
			.getEnterpriseEIDSystemForResourceType("Patient");

		Map<String, Patient> mapping = new HashMap<>();
		for (String id : new String[] { "P1", "P2", "P3", "GP1" }) {
			Patient patient = new Patient();
			patient.setActive(true);
			patient.addIdentifier()
				.setSystem(patientEidSystem)
				.setValue(eidValue);
			patient.addName()
				.setFamily("Simpson");
			patient.setId(new IdType("Patient/" + id));

			MdmResourceUtil.setMdmManaged(patient);
			if (id.equalsIgnoreCase("GP1")) {
				MdmResourceUtil.setGoldenResource(patient);
			}

			Patient createdPatient = (Patient) myPatientDao.create(patient, requestDetails)
				.getResource();

			mapping.put(id, patient);
		}

		// Just for setup; we won't be doing any link changing here
		String inputState = """
   			GP1, AUTO, MATCH, P1
   			GP1, AUTO, MATCH, P2
   			GP1, AUTO, MATCH, P3
			""";
		MDMState<Patient, JpaPid> state = new MDMState<>();
		state.setInputState(inputState);
		state.setParameterToValue(mapping);
		MDMLinkResults createdState = myLinkHelper.setup(state);
		myLinkHelper.logMdmLinks();

		MdmLink firstLink = createdState.getResults().get(0);

		// test
		Set<JpaPid> pids = withTransaction(() -> {
			return myResourceExpander.expandPatients(List.of(new IdType("Patient/P1")), RequestPartitionId.allPartitions());
		});

		assertNotNull(pids);
		assertEquals(4, pids.size());
	}

	@Test
	public void expandMdmBySourceResourceIdsForSingleResourceType_largeNumber_works() {
		// setup
		int count = 10000;
		RequestPartitionId requestPartitionId = RequestPartitionId.allPartitions();
		String eidValue = "eid-value";
		String patientEidSystem = myMdmSettings.getMdmRules()
			.getEnterpriseEIDSystemForResourceType("Patient");
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		DaoMethodOutcome outcome;

		IIdType id = null;
		for (int i = 0; i < count; i++) {
			Patient patient = new Patient();
			patient.setId("Patient/pat-" + i);
			patient.setActive(true);
			patient.addName()
					.setFamily("Simpsons");
			patient.addIdentifier()
				.setSystem(patientEidSystem)
				.setValue(eidValue);

			outcome = myPatientDao.update(patient, requestDetails);
			if (id == null) {
				id = outcome.getId();
			}
		}

		// test
		Set<String> expanded = myMdmEidMatchOnlyExpandSvc.expandMdmBySourceResourceIdsForSingleResourceType(requestPartitionId,
			Set.of(id));

		// validate
		assertEquals(count, expanded.size());
	}

	private <T> T withTransaction(Callable<T> theCallable) {
		return myHapiTransactionService
			.withSystemRequest()
			.withRequestPartitionId(RequestPartitionId.allPartitions())
			.readOnly()
			.execute(theCallable);
	}
}
