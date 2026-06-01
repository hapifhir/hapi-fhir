/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.term;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hl7.fhir.instance.model.api.IIdType;

public class UploadStatistics {
	private final IIdType myTarget;

	private int myUpdatedConceptCount;
	private int myAddedConceptCount = 0;
	private int myAddedDesignationCount = 0;
	private int myAddedPropertyCount = 0;
	private int myAddedConceptLinkCount = 0;

	public UploadStatistics(IIdType theTarget) {
		this(0, theTarget);
	}

	public UploadStatistics(int theUpdatedConceptCount, IIdType theTarget) {
		myUpdatedConceptCount = theUpdatedConceptCount;
		myTarget = theTarget;
	}

	public int getAddedConceptLinkCount() {
		return myAddedConceptLinkCount;
	}

	public UploadStatistics incrementConceptsAddedCount() {
		myAddedConceptCount++;
		return this;
	}

	public UploadStatistics incrementDesignationsAddedCount() {
		myAddedDesignationCount++;
		return this;
	}

	public UploadStatistics incrementDesignationsAddedCount(int theCount) {
		myAddedDesignationCount += theCount;
		return this;
	}

	public void incrementPropertiesAddedCount() {
		myAddedPropertyCount++;
	}

	public int getAddedConceptCount() {
		return myAddedConceptCount;
	}

	public int getAddedDesignationCount() {
		return myAddedDesignationCount;
	}

	public int getAddedPropertyCount() {
		return myAddedPropertyCount;
	}

	public void incrementConceptLinksAddedCount() {
		myAddedConceptLinkCount++;
	}

	public void incrementUpdatedConceptCount() {
		myUpdatedConceptCount++;
	}

	public int getUpdatedConceptCount() {
		return myUpdatedConceptCount;
	}

	public IIdType getTarget() {
		return myTarget;
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE);
		if (myTarget != null) {
			b.append("target", myTarget.toUnqualifiedVersionless());
		}
		if (myUpdatedConceptCount > 0) {
			b.append("updatedConcepts", myUpdatedConceptCount);
		}
		if (myAddedConceptCount > 0) {
			b.append("addedConcepts", myAddedConceptCount);
		}
		if (myAddedDesignationCount > 0) {
			b.append("addedDesignations", myAddedDesignationCount);
		}
		if (myAddedPropertyCount > 0) {
			b.append("addedProperties", myAddedPropertyCount);
		}
		if (myAddedConceptLinkCount > 0) {
			b.append("addedConceptLinks", myAddedConceptLinkCount);
		}

		return b.toString();
	}
}
