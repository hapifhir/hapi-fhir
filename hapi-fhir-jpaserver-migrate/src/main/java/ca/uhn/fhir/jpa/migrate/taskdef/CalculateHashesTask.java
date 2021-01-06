package ca.uhn.fhir.jpa.migrate.taskdef;

/*-
 * #%L
 * HAPI FHIR JPA Server - Migration
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.VersionEnum;
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
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;

public class CalculateHashesTask extends BaseColumnCalculatorTask {

	/**
	 * Constructor
	 */
	public CalculateHashesTask(VersionEnum theRelease, String theVersion) {
		super(theRelease, theVersion);
		setDescription("Calculate resource search parameter index hashes");
		setPidColumnName("SP_ID");
	}

	@Override
	protected boolean shouldSkipTask() {
		try {
			Set<String> tableNames = JdbcUtils.getTableNames(getConnectionProperties());
			boolean shouldSkip = tableNames.contains("HFJ_RES_REINDEX_JOB");
			// This table was added shortly after hash indexes were added, so it is a reasonable indicator for whether this
			// migration has already been run
			if (shouldSkip) {
				logInfo(ourLog, "The table HFJ_RES_REINDEX_JOB already exists.  Skipping calculate hashes task.");
			}
				return shouldSkip;
			} catch (SQLException e) {
			logInfo(ourLog, "Error retrieving table names, skipping task");
			return true;
		}
	}
}
