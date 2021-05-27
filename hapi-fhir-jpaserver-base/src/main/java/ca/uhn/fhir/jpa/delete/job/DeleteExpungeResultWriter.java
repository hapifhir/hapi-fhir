package ca.uhn.fhir.jpa.delete.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class DeleteExpungeResultWriter implements ItemWriter<String> {
	private static final Logger ourLog = LoggerFactory.getLogger(DeleteExpungeResultWriter.class);

	@Override
	public void write(List<? extends String> items) throws Exception {
		ourLog.info(">>> Writing list " + items);
	}
}
