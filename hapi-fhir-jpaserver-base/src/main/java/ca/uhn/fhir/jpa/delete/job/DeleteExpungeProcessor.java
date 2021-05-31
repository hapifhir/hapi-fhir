package ca.uhn.fhir.jpa.delete.job;

import ca.uhn.fhir.jpa.dao.expunge.ResourceForeignKey;
import ca.uhn.fhir.jpa.dao.expunge.ResourceTableFKProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Input: list of pids of resources to be deleted and expunged
 * Output: list of sql statements to be executed
 */
public class DeleteExpungeProcessor implements ItemProcessor<List<Long>, List<String>> {
	private static final Logger ourLog = LoggerFactory.getLogger(DeleteExpungeProcessor.class);

	@Autowired
	ResourceTableFKProvider myResourceTableFKProvider;

	@Override
	public List<String> process(List<Long> thePids) throws Exception {
		List<String> retval = new ArrayList<>();

		String pidListString = thePids.toString().replace("[", "(").replace("]", ")");
		List<ResourceForeignKey> resourceForeignKeys = myResourceTableFKProvider.getResourceForeignKeys();

		for (ResourceForeignKey resourceForeignKey : resourceForeignKeys) {
			retval.add(deleteRecordsByColumnSql(pidListString, resourceForeignKey));
		}

		// Lastly we need to delete records from the resource table all of these other tables link to:
		ResourceForeignKey resourceTablePk = new ResourceForeignKey("HFJ_RESOURCE", "RES_ID");
		retval.add(deleteRecordsByColumnSql(pidListString, resourceTablePk));
		return retval;
	}

	private String deleteRecordsByColumnSql(String thePidListString, ResourceForeignKey theResourceForeignKey) {
		return "DELETE FROM " + theResourceForeignKey.table + " WHERE " + theResourceForeignKey.key + " IN " + thePidListString;
	}
}
