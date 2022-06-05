package ca.uhn.fhir.jpa.api.pid;

import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

class ResourcePidListBuilderTest {

	public static final ResourcePersistentId PID_1 = new ResourcePersistentId(1L);
	public static final ResourcePersistentId PID_2 = new ResourcePersistentId(2L);
	public static final String RESOURCE_TYPE = "Patient";
	public static final TypedResourcePid TRP_1 = new TypedResourcePid(RESOURCE_TYPE, PID_1);
	public static final TypedResourcePid TRP_2 = new TypedResourcePid(RESOURCE_TYPE, PID_2);
	public static final Date END = new Date();

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

	@Test
	public void testHomogeneousSingleChunk() {
		// setup
		List<ResourcePersistentId> ids = List.of(PID_1, PID_2);
		IResourcePidList chunk = new HomogeneousResourcePidList(RESOURCE_TYPE, ids, END);
		List<IResourcePidList> chunks = List.of(chunk);

		// execute
		HomogeneousResourcePidList list = (HomogeneousResourcePidList) ResourcePidListBuilder.fromChunksAndDate(chunks, END);

		// verify
		assertTwoItems(list);
	}

	@Test
	public void testHomogeneousDoubleChunk() {
		// setup
		List<ResourcePersistentId> ids = List.of(PID_1, PID_2);
		IResourcePidList chunk = new HomogeneousResourcePidList(RESOURCE_TYPE, ids, END);
		List<IResourcePidList> chunks = List.of(chunk, chunk);

		// execute
		HomogeneousResourcePidList list = (HomogeneousResourcePidList) ResourcePidListBuilder.fromChunksAndDate(chunks, END);

		// verify
		assertTwoItems(list);
	}

	// FIXME KHS moar tests.  e.g. different lists

	private void assertTwoItems(HomogeneousResourcePidList list) {
		assertThat(list.getIds(), contains(PID_1, PID_2));
		assertFalse(list.isEmpty());
		assertThat(list.getTypedResourcePids(), contains(TRP_1, TRP_2));
		assertEquals(END, list.getLastDate());
		assertEquals(RESOURCE_TYPE, list.getResourceType());
	}
}
