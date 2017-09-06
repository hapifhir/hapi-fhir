package ca.uhn.fhir.jpa.dao.data;

import ca.uhn.fhir.jpa.entity.ResourceIndexedCompositeStringUnique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

public interface IResourceIndexedCompositeStringUniqueDao extends JpaRepository<ResourceIndexedCompositeStringUnique, Long> {

	@Query("SELECT r FROM ResourceIndexedCompositeStringUnique r WHERE r.myIndexString = :str")
	ResourceIndexedCompositeStringUnique findByQueryString(@Param("str") String theQueryString);

	@Query("SELECT r.myResourceId FROM ResourceIndexedCompositeStringUnique r WHERE r.myIndexString IN :str")
	Collection<Long> findResourcePidsByQueryStrings(@Param("str") Collection<String> theQueryString);

}
