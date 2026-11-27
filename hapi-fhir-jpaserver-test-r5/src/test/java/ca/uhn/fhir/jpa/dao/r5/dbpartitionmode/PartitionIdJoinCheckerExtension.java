package ca.uhn.fhir.jpa.dao.r5.dbpartitionmode;

import net.sf.jsqlparser.JSQLParserException;
import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.listener.ChainListener;
import net.ttddyy.dsproxy.listener.QueryExecutionListener;
import net.ttddyy.dsproxy.support.ProxyDataSource;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Fails a test whose SQL joins partitioned tables without matching on {@literal PARTITION_ID} (see
 * {@link PartitionIdJoinChecker}), or that the checker cannot parse. It checks every statement the test method runs,
 * from any thread, but not those of {@code @BeforeEach} or {@code @AfterEach} methods. The test's Spring context must
 * use a datasource-proxy {@link ProxyDataSource}.
 * <p>
 * <b>If a query does not match on {@literal PARTITION_ID}:</b> a database that keeps partitions on different nodes,
 * such as distributed PostgreSQL, rejects it, because it can't tell whether the joined rows are on the same node.
 * Fix the code that builds the query. For example, a chained {@code :not} replaced
 * <pre>
 * NOT EXISTS (SELECT ... WHERE s0.PARTITION_ID = t0.TARGET_RES_PARTITION_ID AND s0.RES_ID = t0.TARGET_RESOURCE_ID)
 * </pre>
 * with an uncorrelated tuple subquery (see {@code TuplePredicateBuilder}):
 * <pre>
 * (t0.TARGET_RES_PARTITION_ID, t0.TARGET_RESOURCE_ID) NOT IN (SELECT s0.PARTITION_ID, s0.RES_ID ...)
 * </pre>
 * If the database accepts the query, fix the rules in {@link PartitionIdJoinChecker} instead.
 * <p>
 * <b>If a query cannot be parsed:</b> it was not checked; the log has the parser error.
 */
// Created by Claude Opus 5.5
public class PartitionIdJoinCheckerExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {
	private static final Logger ourLog = LoggerFactory.getLogger(PartitionIdJoinCheckerExtension.class);
	private static final QueryRecorder ourQueryRecorder = new QueryRecorder();
	private static PartitionIdJoinChecker ourChecker;

	@Override
	public void beforeTestExecution(ExtensionContext theContext) throws Exception {
		DataSource dataSource = SpringExtension.getApplicationContext(theContext).getBean(DataSource.class);
		if (ourChecker == null) {
			Set<String> partitionedTables = PartitionIdJoinChecker.loadPartitionedTables(dataSource);
			assertThat(partitionedTables)
					.as("tables whose primary key includes PARTITION_ID; without them nothing is checked")
					.isNotEmpty();
			// Other databases keep joins on TARGET_RES_PARTITION_ID inside correlated subqueries
			ourChecker = new PartitionIdJoinChecker(partitionedTables, isPostgres(dataSource));
		}
		ChainListener listeners = ((ProxyDataSource) dataSource).getProxyConfig().getQueryListener();
		if (!listeners.getListeners().contains(ourQueryRecorder)) {
			listeners.addListener(ourQueryRecorder);
		}
		ourQueryRecorder.start();
	}

	@Override
	public void afterTestExecution(ExtensionContext theContext) {
		Set<String> queries = new LinkedHashSet<>(ourQueryRecorder.stop());
		Map<String, List<String>> violations = new LinkedHashMap<>();
		List<String> unparsed = new ArrayList<>();
		for (String sql : queries) {
			try {
				List<String> queryViolations = ourChecker.findViolations(sql);
				if (!queryViolations.isEmpty()) {
					violations.put(sql, queryViolations);
				}
			} catch (JSQLParserException e) {
				unparsed.add(sql);
				ourLog.warn("PARTITION_ID join check could not parse: {}", sql, e);
			}
		}
		ourLog.info(
				"PARTITION_ID join check: {} distinct queries, {} not parsed, {} rejected",
				queries.size(),
				unparsed.size(),
				violations.size());
		assertThat(unparsed)
				.as("queries the PARTITION_ID join check could not parse, so could not check; see "
						+ "PartitionIdJoinCheckerExtension")
				.isEmpty();
		assertThat(violations)
				.as("queries that join partitioned tables without matching on PARTITION_ID; see "
						+ "PartitionIdJoinCheckerExtension")
				.isEmpty();
	}

	private static boolean isPostgres(DataSource theDataSource) throws SQLException {
		try (Connection connection = theDataSource.getConnection()) {
			return "PostgreSQL".equalsIgnoreCase(connection.getMetaData().getDatabaseProductName());
		}
	}

	/**
	 * Records the SQL of every statement, from any thread, while started.
	 */
	private static class QueryRecorder implements QueryExecutionListener {
		private final Queue<String> myQueries = new ConcurrentLinkedQueue<>();
		private volatile boolean myRecording;

		private void start() {
			myQueries.clear();
			myRecording = true;
		}

		private List<String> stop() {
			myRecording = false;
			List<String> retVal = new ArrayList<>(myQueries);
			myQueries.clear();
			return retVal;
		}

		@Override
		public void beforeQuery(ExecutionInfo theExecutionInfo, List<QueryInfo> theQueryInfoList) {
			// nothing to do before the statement runs
		}

		@Override
		public void afterQuery(ExecutionInfo theExecutionInfo, List<QueryInfo> theQueryInfoList) {
			if (myRecording) {
				theQueryInfoList.forEach(t -> myQueries.add(t.getQuery()));
			}
		}
	}
}
