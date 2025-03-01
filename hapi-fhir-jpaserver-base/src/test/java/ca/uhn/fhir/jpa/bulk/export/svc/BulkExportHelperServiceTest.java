package ca.uhn.fhir.jpa.bulk.export.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.util.DateUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class BulkExportHelperServiceTest {

	@Mock
	private MatchUrlService myMatchUrlService;

	@Mock
	private FhirContext myContext;

	@InjectMocks
	private BulkExportHelperService myFixture;

	@AfterEach
	void tearDown() {
		verifyNoMoreInteractions(myMatchUrlService, myContext);
	}

	@Test
	void addLastUpdatedFilterShouldBeNullWhenNoDatesGiven() {
		// Arrange
		final SearchParameterMap searchParameterMap = new SearchParameterMap();
		// Act
		myFixture.addLastUpdatedFilter(searchParameterMap, null, null);
		// Assert
		assertThat(searchParameterMap.getLastUpdated()).isNull();
	}

	@Test
	void addLastUpdatedFilterShouldContainsStartDateWhenStartDateIsGiven() {
		// Arrange
		final SearchParameterMap searchParameterMap = new SearchParameterMap();
		final Date startDate = new Date();
		final DateRangeParam expected = new DateRangeParam(startDate, null);
		// Act
		myFixture.addLastUpdatedFilter(searchParameterMap, startDate, null);
		// Assert
		assertThat(searchParameterMap.getLastUpdated()).isEqualTo(expected);
	}

	@Test
	void addLastUpdatedFilterShouldContainsEndDateWhenEndDateIsGiven() {
		// Arrange
		final SearchParameterMap searchParameterMap = new SearchParameterMap();
		final Date endDate = new Date();
		final DateRangeParam expected = new DateRangeParam(null, endDate);
		// Act
		myFixture.addLastUpdatedFilter(searchParameterMap, null, endDate);
		// Assert
		assertThat(searchParameterMap.getLastUpdated()).isEqualTo(expected);
	}

	@Test
	void addLastUpdatedFilterShouldContainsDateRangeWhenStartAndEndDateIsGiven() {
		// Arrange
		final SearchParameterMap searchParameterMap = new SearchParameterMap();
		final Date startDate = new Date();
		final Date endDate = DateUtils.getEndOfDay(startDate);
		final DateRangeParam expected = new DateRangeParam(startDate, endDate);
		// Act
		myFixture.addLastUpdatedFilter(searchParameterMap, startDate, endDate);
		// Assert
		assertThat(searchParameterMap.getLastUpdated()).isEqualTo(expected);
	}

}
