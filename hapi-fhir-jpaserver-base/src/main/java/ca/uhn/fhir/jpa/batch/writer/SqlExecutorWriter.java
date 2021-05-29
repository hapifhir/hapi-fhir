package ca.uhn.fhir.jpa.batch.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class SqlExecutorWriter implements ItemWriter<List<String>> {
	private static final Logger ourLog = LoggerFactory.getLogger(SqlExecutorWriter.class);

	@Override
	public void write(List<? extends List<String>> theSql) throws Exception {
		ourLog.info(">>> Executing sql " + theSql);
	}
}
