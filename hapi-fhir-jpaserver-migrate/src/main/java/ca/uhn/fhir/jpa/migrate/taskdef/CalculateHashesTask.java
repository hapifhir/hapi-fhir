package ca.uhn.fhir.jpa.migrate.taskdef;

/*-
 * #%L
 * HAPI FHIR JPA Server - Migration
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class CalculateHashesTask extends BaseTableColumnTask<CalculateHashesTask> {

	private static final Logger ourLog = LoggerFactory.getLogger(CalculateHashesTask.class);
	private int myBatchSize = 10000;
	private Map<String, Function<MandatoryKeyMap<String, Object>, Long>> myCalculators = new HashMap<>();

	public void setBatchSize(int theBatchSize) {
		myBatchSize = theBatchSize;
	}


	@Override
	public void execute() {
		if (isDryRun()) {
			return;
		}

		List<Map<String, Object>> rows;
		do {
			rows = getTxTemplate().execute(t -> {
				JdbcTemplate jdbcTemplate = newJdbcTemnplate();
				jdbcTemplate.setMaxRows(myBatchSize);
				String sql = "SELECT * FROM " + getTableName() + " WHERE " + getColumnName() + " IS NULL";
				ourLog.info("Finding up to {} rows in {} that requires hashes", myBatchSize, getTableName());
				return jdbcTemplate.queryForList(sql);
			});

			updateRows(rows);
		} while (rows.size() > 0);
	}

	private void updateRows(List<Map<String, Object>> theRows) {
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
				List<Long> arguments = new ArrayList<>();
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
				arguments.add((Long) nextRow.get("SP_ID"));

				// Apply update SQL
				newJdbcTemnplate().update(sqlBuilder.toString(), arguments.toArray());

			}

			return theRows.size();
		});
		ourLog.info("Updated {} rows on {} in {}", theRows.size(), getTableName(), sw.toString());
	}

	public CalculateHashesTask addCalculator(String theColumnName, Function<MandatoryKeyMap<String, Object>, Long> theConsumer) {
		Validate.isTrue(myCalculators.containsKey(theColumnName) == false);
		myCalculators.put(theColumnName, theConsumer);
		return this;
	}


	public static class MandatoryKeyMap<K, V> extends ForwardingMap<K, V> {

		private final Map<K, V> myWrap;

		public MandatoryKeyMap(Map<K, V> theWrap) {
			myWrap = theWrap;
		}

		@Override
		public V get(@NullableDecl Object theKey) {
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
