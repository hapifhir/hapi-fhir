package ca.uhn.fhir.jpa.reindex;

import ca.uhn.fhir.jpa.api.pid.IResourcePidStream;
import ca.uhn.fhir.jpa.api.pid.TypedResourcePid;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(value = MethodOrderer.MethodName.class)
public class ResourceReindexSvcImplTest extends BaseJpaR4Test {

	@Autowired
	private IBatch2DaoSvc mySvc;

	@Test
	public void testFetchResourceIdsPage_NoUrl_WithData() {

		// Setup

		createPatient(withActiveFalse());
		sleepUntilTimeChanges();

		Date start = new Date();

		Long id0 = createPatient(withActiveFalse()).getIdPartAsLong();
		sleepUntilTimeChanges();
		Long id1 = createPatient(withActiveFalse()).getIdPartAsLong();
		sleepUntilTimeChanges();
		Date beforeLastInRange = new Date();
		sleepUntilTimeChanges();
		Long id2 = createObservation(withObservationCode("http://foo", "bar")).getIdPartAsLong();
		sleepUntilTimeChanges();

		Date end = new Date();

		sleepUntilTimeChanges();

		createPatient(withActiveFalse());

		// Execute

		myCaptureQueriesListener.clear();
		IResourcePidStream queryStream = mySvc.fetchResourceIdStream(start, end, null, null);

		// Verify
		List<TypedResourcePid> typedPids = queryStream.visitStream(Stream::toList);
		assertEquals(3, typedPids.size());
		assertThat(typedPids).containsExactly(new TypedResourcePid("Patient", id0), new TypedResourcePid("Patient", id1), new TypedResourcePid("Observation", id2));

		assertEquals(1, myCaptureQueriesListener.logSelectQueries().size());
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(1, myCaptureQueriesListener.getCommitCount());
		assertEquals(0, myCaptureQueriesListener.getRollbackCount());

	}


	@Test
	public void testFetchResourceIdsPage_NoUrl_NoData() {

		// Setup

		Date start = new Date();
		Date end = new Date();

		// Execute

		myCaptureQueriesListener.clear();
		IResourcePidStream queryStream = mySvc.fetchResourceIdStream(start, end, null, null);

		// Verify
		List<TypedResourcePid> typedPids = queryStream.visitStream(Stream::toList);

		assertTrue(typedPids.isEmpty());

		assertEquals(1, myCaptureQueriesListener.logSelectQueries().size());
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(1, myCaptureQueriesListener.getCommitCount());
		assertEquals(0, myCaptureQueriesListener.getRollbackCount());

	}


	@Test
	public void testFetchResourceIdsPage_WithUrl_WithData() {

		// Setup

		final Long patientId0 = createPatient(withActiveFalse()).getIdPartAsLong();
		sleepUntilTimeChanges();

		// Start of resources within range
		Date start = new Date();
		sleepUntilTimeChanges();
		Long patientId1 = createPatient(withActiveFalse()).getIdPartAsLong();
		createObservation(withObservationCode("http://foo", "bar"));
		createObservation(withObservationCode("http://foo", "bar"));
		sleepUntilTimeChanges();
		Date beforeLastInRange = new Date();
		sleepUntilTimeChanges();
		Long patientId2 = createPatient(withActiveFalse()).getIdPartAsLong();
		sleepUntilTimeChanges();
		Date end = new Date();
		sleepUntilTimeChanges();
		// End of resources within range

		createObservation(withObservationCode("http://foo", "bar"));
		final Long patientId3 = createPatient(withActiveFalse()).getIdPartAsLong();
		sleepUntilTimeChanges();

		// Execute

		myCaptureQueriesListener.clear();
		IResourcePidStream queryStream = mySvc.fetchResourceIdStream(start, end, null, "Patient?active=false");

		// Verify
		List<TypedResourcePid> typedResourcePids = queryStream.visitStream(Stream::toList);

		assertEquals(2, typedResourcePids.size());
		assertThat(typedResourcePids).containsExactly(new TypedResourcePid("Patient", patientId1), new TypedResourcePid("Patient", patientId2));

		assertEquals(1, myCaptureQueriesListener.logSelectQueries().size());
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(1, myCaptureQueriesListener.getCommitCount());
		assertEquals(0, myCaptureQueriesListener.getRollbackCount());

	}

}
