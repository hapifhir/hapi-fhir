package ca.uhn.fhir.jpa.model.util;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class SearchParamHashUtilTest {

	private final PartitionSettings myPartitionSettings = new PartitionSettings();

	@BeforeEach
	void setUp() {
		myPartitionSettings.setPartitioningEnabled(false);
	}

	@Test
	public void hashSearchParam_withPartitionDisabled_generatesCorrectHashIdentity() {
		Long hashIdentity = SearchParamHash.hashSearchParam(myPartitionSettings, null, "Patient", "name");
		// Make sure hashing function gives consistent results
		assertEquals(-1575415002568401616L, hashIdentity);
	}

	@Test
	public void hashSearchParam_withPartitionDisabledAndNullValue_generatesCorrectHashIdentity() {
		Long hashIdentity = SearchParamHash.hashSearchParam(myPartitionSettings, null, "Patient", "name", null);
		// Make sure hashing function gives consistent results
		assertEquals(-440750991942222070L, hashIdentity);
	}

	@Test
	public void hashSearchParam_withIncludePartitionInSearchHashesAndNullRequestPartitionId_doesNotThrowException() {
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setIncludePartitionInSearchHashes(true);

		Long hashIdentity = SearchParamHash.hashSearchParam(myPartitionSettings, null, "Patient", "name");
		assertEquals(-1575415002568401616L, hashIdentity);
	}

	@Test
	public void hashSearchParam_withIncludePartitionInSearchHashesAndRequestPartitionId_includesPartitionIdInHash() {
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setIncludePartitionInSearchHashes(true);
		RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionId(1);

		Long hashIdentity = SearchParamHash.hashSearchParam(myPartitionSettings, requestPartitionId, "Patient", "name");
		assertEquals(-6667609654163557704L, hashIdentity);
	}

	@Test
	public void hashSearchParam_withIncludePartitionInSearchHashesAndMultipleRequestPartitionIds_throwsException() {
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setIncludePartitionInSearchHashes(true);
		RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionIds(1, 2);

		try {
			SearchParamHash.hashSearchParam(myPartitionSettings, requestPartitionId, "Patient", "name");
			fail();
		} catch (InternalErrorException e) {
			assertEquals(Msg.code(1527) + "Can not search multiple partitions when partitions are included in search hashes", e.getMessage());
		}
	}
}
