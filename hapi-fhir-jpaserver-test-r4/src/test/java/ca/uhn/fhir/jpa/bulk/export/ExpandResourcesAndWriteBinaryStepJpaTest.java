package ca.uhn.fhir.jpa.bulk.export;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidJson;
import ca.uhn.fhir.batch2.jobs.export.ExpandResourceAndWriteBinaryStep;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportBinaryFileId;
import ca.uhn.fhir.batch2.jobs.export.models.ResourceIdList;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

		StepExecutionDetails<BulkExportJobParameters, ResourceIdList> details = new StepExecutionDetails<>(params, resourceList, jobInstance, new WorkChunk().setId(chunkId));

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

		StepExecutionDetails<BulkExportJobParameters, ResourceIdList> details = new StepExecutionDetails<>(params, resourceList, jobInstance, new WorkChunk().setId(chunkId));

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

	@Test
	public void testIncludeHistory() {
		int testResourceSize = 10;
		int maxFileSize = 3 * testResourceSize;
		myStorageSettings.setBulkExportFileMaximumSize(maxFileSize);

		List<Integer> versionCounts = List.of(1, 3, 5, 4, 2);
		List<IIdType> patientIds = createPatients(versionCounts.size(), testResourceSize);
		Map<IIdType, Integer> patientVersionCountMap = getPatientVersionCountMap(patientIds, versionCounts);
		Map<Long, Set<Long>> patientVersionIdsMap = generatePatientsHistory(patientIds, patientVersionCountMap);

		ResourceIdList resourceList = new ResourceIdList();
		resourceList.setResourceType("Patient");
		List<TypedPidJson> patientJsonIds = patientIds.stream().map(id -> new TypedPidJson().setResourceType("Patient").setPid(id.getIdPart())).toList();
		resourceList.setIds(patientJsonIds);

		BulkExportJobParameters params = new BulkExportJobParameters();
		params.setIncludeHistory(true);
		JobInstance jobInstance = new JobInstance();
		String chunkId = "ABC";

		StepExecutionDetails<BulkExportJobParameters, ResourceIdList> details = new StepExecutionDetails<>(params, resourceList, jobInstance, new WorkChunk().setId(chunkId));

		// Test

		myExpandResourcesStep.run(details, mySink);

		// Verify

		verify(mySink, atLeast(1)).accept(myWorkChunkCaptor.capture());

		Map<Long, Set<Long>>  exportedResourceVersionIdsMap = new HashMap<>();
		for (BulkExportBinaryFileId next : myWorkChunkCaptor.getAllValues()) {

			Binary nextBinary = myBinaryDao.read(new IdType(next.getBinaryId()), mySrd);
			String nextNdJsonString = new String(nextBinary.getContent(), StandardCharsets.UTF_8);
			IBaseResource resource = myFhirContext.newJsonParser().parseResource(nextNdJsonString);

			exportedResourceVersionIdsMap.computeIfAbsent(
					resource.getIdElement().getIdPartAsLong(),
					k -> new HashSet<>())
				.add(resource.getIdElement().getVersionIdPartAsLong());
		}

		assertThat(exportedResourceVersionIdsMap).isEqualTo(patientVersionIdsMap);
	}

	private Map<IIdType, Integer> getPatientVersionCountMap(List<IIdType> thePatientIds, List<Integer> theCounts) {
		assertThat(thePatientIds).hasSameSizeAs(theCounts);
		Map<IIdType, Integer> map = new HashMap<>();
		for (int i = 0; i < thePatientIds.size(); i++) {
			map.put(thePatientIds.get(i), theCounts.get(i));
		}
		return map;
	}


	private Map<Long, Set<Long>> generatePatientsHistory(List<IIdType> thePatientIds, Map<IIdType, Integer> thePatientVersionCountMap) {
		Map<Long, Set<Long>> retVal = new HashMap<>();

		for (IIdType patientId : thePatientIds) {

			Set<Long> patientVersionIds = new HashSet<>();
			retVal.computeIfAbsent(patientId.getIdPartAsLong(), k -> patientVersionIds);

			int patientAdditionalVersions = thePatientVersionCountMap.get(patientId) - 1;
			IIdType lastVersonId = patientId;
			patientVersionIds.add(lastVersonId.getVersionIdPartAsLong());

			for (int vCount = 0; vCount < patientAdditionalVersions; vCount++) {
				Parameters patch = getPatch(vCount);
				DaoMethodOutcome patchOutcome = myPatientDao.patch(lastVersonId, null, PatchTypeEnum.FHIR_PATCH_JSON, null, patch, mySrd);
				assertThat(patchOutcome).isNotNull();
				lastVersonId = patchOutcome.getId();
				patientVersionIds.add(lastVersonId.getVersionIdPartAsLong());
			}
		}

		return retVal;
	}

	private Parameters getPatch(int theVersion) {
		Parameters patch = new Parameters();
		Parameters.ParametersParameterComponent op = patch.addParameter().setName("operation");
		op.addPart().setName("type").setValue(new CodeType("replace"));
		op.addPart().setName("path").setValue(new CodeType("Patient.name.given"));
		op.addPart().setName("value").setValue(new StringType("given-v" + theVersion+1));
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
