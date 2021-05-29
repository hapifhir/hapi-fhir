package ca.uhn.fhir.jpa.delete.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Input: list of pids of resources to be deleted and expunged
 * Output: list of sql statements to be executed
 */
public class DeleteExpungeProcessor implements ItemProcessor<List<Long>, List<String>> {
	private static final Logger ourLog = LoggerFactory.getLogger(DeleteExpungeProcessor.class);
	@Override
	public List<String> process(List<Long> thePids) throws Exception {
		ourLog.info(">>> Processing {} pids ", thePids.size());
		List<String> retval = new ArrayList<>();
		for (Long item : thePids) {
			retval.add("delete " + item);
		}
		return retval;
	}
}
