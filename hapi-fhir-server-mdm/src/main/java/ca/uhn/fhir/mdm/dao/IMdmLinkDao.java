package ca.uhn.fhir.mdm.dao;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
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

import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.paging.MdmPageRequest;
import ca.uhn.fhir.mdm.model.MdmPidTuple;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IMdmLinkDao<T extends IMdmLink> {
	int deleteWithAnyReferenceToPid(ResourcePersistentId thePid);

	int deleteWithAnyReferenceToPidAndMatchResultNot(ResourcePersistentId thePid, MdmMatchResultEnum theMatchResult);

	List<MdmPidTuple> expandPidsFromGroupPidGivenMatchResult(ResourcePersistentId theGroupPid, MdmMatchResultEnum theMdmMatchResultEnum);

	List<MdmPidTuple> expandPidsBySourcePidAndMatchResult(ResourcePersistentId theSourcePid, MdmMatchResultEnum theMdmMatchResultEnum);

	List<MdmPidTuple> expandPidsByGoldenResourcePidAndMatchResult(ResourcePersistentId theSourcePid, MdmMatchResultEnum theMdmMatchResultEnum);

	List<ResourcePersistentId> findPidByResourceNameAndThreshold(String theResourceName, Date theHighThreshold, Pageable thePageable);

	List<ResourcePersistentId> findPidByResourceNameAndThresholdAndPartitionId(String theResourceName, Date theHighThreshold,List<Integer> thePartitionIds, Pageable thePageable);

	List<T> findAllById(List<ResourcePersistentId> thePids);

	Optional<T> findById(ResourcePersistentId thePid);

	void deleteAll(List<T> theLinks);

	List<T> findAll(Example<T> theExample);

	List<T> findAll();

	Long count();

	void deleteAll();

	T save(T theMdmLink);

	Optional<T> findOne(Example<T> theExample);

	void delete(T theMdmLink);

	T validateMdmLink(IMdmLink theMdmLink) throws UnprocessableEntityException;

	Page<T> search(IIdType theGoldenResourceId, IIdType theSourceId, MdmMatchResultEnum theMatchResult, MdmLinkSourceEnum theLinkSource, MdmPageRequest thePageRequest, List<Integer> thePartitionId);

	Optional<? extends IMdmLink> findBySourcePidAndMatchResult(ResourcePersistentId theSourcePid, MdmMatchResultEnum theMatch);

	void deleteLinksWithAnyReferenceToPids(List<ResourcePersistentId> theResourcePersistentIds);
}
