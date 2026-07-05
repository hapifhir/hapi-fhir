/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.batch2.jobs.term.base;

import ca.uhn.fhir.jpa.term.UploadStatistics;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.HapiToStringBuilder;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * For terminology import jobs, this class is the data model that handles two
 * jobs (TODO: this should be split up)
 * <ul>
 *     <li>
 *         It conveys defaults about work chunks for the individual work steps
 *         (ie. the attachment IDs of job attachments containing fragments of CSVs
 *         that should be processed by the individual work steps)
 *     </li>
 *     <li>
 *         It conveys processing statistics and IDs of resources to activate from the
 *         processing steps to the final reducer step.
 *     </li>
 * </ul>
 */
public class TerminologyFileSetJson implements IModelJson {

	@JsonProperty("resourcesToActivate")
	private Set<String> myResourcesToActivate;

	@JsonProperty("conceptPidsToGenerateClosureFor")
	private List<Long> myConceptPidsToGenerateClosureFor;

	@JsonProperty("stepIdToRecordsAdded")
	private Map<String, RecordsAddedCounter> myStepIdToRecordsAdded;

	@JsonProperty("attachmentId")
	private String myAttachmentId;

	@JsonProperty("sourceFilename")
	private String mySourceFilename;

	/**
	 * Constructor
	 */
	public TerminologyFileSetJson() {
		super();
	}

	public List<Long> getConceptPidsToGenerateClosureFor() {
		if (myConceptPidsToGenerateClosureFor == null) {
			myConceptPidsToGenerateClosureFor = new ArrayList<>();
		}
		return myConceptPidsToGenerateClosureFor;
	}

	public Set<String> getResourcesToActivate() {
		if (myResourcesToActivate == null) {
			myResourcesToActivate = new HashSet<>();
		}
		return myResourcesToActivate;
	}

	public void setResourcesToActivate(Set<String> theResourcesToActivate) {
		myResourcesToActivate = theResourcesToActivate;
	}

	public void addResourceToActivate(String theResourceToActivate) {
		getResourcesToActivate().add(theResourceToActivate);
	}

	public Map<String, RecordsAddedCounter> getStepIdToRecordsAdded() {
		if (myStepIdToRecordsAdded == null) {
			myStepIdToRecordsAdded = new HashMap<>();
		}
		return myStepIdToRecordsAdded;
	}

	public RecordsAddedCounter getRecordsAddedCounter(String theStepId) {
		return getStepIdToRecordsAdded().computeIfAbsent(theStepId, k -> new RecordsAddedCounter());
	}

	/**
	 * The filename from the source archive that this chunk was extracted from.
	 */
	public String getSourceFilename() {
		return mySourceFilename;
	}

	/**
	 * The filename from the source archive that this chunk was extracted from.
	 */
	public void setSourceFilename(@Nonnull String theSourceFilename) {
		Validate.notBlank(theSourceFilename, "theSourceFilename must not be null or blank");
		mySourceFilename = theSourceFilename;
	}

	/**
	 * An ID for an {@link ca.uhn.fhir.batch2.api.IJobPersistence#fetchAttachmentById(String, String) attachment}
	 * containing the data that should be processed by this step.
	 */
	public String getAttachmentId() {
		return myAttachmentId;
	}

	/**
	 * An ID for an {@link ca.uhn.fhir.batch2.api.IJobPersistence#fetchAttachmentById(String, String) attachment}
	 * containing the data that should be processed by this step.
	 */
	public void setAttachmentId(@Nonnull String theAttachmentId) {
		Validate.notBlank(theAttachmentId, "theChunkAttachmentId must not be null or blank");
		myAttachmentId = theAttachmentId;
	}

	public static class RecordsAddedCounter implements IModelJson {

		@JsonInclude(JsonInclude.Include.NON_EMPTY)
		@JsonProperty("conceptsAdded")
		private int myConceptsAdded = 0;

		@JsonInclude(JsonInclude.Include.NON_EMPTY)
		@JsonProperty("conceptLinksAdded")
		private int myConceptLinksAdded = 0;

		@JsonInclude(JsonInclude.Include.NON_EMPTY)
		@JsonProperty("propertiesAdded")
		private int myPropertiesAdded = 0;

		@JsonInclude(JsonInclude.Include.NON_EMPTY)
		@JsonProperty("designationsAdded")
		private int myDesignationsAdded = 0;

		@JsonInclude(JsonInclude.Include.NON_EMPTY)
		@JsonProperty("conceptMapsAdded")
		private int myConceptMapsAdded = 0;

		@JsonInclude(JsonInclude.Include.NON_EMPTY)
		@JsonProperty("conceptMapMappingsAdded")
		private int myConceptMapMappingsAdded = 0;

