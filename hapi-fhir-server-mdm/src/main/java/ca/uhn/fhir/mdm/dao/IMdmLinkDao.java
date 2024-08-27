/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.mdm.dao;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmLinkWithRevision;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.paging.MdmPageRequest;
import ca.uhn.fhir.mdm.api.params.MdmHistorySearchParameters;
import ca.uhn.fhir.mdm.api.params.MdmQuerySearchParameters;
import ca.uhn.fhir.mdm.model.MdmPidTuple;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revisions;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IMdmLinkDao<P extends IResourcePersistentId, M extends IMdmLink<P>> {
	int deleteWithAnyReferenceToPid(P thePid);

	int deleteWithAnyReferenceToPidAndMatchResultNot(P thePid, MdmMatchResultEnum theMatchResult);

	List<MdmPidTuple<P>> expandPidsFromGroupPidGivenMatchResult(
			P theGroupPid, MdmMatchResultEnum theMdmMatchResultEnum);

	List<MdmPidTuple<P>> expandPidsBySourcePidAndMatchResult(P theSourcePid, MdmMatchResultEnum theMdmMatchResultEnum);

	List<MdmPidTuple<P>> expandPidsByGoldenResourcePidAndMatchResult(
			P theSourcePid, MdmMatchResultEnum theMdmMatchResultEnum);

	// TODO: on next bump, make this method non-default
	default List<M> findLinksAssociatedWithGoldenResourceOfSourceResourceExcludingNoMatch(P theSourcePid) {
		throw new UnsupportedOperationException(Msg.code(2428));
	}

	List<P> findPidByResourceNameAndThreshold(String theResourceName, Date theHighThreshold, Pageable thePageable);

	List<P> findPidByResourceNameAndThresholdAndPartitionId(
			String theResourceName, Date theHighThreshold, List<Integer> thePartitionIds, Pageable thePageable);

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

	// TODO KHS is this method still required?  Probably not?  But leaving it in for now...
	M validateMdmLink(IMdmLink theMdmLink) throws UnprocessableEntityException;

	@Deprecated
	Page<M> search(
			IIdType theGoldenResourceId,
			IIdType theSourceId,
			MdmMatchResultEnum theMatchResult,
			MdmLinkSourceEnum theLinkSource,
			MdmPageRequest thePageRequest,
			List<Integer> thePartitionId);

	Page<M> search(MdmQuerySearchParameters theMdmQuerySearchParameters);

	Optional<M> findBySourcePidAndMatchResult(P theSourcePid, MdmMatchResultEnum theMatch);

	void deleteLinksWithAnyReferenceToPids(List<P> theResourcePersistentIds);

	// TODO: LD:  delete for good on the next bump
	@Deprecated(since = "6.5.6", forRemoval = true)
	default Revisions<Long, M> findHistory(P thePid) {
		throw new UnsupportedOperationException(Msg.code(2296) + "Deprecated and not supported in non-JPA");
	}

	default List<MdmLinkWithRevision<M>> getHistoryForIds(MdmHistorySearchParameters theMdmHistorySearchParameters) {
		throw new UnsupportedOperationException(Msg.code(2299) + "not yet implemented");
	}
}
