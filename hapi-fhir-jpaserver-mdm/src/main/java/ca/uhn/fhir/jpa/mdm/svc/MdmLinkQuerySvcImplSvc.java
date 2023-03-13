package ca.uhn.fhir.jpa.mdm.svc;

/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.PartitionablePartitionId;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.IMdmLinkQuerySvc;
import ca.uhn.fhir.mdm.api.MdmHistorySearchParameters;
import ca.uhn.fhir.mdm.api.MdmLinkJson;
import ca.uhn.fhir.mdm.api.MdmLinkRevisionJson;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.MdmQuerySearchParameters;
import ca.uhn.fhir.mdm.api.paging.MdmPageRequest;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.history.Revision;
import org.springframework.data.history.RevisionMetadata;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MdmLinkQuerySvcImplSvc implements IMdmLinkQuerySvc {

	private static final Logger ourLog = LoggerFactory.getLogger(MdmLinkQuerySvcImplSvc.class);

	@Autowired
	MdmLinkDaoSvc myMdmLinkDaoSvc;

	@Autowired
	IMdmModelConverterSvc myMdmModelConverterSvc;

	@Override
	@Deprecated
	@Transactional
	public Page<MdmLinkJson> queryLinks(IIdType theGoldenResourceId, IIdType theSourceResourceId, MdmMatchResultEnum theMatchResult, MdmLinkSourceEnum theLinkSource, MdmTransactionContext theMdmContext, MdmPageRequest thePageRequest) {
		MdmQuerySearchParameters mdmQuerySearchParameters = new MdmQuerySearchParameters(thePageRequest)
			.setGoldenResourceId(theGoldenResourceId)
			.setSourceId(theSourceResourceId)
			.setMatchResult(theMatchResult)
			.setLinkSource(theLinkSource);

		return queryLinks(mdmQuerySearchParameters, theMdmContext);
	}

	@Override
	@Deprecated
	@Transactional
	public Page<MdmLinkJson> queryLinks(IIdType theGoldenResourceId, IIdType theSourceResourceId, MdmMatchResultEnum theMatchResult, MdmLinkSourceEnum theLinkSource, MdmTransactionContext theMdmContext, MdmPageRequest thePageRequest, List<Integer> thePartitionIds) {
		MdmQuerySearchParameters mdmQuerySearchParameters = new MdmQuerySearchParameters(thePageRequest)
			.setGoldenResourceId(theGoldenResourceId)
			.setSourceId(theSourceResourceId)
			.setMatchResult(theMatchResult)
			.setLinkSource(theLinkSource)
			.setPartitionIds(thePartitionIds);

		return queryLinks(mdmQuerySearchParameters, theMdmContext);
	}

	@Override
	@Transactional
	public Page<MdmLinkJson> queryLinks(MdmQuerySearchParameters theMdmQuerySearchParameters, MdmTransactionContext theMdmContext) {
		@SuppressWarnings("unchecked")
		Page<? extends IMdmLink> mdmLinks = myMdmLinkDaoSvc.executeTypedQuery(theMdmQuerySearchParameters);
		return mdmLinks.map(myMdmModelConverterSvc::toJson);
	}


	@Override
	@Transactional
	public Page<MdmLinkJson> getDuplicateGoldenResources(MdmTransactionContext theMdmContext, MdmPageRequest thePageRequest) {
		return getDuplicateGoldenResources(theMdmContext, thePageRequest, null, null);
	}

	@Override
	@Transactional
	public Page<MdmLinkJson> getDuplicateGoldenResources(MdmTransactionContext theMdmContext, MdmPageRequest thePageRequest,
																		  List<Integer> thePartitionIds, String theRequestResourceType) {
		MdmQuerySearchParameters mdmQuerySearchParameters = new MdmQuerySearchParameters(thePageRequest)
			.setMatchResult(MdmMatchResultEnum.POSSIBLE_DUPLICATE)
			.setPartitionIds(thePartitionIds)
			.setResourceType(theRequestResourceType);

		@SuppressWarnings("unchecked")
		Page<? extends IMdmLink> mdmLinkPage = myMdmLinkDaoSvc.executeTypedQuery(mdmQuerySearchParameters);
		return mdmLinkPage.map(myMdmModelConverterSvc::toJson);
	}

	@Override
	public List<MdmLinkRevisionJson> queryLinkHistory(MdmHistorySearchParameters theMdmHistorySearchParameters) {
		final List<Revision<Integer, IMdmLink<? extends IResourcePersistentId<?>>>> hardCodedMdmLinkRevisions = getHardCodedMdmLinkRevisions();

		final List mdmLinkHistoryFromDao = myMdmLinkDaoSvc.findMdmLinkHistory(theMdmHistorySearchParameters);

		// TODO:  filter, then convert to JSON
//		final List<MdmLinkRevisionJson> revisionJsonsFromHardCodedRevisions = hardCodedMdmLinkRevisions.stream().map(myMdmModelConverterSvc::toJson).collect(Collectors.toUnmodifiableList());
//		return filterAndSortMdmLinkRevisions(revisionJsonsFromHardCodedRevisions, theMdmHistorySearchParameters.getMdmGoldenResourceIds(), theMdmHistorySearchParameters.getMdmTargetResourceIds());
		final List<MdmLinkRevisionJson> revisionJsonsFromHardCodedRevisions = hardCodedMdmLinkRevisions.stream().map(myMdmModelConverterSvc::toJson).collect(Collectors.toUnmodifiableList());
		final List<Revision<Integer, IMdmLink<? extends IResourcePersistentId<?>>>> mdmLinkRevisions = filterAndSortMdmLinkRevisions(hardCodedMdmLinkRevisions, theMdmHistorySearchParameters.getMdmGoldenResourceIds(), theMdmHistorySearchParameters.getMdmTargetResourceIds());

		return mdmLinkRevisions.stream().map(myMdmModelConverterSvc::toJson).collect(Collectors.toUnmodifiableList());
	}

	// TODO:   possibly use this code in a unit test but delete it here:
	@Nonnull
	private List<MdmLinkRevisionJson> filterAndSortMdmLinkRevisionsJson(List<MdmLinkRevisionJson> theAllMdmLinkRevisions, List<String> theGoldenResourceIdsToUse, List<String> theResourceIdsToUse) {
		return theAllMdmLinkRevisions.stream()
			.filter(revision -> filterMeJson(revision, theGoldenResourceIdsToUse, theResourceIdsToUse))
			.sorted((revision1, revision2) -> {
				final int timestampCompare = revision2.getRevisionTimestamp().compareTo(revision1.getRevisionTimestamp());
				final int goldenResourceCompare = revision1.getMdmLink().getGoldenResourceId().compareTo(revision2.getMdmLink().getGoldenResourceId());

				if (goldenResourceCompare == 0) {
					return timestampCompare;
				}

				return goldenResourceCompare;
			})
			.collect(Collectors.toUnmodifiableList());
	}

	private List<Revision<Integer, IMdmLink<? extends IResourcePersistentId<?>>>> filterAndSortMdmLinkRevisions(List<Revision<Integer, IMdmLink<? extends IResourcePersistentId<?>>>> theAllMdmLinkRevisions, List<String> theGoldenResourceIdsToUse, List<String> theResourceIdsToUse) {
		return theAllMdmLinkRevisions.stream()
			.filter(revision -> filterMe(revision, theGoldenResourceIdsToUse, theResourceIdsToUse))
			.sorted((revision1, revision2) -> {
				final int timestampCompare = revision2.getRequiredRevisionInstant().compareTo(revision1.getRequiredRevisionInstant());
				final int goldenResourceCompare = revision1.getEntity().getGoldenResourcePersistenceId().getId().toString().compareTo(revision2.getEntity().getGoldenResourcePersistenceId().getId().toString());

				if (goldenResourceCompare == 0) {
					return timestampCompare;
				}

				return goldenResourceCompare;
			})
			.collect(Collectors.toUnmodifiableList());
	}

//	private List<MdmLinkRevisionJson> filterAndSortMdmLinkRevisionsJsons(List<MdmLinkRevisionJson> theAllMdmLinkRevisions, List<String> theGoldenResourceIdsToUse, List<String> theResourceIdsToUse) {
//		return theAllMdmLinkRevisions.stream()
//			.filter(revision -> filterMeJson(revision, theGoldenResourceIdsToUse, theResourceIdsToUse))
//			.sorted((revision1, revision2) -> {
//				final int timestampCompare = revision2.getRequiredRevisionInstant().compareTo(revision1.getRequiredRevisionInstant());
//				final int goldenResourceCompare = revision1.getEntity().getGoldenResourcePersistenceId().getId().toString().compareTo(revision2.getEntity().getGoldenResourcePersistenceId().getId().toString()));
//
//				if (goldenResourceCompare == 0) {
//					return timestampCompare;
//				}
//
//				return goldenResourceCompare;
//			})
//			.collect(Collectors.toUnmodifiableList());
//	}

	// TODO:   possibly use this code in a unit test but delete it here:
	@Nonnull
	private boolean filterMe(Revision<Integer, IMdmLink<? extends IResourcePersistentId<?>>> mdmLinkRevision, List<String> theMdmGoldenResourceIds, List<String> theResourceIds) {
		final boolean isWithinGoldenResource = theMdmGoldenResourceIds.contains(mdmLinkRevision.getEntity().getGoldenResourcePersistenceId().getId().toString());
		final boolean isWithTargetResource = theResourceIds.contains(mdmLinkRevision.getEntity().getSourcePersistenceId().getId().toString());

		return isWithinGoldenResource || isWithTargetResource;
	}

	@Nonnull
	private boolean filterMeJson(MdmLinkRevisionJson mdmLinkRevisionJson, List<String> theMdmGoldenResourceIds, List<String> theResourceIds) {
		final boolean isWithinGoldenResource = theMdmGoldenResourceIds.contains(mdmLinkRevisionJson.getMdmLink().getGoldenResourceId());
		final boolean isWithTargetResource = theResourceIds.contains(mdmLinkRevisionJson.getMdmLink().getSourceId());

		return isWithinGoldenResource || isWithTargetResource;
	}

	// TODO:   possibly use this code in a unit test but delete it here:
	@Nonnull
	private static List<MdmLinkRevisionJson> getHardCodedMdmLinksJson() {
		final LocalDateTime march9 = LocalDateTime.of(2023, Month.MARCH, 9, 11, 20, 37);
		final LocalDateTime march8 = LocalDateTime.of(2023, Month.MARCH, 8, 11, 20, 37);
		final LocalDateTime march7 = LocalDateTime.of(2023, Month.MARCH, 7, 11, 20, 37);
		final LocalDateTime march6 = LocalDateTime.of(2023, Month.MARCH, 6, 11, 20, 37);
		final LocalDateTime march5 = LocalDateTime.of(2023, Month.MARCH, 5, 11, 20, 37);
		final LocalDateTime march4 = LocalDateTime.of(2023, Month.MARCH, 4, 11, 20, 37);
		final LocalDateTime march3 = LocalDateTime.of(2023, Month.MARCH, 3, 11, 20, 37);
		final LocalDateTime march2 = LocalDateTime.of(2023, Month.MARCH, 2, 11, 20, 37);
		final LocalDateTime march1 = LocalDateTime.of(2023, Month.MARCH, 1, 11, 20, 37);

		// 2 revisions for MDM link 1:  from MATCH to NO_MATCH
		final MdmLinkRevisionJson mdmLinkRevisionJson1_v1 = buildMdmLinkRevisionJson(1, march1, "1", "A", MdmMatchResultEnum.MATCH, MdmLinkSourceEnum.MANUAL);
		final MdmLinkRevisionJson mdmLinkRevisionJson1_v2 = buildMdmLinkRevisionJson(2, march2, "1", "A", MdmMatchResultEnum.NO_MATCH, MdmLinkSourceEnum.MANUAL);

		// 3 revisions for MDM link 2:  each with a different source ID
		final MdmLinkRevisionJson mdmLinkRevisionJson2_v1 = buildMdmLinkRevisionJson(1, march1, "2", "B", MdmMatchResultEnum.MATCH, MdmLinkSourceEnum.AUTO);
		final MdmLinkRevisionJson mdmLinkRevisionJson2_v2 = buildMdmLinkRevisionJson(2, march2, "2", "B_1", MdmMatchResultEnum.MATCH, MdmLinkSourceEnum.AUTO);
		final MdmLinkRevisionJson mdmLinkRevisionJson2_v3 = buildMdmLinkRevisionJson(3, march3, "2", "B_2", MdmMatchResultEnum.MATCH, MdmLinkSourceEnum.AUTO);

		final MdmLinkRevisionJson mdmLinkRevisionJson3 = buildMdmLinkRevisionJson(1, march4, "3", "C", MdmMatchResultEnum.GOLDEN_RECORD, MdmLinkSourceEnum.MANUAL);
		final MdmLinkRevisionJson mdmLinkRevisionJson4 = buildMdmLinkRevisionJson(1, march5, "3", "D", MdmMatchResultEnum.GOLDEN_RECORD, MdmLinkSourceEnum.MANUAL);
		final MdmLinkRevisionJson mdmLinkRevisionJson5 = buildMdmLinkRevisionJson(1, march6, "3", "E", MdmMatchResultEnum.GOLDEN_RECORD, MdmLinkSourceEnum.MANUAL);
		final MdmLinkRevisionJson mdmLinkRevisionJson6 = buildMdmLinkRevisionJson(1, march7, "3", "F", MdmMatchResultEnum.GOLDEN_RECORD, MdmLinkSourceEnum.MANUAL);

		final MdmLinkRevisionJson mdmLinkRevisionJson7 = buildMdmLinkRevisionJson(1, march8, "4", "G", MdmMatchResultEnum.GOLDEN_RECORD, MdmLinkSourceEnum.MANUAL);
		final MdmLinkRevisionJson mdmLinkRevisionJson8 = buildMdmLinkRevisionJson(1, march9, "4", "H", MdmMatchResultEnum.GOLDEN_RECORD, MdmLinkSourceEnum.MANUAL);
		final MdmLinkRevisionJson mdmLinkRevisionJson9 = buildMdmLinkRevisionJson(1, march9, "4", "I", MdmMatchResultEnum.GOLDEN_RECORD, MdmLinkSourceEnum.MANUAL);

		return List.of(mdmLinkRevisionJson1_v1, mdmLinkRevisionJson1_v2, mdmLinkRevisionJson2_v1, mdmLinkRevisionJson2_v2, mdmLinkRevisionJson2_v3, mdmLinkRevisionJson3, mdmLinkRevisionJson4, mdmLinkRevisionJson5, mdmLinkRevisionJson6, mdmLinkRevisionJson7, mdmLinkRevisionJson8, mdmLinkRevisionJson9);
	}

	// TODO:   possibly use this code in a unit test but delete it here:
	@Nonnull
	private static MdmLinkRevisionJson buildMdmLinkRevisionJson(int theRevisionNumber, LocalDateTime theRevisionTimestamp, String theGoldenResourceId, String theSourceId, MdmMatchResultEnum theMdmMatchResultEnum, MdmLinkSourceEnum theMdmLinkSourceEnum) {
		final MdmLinkJson mdmLink = new MdmLinkJson();

		mdmLink.setLinkSource(theMdmLinkSourceEnum);
		mdmLink.setMatchResult(theMdmMatchResultEnum);
		mdmLink.setGoldenResourceId(theGoldenResourceId);
		mdmLink.setSourceId(theSourceId);
		mdmLink.setCreated(new Date());
		mdmLink.setUpdated(new Date());

		return new MdmLinkRevisionJson(mdmLink, theRevisionNumber, theRevisionTimestamp);
	}

	// TODO:   possibly use this code in a unit test but delete it here:
	private List<Revision<Integer, IMdmLink<? extends IResourcePersistentId<?>>>> getHardCodedMdmLinkRevisions() {
		final LocalDateTime march9 = LocalDateTime.of(2023, Month.MARCH, 9, 11, 20, 37);
		final LocalDateTime march8 = LocalDateTime.of(2023, Month.MARCH, 8, 11, 20, 37);
		final LocalDateTime march7 = LocalDateTime.of(2023, Month.MARCH, 7, 11, 20, 37);
		final LocalDateTime march6 = LocalDateTime.of(2023, Month.MARCH, 6, 11, 20, 37);
		final LocalDateTime march5 = LocalDateTime.of(2023, Month.MARCH, 5, 11, 20, 37);
		final LocalDateTime march4 = LocalDateTime.of(2023, Month.MARCH, 4, 11, 20, 37);
		final LocalDateTime march3 = LocalDateTime.of(2023, Month.MARCH, 3, 11, 20, 37);
		final LocalDateTime march2 = LocalDateTime.of(2023, Month.MARCH, 2, 11, 20, 37);
		final LocalDateTime march1 = LocalDateTime.of(2023, Month.MARCH, 1, 11, 20, 37);

		final var mdmLinkRevisionJson1_v1 = buildMdmLinkRevisionJson(1, march1, "1", "A", MdmMatchResultEnum.MATCH, MdmLinkSourceEnum.MANUAL);

		final var mdmLinkRevision1_v1 = buildRevision(1, march1, 1L, 11L, MdmMatchResultEnum.MATCH, MdmLinkSourceEnum.MANUAL, RevisionMetadata.RevisionType.INSERT);
		final var mdmLinkRevision1_v2 = buildRevision(2, march2, 1L, 11L, MdmMatchResultEnum.NO_MATCH, MdmLinkSourceEnum.MANUAL, RevisionMetadata.RevisionType.INSERT);

		final var mdmLinkRevision2_v1 = buildRevision(1, march1, 2L, 12L, MdmMatchResultEnum.MATCH, MdmLinkSourceEnum.MANUAL, RevisionMetadata.RevisionType.INSERT);
		final var mdmLinkRevision2_v2 = buildRevision(2, march2, 2L, 13L, MdmMatchResultEnum.MATCH, MdmLinkSourceEnum.MANUAL, RevisionMetadata.RevisionType.INSERT);
		final var mdmLinkRevision2_v3 = buildRevision(3, march3, 2L, 14L, MdmMatchResultEnum.MATCH, MdmLinkSourceEnum.MANUAL, RevisionMetadata.RevisionType.INSERT);

		final var mdmLinkRevision3 = buildRevision(1, march4, 3L, 15L, MdmMatchResultEnum.MATCH, MdmLinkSourceEnum.MANUAL, RevisionMetadata.RevisionType.INSERT);
		final var mdmLinkRevision4 = buildRevision(1, march5, 3L, 16L, MdmMatchResultEnum.MATCH, MdmLinkSourceEnum.MANUAL, RevisionMetadata.RevisionType.INSERT);
		final var mdmLinkRevision5 = buildRevision(1, march6, 3L, 17L, MdmMatchResultEnum.MATCH, MdmLinkSourceEnum.MANUAL, RevisionMetadata.RevisionType.INSERT);
		final var mdmLinkRevision6 = buildRevision(1, march7, 3L, 18L, MdmMatchResultEnum.MATCH, MdmLinkSourceEnum.MANUAL, RevisionMetadata.RevisionType.INSERT);

		final var mdmLinkRevision7 = buildRevision(1, march8, 4L, 19L, MdmMatchResultEnum.MATCH, MdmLinkSourceEnum.MANUAL, RevisionMetadata.RevisionType.INSERT);
		final var mdmLinkRevision8 = buildRevision(1, march9, 4L, 20L, MdmMatchResultEnum.MATCH, MdmLinkSourceEnum.MANUAL, RevisionMetadata.RevisionType.INSERT);
		final var mdmLinkRevision9 = buildRevision(1, march9, 4L, 21L, MdmMatchResultEnum.MATCH, MdmLinkSourceEnum.MANUAL, RevisionMetadata.RevisionType.INSERT);

		return List.of(mdmLinkRevision1_v1, mdmLinkRevision1_v2, mdmLinkRevision2_v1, mdmLinkRevision2_v2, mdmLinkRevision2_v3, mdmLinkRevision3, mdmLinkRevision4, mdmLinkRevision5, mdmLinkRevision6, mdmLinkRevision7, mdmLinkRevision8, mdmLinkRevision9);
	}

	// TODO:   possibly use this code in a unit test but delete it here:
	private static Instant toInstant(LocalDateTime theLocalDateTime) {
		return theLocalDateTime.atZone(ZoneId.systemDefault()).toInstant();
	}

	// TODO:   possibly use this code in a unit test but delete it here:
	private static Revision<Integer, IMdmLink<? extends IResourcePersistentId<?>>> buildRevision(Integer theRevisionNumber, LocalDateTime theRevisionTimestamp, Long theGoldenResourceId, Long theTargetResourceId, MdmMatchResultEnum theMdmMatchResultEnum, MdmLinkSourceEnum theMdmLinkSourceEnum, RevisionMetadata.RevisionType theRevisionType) {

		return Revision.of(getRevisionMetadata(theRevisionNumber, toInstant(theRevisionTimestamp), theRevisionType), buildIMdmLink(theGoldenResourceId, theTargetResourceId, theMdmMatchResultEnum, theMdmLinkSourceEnum));
	}

	// TODO:   possibly use this code in a unit test but delete it here:
	private MdmLinkRevisionJson buildMdmLinkRevisionJson(IMdmLink<? extends IResourcePersistentId<?>> theMdmLink, Instant theRevisionTimestamp, Integer theRevisionNumber, RevisionMetadata.RevisionType theRevisionType) {
		// TODO: HapiFhirEnversRevision
		final Revision<Integer, IMdmLink<? extends IResourcePersistentId<?>>> revision = Revision.of(getRevisionMetadata(theRevisionNumber, theRevisionTimestamp, theRevisionType), theMdmLink);

		return myMdmModelConverterSvc.toJson(revision);
	}

	// TODO:   possibly use this code in a unit test but delete it here:
	private static RevisionMetadata getRevisionMetadata(int theRevisionNumber, Instant theRevisionInstant, RevisionMetadata.RevisionType theRevisionType) {
		return new RevisionMetadata<Integer>() {
			@Override
			public Optional<Integer> getRevisionNumber() {
				return Optional.of(theRevisionNumber);
			}

			@Override
			public Optional<Instant> getRevisionInstant() {
				return Optional.of(theRevisionInstant);
			}

			@Override
			public <T> T getDelegate() {
				return null;
			}

			@Override
			public RevisionType getRevisionType() {
				return theRevisionType;
			}
		};
	}

	// TODO:   possibly use this code in a unit test but delete it here:
	private static IMdmLink<JpaPid> buildIMdmLink(Long theGoldenResourceId, Long theTargetResourceId, MdmMatchResultEnum theMdmMatchResultEnum, MdmLinkSourceEnum theMdmLinkSourceEnum) {
		return new IMdmLink<JpaPid>() {
			@Override
			public JpaPid getId() {
				return null;
			}

			@Override
			public IMdmLink<JpaPid> setId(JpaPid theId) {
				return this;
			}

			@Override
			public JpaPid getGoldenResourcePersistenceId() {
				return JpaPid.fromId(theGoldenResourceId);
			}

			@Override
			public IMdmLink<JpaPid> setGoldenResourcePersistenceId(JpaPid theGoldenResourcePid) {
				return null;
			}

			@Override
			public JpaPid getSourcePersistenceId() {
				return JpaPid.fromId(theTargetResourceId);
			}

			@Override
			public IMdmLink<JpaPid> setSourcePersistenceId(JpaPid theSourcePid) {
				return this;
			}

			@Override
			public MdmMatchResultEnum getMatchResult() {
				return theMdmMatchResultEnum;
			}

			@Override
			public IMdmLink<JpaPid> setMatchResult(MdmMatchResultEnum theMatchResult) {
				return this;
			}

			@Override
			public MdmLinkSourceEnum getLinkSource() {
				return theMdmLinkSourceEnum;
			}

			@Override
			public IMdmLink<JpaPid> setLinkSource(MdmLinkSourceEnum theLinkSource) {
				return this;
			}

			@Override
			public Date getCreated() {
				return new Date();
			}

			@Override
			public IMdmLink<JpaPid> setCreated(Date theCreated) {
				return this;
			}

			@Override
			public Date getUpdated() {
				return new Date();
			}

			@Override
			public IMdmLink<JpaPid> setUpdated(Date theUpdated) {
				return this;
			}

			@Override
			public String getVersion() {
				return null;
			}

			@Override
			public IMdmLink<JpaPid> setVersion(String theVersion) {
				return this;
			}

			@Override
			public Boolean getEidMatch() {
				return null;
			}

			@Override
			public Boolean isEidMatchPresent() {
				return null;
			}

			@Override
			public IMdmLink<JpaPid> setEidMatch(Boolean theEidMatch) {
				return this;
			}

			@Override
			public Boolean getHadToCreateNewGoldenResource() {
				return null;
			}

			@Override
			public IMdmLink<JpaPid> setHadToCreateNewGoldenResource(Boolean theHadToCreateNewGoldenResource) {
				return this;
			}

			@Override
			public Long getVector() {
				return null;
			}

			@Override
			public IMdmLink<JpaPid> setVector(Long theVector) {
				return this;
			}

			@Override
			public Double getScore() {
				return null;
			}

			@Override
			public IMdmLink<JpaPid> setScore(Double theScore) {
				return this;
			}

			@Override
			public Long getRuleCount() {
				return null;
			}

			@Override
			public IMdmLink<JpaPid> setRuleCount(Long theRuleCount) {
				return this;
			}

			@Override
			public String getMdmSourceType() {
				return null;
			}

			@Override
			public IMdmLink<JpaPid> setMdmSourceType(String theMdmSourceType) {
				return this;
			}

			@Override
			public void setPartitionId(PartitionablePartitionId thePartitionablePartitionId) {

			}

			@Override
			public PartitionablePartitionId getPartitionId() {
				return null;
			}
		};
	}

}
