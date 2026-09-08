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
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.term.TermTestUtil;
import ca.uhn.fhir.jpa.term.TerminologyTestHelper;
import ca.uhn.fhir.jpa.term.models.TermCodeSystemDeleteJobParameters;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static ca.uhn.fhir.batch2.jobs.termcodesystem.TermCodeSystemJobConfig.TERM_CODE_SYSTEM_DELETE_JOB_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class TermCodeSystemDeleteJobTest extends BaseJpaR4Test {

	@Autowired
	private TerminologyTestHelper myTerminologyTestHelper;

	@Autowired
	private Batch2JobHelper myBatch2JobHelper;

	@Autowired
	private IJobCoordinator myJobCoordinator;

	@Test
	public void runDeleteJobMultipleVersions() throws Exception {
		String firstCurrentVer = "2.67";
		myTerminologyTestHelper.startImportLoincJobAndWaitForCompletion(firstCurrentVer, true);

		String secondCurrentVer = "2.68";
		myTerminologyTestHelper.startImportLoincJobAndWaitForCompletion(secondCurrentVer, true);

		long[] termCodeSystemPidVect = new long[1];  //bypass final restriction
		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());

			TermCodeSystem termCodeSystem = myTermCodeSystemDao.findByCodeSystemUri("http://loinc.org");
			assertNotNull(termCodeSystem);
			termCodeSystemPidVect[0] = termCodeSystem.getPid();

			assertEquals(2, myTermCodeSystemVersionDao.count(), TermTestUtil.MSG_ONE_CODE_SYSTEM_VERSION_PER_UPLOAD);
			assertEquals(82 * 2, myTermConceptDao.count());
		});

		TermCodeSystemDeleteJobParameters parameters = new TermCodeSystemDeleteJobParameters();
		parameters.setTermPid(termCodeSystemPidVect[0]);

		JobInstanceStartRequest request = new JobInstanceStartRequest();
		request.setJobDefinitionId(TERM_CODE_SYSTEM_DELETE_JOB_NAME);
		request.setParameters(JsonUtil.serialize(parameters));
		Batch2JobStartResponse response = myJobCoordinator.startInstance(new SystemRequestDetails(), request);

		myBatch2JobHelper.awaitJobCompletion(response);

		runInTransaction(() -> {
			assertEquals(0, myTermCodeSystemDao.count());
			assertNull(myTermCodeSystemDao.findByCodeSystemUri("http://loinc.org"));
			assertEquals(0, myTermCodeSystemVersionDao.count());
			assertEquals(0, myTermConceptDao.count());
		});
	}


	@Test
	public void runWithParameterZeroFailsValidation() {
		JobInstanceStartRequest request = new JobInstanceStartRequest();
		request.setJobDefinitionId(TERM_CODE_SYSTEM_DELETE_JOB_NAME);
		request.setParameters(new TermCodeSystemDeleteJobParameters()); // no pid

		InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
			myJobCoordinator.startInstance(new SystemRequestDetails(), request);
		});
		assertThat(exception.getMessage().contains("Invalid Term Code System PID 0")).as(exception.getMessage()).isTrue();
	}


}
