package ca.uhn.fhir.jpa.dao.expunge;

import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public interface IExpungeDaoService {
	Slice<Long> findHistoricalVersionsOfDeletedResources(String theResourceName, Long theResourceId, int theI);

	Slice<Long> findHistoricalVersionsOfNonDeletedResources(String theResourceName, Long theResourceId, Long theVersion, int theI);

	void expungeHistoricalVersions(List<Long> thePartition, AtomicInteger theRemainingCount);

	void expungeCurrentVersionOfResources(List<Long> thePartition, AtomicInteger theRemainingCount);

	void expungeHistoricalVersionsOfIds(List<Long> thePartition, AtomicInteger theRemainingCount);

	void deleteByResourceIdPartitions(List<Long> thePartition);

	void deleteAllSearchParams(Long theResourceId);
}
