package ca.uhn.fhir.jpa.api.pid;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class ResourcePidListBuilderTest {

	@Test
	public void testEmpty() {
		// setup
		List<IResourcePidList> chunks = new ArrayList<>();
		Date end = null;

		// execute
		EmptyResourcePidList emptyList = (EmptyResourcePidList) ResourcePidListBuilder.fromChunksAndDate(chunks, end);

		// verify
		assertEquals(0, emptyList.size());
		assertTrue(emptyList.isEmpty());
		assertThat(emptyList.getIds(), hasSize(0));
		assertThat(emptyList.getTypedResourcePids(), hasSize(0));
		assertNull(emptyList.getLastDate());
		try {
			emptyList.getResourceType(0);
			fail();
		} catch (ArrayIndexOutOfBoundsException e) {
			assertEquals("Attempting to get resource type from an empty resource pid list", e.getMessage());
			// expected exception
		}
	}
}
