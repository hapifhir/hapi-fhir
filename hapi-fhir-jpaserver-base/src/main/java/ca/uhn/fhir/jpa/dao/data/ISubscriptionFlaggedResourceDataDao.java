package ca.uhn.fhir.jpa.dao.data;

import org.springframework.data.repository.CrudRepository;

import ca.uhn.fhir.jpa.entity.SubscriptionFlaggedResource;

public interface ISubscriptionFlaggedResourceDataDao extends CrudRepository<SubscriptionFlaggedResource, Long> {

}
