package ca.uhn.fhir.jpa.migrate.taskdef;

/*-
 * #%L
 * HAPI FHIR JPA Server - Migration
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.util.StopWatch;
import com.google.common.collect.ForwardingMap;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Function;

public class CalculateHashesTask extends BaseTableColumnTask<CalculateHashesTask> {

	private static final Logger ourLog = LoggerFactory.getLogger(CalculateHashesTask.class);
	private int myBatchSize = 10000;
	private Map<String, Function<MandatoryKeyMap<String, Object>, Long>> myCalculators = new HashMap<>();
	private ThreadPoolExecutor myExecutor;

	public void setBatchSize(int theBatchSize) {
		myBatchSize = theBatchSize;
	}

	/**
	 * Constructor
	 */
	public CalculateHashesTask() {
		super();
	}

	@Override
	public synchronized void execute() throws SQLException {
		if (isDryRun()) {
			return;
		}

		initializeExecutor();
		try {

			while(true) {
				MyRowCallbackHandler rch = new MyRowCallbackHandler();
				getTxTemplate().execute(t -> {
					JdbcTemplate jdbcTemplate = newJdbcTemnplate();
					jdbcTemplate.setMaxRows(100000);
					String sql = "SELECT * FROM " + getTableName() + " WHERE " + getColumnName() + " IS NULL";
					ourLog.info("Finding up to {} rows in {} that requires hashes", myBatchSize, getTableName());

					jdbcTemplate.query(sql, rch);
					rch.done();

					return null;
				});

				rch.submitNext();
				List<Future<?>> futures = rch.getFutures();
				if (futures.isEmpty()) {
					break;
				}

				ourLog.info("Waiting for {} tasks to complete", futures.size());
				for (Future<?> next : futures) {
					try {
						next.get();
					} catch (Exception e) {
						throw new SQLException(e);
					}
				}

			}

		} finally {
			destroyExecutor();
		}
	}

	private void destroyExecutor() {
		myExecutor.shutdownNow();
	}

	private void initializeExecutor() {
		int maximumPoolSize = Runtime.getRuntime().availableProcessors();

		LinkedBlockingQueue<Runnable> executorQueue = new LinkedBlockingQueue<>(maximumPoolSize);
		BasicThreadFactory threadFactory = new BasicThreadFactory.Builder()
			.namingPattern("worker-" + "-%d")
			.daemon(false)
			.priority(Thread.NORM_PRIORITY)
			.build();
		RejectedExecutionHandler rejectedExecutionHandler = new RejectedExecutionHandler() {
			@Override
			public void rejectedExecution(Runnable theRunnable, ThreadPoolExecutor theExecutor) {
				ourLog.info("Note: Executor queue is full ({} elements), waiting for a slot to become available!", executorQueue.size());
				StopWatch sw = new StopWatch();
				try {
					executorQueue.put(theRunnable);
				} catch (InterruptedException theE) {
					throw new RejectedExecutionException("Task " + theRunnable.toString() +
						" rejected from " + theE.toString());
				}
				ourLog.info("Slot become available after {}ms", sw.getMillis());
			}
		};
		myExecutor = new ThreadPoolExecutor(
			1,
			maximumPoolSize,
			0L,
			TimeUnit.MILLISECONDS,
			executorQueue,
			threadFactory,
			rejectedExecutionHandler);
	}

	private Future<?> updateRows(List<Map<String, Object>> theRows) {
		Runnable task = () -> {
			StopWatch sw = new StopWatch();
			getTxTemplate().execute(t -> {

				// Loop through rows
				assert theRows != null;
				for (Map<String, Object> nextRow : theRows) {

					Map<String, Long> newValues = new HashMap<>();
					MandatoryKeyMap<String, Object> nextRowMandatoryKeyMap = new MandatoryKeyMap<>(nextRow);

					// Apply calculators
					for (Map.Entry<String, Function<MandatoryKeyMap<String, Object>, Long>> nextCalculatorEntry : myCalculators.entrySet()) {
						String nextColumn = nextCalculatorEntry.getKey();
						Function<MandatoryKeyMap<String, Object>, Long> nextCalculator = nextCalculatorEntry.getValue();
						Long value = nextCalculator.apply(nextRowMandatoryKeyMap);
						newValues.put(nextColumn, value);
					}

					// Generate update SQL
					StringBuilder sqlBuilder = new StringBuilder();
					List<Number> arguments = new ArrayList<>();
					sqlBuilder.append("UPDATE ");
					sqlBuilder.append(getTableName());
					sqlBuilder.append(" SET ");
					for (Map.Entry<String, Long> nextNewValueEntry : newValues.entrySet()) {
						if (arguments.size() > 0) {
							sqlBuilder.append(", ");
						}
						sqlBuilder.append(nextNewValueEntry.getKey()).append(" = ?");
						arguments.add(nextNewValueEntry.getValue());
					}
					sqlBuilder.append(" WHERE SP_ID = ?");
					arguments.add((Number) nextRow.get("SP_ID"));

					// Apply update SQL
					newJdbcTemnplate().update(sqlBuilder.toString(), arguments.toArray());

				}

				return theRows.size();
			});
			ourLog.info("Updated {} rows on {} in {}", theRows.size(), getTableName(), sw.toString());
		};
		return myExecutor.submit(task);
	}

	public CalculateHashesTask addCalculator(String theColumnName, Function<MandatoryKeyMap<String, Object>, Long> theConsumer) {
		Validate.isTrue(myCalculators.containsKey(theColumnName) == false);
		myCalculators.put(theColumnName, theConsumer);
		return this;
	}

	private class MyRowCallbackHandler implements RowCallbackHandler {

		private List<Map<String, Object>> myRows = new ArrayList<>();
		private List<Future<?>> myFutures = new ArrayList<>();

		@Override
		public void processRow(ResultSet rs) throws SQLException {
			Map<String, Object> row = new ColumnMapRowMapper().mapRow(rs, 0);
			myRows.add(row);

			if (myRows.size() >= myBatchSize) {
				submitNext();
			}
		}

		private void submitNext() {
			if (myRows.size() > 0) {
				myFutures.add(updateRows(myRows));
				myRows = new ArrayList<>();
			}
		}

		public List<Future<?>> getFutures() {
			return myFutures;
		}

		public void done() {
			if (myRows.size() > 0) {
				submitNext();
			}
		}
	}


	public static class MandatoryKeyMap<K, V> extends ForwardingMap<K, V> {

		private final Map<K, V> myWrap;

		public MandatoryKeyMap(Map<K, V> theWrap) {
			myWrap = theWrap;
		}

		@Override
		public V get(Object theKey) {
			if (!containsKey(theKey)) {
				throw new IllegalArgumentException("No key: " + theKey);
			}
			return super.get(theKey);
		}

		public String getString(String theKey) {
			return (String) get(theKey);
		}

		@Override
		protected Map<K, V> delegate() {
			return myWrap;
		}

		public String getResourceType() {
			return getString("RES_TYPE");
		}

		public String getParamName() {
			return getString("SP_NAME");
		}
	}
}
