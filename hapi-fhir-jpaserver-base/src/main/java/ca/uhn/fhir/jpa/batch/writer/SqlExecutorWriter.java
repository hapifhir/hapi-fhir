package ca.uhn.fhir.jpa.batch.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.List;

/**
 * This Spring Batch writer accepts a list of SQL commands and executes them.
 * The total number of entities updated or deleted is stored in the execution context
 * with the key {@link #ENTITY_TOTAL_UPDATED_OR_DELETED}.  The entire list is committed within a
 * single transaction (provided by Spring Batch).
 */
public class SqlExecutorWriter implements ItemWriter<List<String>> {
	private static final Logger ourLog = LoggerFactory.getLogger(SqlExecutorWriter.class);

	public static final String ENTITY_TOTAL_UPDATED_OR_DELETED = "entity.total.updated-or-deleted";

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager myEntityManager;
	private Long totalUpdated = 0L;
	private StepExecution myStepExecution;

	@BeforeStep
	public void setStepExecution(StepExecution stepExecution) {
		myStepExecution = stepExecution;
	}

	@Override
	public void write(List<? extends List<String>> theSqlLists) throws Exception {
		for (List<String> sqlList : theSqlLists) {
			ourLog.info("Executing {} sql commands", sqlList.size());
			for (String sql : sqlList) {
				ourLog.trace("Executing sql " + sql);
				totalUpdated += myEntityManager.createNativeQuery(sql).executeUpdate();
				myStepExecution.getExecutionContext().putLong(ENTITY_TOTAL_UPDATED_OR_DELETED, totalUpdated);
			}
		}
		ourLog.debug("{} records updated", totalUpdated);
	}
}
