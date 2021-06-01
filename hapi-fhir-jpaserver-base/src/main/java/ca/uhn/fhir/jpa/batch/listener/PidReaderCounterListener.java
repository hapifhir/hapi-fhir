package ca.uhn.fhir.jpa.batch.listener;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterProcess;
import org.springframework.batch.core.annotation.BeforeStep;

import java.util.List;

public class PidReaderCounterListener {
	public static final String RESOURCE_TOTAL_PROCESSED = "resource.total.processed";

	private StepExecution myStepExecution;
	private Long myTotalPidsProcessed = 0L;

	@BeforeStep
	public void setStepExecution(StepExecution stepExecution) {
		myStepExecution = stepExecution;
	}

	@AfterProcess
	public void afterProcess(List<Long> thePids, List<String> theSqlList) {
		myTotalPidsProcessed += thePids.size();
		myStepExecution.getExecutionContext().putLong(RESOURCE_TOTAL_PROCESSED, myTotalPidsProcessed);
	}
}
