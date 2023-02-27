package ca.uhn.fhir.jpa.dao.data;


import ca.uhn.fhir.jpa.model.entity.ResourceSearchUrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IResourceSearchUrlDao extends JpaRepository<ResourceSearchUrlEntity, Long>, IHapiFhirJpaRepository{
}
