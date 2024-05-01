package ca.uhn.fhir.jpa.dao.data;

import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.fhir.jpa.entity.Batch2WorkChunkMetadataView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface IBatch2WorkChunkMetadataViewRepository extends JpaRepository<Batch2WorkChunkMetadataView, String> {

	@Query("SELECT v FROM Batch2WorkChunkMetadataView v WHERE v.myInstanceId = :instanceId AND v.myStatus IN :states "
			+ " ORDER BY v.myInstanceId, v.myTargetStepId, v.myStatus, v.mySequence, v.myId ASC")
	Page<Batch2WorkChunkMetadataView> fetchWorkChunkMetadataForJobInStates(
			Pageable thePageRequest,
			@Param("instanceId") String theInstanceId,
			@Param("states") Collection<WorkChunkStatusEnum> theStates);
}
