package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.IdType;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BulkExportJobParametersBuilderTest {

	private final BulkExportJobParametersBuilder myFixture = new BulkExportJobParametersBuilder();

	@Test
	void resourceTypes() {
		// Arrange
		final List<String> expected = List.of("Patient", "Observation", "MedicationRequest");
		final IPrimitiveType<String> resourceTypes = new StringDt(String.join(",", expected));
		// Act
		myFixture.resourceTypes(resourceTypes);
		// Assert
		assertThat(myFixture.build().getResourceTypes()).containsAll(expected);
	}

	@Test
	void resourceTypesWhenNull() {
		// Act
		myFixture.resourceTypes(null);
		// Assert
		assertThat(myFixture.build().getResourceTypes()).isEmpty();
	}

	@Test
	void since() {
		// Arrange
		final Date expected = new Date();
		final IPrimitiveType<Date> since = new DateDt(expected);
		// Act
		myFixture.since(since);
		// Assert
		assertThat(myFixture.build().getSince()).isEqualTo(expected);
	}

	@Test
	void until() {
		// Arrange
		final Date expected = new Date();
		final IPrimitiveType<Date> until = new DateDt(expected);
		// Act
		myFixture.until(until);
		// Assert
		assertThat(myFixture.build().getUntil()).isEqualTo(expected);
	}

	@Test
	void filters() {
		// Arrange
		final List<String> expected = List.of("Patient", "Observation", "MedicationRequest");
		final List<IPrimitiveType<String>> filters = expected.stream().map(value -> (IPrimitiveType<String>) new StringDt(value)).toList();
		// Act
		myFixture.filters(filters);
		// Assert
		assertThat(myFixture.build().getFilters()).containsAll(expected);
	}

	@Test
	void filtersWhenNull() {
		// Act
		myFixture.filters(null);
		// Assert
		assertThat(myFixture.build().getFilters()).isEmpty();
	}

	@Test
	void outputFormat() {
		// Arrange
		final String expected = "some value";
		final IPrimitiveType<String> outputFormat = new StringDt(expected);
		// Act
		myFixture.outputFormat(outputFormat);
		// Assert
		assertThat(myFixture.build().getOutputFormat()).isEqualTo(expected);
	}

	@Test
	void outputFormatWhenNull() {
		// Act
		myFixture.outputFormat(null);
		// Assert
		assertThat(myFixture.build().getOutputFormat()).isEqualTo(Constants.CT_FHIR_NDJSON);
	}

	@Test
	void exportStyle() {
		// Arrange
		final BulkExportJobParameters.ExportStyle expected = BulkExportJobParameters.ExportStyle.SYSTEM;
		// Act
		myFixture.exportStyle(expected);
		// Assert
		assertThat(myFixture.build().getExportStyle()).isEqualTo(expected);
	}

	@Test
	void patientIds() {
		// Arrange
		final List<String> expected = List.of("ID1", "ID2", "ID3");
		final List<IPrimitiveType<String>> patientIds = expected.stream().map(value -> (IPrimitiveType<String>) new StringDt(value)).toList();
		// Act
		myFixture.patientIds(patientIds);
		// Assert
		assertThat(myFixture.build().getPatientIds()).containsAll(expected);
	}

	@Test
	void patientIdsWhenNull() {
		// Act
		myFixture.patientIds(null);
		// Assert
		assertThat(myFixture.build().getPatientIds()).isEmpty();
	}

	@Test
	void groupId() {
		// Arrange
		final String expected = "GROUP_ID";
		final IdType groupId = new IdType(expected);
		// Act
		myFixture.groupId(groupId);
		// Assert
		assertThat(myFixture.build().getGroupId()).isEqualTo(expected);
	}

	@Test
	void expandMdm() {
		// Arrange
		final IPrimitiveType<Boolean> expandMdm = new BooleanDt(Boolean.TRUE);
		// Act
		myFixture.expandMdm(expandMdm);
		// Assert
		assertThat(myFixture.build().isExpandMdm()).isTrue();
	}

	@Test
	void expandMdmWhenNull() {
		// Act
		myFixture.expandMdm(null);
		// Assert
		assertThat(myFixture.build().isExpandMdm()).isFalse();
	}

	@Test
	void partitionId() {
		// Arrange
		final RequestPartitionId expected = RequestPartitionId.fromPartitionName("PARTITION_NAME");
		// Act
		myFixture.partitionId(expected);
		// Assert
		assertThat(myFixture.build().getPartitionId()).isEqualTo(expected);
	}

	@Test
	void exportIdentifier() {
		// Arrange
		final String expected = "EXPORT_IDENTIFIER";
		final StringDt exportIdentifier = new StringDt(expected);
		// Act
		myFixture.exportIdentifier(exportIdentifier);
		// Assert
		assertThat(myFixture.build().getExportIdentifier()).isEqualTo(expected);
	}

	@Test
	void postFetchFilterUrl() {
		// Arrange
		final List<String> expected = List.of("URL1", "URL2", "URL3");
		final List<IPrimitiveType<String>> postFetchFilterUrls = expected.stream().map(value -> (IPrimitiveType<String>) new StringDt(value)).toList();
		// Act
		myFixture.postFetchFilterUrl(postFetchFilterUrls);
		// Assert
		assertThat(myFixture.build().getPostFetchFilterUrls()).containsAll(expected);
	}

	@Test
	void postFetchFilterUrlWhenNull() {
		// Act
		myFixture.postFetchFilterUrl(null);
		// Assert
		assertThat(myFixture.build().getPostFetchFilterUrls()).isEmpty();
	}

}
