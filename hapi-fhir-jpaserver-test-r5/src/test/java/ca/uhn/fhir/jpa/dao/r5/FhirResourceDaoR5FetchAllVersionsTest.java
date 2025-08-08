package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class FhirResourceDaoR5FetchAllVersionsTest extends BaseJpaR5Test {

	@Test
	public void testFetchAllVersions() {
		List<String> expected = new ArrayList<>();
		List<IResourcePersistentId> resourceIds = new ArrayList<>();
		for (int resourceIdx = 0; resourceIdx < 3; resourceIdx++) {
			Long id = createPatient(withFamily("Resource-" + resourceIdx + "-Version-1")).getIdPartAsLong();
			expected.add(id + " 1");
			resourceIds.add(JpaPid.fromId(id));
			for (int version = 2; version <= 5; version++) {
				createPatient(withId(id.toString()), withFamily("Resource-" + resourceIdx + "-Version-" + version));
				expected.add(id + " " + version);
			}
		}

		// Create some resources which should not match
		createPatient(withId("A"), withFamily("Shouldn't match version 1"));
		createPatient(withId("A"), withFamily("Shouldn't match version 2"));

		// Test
		List<String> actualStrings= runInTransaction(()-> {
				Stream<IResourcePersistentId> actual = myPatientDao.fetchAllVersionsOfResources(newSrd(), resourceIds);
				return actual
					.map(t -> t.getId() + " " + t.getVersion())
					.toList();
			});

		// Verify
		assertThat(actualStrings).containsExactlyInAnyOrderElementsOf(expected);
	}

}
