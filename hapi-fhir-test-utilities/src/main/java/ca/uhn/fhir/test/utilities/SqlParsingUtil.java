/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.test.utilities;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.insert.Insert;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class SqlParsingUtil {

	/**
	 * Non instantiable
	 */
	private SqlParsingUtil() {}


	public static String parseInsertStatementTableName(String theInsertSql) throws JSQLParserException {
		Insert parsedStatement = (Insert) CCJSqlParserUtil.parse(theInsertSql);
		return parsedStatement.getTable().getName();
	}

	public static Map<String, String> parseInsertStatementParams(String theInsertSql) throws JSQLParserException {
		Insert parsedStatement = (Insert) CCJSqlParserUtil.parse(theInsertSql);

		Map<String, String> retVal = new HashMap<>();

		for (int i = 0; i < parsedStatement.getColumns().size(); i++) {
			String columnName = parsedStatement.getColumns().get(i).getColumnName();
			String columnValue = parsedStatement.getValues().getExpressions().get(i).toString();
			assertFalse(retVal.containsKey(columnName), ()->"Duplicate column in insert statement: " + columnName);
			retVal.put(columnName, columnValue);
		}

		return retVal;
	}


}
