package ca.uhn.fhir.jpa.bulk.export;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.export.ExpandResourceAndWriteBinaryStep;
import ca.uhn.fhir.batch2.jobs.export.ExpandResourcesStep;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportBinaryFileId;
import ca.uhn.fhir.batch2.jobs.export.models.ExpandedResourcesList;
import ca.uhn.fhir.batch2.jobs.export.models.ResourceIdList;
import ca.uhn.fhir.batch2.jobs.models.BatchResourceId;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import jakarta.persistence.Id;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ExpandResourcesAndWriteBinaryStepJpaTest extends BaseJpaR4Test {

	@Autowired
	private ExpandResourceAndWriteBinaryStep myExpandResourcesStep;

	@Mock
	private IJobDataSink<BulkExportBinaryFileId> mySink;
	@Captor
	private ArgumentCaptor<BulkExportBinaryFileId> myWorkChunkCaptor;

	@Override
	public void afterCleanupDao() {
		super.afterCleanupDao();

		JpaStorageSettings defaults = new JpaStorageSettings();
		myStorageSettings.setBulkExportFileMaximumSize(defaults.getBulkExportFileMaximumSize());
	}

	@Test
	public void testMaximumChunkSize() {
		/*
		 * We're going to set the maximum file size to 3000, and create some resources with
		 * a name that is 1000 chars long. With the other boilerplate text in a resource that
		 * will put the resource length at just over 1000 chars, meaning that any given
		 * chunk or file should have only 2 resources in it.
		 */
		int testResourceSize = 1000;
		int maxFileSize = 3 * testResourceSize;
		myStorageSettings.setBulkExportFileMaximumSize(maxFileSize);

		List<BatchResourceId> expectedIds = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			Patient p = new Patient();
			p.addName().setFamily(StringUtils.leftPad("", testResourceSize, 'A'));
			String id = myPatientDao.create(p, mySrd).getId().getIdPart();
			expectedIds.add(new BatchResourceId().setResourceType("Patient").setId(id));
		}
		Collections.sort(expectedIds);

		ResourceIdList resourceList = new ResourceIdList();
		resourceList.setResourceType("Patient");
		resourceList.setIds(expectedIds);

		BulkExportJobParameters params = new BulkExportJobParameters();
		JobInstance jobInstance = new JobInstance();
		String chunkId = "ABC";

		StepExecutionDetails<BulkExportJobParameters, ResourceIdList> details = new StepExecutionDetails<>(params, resourceList, jobInstance, new WorkChunk().setId(chunkId));

		// Test

		myExpandResourcesStep.run(details, mySink);

		// Verify
		verify(mySink, atLeast(1)).accept(myWorkChunkCaptor.capture());
		List<BatchResourceId> actualResourceIdList = new ArrayList<>();
		for (BulkExportBinaryFileId next : myWorkChunkCaptor.getAllValues()) {

			Binary nextBinary = myBinaryDao.read(new IdType(next.getBinaryId()), mySrd);
			String nextNdJsonString = new String(nextBinary.getContent(), StandardCharsets.UTF_8);

			// This is the most important check here
			assertThat(nextNdJsonString.length()).isLessThanOrEqualTo(maxFileSize);

			Arrays.stream(nextNdJsonString.split("\\n"))
				.filter(StringUtils::isNotBlank)
				.map(t->myFhirContext.newJsonParser().parseResource(t))
				.map(t->new BatchResourceId().setResourceType(t.getIdElement().getResourceType()).setId(t.getIdElement().getIdPart()))
				.forEach(actualResourceIdList::add);

		}
		Collections.sort(actualResourceIdList);
		assertEquals(expectedIds, actualResourceIdList);
	}

	@Test
	public void testMaximumChunkSize_SingleFileExceedsMaximum() {
		/*
		 * We're going to set the maximum file size to 1000, and create some resources
		 * with a name that is 1500 chars long. In this case, we'll exceed the
		 * configured maximum, so it should be one output file per resourcs.
		 */
		int testResourceSize = 1500;
		int maxFileSize = 1000;
		myStorageSettings.setBulkExportFileMaximumSize(maxFileSize);

		List<BatchResourceId> expectedIds = new ArrayList<>();
		int numberOfResources = 10;
		for (int i = 0; i < numberOfResources; i++) {
			Patient p = new Patient();
			p.addName().setFamily(StringUtils.leftPad("", testResourceSize, 'A'));
			String id = myPatientDao.create(p, mySrd).getId().getIdPart();
			expectedIds.add(new BatchResourceId().setResourceType("Patient").setId(id));
		}
		Collections.sort(expectedIds);

		ResourceIdList resourceList = new ResourceIdList();
		resourceList.setResourceType("Patient");
		resourceList.setIds(expectedIds);

		BulkExportJobParameters params = new BulkExportJobParameters();
		JobInstance jobInstance = new JobInstance();
		String chunkId = "ABC";

		StepExecutionDetails<BulkExportJobParameters, ResourceIdList> details = new StepExecutionDetails<>(params, resourceList, jobInstance, new WorkChunk().setId(chunkId));

		// Test
		myExpandResourcesStep.run(details, mySink);

		// Verify

		// This is the most important check - we should have one file per resource
		verify(mySink, times(numberOfResources)).accept(myWorkChunkCaptor.capture());

		List<BatchResourceId> actualResourceIdList = new ArrayList<>();
		for (BulkExportBinaryFileId next : myWorkChunkCaptor.getAllValues()) {

			Binary nextBinary = myBinaryDao.read(new IdType(next.getBinaryId()), mySrd);
			String nextNdJsonString = new String(nextBinary.getContent(), StandardCharsets.UTF_8);

			Arrays.stream(nextNdJsonString.split("\\n"))
				.filter(StringUtils::isNotBlank)
				.map(t->myFhirContext.newJsonParser().parseResource(t))
				.map(t->new BatchResourceId().setResourceType(t.getIdElement().getResourceType()).setId(t.getIdElement().getIdPart()))
				.forEach(actualResourceIdList::add);

		}
		Collections.sort(actualResourceIdList);
		assertEquals(expectedIds, actualResourceIdList);
	}

}
