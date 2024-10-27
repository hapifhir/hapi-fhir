package ca.uhn.fhir.mdm.model.mdmevents;

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

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;
import java.util.Objects;

public class MdmLinkWithRevisionJson implements IModelJson {
	@JsonProperty(value = "mdmLink", required = true)
	MdmLinkJson myMdmLink;

	@JsonProperty(value = "revisionNumber", required = true)
	Long myRevisionNumber;

	@JsonProperty(value = "revisionTimestamp", required = true)
	Date myRevisionTimestamp;

	public MdmLinkWithRevisionJson(MdmLinkJson theMdmLink, Long theRevisionNumber, Date theRevisionTimestamp) {
		myMdmLink = theMdmLink;
		myRevisionNumber = theRevisionNumber;
		myRevisionTimestamp = theRevisionTimestamp;
	}

	public MdmLinkJson getMdmLink() {
		return myMdmLink;
	}

	public Long getRevisionNumber() {
		return myRevisionNumber;
	}

	public Date getRevisionTimestamp() {
		return myRevisionTimestamp;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;
		if (theO == null || getClass() != theO.getClass()) return false;
		final MdmLinkWithRevisionJson that = (MdmLinkWithRevisionJson) theO;
		return myMdmLink.equals(that.myMdmLink)
				&& myRevisionNumber.equals(that.myRevisionNumber)
				&& myRevisionTimestamp.equals(that.myRevisionTimestamp);
	}

	@Override
	public int hashCode() {
		return Objects.hash(myMdmLink, myRevisionNumber, myRevisionTimestamp);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("myMdmLink", myMdmLink)
				.append("myRevisionNumber", myRevisionNumber)
				.append("myRevisionTimestamp", myRevisionTimestamp)
				.toString();
	}
}
