package ca.uhn.fhir.jpa.dao.data;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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

	@Query("SELECT t.myId FROM SubscriptionTable t WHERE t.myStatus = :status AND t.myNextCheck <= :next_check")
	public Collection<Long> findSubscriptionsWhichNeedToBeChecked(@Param("status") String theStatus, @Param("next_check") Date theNextCheck);
}
