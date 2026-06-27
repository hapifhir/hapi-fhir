package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetDao;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.term.api.ITermValueSetExpansionSvc;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;

import java.util.Date;
import java.util.List;

import static ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum.EXPANDED;
import static ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum.EXPANSION_IN_PROGRESS;
import static ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum.FAILED_TO_EXPAND;
import static ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum.NOT_ACTIVE;
import static ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum.NOT_EXPANDED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link TermValueSetExpansionSvc#getExpansionStatus}.
 *
 * <ul>
 *   <li>Assertions check only the returned {@link Parameters}, never DAO interactions.
 *   <li>Each test stubs one DAO finder to return a distinct marker URL, so the chosen query branch
 *       is visible in the output.
 *   <li>Logic that only affects DAO inputs (LIKE pattern, status parsing, paging) is verified
 *       end-to-end in {@code ResourceProviderR4ValueSetExpansionTest}.
 * </ul>
 */
// Created by claude-opus-4-8
@ExtendWith(MockitoExtension.class)
class TermValueSetExpansionSvcTest {

	private static final Date EXPANSION_TIMESTAMP = new Date(1_700_000_000_000L);

	private final ITermValueSetDao myTermValueSetDao = mock(ITermValueSetDao.class);
	private final ITermValueSetExpansionSvc mySvc = new TermValueSetExpansionSvc(myTermValueSetDao, FhirContext.forR4Cached());

	@Test
	void getExpansionStatus_withBothUrlAndName_throwsInvalidRequestException() {
		StringParam url = new StringParam("http://loinc.org");
		StringParam name = new StringParam("Panel");
		assertThatThrownBy(() -> call(url, name, null, 100, 0))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("HAPI-2985: Only one of 'url' or 'name'");
	}

	@Test
	void getExpansionStatus_withInvalidStatus_throwsInvalidRequestException() {
		String invalidStatus = "NOT_A_VALID_STATUS";
		List<String> statuses = List.of(invalidStatus);
		assertThatThrownBy(() -> call(null, null, statuses, 100, 0))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("HAPI-2986: Invalid expansionStatus value: `" + invalidStatus + "`");
	}

	@Test
	void getExpansionStatus_withNoFilter_returnsRowsFromStatusInQuery() {
		givenCounts();
		when(myTermValueSetDao.findByExpansionStatusIn(any(), any()))
				.thenReturn(new SliceImpl<>(List.of(marker("marker:statusIn"))));

		Parameters response = call(null, null, null, 100, 0);

		assertThat(extractValueSetUrls(response)).containsExactly("marker:statusIn");
	}

	@Test
	void getExpansionStatus_withEmptyStatusList_returnsRowsFromStatusInQuery() {
		givenCounts();
		when(myTermValueSetDao.findByExpansionStatusIn(any(), any()))
				.thenReturn(new SliceImpl<>(List.of(marker("marker:statusIn"))));

		Parameters response = call(null, null, List.of(), 100, 0);

		assertThat(extractValueSetUrls(response)).containsExactly("marker:statusIn");
	}

	@Test
	void getExpansionStatus_withUrlExact_returnsRowsFromUrlEqualsQuery() {
		givenCounts();
		when(myTermValueSetDao.findByExpansionStatusInAndUrlEquals(any(), any(), any()))
				.thenReturn(new SliceImpl<>(List.of(marker("marker:urlEquals"))));

		Parameters response = call(new StringParam("http://loinc.org").setExact(true), null, null, 100, 0);

		assertThat(extractValueSetUrls(response)).containsExactly("marker:urlEquals");
	}

	@Test
	void getExpansionStatus_withUrlNonExact_returnsRowsFromUrlLikeQuery() {
		givenCounts();
		when(myTermValueSetDao.findByExpansionStatusInAndUrlLike(any(), any(), any()))
				.thenReturn(new SliceImpl<>(List.of(marker("marker:urlLike"))));

		Parameters response = call(new StringParam("loinc"), null, null, 100, 0);

		assertThat(extractValueSetUrls(response)).containsExactly("marker:urlLike");
	}

