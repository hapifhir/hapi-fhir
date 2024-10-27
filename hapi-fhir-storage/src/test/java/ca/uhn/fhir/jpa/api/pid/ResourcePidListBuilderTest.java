package ca.uhn.fhir.jpa.api.pid;

import ca.uhn.fhir.jpa.model.dao.JpaPid;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class ResourcePidListBuilderTest {

	public static final JpaPid PID_1 = JpaPid.fromId(1L);
	public static final JpaPid PID_2 = JpaPid.fromId(2L);
	public static final JpaPid PID_3 = JpaPid.fromId(3L);
	public static final JpaPid PID_4 = JpaPid.fromId(4L);
	public static final JpaPid PID_5 = JpaPid.fromId(5L);
	public static final JpaPid PID_6 = JpaPid.fromId(6L);
	public static final String RESOURCE_TYPE = "Patient";
	public static final String OTHER_RESOURCE_TYPE = "Observation";
	public static final TypedResourcePid TRP_1 = new TypedResourcePid(RESOURCE_TYPE, PID_1);
	public static final TypedResourcePid TRP_2 = new TypedResourcePid(RESOURCE_TYPE, PID_2);
	public static final TypedResourcePid TRP_3 = new TypedResourcePid(RESOURCE_TYPE, PID_3);
	public static final TypedResourcePid TRP_4 = new TypedResourcePid(RESOURCE_TYPE, PID_4);
	public static final TypedResourcePid TRP_5 = new TypedResourcePid(OTHER_RESOURCE_TYPE, PID_5);
	public static final TypedResourcePid TRP_6 = new TypedResourcePid(OTHER_RESOURCE_TYPE, PID_6);
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
		assertThat(emptyList.getIds()).hasSize(0);
		assertThat(emptyList.getTypedResourcePids()).hasSize(0);
		assertNull(emptyList.getLastDate());
		try {
			emptyList.getResourceType(0);
			fail();
		} catch (ArrayIndexOutOfBoundsException e) {
			assertEquals("HAPI-2095: Attempting to get resource type from an empty resource pid list", e.getMessage());
			// expected exception
		}
	}

	@Test
	public void testHomogeneousSingleChunk() {
		// setup
		IResourcePidList chunk = new HomogeneousResourcePidList(RESOURCE_TYPE, List.of(PID_1, PID_2), END, null);
		List<IResourcePidList> chunks = List.of(chunk);

		// execute
		HomogeneousResourcePidList list = (HomogeneousResourcePidList) ResourcePidListBuilder.fromChunksAndDate(chunks, END);

		// verify
		assertTwoItems(list);
	}

	@Test
	public void testHomogeneousDoubleChunk() {
		// setup
		IResourcePidList chunk = new HomogeneousResourcePidList(RESOURCE_TYPE, List.of(PID_1, PID_2), END, null);
		List<IResourcePidList> chunks = List.of(chunk, chunk);

		// execute
		HomogeneousResourcePidList list = (HomogeneousResourcePidList) ResourcePidListBuilder.fromChunksAndDate(chunks, END);

		// verify
		assertTwoItems(list);
	}

	@Test
	public void testHomogeneousDoubleChunkDift() {
		// setup
		IResourcePidList chunk1 = new HomogeneousResourcePidList(RESOURCE_TYPE, List.of(PID_1, PID_2), END, null);

		IResourcePidList chunk2 = new HomogeneousResourcePidList(RESOURCE_TYPE, List.of(PID_3, PID_4), END, null);

		List<IResourcePidList> chunks = List.of(chunk1, chunk2);

		// execute
		HomogeneousResourcePidList list = (HomogeneousResourcePidList) ResourcePidListBuilder.fromChunksAndDate(chunks, END);

		// verify
		assertFalse(list.isEmpty());
		assertEquals(END, list.getLastDate());
		assertEquals(RESOURCE_TYPE, list.getResourceType());
		assertThat(list.getIds()).containsExactly(PID_1, PID_2, PID_3, PID_4);
		assertThat(list.getTypedResourcePids()).containsExactly(TRP_1, TRP_2, TRP_3, TRP_4);
	}

	@Test
	public void testHomogeneousDoubleChunkDiftResourceType() {
		// setup
		IResourcePidList chunk1 = new HomogeneousResourcePidList(RESOURCE_TYPE, List.of(PID_1, PID_2), END, null);

		IResourcePidList chunk2 = new HomogeneousResourcePidList(OTHER_RESOURCE_TYPE, List.of(PID_5, PID_6), END, null);

		List<IResourcePidList> chunks = List.of(chunk1, chunk2);

		// execute
		MixedResourcePidList list = (MixedResourcePidList) ResourcePidListBuilder.fromChunksAndDate(chunks, END);

		// verify
		assertFalse(list.isEmpty());
		assertEquals(END, list.getLastDate());
		assertEquals(RESOURCE_TYPE, list.getResourceType(0));
		assertEquals(RESOURCE_TYPE, list.getResourceType(1));
		assertEquals(OTHER_RESOURCE_TYPE, list.getResourceType(2));
		assertEquals(OTHER_RESOURCE_TYPE, list.getResourceType(3));
		assertThat(list.getIds()).containsExactly(PID_1, PID_2, PID_5, PID_6);
		assertThat(list.getTypedResourcePids()).containsExactly(TRP_1, TRP_2, TRP_5, TRP_6);
	}

	@Test
	public void testMixedChunkDiftResourceType() {
		// setup
		IResourcePidList chunk = new MixedResourcePidList(List.of(RESOURCE_TYPE, OTHER_RESOURCE_TYPE), List.of(PID_1, PID_5), END, null);

		List<IResourcePidList> chunks = List.of(chunk, chunk);

		// execute
		MixedResourcePidList list = (MixedResourcePidList) ResourcePidListBuilder.fromChunksAndDate(chunks, END);

		// verify
		assertFalse(list.isEmpty());
		assertEquals(END, list.getLastDate());
		assertEquals(RESOURCE_TYPE, list.getResourceType(0));
		assertEquals(OTHER_RESOURCE_TYPE, list.getResourceType(1));
		assertThat(list.getIds()).containsExactly(PID_1, PID_5);
		assertThat(list.getTypedResourcePids()).containsExactly(TRP_1, TRP_5);
	}

	private void assertTwoItems(IResourcePidList list) {
		assertFalse(list.isEmpty());
		assertEquals(END, list.getLastDate());
		assertEquals(RESOURCE_TYPE, list.getResourceType(0));
		assertThat(list.getIds()).containsExactly(PID_1, PID_2);
		assertThat(list.getTypedResourcePids()).containsExactly(TRP_1, TRP_2);
	}


}
