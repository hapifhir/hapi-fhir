package ca.uhn.fhir.jpa.batch2.jobs.term.base;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.HapiToStringBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hl7.fhir.r4.model.CodeSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class TerminologyFileSetJson implements IModelJson {

	@JsonProperty("chunkForCurrentStep")
	private Chunk myChunkForCurrentStep;
	@JsonProperty("resourcesToActivate")
	private Set<String> myResourcesToActivate;
	@JsonProperty("conceptPidsToGenerateClosureFor")
	private List<Long> myConceptPidsToGenerateClosureFor;
	@JsonProperty("stepIdToRecordsAdded")
	private Map<String, RecordsAddedCounter> myStepIdToRecordsAdded;

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

	public Chunk getChunkForCurrentStep() {
		return myChunkForCurrentStep;
	}

	public void setChunkForCurrentStep(Chunk theChunkForCurrentStep) {
		myChunkForCurrentStep = theChunkForCurrentStep;
	}

	public void addResourceToActivate(String theResourceToActivate) {
		getResourcesToActivate().add(theResourceToActivate);
	}



	public boolean isEmpty() {
		return myChunkForCurrentStep == null &&
			(myResourcesToActivate == null || myResourcesToActivate.isEmpty()) &&
			(myStepIdToRecordsAdded == null || myStepIdToRecordsAdded.isEmpty());
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

	public static class Chunk implements IModelJson {

		@JsonProperty("attachmentId")
		private String myAttachmentId;
		@JsonProperty("sourceFilename")
		private String mySourceFilename;

		/**
		 * Constructor
		 */
		public Chunk() {
			super();
		}

		/**
		 * Constructor
		 */
		public Chunk(String theSourceFilename, String theAttachmentId) {
			setSourceFilename(theSourceFilename);
			setAttachmentId(theAttachmentId);
		}

		@Override
		public boolean equals(Object theO) {
			if (!(theO instanceof Chunk chunk)) {
				return false;
			}
			return Objects.equals(myAttachmentId, chunk.myAttachmentId) && Objects.equals(mySourceFilename, chunk.mySourceFilename);
		}

		@Override
		public int hashCode() {
			return Objects.hash(myAttachmentId, mySourceFilename);
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
				.append("sourceFilename", mySourceFilename)
				.append("attachment", myAttachmentId)
				.toString();
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

		public int getOtherChanges() {
			return myOtherChanges;
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

		public void copyFrom(RecordsAddedCounter theRecordsAddedCounter) {
			myConceptsAdded += theRecordsAddedCounter.myConceptsAdded;
			myConceptLinksAdded += theRecordsAddedCounter.myConceptLinksAdded;
			myPropertiesAdded += theRecordsAddedCounter.myPropertiesAdded;
			myDesignationsAdded += theRecordsAddedCounter.myDesignationsAdded;
			myConceptMapsAdded += theRecordsAddedCounter.myConceptMapsAdded;
			myConceptMapMappingsAdded += theRecordsAddedCounter.myConceptMapMappingsAdded;
			myValueSetsAdded += theRecordsAddedCounter.myValueSetsAdded;
			myValueSetCodesAdded += theRecordsAddedCounter.myValueSetCodesAdded;
			myValueSetInclusionsAdded += theRecordsAddedCounter.myValueSetInclusionsAdded;
			myOtherChanges += theRecordsAddedCounter.myOtherChanges;
		}

		@Override
		public String toString() {
			HapiToStringBuilder b = new HapiToStringBuilder(RecordsAddedCounter.this, ToStringStyle.NO_CLASS_NAME_STYLE);
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
			return b.toString();
		}

	}


}
