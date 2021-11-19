package ca.uhn.fhir.jpa.term.job;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemDao;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemVersionDao;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static ca.uhn.fhir.jpa.batch.config.BatchConstants.JOB_PARAM_CODE_SYSTEM_ID;

/**
 * Deletes the TermConcept(s) related to the TermCodeSystemVersion being deleted
 * Executes in its own step to be in own transaction because it is a DB-heavy operation
 */
@Component
public class TermConceptDeleteTasklet implements Tasklet {
	private static final Logger ourLog = LoggerFactory.getLogger(TermConceptDeleteTasklet.class);

	@Autowired
	private ITermCodeSystemDao myTermCodeSystemDao;

	@Autowired
	private ITermCodeSystemVersionDao myCodeSystemVersionDao;

	@Override
	public RepeatStatus execute(@NotNull StepContribution contribution, ChunkContext context) throws Exception {
		long codeSystemPid = (Long) context.getStepContext().getJobParameters().get(JOB_PARAM_CODE_SYSTEM_ID);
		ourLog.info("Deleting code system {}", codeSystemPid);

		myTermCodeSystemDao.findById(codeSystemPid).orElseThrow(IllegalStateException::new);
		myTermCodeSystemDao.deleteById(codeSystemPid);

		return RepeatStatus.FINISHED;
	}

}
