package ca.uhn.fhir.rest.param;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


public class DateRangeParamTest {
	private FhirContext fhirContext;

	@BeforeEach
	public void initMockContext() {
		fhirContext = Mockito.mock(FhirContext.class);
	}

	/**
	 * Can happen e.g. when the query parameter for {@code _lastUpdated} is left empty.
	 */
	@Test
	public void testParamWithoutPrefixAndWithoutValue_dateRangeParamRemainsEmpty() {
		QualifiedParamList qualifiedParamList = new QualifiedParamList(1);
		qualifiedParamList.add("");

		List<QualifiedParamList> params = new ArrayList<>(1);
		params.add(qualifiedParamList);
		DateRangeParam dateRangeParam = new DateRangeParam();
		dateRangeParam.setValuesAsQueryTokens(fhirContext, "_lastUpdated", params);

		assertThat(dateRangeParam.isEmpty()).isTrue();
	}

	/**
	 * Can happen e.g. when the query parameter for {@code _lastUpdated} is given as {@code lt} without any value.
	 */
	@Test
	public void testUpperBoundWithPrefixWithoutValue_throwsDateFormatException() {
		QualifiedParamList qualifiedParamList = new QualifiedParamList(1);
		qualifiedParamList.add("lt");

		List<QualifiedParamList> params = new ArrayList<>(1);
		params.add(qualifiedParamList);
		DateRangeParam dateRangeParam = new DateRangeParam();
		try {
			dateRangeParam.setValuesAsQueryTokens(fhirContext, "_lastUpdated", params);
			fail("");
		} catch (DataFormatException e) {
			// good
		}
	}

	/**
	 * Can happen e.g. when the query parameter for {@code _lastUpdated} is given as {@code gt} without any value.
	 */
	@Test
	public void testLowerBoundWithPrefixWithoutValue_throwsDateFormatException() {
		QualifiedParamList qualifiedParamList = new QualifiedParamList(1);
		qualifiedParamList.add("gt");

		List<QualifiedParamList> params = new ArrayList<>(1);
		params.add(qualifiedParamList);
		DateRangeParam dateRangeParam = new DateRangeParam();
		try {
			dateRangeParam.setValuesAsQueryTokens(fhirContext, "_lastUpdated", params);
			fail("");
		} catch (DataFormatException e) {
			// good
		}
	}

	@Test
	public void testSetValueAsQueryTokens_neYear_setsUpperAndLowerBounds() {
		QualifiedParamList qualifiedParamList = new QualifiedParamList(1);
		qualifiedParamList.add("ne1965");

		List<QualifiedParamList> params = new ArrayList<>(1);
		params.add(qualifiedParamList);
		DateRangeParam dateRangeParam = new DateRangeParam();

		dateRangeParam.setValuesAsQueryTokens(fhirContext, "_lastUpdated", params);
		assertThat(dateRangeParam.getLowerBound().getValueAsString()).isEqualTo("1965");
		assertThat(dateRangeParam.getUpperBound().getValueAsString()).isEqualTo("1965");
		assertThat(dateRangeParam.getLowerBound().getPrefix()).isEqualTo(ParamPrefixEnum.NOT_EQUAL);
		assertThat(dateRangeParam.getUpperBound().getPrefix()).isEqualTo(ParamPrefixEnum.NOT_EQUAL);
	}

	@Test
	public void testSetValueAsQueryTokens_neMonth_setsUpperAndLowerBounds() {
		QualifiedParamList qualifiedParamList = new QualifiedParamList(1);
		qualifiedParamList.add("ne1965-11");

		List<QualifiedParamList> params = new ArrayList<>(1);
		params.add(qualifiedParamList);
		DateRangeParam dateRangeParam = new DateRangeParam();

		dateRangeParam.setValuesAsQueryTokens(fhirContext, "_lastUpdated", params);
		assertThat(dateRangeParam.getLowerBound().getValueAsString()).isEqualTo("1965-11");
		assertThat(dateRangeParam.getUpperBound().getValueAsString()).isEqualTo("1965-11");
		assertThat(dateRangeParam.getLowerBound().getPrefix()).isEqualTo(ParamPrefixEnum.NOT_EQUAL);
		assertThat(dateRangeParam.getUpperBound().getPrefix()).isEqualTo(ParamPrefixEnum.NOT_EQUAL);
	}

	@Test
	public void testSetValueAsQueryTokens_neDay_setsUpperAndLowerBounds() {
		QualifiedParamList qualifiedParamList = new QualifiedParamList(1);
		qualifiedParamList.add("ne1965-11-23");

		List<QualifiedParamList> params = new ArrayList<>(1);
		params.add(qualifiedParamList);
		DateRangeParam dateRangeParam = new DateRangeParam();

		dateRangeParam.setValuesAsQueryTokens(fhirContext, "_lastUpdated", params);
		assertThat(dateRangeParam.getLowerBound().getValueAsString()).isEqualTo("1965-11-23");
		assertThat(dateRangeParam.getUpperBound().getValueAsString()).isEqualTo("1965-11-23");
		assertThat(dateRangeParam.getLowerBound().getPrefix()).isEqualTo(ParamPrefixEnum.NOT_EQUAL);
		assertThat(dateRangeParam.getUpperBound().getPrefix()).isEqualTo(ParamPrefixEnum.NOT_EQUAL);
	}

	@Test
	public void testCopyConstructor() {
		DateParam dateStart = new DateParam("gt2021-01-01");
		DateParam dateEnd = new DateParam("lt2021-02-01");
		DateRangeParam input = new DateRangeParam(dateStart, dateEnd);

		DateRangeParam copy = new DateRangeParam(input);

		assertThat(copy.getLowerBound()).isEqualTo(dateStart);
		assertThat(copy.getUpperBound()).isEqualTo(dateEnd);

	}

}