	@Test
	void getExpansionStatus_withNameExact_returnsRowsFromNameEqualsQuery() {
		givenCounts();
		when(myTermValueSetDao.findByExpansionStatusInAndNameEquals(any(), any(), any()))
				.thenReturn(new SliceImpl<>(List.of(marker("marker:nameEquals"))));

		Parameters response = call(null, new StringParam("My ValueSet").setExact(true), null, 100, 0);

		assertThat(extractValueSetUrls(response)).containsExactly("marker:nameEquals");
	}

	@Test
	void getExpansionStatus_withNameNonExact_returnsRowsFromNameLikeQuery() {
		givenCounts();
		when(myTermValueSetDao.findByExpansionStatusInAndNameLike(any(), any(), any()))
				.thenReturn(new SliceImpl<>(List.of(marker("marker:nameLike"))));

		Parameters response = call(null, new StringParam("Panel"), null, 100, 0);

		assertThat(extractValueSetUrls(response)).containsExactly("marker:nameLike");
	}

	@Test
	void getExpansionStatus_withMixedStatusCounts_returnsPerStatusSummary() {
		givenCounts(new Object[] {EXPANDED, 5L}, new Object[] {FAILED_TO_EXPAND, 3L});
		when(myTermValueSetDao.findByExpansionStatusIn(any(), any())).thenReturn(new SliceImpl<>(List.of()));

		Parameters response = call(null, null, null, 100, 0);

		ParametersParameterComponent summary = response.getParameter("summary");
		assertThat(summary).isNotNull();
		assertThat(extractParamInt(summary, "total")).isEqualTo(8);
		assertThat(extractParamInt(summary, EXPANDED.name())).isEqualTo(5);
		assertThat(extractParamInt(summary, FAILED_TO_EXPAND.name())).isEqualTo(3);
		assertThat(extractParamInt(summary, NOT_EXPANDED.name())).isZero();
		assertThat(extractParamInt(summary, EXPANSION_IN_PROGRESS.name())).isZero();
		assertThat(extractParamInt(summary, NOT_ACTIVE.name())).isZero();
	}

	@Test
	void getExpansionStatus_withNoValueSets_returnsZeroedSummaryAndNoEntries() {
		givenCounts();
		when(myTermValueSetDao.findByExpansionStatusIn(any(), any())).thenReturn(new SliceImpl<>(List.of()));

		Parameters response = call(null, null, null, 100, 0);

		ParametersParameterComponent summary = response.getParameter("summary");
		assertThat(extractParamInt(summary, "total")).isZero();
		assertThat(extractParamInt(summary, EXPANDED.name())).isZero();
		assertThat(extractParamInt(summary, FAILED_TO_EXPAND.name())).isZero();
		assertThat(extractValueSetUrls(response)).isEmpty();
	}

	@Test
	void getExpansionStatus_whenMoreResultsExist_setsHasMoreTrue() {
		givenCounts();
		when(myTermValueSetDao.findByExpansionStatusIn(any(), any()))
				.thenReturn(new SliceImpl<>(List.of(), PageRequest.of(0, 10), true));

		Parameters response = call(null, null, null, 10, 0);

		assertThat(extractParamString(response.getParameter("summary"), "hasMore")).isEqualTo("true");
	}

	@Test
	void getExpansionStatus_whenLastPage_setsHasMoreFalse() {
		givenCounts();
		when(myTermValueSetDao.findByExpansionStatusIn(any(), any()))
				.thenReturn(new SliceImpl<>(List.of(), PageRequest.of(0, 10), false));

		Parameters response = call(null, null, null, 10, 0);

		assertThat(extractParamString(response.getParameter("summary"), "hasMore")).isEqualTo("false");
	}