		@JsonInclude(JsonInclude.Include.NON_EMPTY)
		@JsonProperty("valueSetsAdded")
		private int myValueSetsAdded = 0;

		@JsonInclude(JsonInclude.Include.NON_EMPTY)
		@JsonProperty("valueSetsCodesAdded")
		private int myValueSetCodesAdded = 0;

		@JsonInclude(JsonInclude.Include.NON_EMPTY)
		@JsonProperty("valueSetsInclusionsAdded")
		private int myValueSetInclusionsAdded = 0;

		@JsonInclude(JsonInclude.Include.NON_EMPTY)
		@JsonProperty("otherChanges")
		private int myOtherChanges = 0;

		@JsonInclude(JsonInclude.Include.NON_EMPTY)
		@JsonProperty("conceptsRemoved")
		private int myConceptsRemoved;

		@JsonInclude(JsonInclude.Include.NON_EMPTY)
		@JsonProperty("conceptLinksRemoved")
		private int myConceptLinksRemoved;

		@JsonInclude(JsonInclude.Include.NON_EMPTY)
		@JsonProperty("propertiesRemoved")
		private int myPropertiesRemoved;

		@JsonInclude(JsonInclude.Include.NON_EMPTY)
		@JsonProperty("designationsRemoved")
		private int myDesignationsRemoved;

		public void incrementConceptsAdded(int theAddedConceptCount) {
			Validate.isTrue(theAddedConceptCount >= 0, "theAddedConceptCount must be >= 0");
			myConceptsAdded += theAddedConceptCount;
		}

		public void incrementConceptLinksAdded(int theAddedConceptLinkCount) {
			Validate.isTrue(theAddedConceptLinkCount >= 0, "theAddedConceptLinkCount must be >= 0");
			myConceptLinksAdded += theAddedConceptLinkCount;
		}

		public void incrementPropertiesAdded(int theAddedPropertyCount) {
			Validate.isTrue(theAddedPropertyCount >= 0, "theAddedPropertyCount must be >= 0");
			myPropertiesAdded += theAddedPropertyCount;
		}

		public void incrementDesignationsAdded(int theAddedDesignationCount) {
			Validate.isTrue(theAddedDesignationCount >= 0, "theAddedDesignationCount must be >= 0");
			myDesignationsAdded += theAddedDesignationCount;
		}

		public void incrementConceptMapsAdded(int theConceptMapsAddedCount) {
			Validate.isTrue(theConceptMapsAddedCount >= 0, "theConceptMapsAddedCount must be >= 0");
			myConceptMapsAdded += theConceptMapsAddedCount;
		}

		public void incrementConceptMapMappingsAdded(int theAddedMappingsCount) {
			Validate.isTrue(theAddedMappingsCount >= 0, "theAddedMappingsCount must be >= 0");
			myConceptMapMappingsAdded += theAddedMappingsCount;
		}

		public void incrementValueSetsAdded(int theAddedValueSetsCount) {
			Validate.isTrue(theAddedValueSetsCount >= 0, "theAddedValueSetsCount must be >= 0");
			myValueSetsAdded += theAddedValueSetsCount;
		}

		public void incrementValueSetCodesAdded(int theAddedValueSetCodesCount) {
			Validate.isTrue(theAddedValueSetCodesCount >= 0, "theAddedValueSetCodesCount must be >= 0");
			myValueSetCodesAdded += theAddedValueSetCodesCount;
		}

		public void incrementValueSetInclusionsAdded(int theAddedValueSetInclusionsCount) {
			Validate.isTrue(theAddedValueSetInclusionsCount >= 0, "theAddedValueSetInclusionsCount must be >= 0");
			myValueSetInclusionsAdded += theAddedValueSetInclusionsCount;
		}

		public void incrementOtherChanges(int theOtherChangesCount) {
			Validate.isTrue(theOtherChangesCount >= 0, "theOtherChangesCount must be >= 0");
			myOtherChanges += theOtherChangesCount;
		}

		public void incrementConceptsRemoved(int theRemovedConceptCount) {
			Validate.isTrue(theRemovedConceptCount >= 0, "Removed concept count must be >= zero");
			myConceptsRemoved += theRemovedConceptCount;
		}

		public void incrementConceptLinksRemoved(int theRemovedConceptLinkCount) {
			Validate.isTrue(theRemovedConceptLinkCount >= 0, "Removed concept link count must be >= zero");
			myConceptLinksRemoved += theRemovedConceptLinkCount;
		}

		public void incrementPropertiesRemoved(int theRemovedPropertyCount) {
			Validate.isTrue(theRemovedPropertyCount >= 0, "Removed property count must be >= zero");
			myPropertiesRemoved += theRemovedPropertyCount;
		}

		public void incrementDesignationsRemoved(int theRemovedDesignationCount) {
			Validate.isTrue(theRemovedDesignationCount >= 0, "Removed designation count must be >= zero");
			myDesignationsRemoved += theRemovedDesignationCount;
		}

