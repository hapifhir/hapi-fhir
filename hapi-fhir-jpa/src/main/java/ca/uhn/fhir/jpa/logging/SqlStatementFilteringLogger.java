/*-
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.logging;

import org.hibernate.engine.jdbc.spi.SqlStatementLogger;
import org.hibernate.internal.CoreLogging;
import org.hibernate.service.Service;
import org.jboss.logging.Logger;

/**
 * Logger set as a hibernate service to allow filtering out SQL statements based in statement content
 * instead of package, as hibernate logs always from same class disallowing package discrimination.
 * Note that when content includes class and package name, it can be used for filtering
 * <p/>
 * It self-activates when "org.hibernate.SQL" logger is set to DEBUG.
 * Deactivates fully (even config-checking executor is shutdown) when "org.hibernate.SQL" logger is set lower than DEBUG.
 * To use, simply add filtering statement lines to the 'sql-filters/hibernate-sql-log-filters.txt' classpath file
 * starting with:
 * <li>
 *     <ul>'sw:' to filter statements which start with the following string</ul>
 *     <ul>'frag:' to filter statements which contain the fragment string</ul>
 *     <ul>'stack:' to filter statements logging which stack trace contain the following string</ul>
 * </li>
 */
public class SqlStatementFilteringLogger extends SqlStatementLogger implements Service {

	private static final Logger LOG = CoreLogging.logger("org.hibernate.SQL");

	private final SqlLoggerFilteringUtil myFilteringUtil;

	public SqlStatementFilteringLogger(SqlLoggerFilteringUtil theFilteringUtil) {
		super();
		myFilteringUtil = theFilteringUtil;
	}

	@Override
	public void logStatement(String statement) {
		if (LOG.isDebugEnabled() && myFilteringUtil.allowLog(statement)) {
			super.logStatement(statement);
		}
	}
}
