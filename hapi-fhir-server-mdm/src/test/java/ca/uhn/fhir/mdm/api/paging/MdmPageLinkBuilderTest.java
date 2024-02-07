package ca.uhn.fhir.mdm.api.paging;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MdmPageLinkBuilderTest {

	@Test
	void buildLinkWithExistingParameters() {
		//Given
		String expected = "http://localhost:8000/$mdm-query-links?sourceResourceId=Patient/123&_offset=1&_count=1";
		String baseUrl = "http://localhost:8000/$mdm-query-links?sourceResourceId=Patient/123";

		//When
		String builtUrl = MdmPageLinkBuilder.buildLinkWithOffsetAndCount(baseUrl, 1, 1);

		//Then
		assertThat(builtUrl).isEqualTo(expected);
	}

	@Test
	void buildLinkWithoutExistingParameters() {
		//Given
		String expected = "http://localhost:8000/$mdm-query-links?_offset=1&_count=1";
		String baseUrl = "http://localhost:8000/$mdm-query-links";

		//When
		String builtUrl = MdmPageLinkBuilder.buildLinkWithOffsetAndCount(baseUrl, 1, 1);

		//Then
		assertThat(builtUrl).isEqualTo(expected);
	}
}
