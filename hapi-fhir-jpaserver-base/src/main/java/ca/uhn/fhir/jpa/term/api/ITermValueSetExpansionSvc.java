package ca.uhn.fhir.jpa.term.api;

import ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseParameters;

import java.util.List;

/**
 * Service backing HAPI-specific {@code ValueSet} terminology operations that operate over the
 * pre-expansion metadata stored in {@code TRM_VALUESET}.
 *
 * @see ca.uhn.fhir.jpa.provider.ValueSetOperationProvider
 * @since 8.12.0
 */
public interface ITermValueSetExpansionSvc {

	// Names of the parts emitted in the operation response.
	String SUMMARY = "summary";
	String VALUESET = "ValueSet";
	String TOTAL = "total";
	String HAS_MORE = "hasMore";
	String URL = "url";
	String NAME = "name";
	String VERSION = "version";
	String RESOURCE_ID = "resourceId";
	String EXPANSION_STATUS = "expansionStatus";
	String EXPANSION_TIMESTAMP = "expansionTimestamp";
	String ERROR_MESSAGE = "errorMessage";

	/**
	 * Returns a {@code Parameters} resource with a {@code summary} part (repository-wide status counts)
	 * followed by one {@code valueSet} part per matching ValueSet for the requested page.
	 *
	 * <p>{@code url} and {@code name} are mutually exclusive; both default to a starts-with match and
	 * accept {@code :contains} and {@code :exact} qualifiers. Omitting all filters returns the first
	 * page across every ValueSet.
	 *
	 * @param theUrlParam URL filter, or {@code null} for none
	 * @param theNameParam name filter, or {@code null} for none
	 * @param theExpansionStatuses status filter; {@code null} or empty returns all statuses
	 * @param theCount page size
	 * @param theOffset zero-based row offset
	 * @throws InvalidRequestException if both {@code theUrlParam} and {@code theNameParam} are given,
	 *     or a status value is not a {@link TermValueSetPreExpansionStatusEnum} name
	 */
	IBaseParameters getExpansionStatus(
		@Nullable StringParam theUrlParam,
		@Nullable StringParam theNameParam,
		@Nullable List<String> theExpansionStatuses,
		int theCount,
		int theOffset);
}
