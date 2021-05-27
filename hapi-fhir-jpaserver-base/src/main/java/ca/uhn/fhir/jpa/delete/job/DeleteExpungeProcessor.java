package ca.uhn.fhir.jpa.delete.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class DeleteExpungeProcessor implements ItemProcessor<String, String> {
	private static final Logger ourLog = LoggerFactory.getLogger(DeleteExpungeProcessor.class);
	@Override
	public String process(String item) throws Exception {
		ourLog.info(">>> Processing item " + item);
		return "processed " + item;
	}
}
