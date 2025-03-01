/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.dao.data;

import ca.uhn.fhir.jpa.entity.SubscriptionTable;
import ca.uhn.fhir.jpa.model.entity.IdAndPartitionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// TODO: We deprecated this entity in 8.0.0 and stopped writing it - Eventually delete it
@Deprecated
public interface ISubscriptionTableDao
		extends JpaRepository<SubscriptionTable, IdAndPartitionId>, IHapiFhirJpaRepository {

	@Modifying
	@Query("DELETE FROM SubscriptionTable t WHERE t.myResId = :res_id ")
	void deleteAllForSubscription(@Param("res_id") Long theSubscriptionResourceId);

	@Query("SELECT t FROM SubscriptionTable t WHERE t.myResId = :pid")
	SubscriptionTable findOneByResourcePid(@Param("pid") Long theId);
}
