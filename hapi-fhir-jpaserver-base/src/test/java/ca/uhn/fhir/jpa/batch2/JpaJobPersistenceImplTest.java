package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceParameter;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.dao.data.IBatch2WorkChunkRepository;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import com.github.jsonldjava.shaded.com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JpaJobPersistenceImplTest extends BaseJpaR4Test {

	@Autowired
	private IJobPersistence mySvc;
	@Autowired
	private IBatch2WorkChunkRepository myWorkChunkRepository;

	@Test
	public void testStoreAndFetchInstance() {
		JobInstance instance = createInstance();
		mySvc.storeNewInstance(instance);

		JobInstance foundInstance = mySvc.fetchInstance("instance-id").orElseThrow(() -> new IllegalStateException());
		assertEquals("instance-id", foundInstance.getInstanceId());
		assertEquals("definition-id", foundInstance.getJobDefinitionId());
		assertEquals(1, foundInstance.getJobDefinitionVersion());
		assertEquals(StatusEnum.QUEUED, foundInstance.getStatus());
		assertThat(foundInstance.getParameters(), contains(
			new JobInstanceParameter().setName("foo").setValue("bar"),
			new JobInstanceParameter().setName("foo").setValue("baz")
		));
	}

	@Nonnull
	private JobInstance createInstance() {
		JobInstance instance = new JobInstance();
		instance.setInstanceId("instance-id");
		instance.setJobDefinitionId("definition-id");
		instance.setStatus(StatusEnum.QUEUED);
		instance.setJobDefinitionVersion(1);
		instance.addParameter(new JobInstanceParameter().setName("foo").setValue("bar"));
		instance.addParameter(new JobInstanceParameter().setName("foo").setValue("baz"));
		return instance;
	}

	@Test
	public void testFetchInstanceAndMarkInProgress() {
		JobInstance instance = createInstance();
		mySvc.storeNewInstance(instance);

		JobInstance foundInstance = mySvc.fetchInstanceAndMarkInProgress("instance-id").orElseThrow(() -> new IllegalStateException());
		assertEquals("instance-id", foundInstance.getInstanceId());
		assertEquals("definition-id", foundInstance.getJobDefinitionId());
		assertEquals(1, foundInstance.getJobDefinitionVersion());
		assertEquals(StatusEnum.IN_PROGRESS, foundInstance.getStatus());
		assertThat(foundInstance.getParameters(), contains(
			new JobInstanceParameter().setName("foo").setValue("bar"),
			new JobInstanceParameter().setName("foo").setValue("baz")
		));
	}

	@Test
	public void testStoreAndFetchWorkChunk() {
		JobInstance instance = createInstance();
		mySvc.storeNewInstance(instance);

		Map<String, Object> data = new HashMap<>();
		data.put("string-key", "string-val");
		data.put("list-key", Lists.newArrayList("list-val-1", "list-val-2"));
		data.put("object-key", Collections.singletonMap("object-val-key", "object-val-val"));

		String id = mySvc.storeWorkChunk("definition-id", 1, "step-id", "instance-id", data);
		assertNotNull(id);
		runInTransaction(() -> assertEquals(StatusEnum.QUEUED, myWorkChunkRepository.findById(id).orElseThrow(() -> new IllegalArgumentException()).getStatus()));

		WorkChunk chunk = mySvc.fetchWorkChunkAndMarkInProgress(id).orElseThrow(() -> new IllegalArgumentException());
		assertEquals("instance-id", chunk.getInstanceId());
		assertEquals("definition-id", chunk.getJobDefinitionId());
		assertEquals(1, chunk.getJobDefinitionVersion());
		assertEquals(StatusEnum.IN_PROGRESS, chunk.getStatus());

		runInTransaction(() -> assertEquals(StatusEnum.IN_PROGRESS, myWorkChunkRepository.findById(id).orElseThrow(() -> new IllegalArgumentException()).getStatus()));
	}

	@Test
	public void testFetchUnknownWork() {
		assertFalse(myWorkChunkRepository.findById("FOO").isPresent());
	}

}
