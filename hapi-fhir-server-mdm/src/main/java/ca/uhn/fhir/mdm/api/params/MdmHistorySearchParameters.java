package ca.uhn.fhir.mdm.api.params;

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

import ca.uhn.fhir.mdm.provider.MdmControllerUtil;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MdmHistorySearchParameters {
	private List<IIdType> myGoldenResourceIds = new ArrayList<>();
	private List<IIdType> mySourceIds = new ArrayList<>();

	public MdmHistorySearchParameters() {}

	public List<IIdType> getGoldenResourceIds() {
		return myGoldenResourceIds;
	}

	public List<IIdType> getSourceIds() {
		return mySourceIds;
	}

	public MdmHistorySearchParameters setGoldenResourceIds(List<String> theGoldenResourceIds) {
		myGoldenResourceIds = extractId(theGoldenResourceIds);
		return this;
	}

	public MdmHistorySearchParameters setSourceIds(List<String> theSourceIds) {
		mySourceIds = extractId(theSourceIds);
		return this;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;
		if (theO == null || getClass() != theO.getClass()) return false;
		final MdmHistorySearchParameters that = (MdmHistorySearchParameters) theO;
		return Objects.equals(myGoldenResourceIds, that.myGoldenResourceIds)
				&& Objects.equals(mySourceIds, that.mySourceIds);
	}

	@Override
	public int hashCode() {
		return Objects.hash(myGoldenResourceIds, mySourceIds);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("myMdmGoldenResourceIds", myGoldenResourceIds)
				.append("myMdmTargetResourceIds", mySourceIds)
				.toString();
	}

	@Nonnull
	private static List<IIdType> extractId(List<String> theTheGoldenResourceIds) {
		return theTheGoldenResourceIds.stream()
				.map(MdmHistorySearchParameters::extractId)
				.collect(Collectors.toUnmodifiableList());
	}

	@Nullable
	private static IIdType extractId(String theTheGoldenResourceId) {
		return MdmControllerUtil.extractGoldenResourceIdDtOrNull(
				ProviderConstants.MDM_QUERY_LINKS_GOLDEN_RESOURCE_ID, theTheGoldenResourceId);
	}
}
