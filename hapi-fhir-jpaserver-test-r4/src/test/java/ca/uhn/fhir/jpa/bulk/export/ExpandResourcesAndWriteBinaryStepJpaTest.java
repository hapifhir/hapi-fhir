package ca.uhn.fhir.jpa.bulk.export;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepExecutionServices;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidJson;
import ca.uhn.fhir.batch2.jobs.export.BulkExportJobParametersBuilder;
import ca.uhn.fhir.batch2.jobs.export.ExpandResourceAndWriteBinaryStep;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportBinaryFileId;
import ca.uhn.fhir.batch2.jobs.export.models.ResourceIdList;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.util.BundleUtil;
import jakarta.annotation.Nonnull;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ExpandResourcesAndWriteBinaryStepJpaTest extends BaseJpaR4Test {

	@Autowired
	private ExpandResourceAndWriteBinaryStep myExpandResourcesStep;

	@Mock
	private IJobDataSink<BulkExportBinaryFileId> mySink;
	@Mock
	private IJobStepExecutionServices myJobStepExecutionServices;
	@Captor
	private ArgumentCaptor<BulkExportBinaryFileId> myWorkChunkCaptor;

	@Override
	public void afterCleanupDao() {
		super.afterCleanupDao();

		JpaStorageSettings defaults = new JpaStorageSettings();
		myStorageSettings.setBulkExportFileMaximumSize(defaults.getBulkExportFileMaximumSize());
		myStorageSettings.setBulkExportFileMaximumCapacity(defaults.getBulkExportFileMaximumCapacity());
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

		List<TypedPidJson> expectedIds = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			Patient p = new Patient();
			p.addName().setFamily(StringUtils.leftPad("", testResourceSize, 'A'));
			String id = myPatientDao.create(p, mySrd).getId().getIdPart();
			expectedIds.add(new TypedPidJson().setResourceType("Patient").setPid(id));
		}
		Collections.sort(expectedIds);

		ResourceIdList resourceList = new ResourceIdList();
		resourceList.setResourceType("Patient");
		resourceList.setIds(expectedIds);

		BulkExportJobParameters params = new BulkExportJobParameters();
		JobInstance jobInstance = new JobInstance();
		String chunkId = "ABC";

		StepExecutionDetails<BulkExportJobParameters, ResourceIdList> details = new StepExecutionDetails<>(params, resourceList, jobInstance, new WorkChunk().setId(chunkId), myJobStepExecutionServices);

		// Test

		myExpandResourcesStep.run(details, mySink);

		// Verify
		verify(mySink, atLeast(1)).accept(myWorkChunkCaptor.capture());
		List<TypedPidJson> actualResourceIdList = new ArrayList<>();
		for (BulkExportBinaryFileId next : myWorkChunkCaptor.getAllValues()) {

			Binary nextBinary = myBinaryDao.read(new IdType(next.getBinaryId()), mySrd);
			String nextNdJsonString = new String(nextBinary.getContent(), StandardCharsets.UTF_8);

			// This is the most important check here
			assertThat(nextNdJsonString.length()).isLessThanOrEqualTo(maxFileSize);

			Arrays.stream(nextNdJsonString.split("\\n"))
				.filter(StringUtils::isNotBlank)
				.map(t->myFhirContext.newJsonParser().parseResource(t))
				.map(t->new TypedPidJson().setResourceType(t.getIdElement().getResourceType()).setPid(t.getIdElement().getIdPart()))
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
		 * configured maximum, so it should be one output file per resource.
		 */
		int testResourceSize = 1500;
		int maxFileSize = 1000;
		myStorageSettings.setBulkExportFileMaximumSize(maxFileSize);

		List<TypedPidJson> expectedIds = new ArrayList<>();
		int numberOfResources = 10;
		for (int i = 0; i < numberOfResources; i++) {
			Patient p = new Patient();
			p.addName().setFamily(StringUtils.leftPad("", testResourceSize, 'A'));
			String id = myPatientDao.create(p, mySrd).getId().getIdPart();
			expectedIds.add(new TypedPidJson().setResourceType("Patient").setPid(id));
		}
		Collections.sort(expectedIds);

		ResourceIdList resourceList = new ResourceIdList();
		resourceList.setResourceType("Patient");
		resourceList.setIds(expectedIds);

		BulkExportJobParameters params = new BulkExportJobParameters();
		JobInstance jobInstance = new JobInstance();
		String chunkId = "ABC";

		StepExecutionDetails<BulkExportJobParameters, ResourceIdList> details = new StepExecutionDetails<>(params, resourceList, jobInstance, new WorkChunk().setId(chunkId), myJobStepExecutionServices);

		// Test
		myExpandResourcesStep.run(details, mySink);

		// Verify

		// This is the most important check - we should have one file per resource
		verify(mySink, times(numberOfResources)).accept(myWorkChunkCaptor.capture());

		List<TypedPidJson> actualResourceIdList = new ArrayList<>();
		for (BulkExportBinaryFileId next : myWorkChunkCaptor.getAllValues()) {

			Binary nextBinary = myBinaryDao.read(new IdType(next.getBinaryId()), mySrd);
			String nextNdJsonString = new String(nextBinary.getContent(), StandardCharsets.UTF_8);

			Arrays.stream(nextNdJsonString.split("\\n"))
				.filter(StringUtils::isNotBlank)
				.map(t->myFhirContext.newJsonParser().parseResource(t))
				.map(t->new TypedPidJson().setResourceType(t.getIdElement().getResourceType()).setPid(t.getIdElement().getIdPart()))
				.forEach(actualResourceIdList::add);

		}
		Collections.sort(actualResourceIdList);
		assertEquals(expectedIds, actualResourceIdList);
	}

	@Nested
	class IncludeHistoryTests {

		@Test
		public void testMaxFileResourceCount() {
			// given
			int testResourceSize = 10;
			int maxFileCapacity = 17;
			myStorageSettings.setBulkExportFileMaximumCapacity(maxFileCapacity);

			List<Integer> versionCounts = List.of(6, 3, 5, 4, 2);
			List<IIdType> patientIds = createPatients(versionCounts.size(), testResourceSize);
			Map<IIdType, Integer> patientVersionCountMap = getPatientVersionCountMap(patientIds, versionCounts);
			generatePatientsHistory(patientIds, patientVersionCountMap);

			ResourceIdList resourceList = new ResourceIdList();
			resourceList.setResourceType("Patient");
			List<TypedPidJson> patientJsonIds = patientIds.stream().map(id -> new TypedPidJson().setResourceType("Patient").setPid(id.getIdPart())).toList();
			resourceList.setIds(patientJsonIds);

			BulkExportJobParametersBuilder paramBuilder = new BulkExportJobParametersBuilder();
			paramBuilder.includeHistory(new BooleanDt(true));
			JobInstance jobInstance = new JobInstance();
			String chunkId = "ABC";

			StepExecutionDetails<BulkExportJobParameters, ResourceIdList> details = new StepExecutionDetails<>(paramBuilder.build(), resourceList, jobInstance, new WorkChunk().setId(chunkId), myJobStepExecutionServices);

			// when
			myExpandResourcesStep.run(details, mySink);

			// then

			verify(mySink, times(2)).accept(myWorkChunkCaptor.capture());

			// should have exported two binary files: first one with 17 patient versions and another with the remaining 3
			List<List<String>> exportedResourceVersionsLists = getExportedResourceVersionsLists();
			assertThat(exportedResourceVersionsLists)
				.extracting(List::size)
				.containsExactlyInAnyOrder(17, 3);
		}

		@Test
		public void testResultInExpectedOrder() {
			// given
			int testResourceSize = 10;
			int maxFileCapacity = 17;
			myStorageSettings.setBulkExportFileMaximumCapacity(maxFileCapacity);

			List<Integer> versionCounts = List.of(6, 3, 5, 4, 2);
			List<IIdType> patientIds = createPatients(versionCounts.size(), testResourceSize);
			Map<IIdType, Integer> patientVersionCountMap = getPatientVersionCountMap(patientIds, versionCounts);
			Map<String, List<String>> patientVersionMap = generatePatientsHistory(patientIds, patientVersionCountMap);
			List<String> versionsList = patientVersionMap.values().stream().flatMap(Collection::stream).toList();

			ResourceIdList resourceList = new ResourceIdList();
			resourceList.setResourceType("Patient");
			List<TypedPidJson> patientJsonIds = patientIds.stream().map(id -> new TypedPidJson().setResourceType("Patient").setPid(id.getIdPart())).toList();
			resourceList.setIds(patientJsonIds);

			BulkExportJobParametersBuilder paramBuilder = new BulkExportJobParametersBuilder();
			paramBuilder.includeHistory(new BooleanDt(true));
			JobInstance jobInstance = new JobInstance();
			String chunkId = "ABC";

			StepExecutionDetails<BulkExportJobParameters, ResourceIdList> details = new StepExecutionDetails<>(paramBuilder.build(), resourceList, jobInstance, new WorkChunk().setId(chunkId), myJobStepExecutionServices);

			// when
			myExpandResourcesStep.run(details, mySink);

			// then

			verify(mySink, times(2)).accept(myWorkChunkCaptor.capture());

			List<List<String>> exportedResourceVersionsLists = getExportedResourceVersionsLists();

			// the versions should be ordered by Patient ID ascending version ID descending, so first file should have
			// versions for Patients 1, 2, 3 and the first 3 versions for Patient 4, and the second file should have
			// version 4 of Patient 4 and both versions of Patient 5
			List<List<String>> expectedExportedVersionsLists = ListUtils.partition(versionsList, 17);
			assertThat(exportedResourceVersionsLists).isEqualTo(expectedExportedVersionsLists);
		}

		private @Nonnull List<List<String>> getExportedResourceVersionsLists() {
			List<List<String>> exportedResourceVersionsLists = new ArrayList<>();
			for (BulkExportBinaryFileId next : myWorkChunkCaptor.getAllValues()) {

				Binary nextBinary = myBinaryDao.read(new IdType(next.getBinaryId()), mySrd);
				String nextNdJsonString = new String(nextBinary.getContent(), StandardCharsets.UTF_8);
				Bundle bundle = (Bundle) myFhirContext.newNDJsonParser().parseResource(nextNdJsonString);

				List<IBaseResource> resources = BundleUtil.toListOfResources(myFhirContext, bundle);

				List<String> versionIds = resources.stream().map(r -> r.getIdElement().getValueAsString()).toList();
				exportedResourceVersionsLists.add(versionIds);
			}
			return exportedResourceVersionsLists;
		}

		@Test
		public void testMaxFileSizeRespected() {
			/*
			 * We're going to set the maximum file size to 3000, and create some resources with
			 * a name that is 1000 chars long. With the other boilerplate text in a resource that
			 * will put the resource length at just over 1000 chars, meaning that any given
			 * chunk or file should have only 2 resources in it.
			 */
			int testResourceSize = 1000;
			int maxFileSize = 3 * testResourceSize;
			myStorageSettings.setBulkExportFileMaximumSize(maxFileSize);

			List<Integer> versionCounts = List.of(1, 3, 5, 4, 2);
			List<IIdType> patientIds = createPatients(versionCounts.size(), testResourceSize);
			Map<IIdType, Integer> patientVersionCountMap = getPatientVersionCountMap(patientIds, versionCounts);
			generatePatientsHistory(patientIds, patientVersionCountMap);

			ResourceIdList resourceList = new ResourceIdList();
			resourceList.setResourceType("Patient");
			List<TypedPidJson> patientJsonIds = patientIds.stream().map(id -> new TypedPidJson().setResourceType("Patient").setPid(id.getIdPart())).toList();
			resourceList.setIds(patientJsonIds);

			BulkExportJobParametersBuilder paramBuilder = new BulkExportJobParametersBuilder();
			paramBuilder.includeHistory(new BooleanDt(true));
			JobInstance jobInstance = new JobInstance();
			String chunkId = "ABC";

			StepExecutionDetails<BulkExportJobParameters, ResourceIdList> details = new StepExecutionDetails<>(paramBuilder.build(), resourceList, jobInstance, new WorkChunk().setId(chunkId), myJobStepExecutionServices);

			// Test

			myExpandResourcesStep.run(details, mySink);

			// Verify

			verify(mySink, atLeast(1)).accept(myWorkChunkCaptor.capture());

			List<List<String>> exportedResourceVersionsLists = getExportedResourceVersionsLists();

			// validate all lists but last have 2 resources
			for (int i = 0; i < exportedResourceVersionsLists.size() - 2; i++) {
				assertThat(exportedResourceVersionsLists.get(i)).hasSize(2);
			}
			// and last has 2 or less
			assertThat(exportedResourceVersionsLists.get(exportedResourceVersionsLists.size() - 1)).hasSizeLessThanOrEqualTo(2);

		}

		@Test
		void testSingleFileExceedsMaximum_thenOnlyOneResourcePerFile() {
			/*
			 * We're going to set the maximum file size to 1000, and create some resources
			 * with a name that is 1500 chars long. In this case, we'll exceed the
			 * configured maximum, so it should be one output file per resource.
			 */
			int testResourceSize = 1500;
			int maxFileSize = 1000;
			myStorageSettings.setBulkExportFileMaximumSize(maxFileSize);

			List<Integer> versionCounts = List.of(1, 3, 5, 4, 2);
			List<IIdType> patientIds = createPatients(versionCounts.size(), testResourceSize);
			Map<IIdType, Integer> patientVersionCountMap = getPatientVersionCountMap(patientIds, versionCounts);
			generatePatientsHistory(patientIds, patientVersionCountMap);

			ResourceIdList resourceList = new ResourceIdList();
			resourceList.setResourceType("Patient");
			List<TypedPidJson> patientJsonIds = patientIds.stream().map(id -> new TypedPidJson().setResourceType("Patient").setPid(id.getIdPart())).toList();
			resourceList.setIds(patientJsonIds);

			BulkExportJobParametersBuilder paramBuilder = new BulkExportJobParametersBuilder();
			paramBuilder.includeHistory(new BooleanDt(true));
			JobInstance jobInstance = new JobInstance();
			String chunkId = "ABC";

			StepExecutionDetails<BulkExportJobParameters, ResourceIdList> details = new StepExecutionDetails<>(paramBuilder.build(), resourceList, jobInstance, new WorkChunk().setId(chunkId), myJobStepExecutionServices);

			// Test

			myExpandResourcesStep.run(details, mySink);

			// Verify

			verify(mySink, atLeast(1)).accept(myWorkChunkCaptor.capture());

			List<List<String>> exportedResourceVersionsLists = getExportedResourceVersionsLists();

			// validate all lists but last have no more than 1 resource
			for (int i = 0; i < exportedResourceVersionsLists.size() - 1; i++) {
				assertThat(exportedResourceVersionsLists.get(i)).hasSize(1);
			}
		}

		/**
		 * Test that until parameter is adjusted if it is not set or set to a time after job start
		 * when includeHistory parameter is set
		 */
		@Nested
		class TestUntilParameterAdjustment {

			private final ExpandResourceAndWriteBinaryStep mySpiedStep = spy(myExpandResourcesStep);

			@Captor
			private ArgumentCaptor<BulkExportJobParameters> myJobParamsCaptor;

			Date jobStartTime = new Date();
			Date beforeJobStart = DateUtils.addSeconds(jobStartTime, -2);
			Date afterJobStart = DateUtils.addSeconds(new Date(), 2);

			@Test
			void whenHistoryNotSet_untilNotAdjusted() {
				// given
				var details = callStepWithHistoryAndUntilParams(jobStartTime, false, null);

				// when
				mySpiedStep.run(details, mySink);

				// then
				verify(mySpiedStep, never()).adjustJobParameters(any(), any());
			}

			@Test
			void whenHistorySet_andUntilNotSet_untilSetToJobStartTime() {
				// given
				var details = callStepWithHistoryAndUntilParams(jobStartTime, true, null);

				// when
				mySpiedStep.run(details, mySink);

				// then
				verify(mySpiedStep).adjustJobParameters(any(), any());
				verify(mySpiedStep).processHistoryResources(any(), any(), any(), myJobParamsCaptor.capture());
				assertThat(myJobParamsCaptor.getValue()).returns(jobStartTime, BulkExportJobParameters::getUntil);
			}

			@Test
			void whenHistorySet_andUntilSetEarlierThanJobStartTime_untilNotAdjusted() {
				// given
				var details = callStepWithHistoryAndUntilParams(jobStartTime, true, beforeJobStart);

				// when
				mySpiedStep.run(details, mySink);

				// then
				verify(mySpiedStep).adjustJobParameters(any(), any());
				verify(mySpiedStep).processHistoryResources(any(), any(), any(), myJobParamsCaptor.capture());
				assertThat(myJobParamsCaptor.getValue()).returns(beforeJobStart, BulkExportJobParameters::getUntil);
			}

			@Test
			void whenHistorySet_andUntilSetLaterThanJuoStartTime_untilAdjustedToJobStartTime() {
				// given
				var details = callStepWithHistoryAndUntilParams(jobStartTime, true, afterJobStart);

				// when
				mySpiedStep.run(details, mySink);

				// then
				verify(mySpiedStep).adjustJobParameters(any(), any());
				verify(mySpiedStep).processHistoryResources(any(), any(), any(), myJobParamsCaptor.capture());
				assertThat(myJobParamsCaptor.getValue()).returns(jobStartTime, BulkExportJobParameters::getUntil);
			}

			private StepExecutionDetails<BulkExportJobParameters, ResourceIdList> callStepWithHistoryAndUntilParams(Date theJobStartTime, boolean theIncludeHistory, Date theUntilTime) {
				int testResourceSize = 10;
				int maxFileSize = 1000;
				myStorageSettings.setBulkExportFileMaximumSize(maxFileSize);

				List<Integer> versionCounts = List.of(2);
				List<IIdType> patientIds = createPatients(versionCounts.size(), testResourceSize);

				ResourceIdList resourceList = new ResourceIdList();
				resourceList.setResourceType("Patient");
				List<TypedPidJson> patientJsonIds = patientIds.stream().map(id -> new TypedPidJson().setResourceType("Patient").setPid(id.getIdPart())).toList();
				resourceList.setIds(patientJsonIds);

				BulkExportJobParameters params = new BulkExportJobParameters();
				params.setIncludeHistory(theIncludeHistory);
				params.setUntil(theUntilTime);
				JobInstance jobInstance = new JobInstance();
				jobInstance.setStartTime(theJobStartTime);
				String chunkId = "ABC";

				return new StepExecutionDetails<>(params, resourceList, jobInstance, new WorkChunk().setId(chunkId), myJobStepExecutionServices);
			}

		}

	}

		private Map<IIdType, Integer> getPatientVersionCountMap(List<IIdType> thePatientIds, List<Integer> theCounts) {
		assertThat(thePatientIds).hasSameSizeAs(theCounts);
		Map<IIdType, Integer> map = new HashMap<>();
		for (int i = 0; i < thePatientIds.size(); i++) {
			map.put(thePatientIds.get(i), theCounts.get(i));
		}
		return map;
	}


	private Map<String, List<String>> generatePatientsHistory(List<IIdType> thePatientIds, Map<IIdType, Integer> thePatientVersionCountMap) {
		Map<String, List<String>> retVal = new LinkedHashMap<>();

		for (IIdType patientId : thePatientIds) {

			List<String> patientVersionIds = new LinkedList<>();
			retVal.putIfAbsent(patientId.toVersionless().getValueAsString(), patientVersionIds);

			int patientAdditionalVersions = thePatientVersionCountMap.get(patientId) - 1;
			IIdType lastVersonId = patientId;
			patientVersionIds.add(lastVersonId.getValueAsString());

			for (int vCount = 2; vCount < patientAdditionalVersions+2; vCount++) {
				Parameters patch = getPatch(vCount);
				DaoMethodOutcome patchOutcome = myPatientDao.patch(lastVersonId, null, PatchTypeEnum.FHIR_PATCH_JSON, null, patch, mySrd);
				assertThat(patchOutcome).isNotNull();
				lastVersonId = patchOutcome.getId();
				// to get them in expected sort order
				patientVersionIds.add(0, lastVersonId.getValueAsString());
			}
		}

		return retVal;
	}

	private Parameters getPatch(int theVersion) {
		Parameters patch = new Parameters();
		Parameters.ParametersParameterComponent op = patch.addParameter().setName("operation");
		op.addPart().setName("type").setValue(new CodeType("replace"));
		op.addPart().setName("path").setValue(new CodeType("Patient.name.given"));
		op.addPart().setName("value").setValue(new StringType("given-v" + theVersion));
		return patch;
	}

	private List<IIdType> createPatients(@SuppressWarnings("SameParameterValue") int theCount, int theTestResourceSize) {
		List<IIdType> patientIds = new ArrayList<>();
		for (int i = 0; i < theCount; i++) {
			Patient p = new Patient();
			p.addName()
				.setGiven(List.of(new StringType("given")))
				.setFamily(StringUtils.leftPad("", theTestResourceSize, 'A'));
			IIdType id = myPatientDao.create(p, mySrd).getId();
			patientIds.add(id);
		}
		return patientIds;
	}

}
