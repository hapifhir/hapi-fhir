package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.HasParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static ca.uhn.fhir.jpa.model.config.PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PartitioningAllowedUnqualifiedR4Test extends BasePartitioningR4Test {
	@BeforeEach
	public void beforeEach() {
		myPartitionSettings.setAllowReferencesAcrossPartitions(ALLOWED_UNQUALIFIED);
	}

	@AfterEach
	@Override
	public void after() {
		PartitionSettings defaultPartitionSettings = new PartitionSettings();
		myPartitionSettings.setIncludePartitionInSearchHashes(defaultPartitionSettings.isIncludePartitionInSearchHashes());
		myPartitionSettings.setPartitioningEnabled(defaultPartitionSettings.isPartitioningEnabled());
		myPartitionSettings.setAllowReferencesAcrossPartitions(defaultPartitionSettings.getAllowReferencesAcrossPartitions());
		myPartitionSettings.setDefaultPartitionId(defaultPartitionSettings.getDefaultPartitionId());

		mySrdInterceptorService.unregisterInterceptorsIf(MyReadWriteInterceptor.class::isInstance);

	}

	@Test
	public void testCreate_differentForcedIdInDifferentPartition_created() {
		// Set up
		addNextTargetPartitionForCreateWithId(myPartitionId, myPartitionDate);
		Patient p1 = createPatient("Patient/P1");
		addNextTargetPartitionForCreateWithId(myPartitionId2, myPartitionDate2);
		Patient p2 = createPatient("Patient/P2");

		// Execute/Verify
		runInTransaction(() -> {
			IIdType p1Id = myPatientDao.update(p1, mySrd).getId();
			assertNotNull(p1Id);
			assertThat(p1Id.getIdPart()).isEqualTo("P1");

			IIdType p2Id = myPatientDao.update(p2, mySrd).getId();
			assertNotNull(p2Id);
			assertThat(p2Id.getIdPart()).isEqualTo("P2");

			// Verify that the resources are created in the correct partition
			ResourceTable entity1 = myResourceTableDao.findByTypeAndFhirId(p1Id.getResourceType(), p1Id.getIdPart())
				.orElseThrow(IllegalArgumentException::new);
			ResourceTable entity2 = myResourceTableDao.findByTypeAndFhirId(p1Id.getResourceType(), p2Id.getIdPart())
				.orElseThrow(IllegalArgumentException::new);

			assertThat(entity1.getPartitionId().getPartitionId()).isEqualTo(myPartitionId);
			assertThat(entity2.getPartitionId().getPartitionId()).isEqualTo(myPartitionId2);
		});
	}

	@Test
	public void testCreate_reuseForcedIdInDifferentPartition_throwException() {
		// Set up
		addNextTargetPartitionForCreateWithId(myPartitionId, myPartitionDate);
		Patient p1 = createPatient("Patient/P");
		IIdType idType = myPatientDao.update(p1, mySrd).getId();

		assertNotNull(idType);
		assertThat(idType.getIdPart()).isEqualTo("P");

		// Create the same resource in a different partition
		addNextTargetPartitionForCreateWithId(myPartitionId2, myPartitionDate2);
		Patient p2 = createPatient("Patient/P");

		// Execute
		ResourceVersionConflictException exception = assertThrows(
			ResourceVersionConflictException.class,
			() -> myPatientDao.update(p2, mySrd)
		);

		// Verify
		assertThat(exception.getMessage()).contains("client-assigned ID constraint failure");
	}

	/**
	 * Patient?_has:Observation:subject:device.identifier=1234-5
	 * is a chain on "Observation.device" which is a Reference(Device,DeviceMetric). So we need
	 * to make sure we're using the correct partitions for those resource types.
	 */
	@Test
	void testHasQuery_Chained() {
		// Setup
		myPartitionInterceptor.addTypeToPartitionId("Patient", RequestPartitionId.fromPartitionId(1));
		myPartitionInterceptor.addTypeToPartitionId("Observation", RequestPartitionId.fromPartitionId(2));
		myPartitionInterceptor.addTypeToPartitionId("Device", RequestPartitionId.fromPartitionId(3));
		myPartitionInterceptor.addTypeToPartitionId("DeviceMetric", RequestPartitionId.fromPartitionId(3));

		createDevice(withId("D"), withIdentifier("http://foo", "1234-5"));
		createPatient(withId("A"), withActiveTrue());
		createObservation(withId("B"), withSubject("Patient/A"), withReference("device", "Device/D"));

		// Test
		myCaptureQueriesListener.clear();
		SearchParameterMap params = SearchParameterMap
			.newSynchronous()
			.add(Constants.PARAM_HAS, new HasParam("Observation", "subject", "device.identifier", "1234-5"));
		IBundleProvider results = myPatientDao.search(params, mySrd);

		// Verify
		List<String> actual = toUnqualifiedVersionlessIdValues(results);
		myCaptureQueriesListener.logSelectQueries();
		assertThat(actual).containsExactly("Patient/A");
	}

	/**
	 * Patient?_has:Observation:subject:device.location.identifier=1234-5
	 * is a chain on "Observation.device" which is a Reference(Device,DeviceMetric), followed
	 * by a chain on "Device.location". So we need
	 * to make sure we're using the correct partitions for those resource types even
		 * if each step of the way is in a different partition.
	 */
	@Test
	void testHasQuery_DoubleChained() {
		// Setup
		myPartitionInterceptor.addTypeToPartitionId("Patient", RequestPartitionId.fromPartitionId(1));
		myPartitionInterceptor.addTypeToPartitionId("Observation", RequestPartitionId.fromPartitionId(2));
		myPartitionInterceptor.addTypeToPartitionId("Device", RequestPartitionId.fromPartitionId(3));
		myPartitionInterceptor.addTypeToPartitionId("Location", RequestPartitionId.fromPartitionId(4));

		createLocation(withId("L"), withIdentifier("http://foo", "1234-5"));
		createDevice(withId("D"), withReference("location", "Location/L"));
		createPatient(withId("A"), withActiveTrue());
		createObservation(withId("B"), withSubject("Patient/A"), withReference("device", "Device/D"));

		// Test
		myCaptureQueriesListener.clear();
		SearchParameterMap params = SearchParameterMap
			.newSynchronous()
			.add(Constants.PARAM_HAS, new HasParam("Observation", "subject", "device.location.identifier", "1234-5"));
		IBundleProvider results = myPatientDao.search(params, mySrd);

		// Verify
		List<String> actual = toUnqualifiedVersionlessIdValues(results);
		myCaptureQueriesListener.logSelectQueries();
		assertThat(actual).containsExactly("Patient/A");
	}

	/**
	 * The query Patient?_has:Observation:subject:focus.identifier=1234-5
	 * is a chain on "Observation.focus" which has a Reference(any). Make sure
	 * we can still resolve that even though we don't know what type the target
	 * would be.
	 */
	@Test
	void testHasQuery_ChainedToUntypedTarget() {
		// Setup
		myPartitionInterceptor.addTypeToPartitionId("Patient", RequestPartitionId.fromPartitionId(1));
		myPartitionInterceptor.addTypeToPartitionId("Observation", RequestPartitionId.fromPartitionId(2));
		myPartitionInterceptor.addTypeToPartitionId("Device", RequestPartitionId.fromPartitionId(3));

		createDevice(withId("D"), withIdentifier("http://foo", "1234-5"));
		createPatient(withId("A"), withActiveTrue());
		createObservation(withId("B"), withSubject("Patient/A"), withReference("focus", "Device/D"));

		// Test
		myCaptureQueriesListener.clear();
		SearchParameterMap params = SearchParameterMap
			.newSynchronous()
			.add(Constants.PARAM_HAS, new HasParam("Observation", "subject", "focus.identifier", "1234-5"));
		IBundleProvider results = myPatientDao.search(params, mySrd);

		// Verify
		List<String> actual = toUnqualifiedVersionlessIdValues(results);
		myCaptureQueriesListener.logSelectQueries();
		assertThat(actual).containsExactly("Patient/A");
	}


	@Test
	void testSearch_UnqualifiedTarget() {
		// Setup

		addNextTargetPartitionForReadAllPartitions();
		addNextTargetPartitionForReadAllPartitions();

		// Test / Verify

		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.add(AuditEvent.SP_ENTITY, new ReferenceParam("A"));

		assertThatThrownBy(()->myAuditEventDao.search(map, mySrd))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("Parameter \"entity\" must be in the format [resourceType]/[id]");
	}


	private Patient createPatient(String theId) {
		Patient p = new Patient();
		p.setId(theId);
		p.setActive(true);
		return p;
	}
}
