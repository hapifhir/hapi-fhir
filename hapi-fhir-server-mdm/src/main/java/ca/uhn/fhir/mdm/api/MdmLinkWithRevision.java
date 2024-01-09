package ca.uhn.fhir.mdm.api;

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

import ca.uhn.fhir.jpa.model.entity.EnversRevision;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Objects;

public class MdmLinkWithRevision<T extends IMdmLink<?>> {
	private final T myMdmLink;
	private final EnversRevision myEnversRevision;

	public MdmLinkWithRevision(T theMdmLink, EnversRevision theEnversRevision) {
		myMdmLink = theMdmLink;
		myEnversRevision = theEnversRevision;
	}

	public T getMdmLink() {
		return myMdmLink;
	}

	public EnversRevision getEnversRevision() {
		return myEnversRevision;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;
		if (theO == null || getClass() != theO.getClass()) return false;
		final MdmLinkWithRevision<?> that = (MdmLinkWithRevision<?>) theO;
		return Objects.equals(myMdmLink, that.myMdmLink) && Objects.equals(myEnversRevision, that.myEnversRevision);
	}

	@Override
	public int hashCode() {
		return Objects.hash(myMdmLink, myEnversRevision);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("myMdmLink", myMdmLink)
				.append("myEnversRevision", myEnversRevision)
				.toString();
	}
}
