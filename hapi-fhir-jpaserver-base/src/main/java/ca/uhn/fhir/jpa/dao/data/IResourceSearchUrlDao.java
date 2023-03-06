package ca.uhn.fhir.jpa.dao.data;


import ca.uhn.fhir.jpa.model.entity.ResourceSearchUrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface IResourceSearchUrlDao extends JpaRepository<ResourceSearchUrlEntity, Long>, IHapiFhirJpaRepository{

	@Modifying
	@Query("DELETE FROM ResourceSearchUrlEntity s WHERE (s.myCreatedTime < :cutoff)")
	int deleteAllWhereCreatedBefore(@Param("cutoff") Date theCutoff);

	@Modifying
	@Query("DELETE FROM ResourceSearchUrlEntity s WHERE (s.myResourcePid = :resID)")
	int deleteByResId(@Param("resID") long resId);

}
