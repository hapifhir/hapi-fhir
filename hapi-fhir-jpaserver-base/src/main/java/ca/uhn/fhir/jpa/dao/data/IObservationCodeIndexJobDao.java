package ca.uhn.fhir.jpa.dao.data;

import ca.uhn.fhir.jpa.entity.ObservationCodeIndexJobEntity;
import ca.uhn.fhir.jpa.entity.ObservationCodeIndexJobEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

public interface IObservationCodeIndexJobDao extends JpaRepository<ObservationCodeIndexJobEntity, Long> {

	@Modifying
	@Query("UPDATE ObservationCodeIndexJobEntity j SET j.myDeleted = true WHERE j.myResourceType = :type")
	void markAllOfTypeAsDeleted(@Param("type") String theType);

	@Modifying
	@Query("UPDATE ObservationCodeIndexJobEntity j SET j.myDeleted = true")
	void markAllOfTypeAsDeleted();

	@Modifying
	@Query("UPDATE ObservationCodeIndexJobEntity j SET j.myDeleted = true WHERE j.myId = :pid")
	void markAsDeletedById(@Param("pid") Long theId);

	@Query("SELECT j FROM ObservationCodeIndexJobEntity j WHERE j.myDeleted = :deleted")
	List<ObservationCodeIndexJobEntity> findAll(Pageable thePage, @Param("deleted") boolean theDeleted);

	@Modifying
	@Query("UPDATE ObservationCodeIndexJobEntity j SET j.mySuspendedUntil = :suspendedUntil")
	void setSuspendedUntil(@Param("suspendedUntil") Date theSuspendedUntil);

	@Modifying
	@Query("UPDATE ObservationCodeIndexJobEntity j SET j.myThresholdLow = :low WHERE j.myId = :id")
	void setThresholdLow(@Param("id") Long theId, @Param("low") Date theLow);

	@Query("SELECT j.myIndexCount FROM ObservationCodeIndexJobEntity j WHERE j.myId = :id")
	Optional<Integer> getIndexCount(@Param("id") Long theId);

	@Query("UPDATE ObservationCodeIndexJobEntity j SET j.myIndexCount = :newCount WHERE j.myId = :id")
	@Modifying
	void setIndexCount(@Param("id") Long theId, @Param("newCount") int theNewCount);

}
