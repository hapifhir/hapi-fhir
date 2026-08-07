package ca.uhn.fhir.mdm.batch2;

import ca.uhn.fhir.batch2.jobs.chunk.ChunkRangeJson;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.svc.IGoldenResourceSearchSvc;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.mockito.Mockito.verify;

// Created by claude-opus-4-6
@ExtendWith(MockitoExtension.class)
class MdmIdChunkProducerTest {

	@Mock
	private IGoldenResourceSearchSvc myGoldenResourceSearchSvc;

	@Test
	void fetchResourceIdStream_extractsResourceTypeFromUrl() {
		// setup
		MdmIdChunkProducer producer = new MdmIdChunkProducer(myGoldenResourceSearchSvc);
		Date start = new Date();
		Date end = new Date();
		RequestPartitionId partitionId = RequestPartitionId.fromPartitionId(1);

		ChunkRangeJson chunkRange = new ChunkRangeJson(start, end)
				.setUrl("Patient?")
				.setPartitionId(partitionId);

		// execute
		producer.fetchResourceIdStream(chunkRange);

		// verify — resource type extracted from URL, not from getResourceType()
		verify(myGoldenResourceSearchSvc).fetchGoldenResourceIdStream(start, end, partitionId, "Patient");
	}

	@Test
	void fetchResourceIdStream_extractsResourceTypeFromUrlWithParams() {
		// setup
		MdmIdChunkProducer producer = new MdmIdChunkProducer(myGoldenResourceSearchSvc);
		Date start = new Date();
		Date end = new Date();
		RequestPartitionId partitionId = RequestPartitionId.fromPartitionId(2);

		ChunkRangeJson chunkRange = new ChunkRangeJson(start, end)
				.setUrl("Practitioner?active=true")
				.setPartitionId(partitionId);

		// execute
		producer.fetchResourceIdStream(chunkRange);

		// verify
		verify(myGoldenResourceSearchSvc).fetchGoldenResourceIdStream(start, end, partitionId, "Practitioner");
	}

	@Test
	void fetchResourceIdStream_v1BackwardCompat_usesResourceTypeDirectly() {
		// setup — v1 jobs set resourceType directly, not url
		MdmIdChunkProducer producer = new MdmIdChunkProducer(myGoldenResourceSearchSvc);
		Date start = new Date();
		Date end = new Date();
		RequestPartitionId partitionId = RequestPartitionId.fromPartitionId(3);

		ChunkRangeJson chunkRange = new ChunkRangeJson(start, end)
				.setResourceType("Patient")
				.setPartitionId(partitionId);

		// execute
		producer.fetchResourceIdStream(chunkRange);

		// verify — falls back to getResourceType() when url is blank
		verify(myGoldenResourceSearchSvc).fetchGoldenResourceIdStream(start, end, partitionId, "Patient");
	}
}
