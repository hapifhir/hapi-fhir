package ca.uhn.fhir.jpa.term.job;

//fixme: add CPWRs

import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemDao;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemVersionDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
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

@Component
public class TermCodeSystemDeleteTasklet implements Tasklet {
	private static final Logger ourLog = LoggerFactory.getLogger(TermCodeSystemDeleteTasklet.class);

	@Autowired
	private ITermCodeSystemDao myTermCodeSystemDao;

	@Autowired
	private ITermCodeSystemVersionDao myCodeSystemVersionDao;

	@Override
	public RepeatStatus execute(@NotNull StepContribution contribution, ChunkContext context) throws Exception {
		long codeSystemPid = (Long) context.getStepContext().getJobParameters().get(JOB_PARAM_CODE_SYSTEM_ID);
		ourLog.info(" * Deleting code system {}", codeSystemPid);

		myTermCodeSystemDao.findById(codeSystemPid).orElseThrow(IllegalStateException::new);
		myTermCodeSystemDao.deleteById(codeSystemPid);

		return RepeatStatus.FINISHED;
	}

}
