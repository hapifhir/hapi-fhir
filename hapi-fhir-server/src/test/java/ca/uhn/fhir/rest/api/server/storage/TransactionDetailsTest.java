package ca.uhn.fhir.rest.api.server.storage;

import ca.uhn.fhir.model.primitive.IdDt;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

class TransactionDetailsTest {

	TransactionDetails myTransactionDetails = new TransactionDetails();

	@Test
	void testAddIdResolve() {
	    // given
		IIdType id = new IdDt("Patient/abc");
		TestPid pid = new TestPid(55L);
		myTransactionDetails.addResolvedResourceId(id, pid);

	    // when
		var fetchPid = myTransactionDetails.getResolvedResourceId(id);
	    // then
	    assertEquals(pid, fetchPid);
	}

	@Test
	void testReverseResolve_afterForwardResolve_yieldsId() {
		// given
		IIdType id = new IdDt("Patient/abc");
		TestPid pid = new TestPid(55L);
		myTransactionDetails.addResolvedResourceId(id, pid);

		// when
		IIdType revLookupId = myTransactionDetails.getReverseResolvedId(pid);

		// then
		assertEquals(id, revLookupId);
	}

	@Test
	void testReverseResolve_noEntry_yieldsNull() {
		// given
		TestPid pid = new TestPid(55L);

		// when
		IIdType revLookupId = myTransactionDetails.getReverseResolvedId(pid);

		// then
		assertNull(revLookupId);
	}


	static class TestPid extends BaseResourcePersistentId<Long> {
		private final long myId;

		public TestPid(long theValue) {
			super(null);
			myId = theValue;
		}

		@Override
		public Long getId() {
			return myId;
		}

		@Override
		public Integer getPartitionId() {
			return super.getPartitionId();
		}
	}

}
