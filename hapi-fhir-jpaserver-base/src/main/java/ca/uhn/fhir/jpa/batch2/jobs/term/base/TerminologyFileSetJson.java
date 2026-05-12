package ca.uhn.fhir.jpa.batch2.jobs.term.base;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static org.apache.commons.lang3.ObjectUtils.getIfNull;

/**
 * In terminology import batch2 jobs, the first step extracts the archive containing
 * any+all files that will be processed and creates the chunks that will be processed
 * by subsequent steps. This means that step 1 is creating all the chunks for step 2,
 * but also all the chunks for step 3, and so on. This class is emitted by step 1 and
 * holds the chunks for all later steps. Each step pull the chunks for the next
 * step out and passes them individually and sends all the chunks for future steps beyond
 * that one as a single batch.
 */
public class TerminologyFileSetJson implements IModelJson {

	@JsonProperty("stepIdToFutureChunks")
	private Map<String, List<Chunk>> myStepIdToFutureChunks;
	@JsonProperty("chunkForCurrentStep")
	private Chunk myChunkForCurrentStep;
	@JsonProperty("resourcesToActivate")
	private Set<String> myResourcesToActivate;

	public void addChunk(String theStepId, String theSourceFilename, String theChunkAttachmentId) {
		Validate.notBlank(theStepId, "theStepId must not be null or blank");
		Validate.notBlank(theChunkAttachmentId, "theChunkAttachmentId must not be null or blank");
		initializeStepIdToChunkAttachmentIdsIfNecessary();
		myStepIdToFutureChunks
			.computeIfAbsent(theStepId, k -> new ArrayList<>())
			.add(new Chunk(theSourceFilename, theChunkAttachmentId));
	}

	public List<Chunk> getAndRemoveFutureChunksForStepId(String theStepId) {
		initializeStepIdToChunkAttachmentIdsIfNecessary();
		return getIfNull(myStepIdToFutureChunks.remove(theStepId), List.of());
	}

	private void initializeStepIdToChunkAttachmentIdsIfNecessary() {
		if (myStepIdToFutureChunks == null) {
			myStepIdToFutureChunks = new HashMap<>();
		}
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

	@SuppressWarnings("unchecked")
	public <OT extends TerminologyFileSetJson> OT cloneWithOnlyFutureChunks() {
		TerminologyFileSetJson retVal = new TerminologyFileSetJson();
		populateFutureChunksInClone(retVal);
		return (OT) retVal;
	}

	protected void populateFutureChunksInClone(TerminologyFileSetJson retVal) {
		retVal.myStepIdToFutureChunks = myStepIdToFutureChunks;
	}

	public boolean isEmpty() {
		return myChunkForCurrentStep == null &&
			(myStepIdToFutureChunks == null || myStepIdToFutureChunks.isEmpty()) &&
			(myResourcesToActivate == null || myResourcesToActivate.isEmpty());
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

}
