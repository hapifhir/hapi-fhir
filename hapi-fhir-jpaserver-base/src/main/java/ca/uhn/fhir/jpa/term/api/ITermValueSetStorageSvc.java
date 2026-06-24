package ca.uhn.fhir.jpa.term.api;

import ca.uhn.fhir.jpa.entity.TermValueSetConcept;
import ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum;
import ca.uhn.fhir.jpa.term.UploadStatistics;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.r5.model.ValueSet;

public interface ITermValueSetStorageSvc {

	/**
	 * Creates a new staging version of the given ValueSet, intended for
	 * pre-expanding concepts. The existing version will be marked with status
	 * {@link TermValueSetPreExpansionStatusEnum#EXPANSION_IN_PROGRESS}.
	 * The staging version will be assigned a random UUID version which will
	 * be replaced by the intended version when the staging is complete.
	 *
	 * @param theUrl The ValueSet URI (must not be a versioned URI)
	 * @param theVersion The ValueSet version (can be null)
	 * @return Returns a staging version string
	 * @since 8.12.0
	 */
	@Nonnull
	String startStagingVersion(@Nonnull String theUrl, @Nullable String theVersion);

	/**
	 * Adds concepts to the precalculated expansion of the given ValueSet, using
	 * the <code>ValueSet.url</code> and <code>ValueSet.version</code> to identify
	 * the ValueSet to add to. The concepts in <code>ValueSet.expansion</code>
	 * will be added if they are not already present.
	 *
	 * @param theDelta The ValueSet to add
	 * @param theStartingOrder The index {@link TermValueSetConcept#getOrder()} to use for the first concept added, and incrementing by one for each code.
	 * @return Returns an object containing statistics about what was added
	 */
	@Nonnull
	UploadStatistics addConceptsToExpansion(@Nonnull ValueSet theDelta, int theStartingOrder);




}
