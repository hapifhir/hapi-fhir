package ca.uhn.fhir.jpa.partition;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

import javax.annotation.Nullable;
import java.util.List;

public interface IPartitionLookupSvc {

	/**
	 * This is mostly here for unit test purposes. Regular code is not expected to call this method directly.
	 */
	void start();

	/**
	 * @throws ResourceNotFoundException If the name is not known
	 */
	@Nullable
	PartitionEntity getPartitionByName(String theName) throws ResourceNotFoundException;

	/**
	 * @throws ResourceNotFoundException If the ID is not known
	 */
	PartitionEntity getPartitionById(Integer theId) throws ResourceNotFoundException;

	void clearCaches();

	PartitionEntity createPartition(PartitionEntity thePartition);

	PartitionEntity updatePartition(PartitionEntity thePartition);

	void deletePartition(Integer thePartitionId);

	List<PartitionEntity> listPartitions();
}
