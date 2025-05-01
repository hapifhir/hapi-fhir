package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.PreferReturnEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class BulkDataExportUtilTest {
	private static final String URL = "http://localhost:8080";
	private static final String OPERATION_NAME = "Operation Name";

	@Mock
	private ServletRequestDetails theRequestDetails;

	@AfterEach
	void tearDown() {
		verifyNoMoreInteractions(theRequestDetails);
	}

	@ParameterizedTest
	@EnumSource(value = PreferReturnEnum.class)
	void validatePreferAsyncHeaderShouldThrowException(PreferReturnEnum thePreferReturnEnum) {
		// Arrange
		doReturn(thePreferReturnEnum.getHeaderValue()).when(theRequestDetails).getHeader(Constants.HEADER_PREFER);
		// Act
		assertThatThrownBy(() -> BulkDataExportUtil.validatePreferAsyncHeader(theRequestDetails, OPERATION_NAME))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("Must request async processing for " + OPERATION_NAME);
		// Assert
		verify(theRequestDetails).getHeader(Constants.HEADER_PREFER);
	}

	@Test
	void validatePreferAsyncHeaderShouldNotThrowException() {
		// Arrange
		doReturn(Constants.HEADER_PREFER_RESPOND_ASYNC).when(theRequestDetails).getHeader(Constants.HEADER_PREFER);
		// Act
		assertThatNoException().isThrownBy(() -> BulkDataExportUtil.validatePreferAsyncHeader(theRequestDetails, OPERATION_NAME));
		// Assert
		verify(theRequestDetails).getHeader(Constants.HEADER_PREFER);
	}

	@Test
	void getServerBase() {
		// Arrange
		doReturn(URL + "/").when(theRequestDetails).getServerBaseForRequest();
		// Act
		final String actual = BulkDataExportUtil.getServerBase(theRequestDetails);
		// Assert
		assertThat(actual).isEqualTo(URL);
		verify(theRequestDetails).getServerBaseForRequest();
	}
}
