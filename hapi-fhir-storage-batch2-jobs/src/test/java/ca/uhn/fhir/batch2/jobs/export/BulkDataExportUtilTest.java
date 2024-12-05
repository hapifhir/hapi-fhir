package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.context.FhirVersionEnum;
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

	@Mock
	private ServletRequestDetails theRequestDetails;

	@AfterEach
	void tearDown() {
		verifyNoMoreInteractions(theRequestDetails);
	}

	@ParameterizedTest
	@EnumSource(value = PreferReturnEnum.class)
	void validatePreferAsyncHeader(PreferReturnEnum thePreferReturnEnum) {
		// Arrange
		doReturn(thePreferReturnEnum.getHeaderValue()).when(theRequestDetails).getHeader(Constants.HEADER_PREFER);
		// Act
		assertThatThrownBy(() -> BulkDataExportUtil.validatePreferAsyncHeader(theRequestDetails, "Operation Name"))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("Must request async processing for Operation Name");
		// Assert
		verify(theRequestDetails).getHeader(Constants.HEADER_PREFER);
	}

	@ParameterizedTest
	@EnumSource(value = FhirVersionEnum.class, mode = EnumSource.Mode.EXCLUDE, names = {"R5"})
	void isDeviceResourceSupportedForPatientCompartmentForFhirVersion(FhirVersionEnum theFhirVersionEnum) {
		// Act
		final boolean actual = BulkDataExportUtil.isDeviceResourceSupportedForPatientCompartmentForFhirVersion(theFhirVersionEnum);
		// Assert
		assertThat(actual).isTrue();
	}

	@ParameterizedTest
	@EnumSource(value = FhirVersionEnum.class, mode = EnumSource.Mode.INCLUDE, names = {"R5"})
	void isDeviceResourceNotSupportedForPatientCompartmentForFhirVersion(FhirVersionEnum theFhirVersionEnum) {
		// Act
		final boolean actual = BulkDataExportUtil.isDeviceResourceSupportedForPatientCompartmentForFhirVersion(theFhirVersionEnum);
		// Assert
		assertThat(actual).isFalse();
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