	@Test
	void getExpansionStatus_withFullyPopulatedValueSet_emitsAllParts() {
		givenCounts();
		TermValueSet vs = createTermValueSet("123", "http://vs/full", "Full VS", "1.0", EXPANDED, EXPANSION_TIMESTAMP, "boom");
		when(myTermValueSetDao.findByExpansionStatusIn(any(), any())).thenReturn(new SliceImpl<>(List.of(vs)));

		Parameters response = call(null, null, null, 100, 0);

		ParametersParameterComponent entry = extractValueSetParamByUrl(response, "http://vs/full");
		assertThat(extractParamString(entry, "name")).isEqualTo("Full VS");
		assertThat(extractParamString(entry, "version")).isEqualTo("1.0");
		assertThat(extractParamString(entry, "resourceId")).isEqualTo("ValueSet/123");
		assertThat(extractParamString(entry, "expansionStatus")).isEqualTo("EXPANDED");
		assertThat(extractParamString(entry, "errorMessage")).isEqualTo("boom");
		assertThat(extractAllParamNames(entry)).contains("expansionTimestamp");
	}

	@Test
	void getExpansionStatus_withMinimalValueSet_omitsOptionalParts() {
		givenCounts();
		TermValueSet vs = createTermValueSet("456", "http://vs/min", null, null, NOT_EXPANDED, null, null);
		when(myTermValueSetDao.findByExpansionStatusIn(any(), any())).thenReturn(new SliceImpl<>(List.of(vs)));

		Parameters response = call(null, null, null, 100, 0);

		ParametersParameterComponent entry = extractValueSetParamByUrl(response, "http://vs/min");
		assertThat(extractParamString(entry, "expansionStatus")).isEqualTo("NOT_EXPANDED");
		assertThat(extractAllParamNames(entry)).containsExactlyInAnyOrder("url", "resourceId", "expansionStatus");
	}

	/** Stubs the aggregate status-count query with the given {@code [status, count]} rows. */
	private void givenCounts(Object[]... theRows) {
		when(myTermValueSetDao.countByExpansionStatus()).thenReturn(List.of(theRows));
	}

	private Parameters call(
			StringParam theUrl, StringParam theName, List<String> theStatuses, int theCount, int theOffset) {
		return (Parameters) mySvc.getExpansionStatus(theUrl, theName, theStatuses, theCount, theOffset);
	}

	private static TermValueSet marker(String theUrl) {
		return createTermValueSet("res", theUrl, null, null, NOT_EXPANDED, null, null);
	}

	private static TermValueSet createTermValueSet(
			String theFhirId,
			String theUrl,
			String theName,
			String theVersion,
			TermValueSetPreExpansionStatusEnum theStatus,
			Date theTimestamp,
			String theError) {
		ResourceTable resource = new ResourceTable();
		resource.setFhirId(theFhirId);

		return new TermValueSet().setResource(resource).setUrl(theUrl)
			.setName(theName).setVersion(theVersion)
			.setExpansionStatus(theStatus).setExpansionTimestamp(theTimestamp).setExpansionError(theError);
	}

	private static List<String> extractValueSetUrls(Parameters theResponse) {
		return theResponse.getParameter().stream()
				.filter(p -> "valueSet".equals(p.getName()))
				.map(p -> extractParamString(p, "url"))
				.toList();
	}

	private static ParametersParameterComponent extractValueSetParamByUrl(Parameters theResponse, String theUrl) {
		return theResponse.getParameter().stream()
				.filter(p -> "valueSet".equals(p.getName()))
				.filter(p -> theUrl.equals(extractParamString(p, "url")))
				.findFirst()
				.orElseThrow();
	}

	private static int extractParamInt(ParametersParameterComponent theParam, String theName) {
		return Integer.parseInt(extractParamString(theParam, theName));
	}

	private static String extractParamString(ParametersParameterComponent theParam, String theName) {
		return theParam.getPart().stream()
				.filter(p -> theName.equals(p.getName()))
				.findFirst()
				.map(p -> p.getValue().primitiveValue())
				.orElse(null);
	}

	private static List<String> extractAllParamNames(ParametersParameterComponent theParam) {
		return theParam.getPart().stream()
				.map(ParametersParameterComponent::getName)
				.toList();
	}
}
