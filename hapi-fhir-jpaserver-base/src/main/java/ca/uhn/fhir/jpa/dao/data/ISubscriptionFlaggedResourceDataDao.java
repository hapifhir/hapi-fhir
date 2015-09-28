package ca.uhn.fhir.jpa.dao.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ca.uhn.fhir.jpa.entity.SubscriptionFlaggedResource;

public interface ISubscriptionFlaggedResourceDataDao extends JpaRepository<SubscriptionFlaggedResource, Long> {

	@Query("SELECT r FROM SubscriptionFlaggedResource r WHERE r.mySubscription.myId = :id ORDER BY r.myId ASC")
   public Page<SubscriptionFlaggedResource> findAllBySubscriptionId(@Param("id") Long theId, Pageable thePage);

	@Modifying
	@Query("DELETE FROM SubscriptionFlaggedResource r WHERE r.mySubscription.myId = :id")
	public void deleteAllForSubscription(@Param("id") Long theSubscriptionId);

}
