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
import ca.uhn.fhir.jpa.term.TerminologyTestHelper;
import ca.uhn.fhir.jpa.term.models.TermCodeSystemDeleteJobParameters;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.Properties;

import static ca.uhn.fhir.batch2.jobs.termcodesystem.TermCodeSystemJobConfig.TERM_CODE_SYSTEM_DELETE_JOB_NAME;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum.LOINC_UPLOAD_PROPERTIES_FILE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class TermCodeSystemDeleteJobTest extends BaseJpaR4Test {

	private Properties uploadProperties;

	@Autowired
	private TerminologyTestHelper myTerminologyTestHelper;

	@Autowired
	private Batch2JobHelper myBatch2JobHelper;

	@Autowired
	private IJobCoordinator myJobCoordinator;

	private void initMultipleVersionLoad() throws Exception {
		InputStream is = new ClassPathResource("loinc-ver/" + LOINC_UPLOAD_PROPERTIES_FILE.getCode()).getInputStream();
		uploadProperties = new Properties();
		uploadProperties.load(is);
	}

	@Test
	public void runDeleteJobMultipleVersions() throws Exception {
		initMultipleVersionLoad();

		// loading a loinc CS with version loads two versions (second one with null version)
		String firstCurrentVer = "2.67";
		myTerminologyTestHelper.startImportLoincJobAndWaitForCompletion(firstCurrentVer, true);

		long[] termCodeSystemPidVect = new long[1];  //bypass final restriction
		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());

			TermCodeSystem termCodeSystem = myTermCodeSystemDao.findByCodeSystemUri("http://loinc.org");
			assertNotNull(termCodeSystem);
			termCodeSystemPidVect[0] = termCodeSystem.getPid();

			assertEquals(2, myTermCodeSystemVersionDao.count());
			assertEquals(58, myTermConceptDao.count());
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
