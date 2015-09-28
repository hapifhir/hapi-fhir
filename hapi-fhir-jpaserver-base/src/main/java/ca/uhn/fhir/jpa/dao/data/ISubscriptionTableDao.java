package ca.uhn.fhir.jpa.dao.data;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ca.uhn.fhir.jpa.entity.SubscriptionTable;

public interface ISubscriptionTableDao extends JpaRepository<SubscriptionTable, Long> {

	@Query("SELECT t FROM SubscriptionTable t WHERE t.myResId = :pid")
   public SubscriptionTable findOneByResourcePid(@Param("pid") Long theId);

	@Modifying
	@Query("DELETE FROM SubscriptionTable t WHERE t.myId = :id ")
	public void deleteAllForSubscription(@Param("id") Long theSubscriptionId);

	@Modifying
	@Query("UPDATE SubscriptionTable t SET t.myLastClientPoll = :last_client_poll")
	public int updateLastClientPoll(@Param("last_client_poll") Date theLastClientPoll);

	@Query("SELECT t FROM SubscriptionTable t WHERE t.myLastClientPoll < :cutoff OR (t.myLastClientPoll IS NULL AND t.myCreated < :cutoff)")
	public Collection<SubscriptionTable> findInactiveBeforeCutoff(@Param("cutoff") Date theCutoff);

}
