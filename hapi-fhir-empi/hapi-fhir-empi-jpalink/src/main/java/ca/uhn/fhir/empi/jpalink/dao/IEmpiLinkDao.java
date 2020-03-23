package ca.uhn.fhir.empi.jpalink.dao;

import ca.uhn.fhir.empi.jpalink.entity.EmpiLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEmpiLinkDao extends JpaRepository<EmpiLink, Long> {
}