		public int getOtherChanges() {
			return myOtherChanges;
		}

		public int getConceptsRemoved() {
			return myConceptsRemoved;
		}

		public int getConceptLinksRemoved() {
			return myConceptLinksRemoved;
		}

		public int getPropertiesRemoved() {
			return myPropertiesRemoved;
		}

		public int getDesignationsRemoved() {
			return myDesignationsRemoved;
		}

		public int getConceptsAdded() {
			return myConceptsAdded;
		}

		public int getConceptLinksAdded() {
			return myConceptLinksAdded;
		}

		public int getPropertiesAdded() {
			return myPropertiesAdded;
		}

		public int getDesignationsAdded() {
			return myDesignationsAdded;
		}

		public int getConceptMapsAdded() {
			return myConceptMapsAdded;
		}

		public int getConceptMapMappingsAdded() {
			return myConceptMapMappingsAdded;
		}

		public int getValueSetsAdded() {
			return myValueSetsAdded;
		}

		public int getValueSetCodesAdded() {
			return myValueSetCodesAdded;
		}

		public int getValueSetInclusionsAdded() {
			return myValueSetInclusionsAdded;
		}

		public void addFrom(RecordsAddedCounter theRecordsAddedCounter) {
			myConceptsAdded += theRecordsAddedCounter.myConceptsAdded;
			myConceptLinksAdded += theRecordsAddedCounter.myConceptLinksAdded;
			myPropertiesAdded += theRecordsAddedCounter.myPropertiesAdded;
			myDesignationsAdded += theRecordsAddedCounter.myDesignationsAdded;
			myConceptsRemoved += theRecordsAddedCounter.myConceptsRemoved;
			myConceptLinksRemoved += theRecordsAddedCounter.myConceptLinksRemoved;
			myPropertiesRemoved += theRecordsAddedCounter.myPropertiesRemoved;
			myDesignationsRemoved += theRecordsAddedCounter.myDesignationsRemoved;
			myConceptMapsAdded += theRecordsAddedCounter.myConceptMapsAdded;
			myConceptMapMappingsAdded += theRecordsAddedCounter.myConceptMapMappingsAdded;
			myValueSetsAdded += theRecordsAddedCounter.myValueSetsAdded;
			myValueSetCodesAdded += theRecordsAddedCounter.myValueSetCodesAdded;
			myValueSetInclusionsAdded += theRecordsAddedCounter.myValueSetInclusionsAdded;
			myOtherChanges += theRecordsAddedCounter.myOtherChanges;
		}

		@Override
		public String toString() {
			HapiToStringBuilder b =
					new HapiToStringBuilder(RecordsAddedCounter.this, ToStringStyle.NO_CLASS_NAME_STYLE);
			b.appendIfNonZero("conceptsAdded", myConceptsAdded);
			b.appendIfNonZero("conceptLinksAdded", myConceptLinksAdded);
			b.appendIfNonZero("conceptPropertiesAdded", myPropertiesAdded);
			b.appendIfNonZero("designationsAdded", myDesignationsAdded);
			b.appendIfNonZero("conceptMapsAdded", myConceptMapsAdded);
			b.appendIfNonZero("conceptMapMappingsAdded", myConceptMapMappingsAdded);
			b.appendIfNonZero("valueSetsAdded", myValueSetsAdded);
			b.appendIfNonZero("valueSetCodesAdded", myValueSetCodesAdded);
			b.appendIfNonZero("valueSetInclusionsAdded", myValueSetInclusionsAdded);
			b.appendIfNonZero("otherChanges", myOtherChanges);
			b.appendIfNonZero("conceptsRemoved", myConceptsRemoved);
			b.appendIfNonZero("conceptLinksRemoved", myConceptLinksRemoved);
			b.appendIfNonZero("conceptPropertiesRemoved", myPropertiesRemoved);
			b.appendIfNonZero("designationsRemoved", myDesignationsRemoved);
			return b.toString();
		}

		public void increment(UploadStatistics theUploadStatistics) {
			incrementConceptsAdded(theUploadStatistics.getAddedConceptCount());
			incrementConceptLinksAdded(theUploadStatistics.getAddedConceptLinkCount());
			incrementPropertiesAdded(theUploadStatistics.getAddedPropertyCount());
			incrementDesignationsAdded(theUploadStatistics.getAddedDesignationCount());
			incrementConceptsRemoved(theUploadStatistics.getRemovedConceptCount());
			incrementConceptLinksRemoved(theUploadStatistics.getRemovedConceptLinkCount());
			incrementPropertiesRemoved(theUploadStatistics.getRemovedPropertyCount());
			incrementDesignationsRemoved(theUploadStatistics.getRemovedDesignationCount());
		}
	}
}
