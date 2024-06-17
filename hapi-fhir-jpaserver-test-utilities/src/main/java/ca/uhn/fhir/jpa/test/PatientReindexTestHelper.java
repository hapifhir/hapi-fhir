/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.test;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexAppCtx;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexJobParameters;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.TestUtil;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


public class PatientReindexTestHelper {

	private static final int VERSION_1 = 1;
	private static final int VERSION_2 = 2;
	private static final int VERSION_3 = 3;
	public static final int JOB_WAIT_TIME = 60;

	private final IJobCoordinator myJobCoordinator;
	private final Batch2JobHelper myBatch2JobHelper;
	private final IFhirResourceDao<Patient> myPatientDao;
	private final boolean myIncrementVersionOnReindex;
	private final RequestDetails myRequestDetails = new SystemRequestDetails();

	public static Stream<Arguments> numResourcesParams(){
		return Stream.of(
			Arguments.of(0),
			Arguments.of(1),
			Arguments.of(499),
			Arguments.of(500),
			Arguments.of(750),
			Arguments.of(1000),
			Arguments.of(1001)
		);
	}

	public PatientReindexTestHelper(IJobCoordinator theJobCoordinator, Batch2JobHelper theBatch2JobHelper, IFhirResourceDao<Patient> thePatientDao, boolean theIncrementVersionOnReindex) {
		myJobCoordinator = theJobCoordinator;
		myBatch2JobHelper = theBatch2JobHelper;
		myPatientDao = thePatientDao;
		myIncrementVersionOnReindex = theIncrementVersionOnReindex;
	}

	public void testReindex(int theNumResources){
		createPatients(theNumResources);

		validatePersistedPatients(theNumResources, VERSION_1);

		// Reindex 1
		JobInstanceStartRequest reindexRequest1 = createPatientReindexRequest(theNumResources);
		Batch2JobStartResponse reindexResponse1 = myJobCoordinator.startInstance(myRequestDetails, reindexRequest1);
		JobInstance instance1 = myBatch2JobHelper.awaitJobHasStatus(reindexResponse1.getInstanceId(), JOB_WAIT_TIME, StatusEnum.COMPLETED);

		validateReindexJob(instance1, theNumResources);

		int expectedVersion = myIncrementVersionOnReindex ? VERSION_2 : VERSION_1;
		validatePersistedPatients(theNumResources, expectedVersion);
	}

	public void testSequentialReindexOperation(int theNumResources){
		createPatients(theNumResources);

		validatePersistedPatients(theNumResources, VERSION_1);

		// Reindex 1
		JobInstanceStartRequest reindexRequest1 = createPatientReindexRequest(theNumResources);
		Batch2JobStartResponse reindexResponse1 = myJobCoordinator.startInstance(myRequestDetails, reindexRequest1);
		JobInstance instance1 = myBatch2JobHelper.awaitJobHasStatus(reindexResponse1.getInstanceId(), JOB_WAIT_TIME, StatusEnum.COMPLETED);

		validateReindexJob(instance1, theNumResources);
		int expectedVersion = myIncrementVersionOnReindex ? VERSION_2 : VERSION_1;
		validatePersistedPatients(theNumResources, expectedVersion);

		// Reindex 2
		JobInstanceStartRequest reindexRequest2 = createPatientReindexRequest(theNumResources);
		Batch2JobStartResponse reindexResponse2 = myJobCoordinator.startInstance(myRequestDetails, reindexRequest2);
		JobInstance instance2 = myBatch2JobHelper.awaitJobHasStatus(reindexResponse2.getInstanceId(), JOB_WAIT_TIME, StatusEnum.COMPLETED);

		validateReindexJob(instance2, theNumResources);
		expectedVersion = myIncrementVersionOnReindex ? VERSION_3 : VERSION_1;
		validatePersistedPatients(theNumResources, expectedVersion);
	}

	public void testParallelReindexOperation(int theNumResources){
		createPatients(theNumResources);

		validatePersistedPatients(theNumResources, VERSION_1);

		// Reindex 1
		JobInstanceStartRequest reindexRequest1 = createPatientReindexRequest(theNumResources);
		Batch2JobStartResponse reindexResponse1 = myJobCoordinator.startInstance(myRequestDetails, reindexRequest1);

		// Reindex 2
		JobInstanceStartRequest reindexRequest2 = createPatientReindexRequest(theNumResources);
		Batch2JobStartResponse reindexResponse2 = myJobCoordinator.startInstance(myRequestDetails, reindexRequest2);

		// Wait for jobs to finish
		JobInstance instance1 = myBatch2JobHelper.awaitJobHasStatus(reindexResponse1.getInstanceId(), JOB_WAIT_TIME, StatusEnum.COMPLETED);
		JobInstance instance2 = myBatch2JobHelper.awaitJobHasStatus(reindexResponse2.getInstanceId(), JOB_WAIT_TIME, StatusEnum.COMPLETED);

		validateReindexJob(instance1, theNumResources);

		validateReindexJob(instance2, theNumResources);

		int expectedVersion = myIncrementVersionOnReindex ? VERSION_3 : VERSION_1;
		validatePersistedPatients(theNumResources, expectedVersion);
	}


	private void createPatients(int theNumPatients) {
		RequestDetails requestDetails = new SystemRequestDetails();
		for(int i = 0; i < theNumPatients; i++){
			Patient patient = new Patient();
			patient.getNameFirstRep().setFamily("Family-"+i).addGiven("Given-"+i);
			patient.getIdentifierFirstRep().setValue("Id-"+i);
			myPatientDao.create(patient, requestDetails);
			TestUtil.sleepOneClick();
		}
	}

	private void validatePersistedPatients(int theExpectedNumPatients, long theExpectedVersion) {
		RequestDetails requestDetails = new SystemRequestDetails();
		List<IBaseResource> resources = myPatientDao.search(SearchParameterMap.newSynchronous(), requestDetails).getAllResources();
		assertThat(resources).hasSize(theExpectedNumPatients);
		for(IBaseResource resource : resources){
			assertEquals(Patient.class, resource.getClass());
			Patient patient = (Patient) resource;
			Long actualVersion = patient.getIdElement().getVersionIdPartAsLong();
			if(theExpectedVersion != actualVersion){
				String failureMessage = String.format("Failure for Resource [%s] with index [%s]. Expected version: %s, Actual version: %s",
					patient.getId(), resources.indexOf(resource), theExpectedVersion, actualVersion);
				fail(failureMessage);
			}
		}
	}

	private JobInstanceStartRequest createPatientReindexRequest(int theBatchSize) {
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(ReindexAppCtx.JOB_REINDEX);

		ReindexJobParameters reindexJobParameters = new ReindexJobParameters();
		reindexJobParameters.setBatchSize(Math.max(theBatchSize,1));
		reindexJobParameters.addUrl("Patient?");

		startRequest.setParameters(reindexJobParameters);
		return startRequest;
	}

	private void validateReindexJob(JobInstance theJobInstance, int theRecordsProcessed) {
		assertEquals(0, theJobInstance.getErrorCount());
		assertEquals(theRecordsProcessed, theJobInstance.getCombinedRecordsProcessed());
	}
}
