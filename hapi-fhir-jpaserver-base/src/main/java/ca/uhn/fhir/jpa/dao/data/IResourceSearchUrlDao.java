package ca.uhn.fhir.jpa.dao.data;


import ca.uhn.fhir.jpa.model.entity.ResourceSearchUrlEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface IResourceSearchUrlDao extends JpaRepository<ResourceSearchUrlEntity, Long>, IHapiFhirJpaRepository{

	@Query("SELECT s.myId FROM ResourceSearchUrlEntity s WHERE (s.myCreatedTime < :cutoff)")
	Slice<Long> findWhereCreatedBefore(@Param("cutoff") Date theCutoff, Pageable thePage);

}
