package ca.uhn.fhir.jpa.term.job;

/*
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.term.TerminologyTestHelper;
import ca.uhn.fhir.jpa.term.models.TermCodeSystemDeleteVersionJobParameters;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static ca.uhn.fhir.batch2.jobs.termcodesystem.TermCodeSystemJobConfig.TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class TermCodeSystemVersionDeleteJobTest extends BaseJpaR4Test {

	@Autowired
	private TerminologyTestHelper myTerminologyTestHelper;
	@Autowired
	private Batch2JobHelper myBatchJobHelper;
	@Autowired
	private IJobCoordinator myJobCoordinator;

	@Test
	public void runDeleteJobDeleteOneVersion() throws Exception {
		String oldVer = "2.67";
		myTerminologyTestHelper.startImportLoincJobAndWaitForCompletion(oldVer, true);

		String firstCurrentVer = "2.68";
		myTerminologyTestHelper.startImportLoincJobAndWaitForCompletion(firstCurrentVer, true);

		long[] termCodeSystemVersionPidVect = new long[1];  //bypass final restriction
		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());

			TermCodeSystem termCodeSystem = myTermCodeSystemDao.findByCodeSystemUri("http://loinc.org");
			assertNotNull(termCodeSystem);

			TermCodeSystemVersion termCodeSystemVersion = myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(termCodeSystem.getPid(), firstCurrentVer);
			assertNotNull(termCodeSystemVersion);
			termCodeSystemVersionPidVect[0] = requireNonNull(termCodeSystemVersion.getPid());

			assertEquals(2 * 2, myTermCodeSystemVersionDao.count());
			assertEquals(82 * 2, myTermConceptDao.count());
		});

		TermCodeSystemDeleteVersionJobParameters jobParameters = new TermCodeSystemDeleteVersionJobParameters();
		jobParameters.setCodeSystemVersionPid(termCodeSystemVersionPidVect[0]);

		JobInstanceStartRequest request = new JobInstanceStartRequest();
		request.setJobDefinitionId(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME);
		request.setParameters(jobParameters);

		Batch2JobStartResponse response = myJobCoordinator.startInstance(newSrd(), request);

		JobInstance instance = myBatchJobHelper.awaitJobCompletion(response);
		assertEquals("COMPLETED", instance.getStatus().name());

		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertNotNull(myTermCodeSystemDao.findByCodeSystemUri("http://loinc.org"));
			assertEquals(3, myTermCodeSystemVersionDao.count());
			assertEquals(82, myTermConceptDao.count());
		});
	}


	@Test
	public void runWithParameterZeroFailsValidation() {
		InvalidRequestException thrown = Assertions.assertThrows(
			InvalidRequestException.class,
			() -> {
				TermCodeSystemDeleteVersionJobParameters jobParameters = new TermCodeSystemDeleteVersionJobParameters();
				jobParameters.setCodeSystemVersionPid(0);

				JobInstanceStartRequest request = new JobInstanceStartRequest();
				request.setJobDefinitionId(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME);
				request.setParameters(jobParameters);

				myJobCoordinator.startInstance(newSrd(), request);
			}
		);
		assertThat(thrown.getMessage()).contains("Invalid code system version PID 0");
	}


}
