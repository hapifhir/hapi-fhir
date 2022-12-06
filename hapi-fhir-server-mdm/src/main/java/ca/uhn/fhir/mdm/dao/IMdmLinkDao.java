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
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IMdmLinkDao<P extends IResourcePersistentId, M extends IMdmLink<P>> {
	int deleteWithAnyReferenceToPid(P thePid);

	int deleteWithAnyReferenceToPidAndMatchResultNot(P thePid, MdmMatchResultEnum theMatchResult);

	List<MdmPidTuple> expandPidsFromGroupPidGivenMatchResult(P theGroupPid, MdmMatchResultEnum theMdmMatchResultEnum);

	List<MdmPidTuple> expandPidsBySourcePidAndMatchResult(P theSourcePid, MdmMatchResultEnum theMdmMatchResultEnum);

	List<MdmPidTuple> expandPidsByGoldenResourcePidAndMatchResult(P theSourcePid, MdmMatchResultEnum theMdmMatchResultEnum);

	List<P> findPidByResourceNameAndThreshold(String theResourceName, Date theHighThreshold, Pageable thePageable);

	List<P> findPidByResourceNameAndThresholdAndPartitionId(String theResourceName, Date theHighThreshold, List<Integer> thePartitionIds, Pageable thePageable);

	List<M> findAllById(List<P> thePids);

	Optional<M> findById(P thePid);

	void deleteAll(List<M> theLinks);

	List<M> findAll(Example<M> theExample);

	List<M> findAll();

	Long count();

	void deleteAll();

	M save(M theMdmLink);

	Optional<M> findOne(Example<M> theExample);

	void delete(M theMdmLink);

	// FIXME KHS is this method still required?
	M validateMdmLink(IMdmLink theMdmLink) throws UnprocessableEntityException;

	Page<M> search(IIdType theGoldenResourceId, IIdType theSourceId, MdmMatchResultEnum theMatchResult, MdmLinkSourceEnum theLinkSource, MdmPageRequest thePageRequest, List<Integer> thePartitionId);

	Optional<M> findBySourcePidAndMatchResult(P theSourcePid, MdmMatchResultEnum theMatch);

	void deleteLinksWithAnyReferenceToPids(List<P> theResourcePersistentIds);
}
