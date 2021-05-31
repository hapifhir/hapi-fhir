package ca.uhn.fhir.jpa.batch.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.List;

public class SqlExecutorWriter implements ItemWriter<List<String>> {
	private static final Logger ourLog = LoggerFactory.getLogger(SqlExecutorWriter.class);

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager myEntityManager;

	@Override
	public void write(List<? extends List<String>> theSqlLists) throws Exception {
		ourLog.info("Executing {} lists of sql", theSqlLists.size());
		long totalChanges = 0;
		for (List<String> sqlList : theSqlLists) {
			ourLog.info("Executing {} sql commands", sqlList.size());
			long changes = 0;
			for (String sql : sqlList) {
				ourLog.info(">>> Executing sql " + sql);
				changes += myEntityManager.createNativeQuery(sql).executeUpdate();
			}
			ourLog.info("{} records changed", changes);
		}
		ourLog.info("TOTAL: {} records changed", totalChanges);
	}
}
