package ca.uhn.fhir.jpa.dao.data;

import ca.uhn.fhir.jpa.model.entity.BinaryStorageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IBinaryStorageEntityDao extends JpaRepository<BinaryStorageEntity, String> {

	@Modifying
	@Query("UPDATE BinaryStorageEntity e SET e.mySize = :blob_size WHERE e.myBlobId = :blob_id")
	void setSize(@Param("blob_id") String theId, @Param("blob_size") int theBytes);

	@Modifying
	@Query("UPDATE BinaryStorageEntity e SET e.myHash = :blob_hash WHERE e.myBlobId = :blob_id")
	void setHash(@Param("blob_id") String theId, @Param("blob_hash") String theHash);

	@Query("SELECT e FROM BinaryStorageEntity e WHERE e.myBlobId = :blob_id AND e.myResourceId = :resource_id")
	Optional<BinaryStorageEntity> findByIdAndResourceId(@Param("blob_id") String theBlobId, @Param("resource_id") String theResourceId);
}
