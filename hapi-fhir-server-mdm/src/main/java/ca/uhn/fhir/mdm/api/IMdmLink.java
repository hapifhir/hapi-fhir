package ca.uhn.fhir.mdm.api;

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

import ca.uhn.fhir.jpa.model.entity.PartitionablePartitionId;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;

import java.util.Date;

public interface IMdmLink<T extends IResourcePersistentId> {
	T getId();

	IMdmLink setId(T theId);

	T getGoldenResourcePersistenceId();

	IMdmLink setGoldenResourcePersistenceId(T theGoldenResourcePid);

	T getSourcePersistenceId();

	IMdmLink setSourcePersistenceId(T theSourcePid);

	MdmMatchResultEnum getMatchResult();

	IMdmLink setMatchResult(MdmMatchResultEnum theMatchResult);

	default boolean isNoMatch() {
		return getMatchResult() == MdmMatchResultEnum.NO_MATCH;
	}

	default boolean isMatch() {
		return getMatchResult() == MdmMatchResultEnum.MATCH;
	}

	default boolean isPossibleMatch() {
		return getMatchResult() == MdmMatchResultEnum.POSSIBLE_MATCH;
	}

	default boolean isRedirect() {
		return getMatchResult() == MdmMatchResultEnum.REDIRECT;
	}

	default boolean isPossibleDuplicate() {
		return getMatchResult() == MdmMatchResultEnum.POSSIBLE_DUPLICATE;
	}

	MdmLinkSourceEnum getLinkSource();

	IMdmLink setLinkSource(MdmLinkSourceEnum theLinkSource);

    default boolean isAuto() {
        return getLinkSource() == MdmLinkSourceEnum.AUTO;
    }

    default boolean isManual() {
        return getLinkSource() == MdmLinkSourceEnum.MANUAL;
    }

    Date getCreated();

	 IMdmLink setCreated(Date theCreated);

	Date getUpdated();

	IMdmLink setUpdated(Date theUpdated);

	String getVersion();

	IMdmLink setVersion(String theVersion);

	Boolean getEidMatch();

	Boolean isEidMatchPresent();

	IMdmLink setEidMatch(Boolean theEidMatch);

	Boolean getHadToCreateNewGoldenResource();

	IMdmLink setHadToCreateNewGoldenResource(Boolean theHadToCreateNewGoldenResource);

	Long getVector();

	IMdmLink setVector(Long theVector);

	Double getScore();

	IMdmLink setScore(Double theScore);

	Long getRuleCount();

	IMdmLink setRuleCount(Long theRuleCount);

	String getMdmSourceType();

	IMdmLink setMdmSourceType(String theMdmSourceType);

	void setPartitionId(PartitionablePartitionId thePartitionablePartitionId);

	PartitionablePartitionId getPartitionId();
}
